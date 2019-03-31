 import java.awt.*;  
 import javax.swing.*;  
 import java.awt.image.*;  
 /**  
  * An ImagePanel is a Swing component that can display an OFImage.  
  * It is constructed as a subclass of JComponent with the added functionality  
  * of setting an OFImage that will be displayed on the surface of this  
  * component.  
  *   
  * @author Karina Soraya P  
  * @version 1.6/20181126  
  */  
 public class ImagePanel extends JComponent
{
    private int width, height;
    private OFImage panelImage;
    public ImagePanel()
    {
        width = 360;    
        height = 240;
        panelImage = null;
    }
    public void setImage(OFImage image)
    {
        if(image != null) {
            width = image.getWidth();
            height = image.getHeight();
            panelImage = image;
            repaint();
        }
    }
    public void clearImage()
    {
        Graphics imageGraphics = panelImage.getGraphics();
        imageGraphics.setColor(Color.LIGHT_GRAY);
        imageGraphics.fillRect(0, 0, width, height);
        repaint();
    }
    public Dimension getPreferredSize()
    {
        return new Dimension(width, height);
    }
    public void paintComponent(Graphics g)
    {
        Dimension size = getSize();
        g.clearRect(0, 0, size.width, size.height);
        if(panelImage != null) {
            g.drawImage(panelImage, 0, 0, null);
        }
    }
}