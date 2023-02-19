package SceneData;

import SceneData.DataTypes.MyColor;
import SceneData.Light.Light;
import SceneData.Surface.Surface;

import java.util.ArrayList;
import java.util.List;

public class Scene {
    private String outputFileName;
    private MyColor backgroundColor;
    private Camera camera;
    private List<Light> lights;
    private List<Surface> surfaces;

    public Scene(String outputFileName, MyColor backgroundColor, Camera camera, List<Light> lights, List<Surface> surfaces) {
        this.outputFileName = outputFileName;
        this.backgroundColor = backgroundColor;
        this.camera = camera;
        this.lights = lights;
        this.surfaces = surfaces;
    }

    public Scene(){
        this.outputFileName = "";
        this.backgroundColor = new MyColor();
        this.camera = new Camera();
        this.lights = new ArrayList<>();
        this.surfaces = new ArrayList<>();
    }

    public void clear(){
        this.outputFileName = "";
        this.backgroundColor = new MyColor();
        this.camera = new Camera();
        this.lights = new ArrayList<>();
        this.surfaces = new ArrayList<>();
    }

    public Camera getCamera() {
        return camera;
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    public List<Surface> getSurfaces(){
        return surfaces;
    }

    public MyColor getBackgroundColor() {
        return backgroundColor;
    }

    public List<Light> getLights() {
        return lights;
    }

    @Override
    public String toString() {
        String returnString = "Scene {" +
                "\noutputFileName: '" + outputFileName + '\'' +
                ";\nbackgroundColor: " + backgroundColor +
                ";\ncamera: " + camera +
                ";\nlights = [";

        for(Light light : lights){
            returnString += "\n    " + light + ",";
        }
        returnString += "\n];";

        returnString += "\nsurfaces = [";
        for(Surface surface : surfaces){
            returnString += "\n    " + surface + ",";
        }
        returnString += "\n];\n}";

        return returnString;
    }
}
