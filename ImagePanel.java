import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A JPanel depicting a scaled image.
 * @author Martin Luerssen
 */
public class ImagePanel extends JPanel {
    private Image image;
    private Dimension size = new Dimension();
    private JLabel xlabel = new JLabel("(not yet rendered)");
    private int x_offset, y_offset;
 
    public ImagePanel() {
        add(xlabel);
    }
    
    @Override
    public Dimension getPreferredSize() { 
        return size; 
    }
    
    @Override
    public void paintComponent(Graphics g) {
        xlabel.setVisible(image == null);
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, x_offset, y_offset, this);
        } 
    }
    
    public void setImage(BufferedImage _image) {
        if (_image != null) {
            float imgAspect = (float)_image.getWidth()/_image.getHeight();
            float drawAspect = (float)getWidth() / getHeight();
            if (imgAspect > drawAspect) {
               image = _image.getScaledInstance(getWidth(), (int)(getHeight()/imgAspect*drawAspect), Image.SCALE_SMOOTH); 
               x_offset = 0;
               y_offset = (getHeight()-(int)(getHeight()/imgAspect*drawAspect))/2;
            } else {
               image = _image.getScaledInstance((int)(getWidth()/drawAspect*imgAspect), getHeight(), Image.SCALE_SMOOTH);  
               x_offset = (getWidth()-(int)(getWidth()/drawAspect*imgAspect))/2;
               y_offset = 0;
            }
            size.setSize(image.getWidth(this), image.getHeight(this));
        } else {
            image = null;
            size.setSize(1, 1);
        }
    }
        
}
