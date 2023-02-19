package SceneData.Light;

import SceneData.DataTypes.MyColor;
import SceneData.DataTypes.MyPoint;
import SceneData.DataTypes.MyVector;

public class ParallelLight extends Light {
    private MyVector direction;

    public ParallelLight(MyColor colorVector, MyVector direction) {
        super(colorVector);
        this.direction = direction;
    }

    @Override
    public MyVector getDirection() {
        return direction;
    }

    @Override
    public MyPoint getPosition() {
        return new MyPoint();
    }

    @Override
    public String toString() {
        return "ParallelLight {" +
                super.toString() +
                ",\n\t\tdirection: " + direction +
                "\n\t\t}";
    }
}