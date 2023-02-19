package SceneData;

import SceneData.DataTypes.MyMatrix;
import SceneData.DataTypes.MyPoint;
import SceneData.DataTypes.Ray;
import SceneData.DataTypes.MyVector;

public class Camera {
    private MyPoint position;
    private MyPoint lookAt;
    private MyVector up;
    private int horizontalFov;
    private int resolutionHorizontal;
    private int resolutionVertical;
    private int maxBounces;
    private MyMatrix matrix;

    public Camera(MyPoint position, MyPoint lookAt, MyVector up, int horizontalFov, int resolutionHorizontal, int resolutionVertical, int maxBounces) {
        this.position = position;
        this.lookAt = lookAt;
        this.up = up;
        this.horizontalFov = horizontalFov;
        this.resolutionHorizontal = resolutionHorizontal;
        this.resolutionVertical = resolutionVertical;
        this.maxBounces = maxBounces;
        this.matrix = getCameraTransformationMatrix();
    }

    public Camera(){
        this.position = new MyPoint();
        this.lookAt = new MyPoint();
        this.up = new MyVector();
        this.horizontalFov = 0;
        this.resolutionHorizontal = 0;
        this.resolutionVertical = 0;
        this.maxBounces = 0;
        this.matrix = getCameraTransformationMatrix();
    }

    public int getWidth() {
        return resolutionHorizontal;
    }

    public int getHeight() {
        return resolutionVertical;
    }

    private float getFov() {
        return this.horizontalFov;
    }

    private MyPoint getLookAt() {
        return lookAt;
    }

    private MyPoint getPosition() {
        return position;
    }

    private MyVector getUp() { return up; }

    public int getMaxBounces() {
        return maxBounces;
    }

    /**
     * Returns a ray from the camera to the pixel at (x,y)
     * @param pixelX x-coordinate of the pixel
     * @param pixelY y-coordinate of the pixel
     * @return Ray from the camera to the pixel
     */
    public Ray getRayToPixel(int pixelX, int pixelY, int rayCount, int sqrtRayCount) {
        float offsetX = 0.5f;
        float offsetY = 0.5f;

        if (sqrtRayCount > 1){
            offsetX = (float) (0.1 + (float) (rayCount % sqrtRayCount) / 10);
            offsetY = (float) (0.1 + (float) (rayCount / sqrtRayCount) / 10);
        }

        float x = ((pixelX + offsetX) / this.getWidth());
        float y = ((pixelY + offsetY) / this.getHeight());

        //map to image plane
        float imageX = 2 * x - 1;
        float imageY = 2 * y - 1;

        //calculate fov + dimensions
        float fovX = (float) Math.toRadians(this.getFov());
        float fovY = fovX * (this.getHeight() / this.getWidth());

        imageX *= (float) Math.tan(fovX);
        imageY *= (float) Math.tan(fovY);

        //create direction vector and origin (0,0,0)
        MyVector direction = new MyVector(imageX, imageY, -1);
        direction.normalize();
        MyPoint origin = new MyPoint();

        //transform to world space
        origin = this.matrix.transform(origin);
        direction = this.matrix.transform(direction);

        return new Ray(origin, direction);
    }

    /**
     * Calculates the camera matrix to transform from camera space to world space
     * @return the camera transformation matrix
     */
    private MyMatrix getCameraTransformationMatrix(){
        MyPoint position = this.getPosition();
        MyPoint lookAt = this.getLookAt();
        MyVector up = this.getUp();
        MyMatrix m = new MyMatrix();

        //calculate and set translation
        m.setTranslation(position);

        //calculate and set rotations
        MyVector Z = MyPoint.subtract(position, lookAt).getNormalized();
        MyVector X = MyVector.crossProduct(up, Z).getNormalized();
        MyVector Y = MyVector.crossProduct(Z, X).getNormalized();
        m.setRotations(X, Y, Z);

        return m;
    }

    @Override
    public String toString() {
        return "Camera {" +
                "\n\tposition = " + position +
                ",\n\tlookAt = " + lookAt +
                ",\n\tup = " + up +
                ",\n\thorizontalFov = " + horizontalFov +
                ",\n\tresolutionHorizontal = " + resolutionHorizontal +
                ",\n\tresolutionVertical = " + resolutionVertical +
                ",\n\tmaxBounces = " + maxBounces +
                "\n}";
    }
}
