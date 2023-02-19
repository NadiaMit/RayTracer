package RayTracing;

import SceneData.DataTypes.*;
import SceneData.DataTypes.MyVector;
import SceneData.Light.*;
import SceneData.Scene;
import SceneData.Surface.Material.Phong;
import SceneData.Surface.Surface;

import java.util.*;
import java.util.stream.IntStream;

public class RayTracer {

    private static final float EPSILON = 0.01f;

    /**
     * Ray traces the scene and returns a 2D array of {@link MyColor}
     * @param scene the scene to ray trace
     * @param superSampling whether or not to use super sampling
     * @return a 2D array of {@link MyColor}
     */
    public MyColor[][] rayTrace(Scene scene, boolean superSampling) {
        int width = scene.getCamera().getWidth();
        int height = scene.getCamera().getHeight();

        MyColor[][] image = new MyColor[height][width];
        final int maxRayCount = 25;
        final int sqrtMaxRayCount = (int) Math.sqrt(maxRayCount);
        final int maxBounces = scene.getCamera().getMaxBounces();
        final List<Surface> surfaces = scene.getSurfaces();
        final List<Light> lights = scene.getLights();
        final MyColor backgroundColor = scene.getBackgroundColor();

        if(superSampling) {
            System.out.println("Super sampling enabled");
        }

        //go through each pixel in the image, get the pixel color and save it in the image array
        IntStream.range(0, height).parallel().forEach((y) -> {
            for(int x = 0; x < width; x++) {
                MyColor color = new MyColor();
                //if super sampling is enabled, cast 25 rays per pixel and take the average of the colors
                if(superSampling) {
                    //cast 25 rays per pixel
                    for (int rayCount = 0; rayCount < maxRayCount; rayCount++) {
                        Ray ray = scene.getCamera().getRayToPixel(x, y, rayCount, sqrtMaxRayCount);
                        color.add(trace(ray, 0, maxBounces, surfaces, backgroundColor, lights));
                    }
                    //take the average of the 25 colors
                    color = color.scale(1.0f / maxRayCount);
                } else {
                    Ray ray = scene.getCamera().getRayToPixel(x, y, 0, 1);
                    color = trace(ray, 0, maxBounces, surfaces, backgroundColor, lights);
                }
                image[(height-1)-y][x] = color;
            }
        });

        return image;
    }


    /**
     * Traces a ray through the scene and returns the color of the pixel
     * @param ray the ray to trace
     * @param depth the current depth of the ray
     * @param maxBounces the maximum number of bounces
     * @param surfaceList the surfaces in the scene, as a list
     * @param backgroundColor the background color of the scene, as a {@link MyColor}
     * @param lightList the lights in the scene, as a list
     * @return the color of the pixel the ray hits, as a {@link MyColor}
     */
    private MyColor trace(Ray ray, int depth, int maxBounces, List<Surface> surfaceList, MyColor backgroundColor, List<Light> lightList){
        MyColor color = new MyColor();
        Surface closestSurface = null;
        Intersection closestIntersection = new Intersection(Float.MAX_VALUE);

        //go through each surface in the scene and safe the closest intersection & surface
        for(int i = 0; i < surfaceList.size(); i++){
            Surface currSurface = surfaceList.get(i);
            Intersection currIntersection = currSurface.intersect(ray, EPSILON);

            if(currIntersection.hasIntersection() && currIntersection.getT() < closestIntersection.getT()){
                closestSurface = currSurface;
                closestIntersection = currIntersection;
            }
        }

        MyColor reflectedColor = new MyColor();
        MyColor refractedColor = new MyColor();
        float reflectance = 0.0f;
        float transmittance = 0.0f;

        //if there is an intersection with a surface, calculate the color of the pixel
        if(closestSurface != null && closestIntersection.hasIntersection() && closestIntersection.getT() != Float.MAX_VALUE){
            //go through every light and calculate the illumination
            for(int i = 0; i < lightList.size(); i++){
                Light light = lightList.get(i);

                //check if there is a surface between the intersection and the light
                boolean hasShadow = checkIfShadow(light, closestIntersection, surfaceList);

                //if there is no surface between, calculate the illumination and add the color to the pixel color
                if(!hasShadow){
                    color.add(illuminate(closestSurface, ray, closestIntersection, light));
                }
            }

            //if the depth is greater than the maximum number of bounces, return the current color
            if(depth > maxBounces){
                return color;
            }
            //calculate the reflected color and scale it with the reflectance
            reflectance = closestSurface.getMaterial().getReflactance();
            if(reflectance > 0.0f){
                //get the reflected ray and trace it
                Ray reflectedRay = getReflectedRay(closestIntersection, ray);
                reflectedColor = trace(reflectedRay, depth+1, maxBounces, surfaceList, backgroundColor, lightList);
                reflectedColor = reflectedColor.scale(reflectance);
            }

            //calculate the refracted color and scale it with the transmittance
            transmittance = closestSurface.getMaterial().getTransmittance();
            if(transmittance > 0.0f){
                //get the refracted ray and trace it
                Ray refractedRay = getRefractedRay(closestIntersection, ray, closestSurface.getMaterial().getRefractionIndex());
                refractedColor = trace(refractedRay, depth+1, maxBounces, surfaceList, backgroundColor, lightList);
                refractedColor = refractedColor.scale(transmittance);
            }

            //scale the color with the reflectance and transmittance and add the reflected and refracted color
            MyColor finalColor = MyColor.scale(color, (1.0f - reflectance - transmittance));
            finalColor.add(reflectedColor);
            finalColor.add(refractedColor);
            return finalColor;
        }
        //if there is no intersection, return the background color
        else{
            color = backgroundColor;
            return color;
        }
    }

    /**
     * Calculates the refracted ray
     * @param closestIntersection the intersection of the ray with the surface
     * @param ray the current ray
     * @param refractionIndex the refraction index of the surface
     * @return the refracted ray
     */
    private Ray getRefractedRay(Intersection closestIntersection, Ray ray, float refractionIndex){
        MyVector normal = closestIntersection.getNormal();
        MyVector direction = ray.getDirection().getNormalized();

        MyVector refractedDirection = refract(direction, normal, refractionIndex);

        return new Ray(closestIntersection.getPoint(), refractedDirection);
    }

    /**
     * Calculates the reflected ray
     * @param closestIntersection the intersection of the ray with the surface
     * @param ray the current ray
     * @return the reflected ray
     */
    private Ray getReflectedRay(Intersection closestIntersection, Ray ray) {
        MyVector normal = closestIntersection.getNormal();
        MyVector direction = ray.getDirection().getNormalized();

        MyVector reflectedDirection = reflect(direction.invert(), normal);

        return new Ray(closestIntersection.getPoint(), reflectedDirection);
    }

    /**
     * Creates the perfect reflection of a vector pointing away from a surface
     * @param incoming the incoming vector, but pointing away from the surface (invert vectors that point towards the surface)
     * @param normal the normal vector
     * @return the perfect reflection of the light vector, as a Vector
     */
    private MyVector reflect(MyVector incoming, MyVector normal){
        // r = 2(i dot n)n - i
        return MyVector.subtract(normal.scale(2 * MyVector.dotProduct(incoming, normal)),incoming).getNormalized();
    }

    /**
     * Calculates the refraction of an incoming vector
     * used the code from https://www.scratchapixel.com/lessons/3d-basic-rendering/introduction-to-shading/reflection-refraction-fresnel.html
     *
     * @param incoming the incoming vector, pointing towards the surface
     * @param normal the normal of the intersection point
     * @param refractionIndex the refraction index of the surface
     * @return the refracted vector
     */
    private MyVector refract(MyVector incoming, MyVector normal, float refractionIndex){
        float NdotI = MyVector.dotProduct(normal, incoming);
        float ior_before = 1.0f;
        float ior_after = refractionIndex;

        if(NdotI < 0.0f){ //outside the surface
            NdotI = -NdotI;
        }
        else{ //inside the surface
            normal = normal.invert();
            float temp = ior_before;
            ior_before = ior_after;
            ior_after = temp;
        }

        float eta = ior_before / ior_after;
        float k = 1 - eta * eta * (1 - NdotI * NdotI);

        if(k < 0.0f){ //total internal reflection -> return the reflected Vector
            return reflect(incoming.invert(), normal);
        }
        else{
            return MyVector.add(incoming.scale(eta), normal.scale(eta * NdotI - (float) Math.sqrt(k))).getNormalized();
        }
    }

    /**
     * Calculates the phong shading and illumination of the pixel
     * @param surface the surface the pixel is on
     * @param ray the ray that hits the surface
     * @param intersection the intersection of the ray and the surface
     * @param light the light that illuminates the surface
     * @return the color of the pixel, as a {@link MyColor}
     */
    private MyColor illuminate(Surface surface, Ray ray, Intersection intersection, Light light){
        MyPoint intersectionPoint = intersection.getPoint();
        MyColor intersectionColor = intersection.getColor();
        Phong phong = surface.getMaterial().getPhong();
        MyColor lightColor = light.getColor();
        float intensity = 1.0f;

        if(light instanceof AmbientLight){
            //Ka * ambientLightColor * surfaceColor
            return MyColor.multiply(lightColor, intersectionColor.scale(phong.getKa()));
        }

        MyVector lightDirection = new MyVector();

        //if light is spotlight, calculate light intensity
        if(light instanceof SpotLight){
            lightDirection = MyPoint.subtract(light.getPosition(), intersectionPoint).getNormalized();
            intensity = ((SpotLight) light).getLightIntensity(lightDirection);
            if(intensity == 0.0f){
                return new MyColor();
            }
        }

        //calculate the light direction for point lights and parallel lights
        if(light instanceof PointLight){
            lightDirection = MyPoint.subtract(light.getPosition(), intersectionPoint).getNormalized();
        }
        else if(light instanceof ParallelLight){
            lightDirection = light.getDirection().invert().getNormalized();
        }

        //difuse Light
        MyVector normal = intersection.getNormal();
        //lambertian = L dot N
        float lambertian = Math.max(MyVector.dotProduct(lightDirection, normal), 0.0f);

        //specular light
        float specular = 0.0f;
        if(lambertian > 0.0f){
            //R = 2(N dot L)N - L
            MyVector reflect = reflect(lightDirection, normal);
            //V = -Ray
            MyVector view = ray.getDirection().invert().getNormalized();

            //R dot V
            float specularAngle = Math.max(MyVector.dotProduct(reflect, view), 0.0f);
            //(R dot V) ^ exponent
            specular = (float) Math.pow(specularAngle, phong.getExponent());
        }

        //Kd * lambertian * diffuseLightColor * surfaceColor
        MyColor diffuseColor = MyColor.multiply(lightColor, intersectionColor.scale(phong.getKd() * lambertian));
        //Ks * specular * specularLightColor
        MyColor specularColor = lightColor.scale(phong.getKs() * specular);

        //(Kd * lambertian * diffuseLightColor * surfaceColor) + (Ks * specular * specularLightColor)
        return MyColor.plus(diffuseColor, specularColor).scale(intensity);
    }



    /**
     * Checks if there is a surface between the intersection and the light
     * @param light the light that illuminates the surface
     * @param intersection the intersection of the ray and the surface
     * @param surfaceList the surfaces in the scene, as a list
     * @return true if there is a surface between, false if not
     */
    private boolean checkIfShadow(Light light, Intersection intersection, List<Surface> surfaceList) {
        if(light instanceof AmbientLight){
            return false;
        }

        Ray shadowRay = new Ray(intersection.getPoint());

        //calculate the light direction for point lights and parallel lights
        if(light instanceof PointLight){
            shadowRay.setDirection(MyPoint.subtract(light.getPosition(), intersection.getPoint()).getNormalized());
        }
        else if(light instanceof ParallelLight){
            shadowRay.setDirection(light.getDirection().invert().getNormalized());
        }

        //go through each surface in the scene and check if there is a intersection
        for(int i = 0; i < surfaceList.size(); i++){
            Surface surface = surfaceList.get(i);
            Intersection shadowIntersection = surface.intersect(shadowRay, EPSILON);
            if(shadowIntersection.hasIntersection()){
                if(light instanceof ParallelLight
                    || shadowIntersection.getT() < MyPoint.subtract(light.getPosition(), intersection.getPoint()).getLength()){
                    return true;
                }

                return false;
            }
        }

        return false;
    }
}
