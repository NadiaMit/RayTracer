package SceneData.DataTypes;

/**
 * Intersection, same as {@link Intersection} but with u and v coordinates for texture mapping
 */
public class TriangleIntersection extends Intersection {
    private float u;
    private float v;

    public TriangleIntersection(float distanceT, MyVector normal, MyPoint point, float u, float v) {
        super(distanceT, normal, point);
        this.u = u;
        this.v = v;
    }

    public TriangleIntersection(float t) {
        super(t);
        this.u = 0;
        this.v = 0;
    }

    public float getU() {
        return u;
    }

    public float getV() {
        return v;
    }

    public void setU(float u) {
        this.u = u;
    }

    public void setV(float v) {
        this.v = v;
    }
}