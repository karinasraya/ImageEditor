import java.awt.Color;

/**
 * An image filter to invert colors.
 * 
 * @author Karina Soraya P  
 * @version 1.12/20181126 
 */
public class InvertFilter extends Filter
{
    public InvertFilter(String name)
    {
        super(name);
    }
    public void apply(OFImage image)
    {
        int height = image.getHeight();
        int width = image.getWidth();
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                Color pix = image.getPixel(x, y);
                image.setPixel(x, y, new Color(255 - pix.getRed(),
                                               255 - pix.getGreen(),
                                               255 - pix.getBlue()));
            }
        }
    }
}