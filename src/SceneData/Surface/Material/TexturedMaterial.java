package SceneData.Surface.Material;

import Reader_Writer.PNGConverter;
import SceneData.DataTypes.MyColor;

public class TexturedMaterial extends Material {
    private static final PNGConverter pngConverter = new PNGConverter();
    private String texturePath;
    private MyColor[][] textureImage;
    private int width;
    private int height;

    public TexturedMaterial(String texture, Phong phong, float reflectance, float transmittance, float refraction) {
        super(phong, reflectance, transmittance, refraction);
        this.texturePath = texture;

        //Read the texture image and store it in a 2D array
        textureImage = pngConverter.readPNG(texturePath);
        //set the width and height of the texture
        height = textureImage.length;
        width = textureImage[0].length;
    }

    public TexturedMaterial(){
        super();
        this.texturePath = "";
        this.width = 0;
        this.height = 0;
    }

    @Override
    public MyColor getColor(float u, float v) {
        //If no texture is given, return black
        if(texturePath.equals("") || textureImage == null){
            return new MyColor();
        }

        if(u > 1){
            u %= 1;
        }
        if(v > 1){
            v %= 1;
        }

        int y = (int) (v * (height-1));
        int x = (int) (u * (width-1));

        return textureImage[y][x];
    }

    @Override
    public String toString() {
        return "TexturedMaterial {" +
                "\n\t\t\ttexture: '" + texturePath + '\'' +
                ", " + super.toString() +
                "\n\t\t\t}";
    }
}