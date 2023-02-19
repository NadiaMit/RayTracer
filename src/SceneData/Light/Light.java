package SceneData.Light;

import SceneData.DataTypes.MyColor;
import SceneData.DataTypes.MyPoint;
import SceneData.DataTypes.MyVector;

public abstract class Light {
    private MyColor colorVector;

    public Light(MyColor colorVector) {
        this.colorVector = colorVector;
    }

    @Override
    public String toString() {
        return "\n\t\tcolor: " + colorVector;
    }

    public MyColor getColor() {
        return colorVector;
    }

    public abstract MyVector getDirection();

    public abstract MyPoint getPosition();
}
