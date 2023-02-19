package Reader_Writer;

import Reader_Writer.Exceptions.XMLReaderException;
import SceneData.Camera;
import SceneData.DataTypes.*;
import SceneData.Light.*;
import SceneData.Scene;
import SceneData.Surface.Material.Material;
import SceneData.Surface.Material.Phong;
import SceneData.Surface.Material.SolidMaterial;
import SceneData.Surface.Material.TexturedMaterial;
import SceneData.Surface.Mesh;
import SceneData.Surface.Sphere;
import SceneData.Surface.Surface;
import SceneData.Surface.Triangle;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XMLReader {

    private String outputFileName;
    private MyColor backgroundColor;
    private Camera camera;
    private List<Light> lights = new ArrayList<>();
    private List<Surface> surfaces = new ArrayList<>();
    private static OBJReader objReader = new OBJReader();

    /**
     * Reads the XML file and creates a Scene object
     * @param filePath the path and name of the XML file
     * @return a Scene object
     * @throws XMLReaderException if the XML file is not found or could not be parsed
     */
    public Scene readXML(String filePath, String scenePath) throws XMLReaderException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        outputFileName = "";
        backgroundColor = new MyColor();
        camera = new Camera();
        lights = new ArrayList<>();
        surfaces = new ArrayList<>();

        try {
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "file");

            //parse XML file
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(filePath));
            doc.getDocumentElement().normalize();

            //get outputFileName
            outputFileName = doc.getDocumentElement().getAttribute("output_file");

            //get backgroundColor
            backgroundColor = (MyColor) readVector(doc.getElementsByTagName("background_color").item(0), EDataType.COLOR);

            //get camera
            NodeList nodeList = doc.getElementsByTagName("camera").item(0).getChildNodes();

            MyPoint position = new MyPoint();
            MyPoint lookAt = new MyPoint();
            MyVector up = new MyVector();
            int horizontalFov = 0;
            int resolutionHorizontal = 0;
            int resolutionVertical = 0;
            int maxBounces = 0;

            //go through every sub-node of the camera node and save the values
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    switch (node.getNodeName()){
                        case "position" -> position = (MyPoint) readVector(node, EDataType.POINT);
                        case "lookat" -> lookAt = (MyPoint) readVector(node, EDataType.POINT);
                        case "up" -> up = (MyVector) readVector(node, EDataType.VECTOR);
                        case "horizontal_fov" -> horizontalFov = Integer.parseInt(node.getAttributes().getNamedItem("angle").getNodeValue());
                        case "resolution" -> {
                            NamedNodeMap attributes = node.getAttributes();
                            resolutionHorizontal = Integer.parseInt(attributes.getNamedItem("horizontal").getNodeValue());
                            resolutionVertical = Integer.parseInt(attributes.getNamedItem("vertical").getNodeValue());
                        }
                        case "max_bounces" -> maxBounces = Integer.parseInt(node.getAttributes().getNamedItem("n").getNodeValue());
                    }
                }
            }

            //create camera object with the saved values
            camera = new Camera(position, lookAt, up, horizontalFov, resolutionHorizontal, resolutionVertical, maxBounces);


            //get lights
            nodeList = doc.getElementsByTagName("lights").item(0).getChildNodes();

            MyColor color = new MyColor();
            MyPoint lightPosition = new MyPoint();
            MyVector lightDirection = new MyVector();
            float angle1 = 0;
            float angle2 = 0;

            //go through every light-types, save the values and create a light object
            for(int i = 0; i < nodeList.getLength(); i++){
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    switch (node.getNodeName()){
                        case "ambient_light" -> {
                            //go through every sub-node of the ambient_light node and save the values
                            for(int j = 0; j < node.getChildNodes().getLength(); j++){
                                Node childNode = node.getChildNodes().item(j);
                                if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                                    switch (childNode.getNodeName()){
                                        case "color" -> {
                                            color = (MyColor) readVector(childNode, EDataType.COLOR);
                                        }
                                    }
                                }
                            }
                            //create ambient light object with the saved values
                            lights.add(new AmbientLight(color));
                        }
                        case "point_light" -> {
                            //go through every sub-node of the point_light node and save the values
                            for(int j = 0; j < node.getChildNodes().getLength(); j++){
                                Node childNode = node.getChildNodes().item(j);
                                if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                                    switch (childNode.getNodeName()){
                                        case "color" -> {
                                            color = (MyColor) readVector(childNode, EDataType.COLOR);
                                        }
                                        case "position" -> {
                                            lightPosition = (MyPoint) readVector(childNode, EDataType.POINT);
                                        }
                                    }
                                }
                            }
                            //create point light object with the saved values
                            lights.add(new PointLight(color, lightPosition));
                        }
                        case "parallel_light" -> {
                            //go through every sub-node of the parallel_light node and save the values
                            for(int j = 0; j < node.getChildNodes().getLength(); j++){
                                Node childNode = node.getChildNodes().item(j);
                                if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                                    switch (childNode.getNodeName()){
                                        case "color" -> {
                                            color = (MyColor) readVector(childNode, EDataType.COLOR);
                                        }
                                        case "direction" -> {
                                            lightDirection = (MyVector) readVector(childNode, EDataType.VECTOR);
                                        }
                                    }
                                }
                            }
                            //create parallel light object with the saved values
                            lights.add(new ParallelLight(color, lightDirection));
                        }
                        case "spot_light" -> {
                            //go through every sub-node of the spot_light node and save the values
                            for(int j = 0; j < node.getChildNodes().getLength(); j++){
                                Node childNode = node.getChildNodes().item(j);
                                if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                                    switch (childNode.getNodeName()){
                                        case "color" -> {
                                            color = (MyColor) readVector(childNode, EDataType.COLOR);
                                        }
                                        case "position" -> {
                                            lightPosition = (MyPoint) readVector(childNode, EDataType.POINT);
                                        }
                                        case "direction" -> {
                                            lightDirection = (MyVector) readVector(childNode, EDataType.VECTOR);
                                        }
                                        case "falloff" -> {
                                            NamedNodeMap attributes = childNode.getAttributes();
                                            angle1 = Float.parseFloat(attributes.getNamedItem("alpha1").getNodeValue());
                                            angle2 = Float.parseFloat(attributes.getNamedItem("alpha2").getNodeValue());
                                        }
                                    }
                                }
                            }
                            //create spot light object with the saved values
                            lights.add(new SpotLight(color, lightPosition, lightDirection, angle1, angle2));
                        }
                    }
                }
            }

            //get surfaces
            nodeList = doc.getElementsByTagName("surfaces").item(0).getChildNodes();
            
            float radius = 0.0F;
            MyPoint surfacePosition = new MyPoint();
            List<Triangle> triangles = new ArrayList<>();
            String meshName = "";
            Material material = null;

            //go through every surface-type, save the values and create a surface object
            for(int i = 0; i < nodeList.getLength(); i++){
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    switch (node.getNodeName()) {
                        case "sphere" -> {
                            //go through every sub-node of the sphere node and save the values
                            radius = Float.parseFloat(node.getAttributes().getNamedItem("radius").getNodeValue());
                            for (int j = 0; j < node.getChildNodes().getLength(); j++) {
                                Node childNode = node.getChildNodes().item(j);
                                if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                                    switch (childNode.getNodeName()) {
                                        case "position" -> {
                                            surfacePosition = (MyPoint) readVector(childNode, EDataType.POINT);
                                        }
                                        case "material_solid" -> {
                                            //reads the material node and if its a solid material, create a solid material object
                                            material = readMaterial(childNode, true, scenePath);
                                        }
                                        case "material_textured" -> {
                                            //reads the material node and if its a textured material, create a textured material object
                                            material = readMaterial(childNode, false, scenePath);
                                        }
                                    }
                                }
                            }
                            //create sphere object with the saved values
                            surfaces.add(new Sphere(radius, surfacePosition, material));
                        }
                        case "mesh" -> {
                            //get the name of the mesh file and read the triangles from the file
                            meshName = node.getAttributes().getNamedItem("name").getNodeValue();
                            triangles = objReader.readOBJ(scenePath + meshName);

                            //go through every sub-node of the sphere node and save the values
                            for (int j = 0; j < node.getChildNodes().getLength(); j++) {
                                Node childNode = node.getChildNodes().item(j);
                                if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                                    switch (childNode.getNodeName()) {
                                        case "material_solid" -> {
                                            //reads the material node and if its a solid material, create a solid material object
                                            material = readMaterial(childNode, true, scenePath);
                                        }
                                        case "material_textured" -> {
                                            //reads the material node and if its a textured material, create a textured material object
                                            material = readMaterial(childNode, false, scenePath);
                                        }
                                    }
                                }
                            }
                            //create mesh object with the saved values
                            surfaces.add(new Mesh(meshName, triangles, material));
                        }

                    }
                }
            }

        } catch (FileNotFoundException e){
            throw new XMLReaderException("File not found: " + filePath);
        }
        catch (ParserConfigurationException | SAXException | IOException e) {
            throw new XMLReaderException("Error while reading XML file: " + filePath);
        }

        //create scene object with the saved values
        return new Scene(outputFileName, backgroundColor, camera, lights, surfaces);
    }

    /**
     * Reads the material node and creates a material object
     * @param materialNode the material node
     * @param isSolid if the material is solid or not
     * @return the material object with the corresponding values
     */
    private Material readMaterial(Node materialNode, boolean isSolid, String scenePath) {
        Phong phong = new Phong();
        float reflectance = 0.0F;
        float transmittance = 0.0F;
        float refraction = 0.0F;
        MyColor color = new MyColor();
        String texture = scenePath;

        //go through every sub-node of the material node and save the values
        for(int i = 0; i < materialNode.getChildNodes().getLength(); i++){
            Node childNode = materialNode.getChildNodes().item(i);
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                switch (childNode.getNodeName()){
                    case "texture" -> {
                        texture  +=  childNode.getAttributes().getNamedItem("name").getNodeValue();
                    }
                    case "color" -> {
                        color = (MyColor) readVector(childNode, EDataType.COLOR);
                    }
                    case "phong" -> {
                        phong = readPhong(childNode);
                    }
                    case "reflectance" -> {
                        reflectance = Float.parseFloat(childNode.getAttributes().getNamedItem("r").getNodeValue());
                    }
                    case "transmittance" -> {
                        transmittance = Float.parseFloat(childNode.getAttributes().getNamedItem("t").getNodeValue());
                    }
                    case "refraction" -> {
                        refraction = Float.parseFloat(childNode.getAttributes().getNamedItem("iof").getNodeValue());
                    }
                }
            }
        }
        //if the material is solid, create a solid material object otherwise a textured material object
        return isSolid ?  new SolidMaterial(color, phong, reflectance, transmittance, refraction)
                        : new TexturedMaterial(texture, phong, reflectance, transmittance, refraction);
    }

    /**
     * Reads the phong node and creates a phong object
     * @param phongNode the phong node
     * @return the phong object with the corresponding values
     */
    private Phong readPhong(Node phongNode) {
        float ka = 0.0f;
        float kd = 0.0f;
        float ks = 0.0f;
        float exponent = 0.0f;

        NamedNodeMap attributes = phongNode.getAttributes();
        ka = Float.parseFloat(attributes.getNamedItem("ka").getNodeValue());
        kd = Float.parseFloat(attributes.getNamedItem("kd").getNodeValue());
        ks = Float.parseFloat(attributes.getNamedItem("ks").getNodeValue());
        exponent = Float.parseFloat(attributes.getNamedItem("exponent").getNodeValue());

        return new Phong(ka, kd, ks, exponent);
    }

    /**
     * Reads the element node and creates a DataType object corresponding to the datatype given
     * @param element the element note to read
     * @param dataType the type of DataType object to create (POINT, VECTOR, COLOR)
     * @return the DataType object with the corresponding values otherwise a null-Vector
     */
    private DataType readVector(Node element, EDataType dataType) {
        NamedNodeMap attributes = element.getAttributes();

        switch (dataType){
            case COLOR -> {
                float r = Float.parseFloat(attributes.getNamedItem("r").getNodeValue());
                float g = Float.parseFloat(attributes.getNamedItem("g").getNodeValue());
                float b = Float.parseFloat(attributes.getNamedItem("b").getNodeValue());
                return new MyColor(r, g, b);
            }
            case POINT -> {
                float x = Float.parseFloat(attributes.getNamedItem("x").getNodeValue());
                float y = Float.parseFloat(attributes.getNamedItem("y").getNodeValue());
                float z = Float.parseFloat(attributes.getNamedItem("z").getNodeValue());
                return new MyPoint(x, y, z);
            }
            case VECTOR -> {
                float x = Float.parseFloat(attributes.getNamedItem("x").getNodeValue());
                float y = Float.parseFloat(attributes.getNamedItem("y").getNodeValue());
                float z = Float.parseFloat(attributes.getNamedItem("z").getNodeValue());
                return new MyVector(x, y, z);
            }
        }
        return new MyVector();
    }
}
