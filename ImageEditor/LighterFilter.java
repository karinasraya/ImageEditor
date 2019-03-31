 /**  
  * An image filter to make the image a bit lighter.  
  *   
  * @author Karina Soraya P  
  * @version 1.2/20181126  
  */  
public class LighterFilter extends Filter
{
    public LighterFilter(String name)
        {
        super(name);
	}
    public void apply(OFImage image)
    {
        int height = image.getHeight();
        int width = image.getWidth();
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                image.setPixel(x, y, image.getPixel(x, y).brighter());
            }
        }
    }
}
