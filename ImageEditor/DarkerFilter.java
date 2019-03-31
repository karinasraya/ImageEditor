/**  
  * An image filter to make the image a bit darker.  
  *   
  * @author Karina Soraya P  
  * @version 1.3/20181126  
  */  
 public class DarkerFilter extends Filter
{
    public DarkerFilter(String name)
    {
        super(name);
    }
    public void apply(OFImage image)
    {
        int height = image.getHeight();
        int width = image.getWidth();
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                image.setPixel(x, y, image.getPixel(x, y).darker());
            }
        }
    }
}