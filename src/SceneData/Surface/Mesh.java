package SceneData.Surface;

import SceneData.DataTypes.*;
import SceneData.Surface.Material.Material;

import java.util.List;

public class Mesh extends Surface{
    private String meshName;
    private List<Triangle> triangles;

    public Mesh(String meshName, List<Triangle> triangles, Material material) {
        super(material);
        this.meshName = meshName;
        this.triangles = triangles;
    }

    /**
     * Returns the color of the mesh at the given point (x, y, z), if the mesh has a texture, otherwise the color of the material.
     * @param x the x coordinate of the point
     * @param y the y coordinate of the point
     * @param z the z coordinate of the point
     * @return
     */
    @Override
    public MyColor getColor(float x, float y, float z) {
        return material.getColor(x, y);
    }

    /**
     * Checks wether the ray intersects with at least one triangle of the mesh or not. If so, the closest intersection is returned, otherwise an intersection with t = -1 is returned.
     * @param ray the ray to check
     * @param epsilon the value, which the intersection point has to be greater than(used to avoid self-intersection)
     * @return the intersection of the ray and the mesh
     */
    @Override
    public Intersection intersect(Ray ray, float epsilon) {
        TriangleIntersection closestIntersection = new TriangleIntersection(Float.MAX_VALUE);

        //check all triangles of the mesh and save the closest valid intersection (t > -1 && t < current smallest intersection)
        for(Triangle triangle : triangles) {
            TriangleIntersection currIntersection = triangle.intersect(ray, epsilon);
            if(currIntersection.hasIntersection() && currIntersection.getT() < closestIntersection.getT()) {
                closestIntersection = currIntersection;
            }
        }

        //if there is an intersection, set the color of the intersection and return it
        if(closestIntersection.hasIntersection() && closestIntersection.getT() != Float.MAX_VALUE) {
            //use the texture coordinates of the intersection to get the color of the intersection
            closestIntersection.setColor(getColor(closestIntersection.getU(), closestIntersection.getV(), 0));
            return closestIntersection;
        }

        //if there is no intersection, return an intersection with t = -1
        return new TriangleIntersection(-1);
    }

    @Override
    public String toString() {
        String returnString = "Mesh {" +
                "\n\t\tmeshName: '" + meshName + '\'' +
                ",\n\t\ttriangles: [";

        for(Triangle triangle : triangles){
            returnString += "\n    " + triangle + ",";
        }
        returnString += "\n];" +
                "," + super.toString() +
                "\n\t}";

        return returnString;
    }
}
