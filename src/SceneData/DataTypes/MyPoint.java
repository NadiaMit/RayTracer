package SceneData.DataTypes;

public class MyPoint extends DataType {

    public MyPoint(float x, float y, float z) {
        super(x,y,z);
    }

    /**
     * creates a new Point with the values (0,0,0)
     */
    public MyPoint() {
        super();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y){
        this.y = y;
    }

    public void setZ(float z){
        this.z = z;
    }

    /**
     * Calculates the Vector between two Points, subtracts one point from the other
     * @param one The first Point
     * @param two The second Point
     * @return The Vector between the two Points
     */
    public static MyVector subtract(MyPoint one, MyPoint two) {
        return new MyVector(
                one.x - two.x,
                one.y - two.y,
                one.z - two.z
        );
    }

    /**
     * Adds a Vector to a Point
     * @param point The Point
     * @param vector The Vector
     * @return The new Point
     */
    public static MyPoint plus(MyPoint point, MyVector vector){
        return new MyPoint(
                point.x + vector.x,
                point.y + vector.y,
                point.z + vector.z
        );
    }

    @Override
    public String toString() {
        return "Point (" +
                "x: " + x +
                ", y: " + y +
                ", z: " + z +
                ')';
    }
}
