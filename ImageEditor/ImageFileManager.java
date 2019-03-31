 import java.awt.image.*;  
 import javax.imageio.*;  
 import java.io.*;  
 /**  
  * ImageFileManager is a small utility class with static methods to load  
  * and save images.  
  *   
  * The files on disk can be in JPG or PNG image format. For files written  
  * by this class, the format is determined by the constant IMAGE_FORMAT.  
  *   
  * @author Karina Soraya P  
  * @version 1.5/20181126  
  */  
 public class ImageFileManager
{
    private static final String IMAGE_FORMAT = "jpg";
    public static OFImage loadImage(File imageFile)
    {
        try {
            BufferedImage image = ImageIO.read(imageFile);
            if(image == null || (image.getWidth(null) < 0)) {
                // we could not load the image - probably invalid file format
                return null;
            }
            return new OFImage(image);
        }
        catch(IOException exc) {
            return null;
        }
    }
    public static void saveImage(OFImage image, File file)
    {
        try {
            ImageIO.write(image, IMAGE_FORMAT, file);
        }
        catch(IOException exc) {
            return;
        }
    }
}