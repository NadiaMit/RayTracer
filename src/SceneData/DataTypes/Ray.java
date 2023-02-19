package SceneData.DataTypes;

public class Ray {
    private MyPoint origin;
    private MyVector direction;
    private float t;

    public Ray(MyPoint origin, MyVector direction) {
        this.origin = origin;
        this.direction = direction;
        this.t = 0;
    }

    public Ray(MyPoint origin) {
        this.origin = origin;
        this.direction = new MyVector();
        this.t = 0;
    }

    public Ray() {
        this.origin = new MyPoint();
        this.direction = new MyVector();
        this.t = 0;
    }

    public MyPoint getOrigin() {
        return origin;
    }

    public MyVector getDirection() {
        return direction;
    }

    public float getT() {
        return t;
    }

    public void setT(float t) {
        this.t = t;
    }

    public void setOrigin(MyPoint point) {
        this.origin = point;
    }

    public void setDirection(MyVector vector) {
        this.direction = vector;
    }

    @Override
    public String toString() {
        return "Ray {" +
                "\n\torigin: " + origin +
                ",\n\tdirection: " + direction +
                "\n\t}";
    }
}
