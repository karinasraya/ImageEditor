import java.awt.Color;
import java.util.List;
import java.util.ArrayList;
/**
 * An image filter to detect edges and highlight them, a bit like 
 * a colored pencil drawing.
 * 
 * @author Karina Soraya P  
 * @version 1.9/20181126 
 */
public class EdgeFilter extends Filter
{
    private static final int TOLERANCE = 20;   
    private OFImage original;
    private int width;
    private int height;
    public EdgeFilter(String name)
    {
        super(name);
    }
    public void apply(OFImage image)
    {
        original = new OFImage(image);
        width = original.getWidth();
        height = original.getHeight();
        
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                image.setPixel(x, y, edge(x, y));
            }
        }
    }
    private Color edge(int xpos, int ypos)
    {
        List<Color> pixels = new ArrayList<Color>(9);
        
        for(int y = ypos-1; y <= ypos+1; y++) {
            for(int x = xpos-1; x <= xpos+1; x++) {
                if( x >= 0 && x < width && y >= 0 && y < height ) {
                    pixels.add(original.getPixel(x, y));
                }
            }
        }

        return new Color(255 - diffRed(pixels), 255 - diffGreen(pixels), 255 - diffBlue(pixels));
    }
    private int diffRed(List<Color> pixels)
    {
        int max = 0;
        int min = 255;
        for(Color color : pixels) {
            int val = color.getRed();
            if(val > max) {
                max = val;
            }
            if(val < min) {
                min = val;
            }
        }
        int difference = max - min - TOLERANCE;
        if(difference < 0) {
            difference = 0;
        }
        return difference;
    }
    private int diffGreen(List<Color> pixels)
    {
        int max = 0;
        int min = 255;
        for(Color color : pixels) {
            int val = color.getGreen();
            if(val > max) {
                max = val;
            }
            if(val < min) {
                min = val;
            }
        }
        int difference = max - min - TOLERANCE;
        if(difference < 0) {
            difference = 0;
        }
        return difference;
    }
    private int diffBlue(List<Color> pixels)
    {
        int max = 0;
        int min = 255;
        for(Color color : pixels) {
            int val = color.getBlue();
            if(val > max) {
                max = val;
            }
            if(val < min) {
                min = val;
            }
        }
        int difference = max - min - TOLERANCE;
        if(difference < 0) {
            difference = 0;
        }
        return difference;
    }
}