package SceneData.Light;

import SceneData.DataTypes.MyColor;
import SceneData.DataTypes.MyPoint;
import SceneData.DataTypes.MyVector;

public class SpotLight extends Light{

    private MyPoint position;
    private MyVector direction;
    private float angle1, angle2;

    public SpotLight(MyColor colorVector, MyPoint position, MyVector direction, float angle1, float angle2) {
        super(colorVector);
        this.position = position;
        this.direction = direction;
        this.angle1 = angle1;
        this.angle2 = angle2;
    }

    @Override
    public MyPoint getPosition() {
        return position;
    }

    @Override
    public MyVector getDirection() {
        return direction;
    }

    /**
     * calculates the angle between the light direction and the vector pointing to the light
     * @param vectorToLight the vector pointing to the light
     * @return the angle in degrees
     */
    public float getAngle(MyVector vectorToLight){
        //angle between the light direction and the vector to the light
        //cos(angle) = (a dot b) / (|a| * |b|)
        float dotProduct = MyVector.dotProduct(direction.getNormalized(), vectorToLight.getNormalized());
        return (float) Math.toDegrees(Math.acos(dotProduct));
    }

    /**
     * Calculates the light intensity in a given direction
     * @param vectorToLight the vector pointing to the light
     * @return the light intensity between 0 and 1
     */
    public float getLightIntensity(MyVector vectorToLight) {
        //get angle between light direction and vector to light
        float angle = getAngle(vectorToLight);

        //from 0 to angle1 -> full intensity
        if(angle < angle1){
            return 1.0f;
        }
        //from angle2 on -> no intensity
        if(angle > angle2){
            return 0.0f;
        }
        //between angle1 and angle2 -> linear falloff
        else{
            return (angle2 - angle) / (angle2 - angle1);
        }
    }

    @Override
    public String toString() {
        return "SpotLight {" +
                super.toString() +
                ",\n\t\tposition: " + position +
                ",\n\t\tdirection: " + direction +
                ",\n\t\tangle1: " + angle1 +
                ",\n\t\tangle2: " + angle2 +
                "\n\t\t}";
    }
}
