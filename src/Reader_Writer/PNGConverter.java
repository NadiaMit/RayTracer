package Reader_Writer;

import SceneData.DataTypes.MyColor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import Reader_Writer.Exceptions.PNGConverterException;

public class PNGConverter {
    /**
     * Creates a PNG image from a 2D array of {@link MyColor}
     * @param image the 2D array of {@link MyColor}
     * @param outputFilePath the path where the image will be saved
     */
    public void createPNG(MyColor[][] image, String outputFilePath) throws PNGConverterException {

        if(image.length == 0 || image[0].length == 0){
            throw new PNGConverterException("Error while creating PNG image: image is empty.");
        }

        int height = image.length;
        int width = image[0].length;

        try {
            //create a new bufferedImage with the same dimensions as the image array
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            //go through each pixel in the image array and set the corresponding pixel in the bufferedImage
            for(int y = 0; y <height; y++){
                for(int x = 0; x < width; x++){
                    Color color = image[y][x].getConvertedRGB();
                    bufferedImage.setRGB(x, y, color.getRGB());
                }
            }

            //create and save the image as PNG
            ImageIO.write(bufferedImage, "PNG", new File(outputFilePath));

            System.out.println("PNG created under: '" + outputFilePath+"'!");

        } catch (IOException ie) {
            throw new PNGConverterException("Could not save the image to the specified path: " + outputFilePath);
        }
    }

    /**
     * Reads a PNG image and returns a 2D array of {@link MyColor}
     * @param inputFilePath the path of the image
     * @return a 2D array of {@link MyColor}
     * @throws PNGConverterException if the image could not be read/found
     */
    public MyColor[][] readPNG(String inputFilePath) throws PNGConverterException {
        try {
            //read the image and save it as a bufferedImage
            BufferedImage image = ImageIO.read(new File(inputFilePath));

            int width = image.getWidth();
            int height = image.getHeight();
            MyColor[][] pixels = new MyColor[height][width];

            //convert the bufferedImage to a 2D array of MyColor
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    Color color = new Color(image.getRGB(x, y));
                    //convert the RGB values to float values between 0 and 1
                    pixels[y][x] = new MyColor((float) color.getRed()/255, (float) color.getGreen()/255, (float) color.getBlue()/255);
                }
            }
            return pixels;

        } catch (IOException ie) {
            throw new PNGConverterException("Could not read image: " + inputFilePath);
        }
    }
}
