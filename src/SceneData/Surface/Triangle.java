package SceneData.Surface;

import SceneData.DataTypes.MyPoint;
import SceneData.DataTypes.MyVector;
import SceneData.DataTypes.Ray;
import SceneData.DataTypes.TriangleIntersection;

public class Triangle {
    private MyPoint a;
    private MyPoint b;
    private MyPoint c;

    private MyPoint a_texture;
    private MyPoint b_texture;
    private MyPoint c_texture;

    private MyVector normal;

    public Triangle(MyPoint a, MyPoint b, MyPoint c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public Triangle(MyPoint[] points, MyPoint[] texturePoints, MyVector normal) {
        if(points.length != 3) {
            throw new IllegalArgumentException("Points array must have 3 elements.");
        }
        this.a = points[0];
        this.b = points[1];
        this.c = points[2];
        this.normal = normal.getNormalized();

        if(texturePoints != null || texturePoints.length == 3) {
            this.a_texture = texturePoints[0];
            this.b_texture = texturePoints[1];
            this.c_texture = texturePoints[2];
        } else {
            this.a_texture = new MyPoint(0, 0, 0);
            this.b_texture = new MyPoint(0, 0, 0);
            this.c_texture = new MyPoint(0, 0, 0);
        }
    }

    /**
     * Implementation of the Möller-Trumbore algorithm
     * used the code from https://www.scratchapixel.com/lessons/3d-basic-rendering/ray-tracing-rendering-a-triangle/moller-trumbore-ray-triangle-intersection.html
     *
     * @param ray the ray to intersect with
     * @param epsilon the minimum distance from the ray origin to the intersection point
     * @return the intersection object -1 if there is no intersection, the t value of the ray if there is an intersection
     */
    public TriangleIntersection intersect(Ray ray, float epsilon) {
        MyVector rayDirection = ray.getDirection().getNormalized();
        MyPoint rayOrigin = ray.getOrigin();

        MyVector edge1 = MyPoint.subtract(b, a);
        MyVector edge2 = MyPoint.subtract(c, a);
        MyVector pvec = MyVector.crossProduct(rayDirection, edge2);
        float det = MyVector.dotProduct(edge1, pvec);

        if(Math.abs(det) < epsilon) {
            return new TriangleIntersection(-1);
        }

        float invDet = 1 / det;
        MyVector tvec = MyPoint.subtract(rayOrigin, a);
        float a_point = MyVector.dotProduct(tvec, pvec) * invDet;

        if (a_point < 0.0 || a_point > 1.0) {
            return new TriangleIntersection(-1);
        }

        MyVector qvec = MyVector.crossProduct(tvec, edge1);
        float b_point = invDet * MyVector.dotProduct(rayDirection, qvec);

        if (b_point < 0.0 || b_point > 1.0 || a_point + b_point > 1.0) {
            return new TriangleIntersection(-1);
        }

        float t = MyVector.dotProduct(edge2, qvec) * invDet;
        if (t > epsilon) {
            ray.setT(t);
            //calculate the texture coordinates for texture mapping
            float u_texture = (1 - a_point - b_point) * a_texture.getX() + a_point * b_texture.getX() + b_point * c_texture.getX();
            float v_texture = (1 - a_point - b_point) * a_texture.getY() + a_point * b_texture.getY() + b_point * c_texture.getY();

            return new TriangleIntersection(t, normal, MyPoint.plus(rayOrigin, rayDirection.scale(ray.getT())), u_texture, v_texture);
        } else {
            return new TriangleIntersection(-1);
        }
    }

    @Override
    public String toString() {
        return "Triangle {" +
                "\n\ta: " + a +
                ",\n\tb: " + b +
                ",\n\tc: " + c +
                ",\n\ta texture: " + a_texture +
                ",\n\tb texture: " + b_texture +
                ",\n\tc texture: " + c_texture +
                ",\n\tnormal: " + normal +
                "\n\t}";
    }
}
