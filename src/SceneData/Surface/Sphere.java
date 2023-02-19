package SceneData.Surface;

import SceneData.DataTypes.*;
import SceneData.Surface.Material.Material;

public class Sphere extends Surface {
    private float radius;
    private float radiusSquared;
    private MyPoint origin;

    public Sphere(float radius, MyPoint origin, Material material) {
        super(material);
        this.radius = radius;
        this.radiusSquared = radius * radius;
        this.origin = origin;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
        this.radiusSquared = radius * radius;
    }

    public MyPoint getOrigin() {
        return origin;
    }

    public void setOrigin(MyPoint origin) {
        this.origin = origin;
    }

    public float getRadiusSquared() {
        return radiusSquared;
    }

    /**
     * Checks wether the ray intersects the sphere or not.
     * @param ray the ray to check
     * @param epsilon the value, which the intersection point has to be greater than(used to avoid self-intersection)
     * @return the intersection(t, point, normal) of the ray and the sphere
     */
    @Override
    public Intersection intersect(Ray ray, float epsilon) {
        MyVector rayDirection = ray.getDirection().getNormalized();
        MyPoint rayOrigin = ray.getOrigin();

        //calculate sphereCenter, a, b, c for the quadratic equation
        MyVector sphereCenter = MyPoint.subtract(rayOrigin, getOrigin());
        float a = MyVector.dotProduct(rayDirection, rayDirection);
        float b = 2 * MyVector.dotProduct(rayDirection, sphereCenter);
        float c = MyVector.dotProduct(sphereCenter, sphereCenter) - getRadiusSquared();

        //solve the quadratic equation, if there are no solutions, return -1 = no intersection
        QuadraticSolution solution = solveQuadratic(a,b,c);
        if (!solution.hasSolution()){
            return new Intersection(-1);
        }

        //the two solutions of the quadratic equation
        float t0 = solution.getT0();
        float t1 = solution.getT1();

        float t = t0; //use the smaller t value

        //check if the two intersection points are valid, and choose the smaller once, otherwise return -1 = no intersection
        if (t < epsilon) {
            t = t1; // if t is negative, we try t1
            if (t < epsilon) {
                return new Intersection(-1); // both t values are negative
            }
        }

        //set the t of the ray
        ray.setT(t);

        //get point of intersection
        //P = O + tD
        MyPoint intersectionPoint = MyPoint.plus(rayOrigin, rayDirection.scale(ray.getT()));
        //get normal at intersection point
        //N = (P - C)
        MyVector normal = MyPoint.subtract(intersectionPoint, getOrigin());
        normal.normalize();

        Intersection intersection = new Intersection(t, normal, intersectionPoint);

        //calculate the vector between the intersection point and the origin of the sphere
        MyVector d = MyPoint.subtract(intersection.getPoint(), getOrigin()).getNormalized();
        //calculate the color of the intersection point and save it in the intersection
        intersection.setColor(this.getColor(d.getX(), d.getY(), d.getZ()));
        return intersection;
    }

    /**
     * Calculates the color of the sphere at the given point (only important for texture mapping, otherwise the static color of the material is returned).</br>
     * x, y, z values are from the vector between the intersection point and the origin of the sphere.
     * @param x the x coordinate of the vector
     * @param y the y coordinate of the vector
     * @param z the z coordinate of the vector
     * @return the color of the sphere at the given point
     */
    @Override
    public MyColor getColor(float x, float y, float z) {
        //calculate v and u of image coordinates
        float u = (float) (0.5 + (Math.atan2(x, z)) / (2 * Math.PI));
        float v = (float) (0.5 - (Math.asin(y) / Math.PI));

        return material.getColor(u, v);
    }

    /**
     * Solves a quadratic equation of the form ax^2 + bx + c = 0
     * used the code from https://www.scratchapixel.com/lessons/3d-basic-rendering/minimal-ray-tracer-rendering-simple-shapes/ray-sphere-intersection.html
     *
     * @return the solution of the quadratic equation as a QuadraticSolution object (which contains t0 and t1, and a boolean saying if the equation has a solution)
     */
    private QuadraticSolution solveQuadratic(float a, float b, float c){
        float x0, x1;
        float disc = b * b - 4 * a * c;

        if (disc < 0){
            return new QuadraticSolution(0, 0, false);
        }
        else if (disc == 0){
            x0 = x1 = - 0.5f * b / a;
        }
        else {
            float q;

            if(b > 0){
                q = -0.5f * (b + (float)Math.sqrt(disc));
            }
            else{
                q = -0.5f * (b - (float)Math.sqrt(disc));
            }

            x0 = q / a;
            x1 = c / q;
        }

        if (x0 > x1){
            float temp = x0;
            x0 = x1;
            x1 = temp;
        }

        return new QuadraticSolution(x0, x1, true);
    }

    @Override
    public String toString() {
        return "Sphere {" +
                "\n\t\tradius: " + radius +
                ",\n\t\tposition: " + origin +
                "," + super.toString() +
                "\n\t}";
    }
}