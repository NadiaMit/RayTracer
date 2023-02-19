package SceneData.DataTypes;

import java.awt.Color;

public class MyColor extends DataType {

    public MyColor(float r, float g, float b) {
        super(r,g,b);
    }

    /**
     * creates a new Color with the values (0,0,0) (black)
     */
    public MyColor() {
        super();
    }

    public float getR() {
        return x;
    }

    public float getG() {
        return y;
    }

    public float getB() {
        return z;
    }

    /**
     * Multiplies the ColorVector with a scalar.
     * @param scalar The scalar to multiply with.
     * @return The resulting ColorVector.
     */
    public MyColor scale(float scalar) {
        return new MyColor(
                this.x * scalar,
                this.y * scalar,
                this.z * scalar
        );
    }

    /**
     * Multiplies the given ColorVector with a scalar and returns a new ColorVector.
     * @param scalar The scalar to multiply with.
     * @return The resulting ColorVector.
     */
    public static MyColor scale(MyColor vector, float scalar) {
        return new MyColor(
                vector.x * scalar,
                vector.y * scalar,
                vector.z * scalar
        );
    }


    /**
     * Adds the given ColorVector to this ColorVector value by value (r+r, b+b, g+g).
     * @param other the other ColorVector added to this one.
     */
    public void add(MyColor other) {
        this.x += other.x;
        this.y += other.y;
        this.z += other.z;
    }

    /**
     * Returns a new ColorVector that is the sum of the two given ColorVectors.
     * @param one the first ColorVector
     * @param two the second ColorVector
     * @return a new ColorVector that is the sum of the two given ColorVectors.
     */
    public static MyColor plus(MyColor one, MyColor two) {
        return new MyColor(
                one.x + two.x,
                one.y + two.y,
                one.z + two.z
        );
    }

    /**
     * Multiplies the two given ColorVectors value by value
     * @param one the first ColorVector
     * @param two the second ColorVector
     * @return a new ColorVector with the multiplied values
     */
    public static MyColor multiply(MyColor one, MyColor two) {
        return new MyColor(
                one.x * two.x,
                one.y * two.y,
                one.z * two.z
        );
    }

    /**
     * Converts this ColorVector to a java.awt.Color but clamps the values to 0-255.
     * @return java.awt.Color with the values of this ColorVector
     */
    public Color getConvertedRGB() {
        return new Color(
                (int) Math.min(x*255, 255),
                (int) Math.min(y*255, 255),
                (int) Math.min(z*255, 255)
        );
    }

    /**
     * Returns the RGB value of this ColorVector as an int.
     * @return the RGB value of this ColorVector as an int.
     */
    public int getRGB() {
        return getConvertedRGB().getRGB();
    }

    @Override
    public String toString() {
        return "Color (" +
                "r: " + x +
                ", g: " + y +
                ", b: " + z +
                ')';
    }
}
