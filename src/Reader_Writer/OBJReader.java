package Reader_Writer;

import Reader_Writer.Exceptions.OBJReaderException;
import SceneData.DataTypes.MyPoint;
import SceneData.DataTypes.MyVector;
import SceneData.Surface.Triangle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OBJReader {

    /**
     * Reads an OBJ file and returns a list of triangles
     * @param filePath Path to the OBJ file
     * @return List of triangles
     * @throws OBJReaderException If the file is not found
     */
    @SuppressWarnings("unchecked")
    public List<Triangle> readOBJ(String filePath) throws OBJReaderException {
        List<String> inputList;

        //read .obj file
        try {
            inputList = readInput(filePath);
        } catch (FileNotFoundException e) {
            throw new OBJReaderException("File '" + filePath + "' not found.");
        }

        List<Triangle> triangleList = new ArrayList<>();
        List<MyPoint> vertexList = new ArrayList<>();
        List<MyPoint> textureList = new ArrayList<>();
        List<MyVector> normalList = new ArrayList<>();

        List<Integer[]> vertexIndices = new ArrayList<>();
        List<Integer[]> textureIndices = new ArrayList<>();
        List<Integer[]> normalIndices = new ArrayList<>();
        boolean hasTexture = false;

        String[] wordsArray;

        //get vertices, normals and texture coordinates and face indices from the .obj file
        for (String row : inputList) {
            wordsArray = row.split(" ");

            if(wordsArray[0].equals("v")) {
                vertexList.add(new MyPoint(Float.parseFloat(wordsArray[1]), Float.parseFloat(wordsArray[2]), Float.parseFloat(wordsArray[3])));
            }
            else if(wordsArray[0].equals("vt")){
                textureList.add(new MyPoint(Float.parseFloat(wordsArray[1]), Float.parseFloat(wordsArray[2]), 0));
                hasTexture = true;
            }
            else if(wordsArray[0].equals("vn")) {
                normalList.add(new MyVector(Float.parseFloat(wordsArray[1]), Float.parseFloat(wordsArray[2]), Float.parseFloat(wordsArray[3])));
            }
            else if(wordsArray[0].equals("f")) {
                vertexIndices.add(new Integer[]{Integer.parseInt(wordsArray[1].split("/")[0]) - 1, Integer.parseInt(wordsArray[2].split("/")[0]) - 1, Integer.parseInt(wordsArray[3].split("/")[0]) - 1});
                normalIndices.add(new Integer[]{Integer.parseInt(wordsArray[1].split("/")[2]) - 1, Integer.parseInt(wordsArray[2].split("/")[2]) - 1, Integer.parseInt(wordsArray[3].split("/")[2]) - 1});

                if(hasTexture) {
                    textureIndices.add(new Integer[]{Integer.parseInt(wordsArray[1].split("/")[1]) - 1, Integer.parseInt(wordsArray[2].split("/")[1]) - 1, Integer.parseInt(wordsArray[3].split("/")[1]) - 1});
                 }
            }
        }

        //create triangles from the face indices
        for(int i = 0; i < vertexIndices.size(); i++) {
            MyPoint[] points = new MyPoint[3];
            MyPoint[] texturePoints = new MyPoint[3];
            MyVector normal;

            Integer[] vertexIndex = vertexIndices.get(i);
            points[0] = vertexList.get(vertexIndex[0]);
            points[1] = vertexList.get(vertexIndex[1]);
            points[2] = vertexList.get(vertexIndex[2]);

            Integer[] normalIndex = normalIndices.get(i);
            normal = normalList.get(normalIndex[0]);

            if(hasTexture){
                Integer[] textureIndex = textureIndices.get(i);
                texturePoints[0] = textureList.get(textureIndex[0]);
                texturePoints[1] = textureList.get(textureIndex[1]);
                texturePoints[2] = textureList.get(textureIndex[2]);
            }

            triangleList.add(new Triangle(points, texturePoints, normal));
        }

        return triangleList;
    }

    /**
     * Reads the input file and returns a list of strings
     * @param filePath Path to the input file
     * @return List of strings
     * @throws FileNotFoundException If the file is not found
     */
    private List<String> readInput(String filePath) throws FileNotFoundException {
        List<String> inputList = new ArrayList<>();
        Scanner myReader = new Scanner(new File(filePath));

        while (myReader.hasNextLine()) {
            inputList.add(myReader.nextLine());
        }

        return inputList;
    }

}
