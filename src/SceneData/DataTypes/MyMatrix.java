package SceneData.DataTypes;


public class MyMatrix {
    private float[][] matrix;

    public MyMatrix(float[][] matrix) {
        this.matrix = matrix;
    }

    /**
     * Creates a new identity matrix.
     */
    public MyMatrix() {
        this.matrix = new float[][] {
            {1, 0, 0, 0},
            {0, 1, 0, 0},
            {0, 0, 1, 0},
            {0, 0, 0, 1}
        };
    }

    public float[][] get() {
        return matrix;
    }

    public void set(float[][] matrix) {
        this.matrix = matrix;
    }

    /**
     * Sets the translation part of the matrix.
     * @param translation The translation vector.
     */
    public void setTranslation(MyPoint translation) {
        matrix[0][3] = translation.getX();
        matrix[1][3] = translation.getY();
        matrix[2][3] = translation.getZ();
    }

    /**
     * Sets the scaling part of the matrix.
     * @param scaling The scaling vector.
     */
    public void setScaling(MyVector scaling) {
        matrix[0][0] = scaling.getX();
        matrix[1][1] = scaling.getY();
        matrix[2][2] = scaling.getZ();
    }

    /**
     * Sets the rotations  of the matrix.
     * @param X The rotation around the X axis.
     * @param Y The rotation around the Y axis.
     * @param Z The rotation around the Z axis.
     */
    public void setRotations(MyVector X, MyVector Y, MyVector Z) {
        matrix[0][0] = X.getX();
        matrix[1][0] = X.getY();
        matrix[2][0] = X.getZ();

        matrix[0][1] = Y.getX();
        matrix[1][1] = Y.getY();
        matrix[2][1] = Y.getZ();

        matrix[0][2] = Z.getX();
        matrix[1][2] = Z.getY();
        matrix[2][2] = Z.getZ();
    }

    /**
     * Transforms a point using the matrix.
     * @param point The point to transform.
     * @return The transformed point.
     */
    public MyPoint transform(MyPoint point) {
        float x = matrix[0][0] * point.getX() + matrix[0][1] * point.getY() + matrix[0][2] * point.getZ() + matrix[0][3];
        float y = matrix[1][0] * point.getX() + matrix[1][1] * point.getY() + matrix[1][2] * point.getZ() + matrix[1][3];
        float z = matrix[2][0] * point.getX() + matrix[2][1] * point.getY() + matrix[2][2] * point.getZ() + matrix[2][3];
        return new MyPoint(x, y, z);
    }

    /**
     * Transforms a vector using the matrix.
     * @param vector The vector to transform.
     * @return The transformed vector.
     */
    public MyVector transform(MyVector vector) {
        float x = matrix[0][0] * vector.getX() + matrix[0][1] * vector.getY() + matrix[0][2] * vector.getZ();
        float y = matrix[1][0] * vector.getX() + matrix[1][1] * vector.getY() + matrix[1][2] * vector.getZ();
        float z = matrix[2][0] * vector.getX() + matrix[2][1] * vector.getY() + matrix[2][2] * vector.getZ();
        return new MyVector(x, y, z);
    }
}
