import RayTracing.RayTracer;
import Reader_Writer.Exceptions.GIFWrtierException;
import Reader_Writer.Exceptions.PNGConverterException;
import Reader_Writer.Exceptions.XMLReaderException;
import Reader_Writer.GIFWriter;
import Reader_Writer.PNGConverter;
import Reader_Writer.XMLReader;
import SceneData.DataTypes.MyColor;
import SceneData.DataTypes.MyPoint;
import SceneData.Scene;
import SceneData.Surface.Sphere;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    final static String SCENE_PATH = "scenes/";
    final static String OUTPUT_PATH = "outputPictures/";
    final static Scanner inputScanner = new Scanner(System.in);
    private static boolean superSampling = false;
    private static XMLReader xmlReader;
    private static PNGConverter pngConverter;
    private static RayTracer rayTracer;
    private static Scene scene;
    private static MyColor[][] image;
    private static String filePath = "";

    public static void main(String[] args) {
        xmlReader = new XMLReader();
        pngConverter = new PNGConverter();
        rayTracer = new RayTracer();
        scene = new Scene();

        printInstructions();

        String[] userInput = userInteraction();
        superSampling = false;

        while(!userInput[0].equalsIgnoreCase("exit")) {

            if(userInput == null || userInput[0].isEmpty()) {
                userInput = userInteraction();
                continue;
            }

            if(userInput.length > 1 && userInput[1].equalsIgnoreCase("supersampling")){
                superSampling = true;
            }
            else{
                superSampling = false;
            }
            
            switch (userInput[0]){
                case "all" -> createAllScenes();
                case "animation" -> createAnimation();
                case "motionblur" -> createMotionBlur();
                case "spotlight" -> createSpotlight();
                case "exit" -> System.out.println("Exiting...");
                case "help" -> printInstructions();
                default -> {
                    filePath = getFilePath(userInput[0]);

                    try {
                        developeScene();

                    } catch (XMLReaderException | PNGConverterException e) {
                        System.out.println(e.getMessage());
                        userInput = userInteraction();
                        continue;
                    }
                }
            }
            userInput = userInteraction();
        }
    }

    /**
     * reads the wanted scene, raytrace it and creates a png image
     * @throws XMLReaderException if the xml file is not found/could not be read
     * @throws PNGConverterException if the png image could not be created
     */
    private static void developeScene() throws XMLReaderException, PNGConverterException{
        scene.clear();
        //read the scene xml file and create a scene object
        scene = xmlReader.readXML(filePath, SCENE_PATH);
        System.out.println("\nScene '" + filePath + "' read sucessfully!");

        //ray trace the scene
        image = rayTracer.rayTrace(scene, superSampling);
        //create a png image
        pngConverter.createPNG(image, OUTPUT_PATH + scene.getOutputFileName());
    }

    /**
     * creates all the scenes
     */
    private static void createAllScenes() {
        for(int i = 1; i <=8; i++){
            filePath = getFilePath("example" + i);

            try {
                developeScene();

            } catch (XMLReaderException | PNGConverterException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * creates an image with a spotlight
     */
    private static void createSpotlight() {
        filePath = getFilePath("spotlight");

        try {
            developeScene();

        } catch (XMLReaderException | PNGConverterException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Creates an animation of spheres moving them
     */
    private static void createMotionBlur() {
        ArrayList<MyColor[][]> images = new ArrayList<>();

        scene.clear();
        try {
            //read the scene xml file and create a scene object
            scene = xmlReader.readXML(SCENE_PATH + "motionBlur.xml", SCENE_PATH);

            System.out.println("Creating motion blur...");

            //move the spheresdown
            for(int i = 3; i >=1; i--){
                float finalI = i * 0.03f;

                scene.getSurfaces().forEach(surface -> {
                    if(surface instanceof Sphere){
                        Sphere sphere = (Sphere) surface;

                        MyPoint position = sphere.getOrigin();
                        position.setY(position.getY() - finalI);
                        sphere.setOrigin(position);
                    }
                });
                //ray trace the scene and add the image to the list
                images.add(rayTracer.rayTrace(scene, false));
            }

            //reset the spheres to their original position
            scene = xmlReader.readXML(SCENE_PATH + "motionBlur.xml", SCENE_PATH);

            //move the spheres up
            for(int i = 1; i <= 3; i++){
                float finalI = i * 0.03f;

                scene.getSurfaces().forEach(surface -> {
                    if(surface instanceof Sphere){
                        Sphere sphere = (Sphere) surface;

                        MyPoint position = sphere.getOrigin();
                        position.setY(position.getY() + finalI);
                        sphere.setOrigin(position);
                    }
                });
                //ray trace the scene and add the image to the list
                images.add(rayTracer.rayTrace(scene, false));
            }

            //average the pixel color of the images and create a png image
            MyColor[][] finalImage = new MyColor[scene.getCamera().getHeight()][scene.getCamera().getWidth()];

            for(int y = 0; y < scene.getCamera().getHeight(); y++){
                for(int x = 0; x < scene.getCamera().getWidth(); x++){
                    MyColor pixelColor = new MyColor();
                    for(MyColor[][] image : images){
                        pixelColor.add(image[y][x]);
                    }
                    pixelColor = pixelColor.scale(1.0f/images.size());
                    finalImage[y][x] = pixelColor;
                }
            }

            //create a png image of the final image
            pngConverter.createPNG(finalImage, OUTPUT_PATH + scene.getOutputFileName());
        }
        catch (XMLReaderException | PNGConverterException e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * Creates an animation of spheres shrinking and growing by changing the radius
     */
    private static void createAnimation() {
        GIFWriter gifWriter = new GIFWriter();

        ArrayList<MyColor[][]> images = new ArrayList<>();

        scene.clear();
        try {
            //read the scene xml file and create a scene object
            scene = xmlReader.readXML(SCENE_PATH + "example2.xml", SCENE_PATH);

            System.out.println("Creating animation...");

            //shrink the spheres
            for(int i = 5; i >=1; i--){
                float finalI = i * 0.1f;

                scene.getSurfaces().forEach(surface -> {
                    if(surface instanceof Sphere){
                        Sphere sphere = (Sphere) surface;
                        sphere.setRadius(sphere.getRadius() - finalI);
                    }
                });
                //ray trace the scene and add the image to the list
                images.add(rayTracer.rayTrace(scene, false));
            }

            //reset the scene
            scene = xmlReader.readXML(SCENE_PATH + "example2.xml", SCENE_PATH);

            //grow the spheres
            for(int i = 1; i <= 5; i++){
                float finalI = i * 0.1f;

                scene.getSurfaces().forEach(surface -> {
                    if(surface instanceof Sphere){
                        Sphere sphere = (Sphere) surface;
                        sphere.setRadius(sphere.getRadius() + finalI);
                    }
                });
                //ray trace the scene and add the image to the list
                images.add(rayTracer.rayTrace(scene, false));
            }

            //create a gif image with the images list
            gifWriter.createGIF(images, OUTPUT_PATH + "animation.gif", scene.getCamera().getWidth(), scene.getCamera().getHeight());
        }
        catch (XMLReaderException | GIFWrtierException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void printInstructions(){
        System.out.println("\nCommands:"+
                "\n- Type in the scene name of the scene file (e.g.: example1, example2)" +
                "\n- To create all scenes (example1 - example8), type in 'all'" +
                "\n- To create an animation, type in 'animation'" +
                "\n- To create a scene with motion blur, type in 'motionblur'" +
                "\n- To create a scene with a spotlight, type in 'spotlight'" +
                "\n- To create a scene with super sampling, type in 'sceneName/all -supersampling'" +
                "\n- To see the instructions again, type in 'help'"+
                "\n- To exit, type in 'exit'."
        );
    }

    /**
     * Reads the user input and returns the scene/effect name and the extra if there is one
     * @return String array with the scene/effect name and the extra if there is one
     */
    private static String[] userInteraction(){
        System.out.print("\nScene/Effect name: ");

        return inputScanner.nextLine().split(" -");
    }

    /**
     * Returns the file path of the scene file
     * @param userInput the scene name
     * @return the file path of the scene file
     */
    private static String getFilePath(String userInput){
        return SCENE_PATH + userInput.replace(" ", "") + ".xml";
    }
}