package Reader_Writer;

import SceneData.DataTypes.MyColor;
import com.squareup.gifencoder.GifEncoder;
import Reader_Writer.Exceptions.GIFWrtierException;
import com.squareup.gifencoder.ImageOptions;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GIFWriter {
    /**
     * Creates a GIF from a list of images
     * @param imageList List of images as {@link MyColor}[][]
     * @param outputGifPath Path + name where the GIF should be saved
     * @param width Width of the GIF/Source images
     * @param height Height of the GIF/Source images
     * @throws GIFWrtierException If an error occurs while creating the GIF
     */
    public void createGIF(List<MyColor[][]> imageList, String outputGifPath, int width, int height) throws GIFWrtierException {
        PNGConverter pngConverter = new PNGConverter();
        int imageCount = imageList.size();

        //create outputstream & options
        try (FileOutputStream outputStream = new FileOutputStream(outputGifPath)) {
            ImageOptions options = new ImageOptions();

            //Set 200ms between each frame
            options.setDelay(200, TimeUnit.MILLISECONDS);

            //Create GIF encoder with same dimension as of the source images
            GifEncoder gifEncoder = new GifEncoder(outputStream, width, height, 0);

            //convert every image to int[][] and add it to the encoder
            for(int i = 0; i < imageCount; i++) {
                gifEncoder.addImage(convertToInt(imageList.get(i)), options);
            }
            //Start the encoding
            gifEncoder.finishEncoding();

            System.out.println("GIF created under: '" + outputGifPath+"'!");
        } catch (IOException e) {
            throw new GIFWrtierException("Error while creating GIF");
        }
    }

    /**
     * Converts a {@link MyColor}[][] image to a int[][]
     * @param image Image to convert
     * @return Converted image
     */
    private int[][] convertToInt(MyColor[][] image) {
        int[][] intImage = new int[image.length][image[0].length];
        
        for(int y = 0; y < image.length; y++) {
            for(int x = 0; x < image[y].length; x++) {
                intImage[y][x] = image[y][x].getRGB();
            }
        }
        
        return intImage;
    }
}
