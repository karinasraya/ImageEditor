import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.border.*;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * ImageViewer is the main class of the image viewer application. It builds and
 * displays the application GUI and initialises all other components.
 * 
 * To start the application, create an object of this class.
 * 
 * @author Karina Soraya P  
 * @version 1.7/20181126  
 */
public class ImageViewer
{
    private static final String VERSION = "Version 3.1";
    private static JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
    private JFrame frame;
    private ImagePanel imagePanel;
    private JLabel filenameLabel;
    private JLabel statusLabel;
    private JButton smallerButton;
    private JButton textButton;
    private JButton largerButton;
    private JButton cropButton;
    private JButton rotButton;
    private JButton rotaButton;
    private JButton rotateButton;
    private OFImage currentImage;    
    private List<Filter> filters;
    
    public ImageViewer()
    {
        currentImage = null;
        filters = createFilters();
        makeFrame();
    }

    private void makeText()
    {
        if(currentImage != null) {
            int width = currentImage.getWidth();
            int height = currentImage.getHeight();
            int xPosition = Integer.parseInt(JOptionPane.showInputDialog("Posisi X"));
            int yPosition = Integer.parseInt(JOptionPane.showInputDialog("Posisi Y"));
            float fontSize = Float.parseFloat(JOptionPane.showInputDialog("Besar Font"));
            String addText = JOptionPane.showInputDialog("Ketik sesuatu..");
            OFImage newImage = new OFImage(width, height);
            
            // copy pixel data into new image
            for(int y = 0; y < height; y++) {
                for(int x = 0; x < width; x++) {
                    Color col = currentImage.getPixel(x, y);
                    newImage.setPixel(x, y, col);
                }
            }
            
            Graphics g = newImage.getGraphics();
            g.setFont(g.getFont().deriveFont(fontSize));
            g.drawString(addText, xPosition, yPosition);
            g.dispose();
            
            currentImage = newImage;
            imagePanel.setImage(currentImage);
        }
    }
    private void openFile()
    {
        int returnVal = fileChooser.showOpenDialog(frame);

        if(returnVal != JFileChooser.APPROVE_OPTION) {
            return;  
        }
        File selectedFile = fileChooser.getSelectedFile();
        currentImage = ImageFileManager.loadImage(selectedFile);
        
        if(currentImage == null) {   
            JOptionPane.showMessageDialog(frame,
                    "The file was not in a recognized image file format.",
                    "Image Load Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        imagePanel.setImage(currentImage);
        setButtonsEnabled(true);
        showFilename(selectedFile.getPath());
        showStatus("File loaded.");
        frame.pack();
    }
    private void close()
    {
        currentImage = null;
        imagePanel.clearImage();
        showFilename(null);
        setButtonsEnabled(false);
    }
    private void saveAs()
    {
        if(currentImage != null) {
            int returnVal = fileChooser.showSaveDialog(frame);
    
            if(returnVal != JFileChooser.APPROVE_OPTION) {
                return;  
            }
            File selectedFile = fileChooser.getSelectedFile();
            ImageFileManager.saveImage(currentImage, selectedFile);
            
            showFilename(selectedFile.getPath());
        }
    }
    private void quit()
    {
        System.exit(0);
    }
    private void applyFilter(Filter filter)
    {
        if(currentImage != null) {
            filter.apply(currentImage);
            frame.repaint();
            showStatus("Applied: " + filter.getName());
        }
        else {
            showStatus("No image loaded.");
        }
    }
    private void showAbout()
    {
        JOptionPane.showMessageDialog(frame, 
                    "ImageViewer\n" + VERSION,
                    "About ImageViewer", 
                    JOptionPane.INFORMATION_MESSAGE);
    }
    private void makeLarger()
    {
        if(currentImage != null) {
            int width = currentImage.getWidth();
            int height = currentImage.getHeight();
            OFImage newImage = new OFImage(width * 2, height * 2);
            for(int y = 0; y < height; y++) {
                for(int x = 0; x < width; x++) {
                    Color col = currentImage.getPixel(x, y);
                    newImage.setPixel(x * 2, y * 2, col);
                    newImage.setPixel(x * 2 + 1, y * 2, col);
                    newImage.setPixel(x * 2, y * 2 + 1, col);
                    newImage.setPixel(x * 2+1, y * 2 + 1, col);
                }
            }
            
            currentImage = newImage;
            imagePanel.setImage(currentImage);
            frame.pack();
        }
    }
    private void makeSmaller()
    {
        if(currentImage != null) {
            int width = currentImage.getWidth() / 2;
            int height = currentImage.getHeight() / 2;
            OFImage newImage = new OFImage(width, height);
            for(int y = 0; y < height; y++) {
                for(int x = 0; x < width; x++) {
                    newImage.setPixel(x, y, currentImage.getPixel(x * 2, y * 2));
                }
            }
            
            currentImage = newImage;
            imagePanel.setImage(currentImage);
            frame.pack();
        }
    }
    private void crop()
    {
        if (currentImage != null)
        {
            int width = currentImage.getWidth();
            int height = currentImage.getWidth();
            int xAwal = Integer.parseInt(JOptionPane.showInputDialog("x.akhir"));
            int yAwal = Integer.parseInt(JOptionPane.showInputDialog("y.akhir"));
            int xAkhir = Integer.parseInt(JOptionPane.showInputDialog("x.awal"));
            int yAkhir = Integer.parseInt(JOptionPane.showInputDialog("y.awal"));
            OFImage newImage = new OFImage(xAkhir - xAwal, yAkhir - yAwal);
            
            for (int y = 0; y < yAkhir - yAwal; y++)
            {
                for (int x = 0; x < xAkhir - xAwal; x++)
                {
                    newImage.setPixel(x, y, currentImage.getPixel(x + xAwal, y + yAwal));
                }
            }
            
            currentImage = newImage;
            imagePanel.setImage(currentImage);
            frame.pack();
        }
    }

    private void showFilename(String filename)
    {
        if(filename == null) {
            filenameLabel.setText("No file displayed.");
        }
        else {
            filenameLabel.setText("File: " + filename);
        }
    }
    private void showStatus(String text)
    {
        statusLabel.setText(text);
    }
    private void setButtonsEnabled(boolean status)
    {
        smallerButton.setEnabled(status);
        largerButton.setEnabled(status);
        cropButton.setEnabled(status);
        rotButton.setEnabled(status);
        rotaButton.setEnabled(status);
        rotateButton.setEnabled(status);
    }
    private List<Filter> createFilters()
    {
        List<Filter> filterList = new ArrayList<Filter>();
        filterList.add(new DarkerFilter("Darker"));
        filterList.add(new LighterFilter("Lighter"));
        filterList.add(new ThresholdFilter("Threshold"));
        filterList.add(new InvertFilter("Invert"));
        filterList.add(new SolarizeFilter("Solarize"));
        filterList.add(new SmoothFilter("Smooth"));
        filterList.add(new PixelizeFilter("Pixelize"));
        filterList.add(new MirrorFilter("Mirror"));
        filterList.add(new GrayScaleFilter("Grayscale"));
        filterList.add(new EdgeFilter("Edge Detection"));
        filterList.add(new FishEyeFilter("Fish Eye"));
       
        return filterList;
    }
    private void makeFrame()
    {
        frame = new JFrame("ImageViewer");
        JPanel contentPane = (JPanel)frame.getContentPane();
        contentPane.setBorder(new EmptyBorder(6, 6, 6, 6));

        makeMenuBar(frame);
        contentPane.setLayout(new BorderLayout(6, 6));
        imagePanel = new ImagePanel();
        imagePanel.setBorder(new EtchedBorder());
        contentPane.add(imagePanel, BorderLayout.CENTER);
        filenameLabel = new JLabel();
        contentPane.add(filenameLabel, BorderLayout.NORTH);

        statusLabel = new JLabel(VERSION);
        contentPane.add(statusLabel, BorderLayout.SOUTH);
        JPanel toolbar = new JPanel();
        toolbar.setLayout(new GridLayout(0, 1));
        
        textButton = new JButton("Add Text");
        textButton.addActionListener(new ActionListener() {
                               public void actionPerformed(ActionEvent e) { makeText();}
                           });
        toolbar.add(textButton);
        
        smallerButton = new JButton("Smaller");
        smallerButton.addActionListener(new ActionListener() {
                               public void actionPerformed(ActionEvent e) { makeSmaller(); }
                           });
        toolbar.add(smallerButton);
        
        largerButton = new JButton("Larger");
        largerButton.addActionListener(new ActionListener() {
                               public void actionPerformed(ActionEvent e) { makeLarger(); }
                           });
        toolbar.add(largerButton);
        
        cropButton = new JButton("Crop");
        cropButton.addActionListener(new ActionListener() {
                               public void actionPerformed(ActionEvent e) { crop(); }
                           });
        toolbar.add(cropButton);
        
        rotateButton = new JButton("Rotate 180");
        rotateButton.addActionListener(new ActionListener() {
                               public void actionPerformed(ActionEvent e) { Rotate180(); }
                           });
        toolbar.add(rotateButton);
        
        rotButton = new JButton("Rotate 90 Left");
        rotButton.addActionListener(new ActionListener() {
                               public void actionPerformed(ActionEvent e) { Rotate90left(); }
                           });
        toolbar.add(rotButton);
        
        rotaButton = new JButton("Rotate 90 Right");
        rotaButton.addActionListener(new ActionListener() {
                               public void actionPerformed(ActionEvent e) { Rotate90right(); }
                           });
        toolbar.add(rotaButton);
        
        JPanel flow = new JPanel();
        flow.add(toolbar);
        
        contentPane.add(flow, BorderLayout.WEST);   
        showFilename(null);
        setButtonsEnabled(false);
        frame.pack();       
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(d.width/2 - frame.getWidth()/2, d.height/2 - frame.getHeight()/2);
        frame.setVisible(true);
    }
    private void Rotate180() {
        if(currentImage != null) {
            // create new image with double size
            int width = currentImage.getWidth();
            int height = currentImage.getHeight();
            OFImage newImage = new OFImage(width, height);
            
            //copy pixel data into new image
            for(int y = 0; y < height; y++) {
                for(int x = 0; x < width; x++) {
                    Color col = currentImage.getPixel(width-x-1, height-y-1);
                    newImage.setPixel(x, y, col);
                }
            }
            
            currentImage = newImage;
            imagePanel.setImage(currentImage);
            frame.pack();
        }
    }

    private void Rotate90left() {
        if(currentImage != null) {
            // create new image with double size
            int width = currentImage.getWidth();
            int height = currentImage.getHeight();
            OFImage newImage = new OFImage(height, width);
            
            //copy pixel data into new image
            for(int y = 0; y < height; y++) {
                for(int x = 0; x < width; x++) {
                    Color col = currentImage.getPixel(x, y);
                    newImage.setPixel(y, width-x-1, col);
                }
            }
            
            currentImage = newImage;
            imagePanel.setImage(currentImage);
            frame.pack();
        }
    }
    
    private void Rotate90right() {
        if(currentImage != null) {
            // create new image with double size
            int width = currentImage.getWidth();
            int height = currentImage.getHeight();
            OFImage newImage = new OFImage(height, width);
            
            //copy pixel data into new image
            for(int y = 0; y < height; y++) {
                for(int x = 0; x < width; x++) {
                    Color col = currentImage.getPixel(x, y);
                    newImage.setPixel(height-y-1, x, col);
                }
            }
            
            currentImage = newImage;
            imagePanel.setImage(currentImage);
            frame.pack();
        }
    }
    private void makeMenuBar(JFrame frame)
    {
        final int SHORTCUT_MASK =
            Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

        JMenuBar menubar = new JMenuBar();
        frame.setJMenuBar(menubar);
        
        JMenu menu;
        JMenuItem item;
        
        // create the File menu
        menu = new JMenu("File");
        menubar.add(menu);
        
        item = new JMenuItem("Open...");
            item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, SHORTCUT_MASK));
            item.addActionListener(new ActionListener() {
                               public void actionPerformed(ActionEvent e) { openFile(); }
                           });
        menu.add(item);

        item = new JMenuItem("Close");
            item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, SHORTCUT_MASK));
            item.addActionListener(new ActionListener() {
                               public void actionPerformed(ActionEvent e) { close(); }
                           });
        menu.add(item);
        menu.addSeparator();

        item = new JMenuItem("Save As...");
            item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, SHORTCUT_MASK));
            item.addActionListener(new ActionListener() {
                               public void actionPerformed(ActionEvent e) { saveAs(); }
                           });
        menu.add(item);
        menu.addSeparator();
        
        item = new JMenuItem("Quit");
            item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, SHORTCUT_MASK));
            item.addActionListener(new ActionListener() {
                               public void actionPerformed(ActionEvent e) { quit(); }
                           });
        menu.add(item);

        // create the Filter menu
        menu = new JMenu("Effect");
        menubar.add(menu);
        
        for(final Filter filter : filters) {
            item = new JMenuItem(filter.getName());
            item.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) { 
                                    applyFilter(filter);
                                }
                           });
             menu.add(item);
         }

        // create the Help menu
        menu = new JMenu("Help");
        menubar.add(menu);
        
        item = new JMenuItem("About ImageViewer...");
            item.addActionListener(new ActionListener() {
                               public void actionPerformed(ActionEvent e) { showAbout(); }
                           });
        menu.add(item);

    }
}