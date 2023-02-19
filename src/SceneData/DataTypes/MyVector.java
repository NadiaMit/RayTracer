package SceneData.DataTypes;

public class MyVector extends DataType{

    public MyVector(float x, float y, float z) {
        super(x, y, z);
    }

    /**
     * creates an empty vector (0, 0, 0)
     */
    public MyVector() {
        super();
    }


    public float[] get() {
        return new float[] {x, y, z};
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

    /**
     * Checks if the vector is a null vector (0, 0, 0)
     * @return True if the vector is a null vector, false otherwise.
     */
    public boolean isNullVector() {
        return x == 0.0f && y == 0.0f && z == 0.0f;
    }

    /**
     * Scales the vector with the given scalar and returns a new Vector with the result.
     * @param scalar The scalar to scale with.
     * @return The resulting vector.
     */
    public MyVector scale(float scalar) {
        return new MyVector(
            this.x * scalar,
            this.y * scalar,
            this.z * scalar
        );
    }

    /**
     * Calculates the dot product of two given vectors and returns a new one with the result.
     * @param one The first vector.
     * @param two The second vector.
     * @return The resulting vector of the dot product.
     */
    public static float dotProduct(MyVector one, MyVector two) {
        return one.x * two.x + one.y * two.y + one.z * two.z;
    }

    /**
     * Calculates the cross product of two given vectors and returns a new one with the result.
     * @param one The first vector.
     * @param two The second vector.
     * @return The resulting vector of the cross product.
     */
    public static MyVector crossProduct(MyVector one, MyVector two) {
        return new MyVector(
            one.y * two.z - one.z * two.y,
            one.z * two.x - one.x * two.z,
            one.x * two.y - one.y * two.x
        );
    }

    /**
     * Subtracts two given vectors and returns a new one with the result.
     * @param one The first vector.
     * @param two The second vector.
     * @return The resulting vector of the subtraction.
     */
    public static MyVector subtract(MyVector one, MyVector two) {
        return new MyVector(
            one.x - two.x,
            one.y - two.y,
            one.z - two.z
        );
    }

    /**
     * Adds two given vectors and returns a new one with the result.
     * @param one The first vector.
     * @param two The second vector.
     * @return The resulting vector of the addition.
     */
    public static MyVector add(MyVector one, MyVector two) {
        return new MyVector(
                one.x + two.x,
                one.y + two.y,
                one.z + two.z
        );
    }

    /**
     * Inverts the vector (changes signs of every value) and returns a new one with the result.
     * @return The resulting inverted vector.
     */
    public MyVector invert(){
        return new MyVector(-x, -y, -z);
    }

    public float getLength() {
        return (float) Math.sqrt(x*x + y*y + z*z);
    }

    /**
     * Normalizes the 'this' Vector
     */
    public void normalize() {
        float length = (float) Math.sqrt(x*x + y*y + z*z);
        this.x = x/length;
        this.y = y/length;
        this.z = z/length;
    }

    /**
     * Returns the normalized version of this Vector without changing the original
     * @return The normalized version of this Vector
     */
    public MyVector getNormalized() {
        float length = (float) Math.sqrt(x*x + y*y + z*z);
        return new MyVector(x/length, y/length, z/length);
    }

    @Override
    public String toString() {
        return "Vector (" +
                "x: " + x +
                ", y: " + y +
                ", z: " + z +
                ')';
    }
}
