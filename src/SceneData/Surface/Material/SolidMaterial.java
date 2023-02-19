package SceneData.Surface.Material;

import SceneData.DataTypes.MyColor;

public class SolidMaterial extends Material {
    private MyColor color;

    public SolidMaterial(MyColor color, Phong phong, float reflectance, float transmittance, float refraction) {
        super(phong, reflectance, transmittance, refraction);
        this.color = color;
    }

    public SolidMaterial(){
        super();
        this.color = new MyColor();
    }

    /**
     * returns the color of the material, ignoring u and v
     */
    public MyColor getColor(float u, float v){//ignore u and v
        return color;
    }

    @Override
    public String toString() {
        return "SolidMaterial {" +
                "\n\t\t\tcolor: " + color +
                ", " + super.toString() +
                "\n\t\t\t}";
    }
}