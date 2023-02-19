package SceneData.Light;

import SceneData.DataTypes.MyColor;
import SceneData.DataTypes.MyPoint;
import SceneData.DataTypes.MyVector;

public class AmbientLight extends Light {
    public AmbientLight(MyColor colorVector) {
        super(colorVector);
    }

    @Override
    public MyVector getDirection() {
        return new MyVector();
    }

    @Override
    public MyPoint getPosition() {
        return new MyPoint();
    }

    @Override
    public String toString() {
        return "AmbientLight {" +
                super.toString() +
                "\n\t\t}";
    }
}