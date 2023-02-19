package SceneData.Light;

import SceneData.DataTypes.MyColor;
import SceneData.DataTypes.MyPoint;
import SceneData.DataTypes.MyVector;

public class PointLight extends Light {
    private MyPoint position;

    public PointLight(MyColor colorVector, MyPoint position) {
        super(colorVector);
        this.position = position;
    }

    @Override
    public MyPoint getPosition() {
        return position;
    }

    @Override
    public MyVector getDirection() {
        return new MyVector();
    }

    @Override
    public String toString() {
        return "PointLight {" +
                super.toString() +
                ",\n\t\tposition: " + position +
                "\n\t\t}";
    }


}