package SceneData.DataTypes;

/**
 * This class represents an intersection between a ray and a surface.<br><br>
 * It contains: <br>
 * - the distance from the ray origin to the intersection point -> t<br>
 * - the normal vector at the intersection point -> normal<br>
 * - the intersection point -> point<br>
 * - the color of the surface at the intersection point -> color
 */
public class Intersection {
    private float t;
    private MyVector normal;
    private MyPoint point;
    private MyColor color;

    public Intersection(float distanceT, MyVector normal, MyPoint point, MyColor color) {
        this.t = distanceT;
        this.normal = normal.getNormalized();
        this.point = point;
        this.color = color;
    }

    public Intersection(float distanceT, MyVector normal, MyPoint point) {
        this.t = distanceT;
        this.normal = normal.getNormalized();
        this.point = point;
        this.color = new MyColor();
    }

    public Intersection(float t) {
        this.t = t;
        this.normal = new MyVector();
        this.point = new MyPoint();
        this.color = new MyColor();
    }

    /**
     * Checks if the ray intersects the surface/the intersection is valid.
     * @return true if the ray intersects the surface, false otherwise
     */
    public boolean hasIntersection() {
        return t > 0;
    }

    public float getT() {
        return t;
    }

    public MyVector getNormal() {
        return normal;
    }

    public MyPoint getPoint() {
        return point;
    }

    public MyColor getColor() {
        return color;
    }

    public void setColor(MyColor color) {
        this.color = color;
    }
}
