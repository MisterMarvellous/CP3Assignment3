package flinders.mandelbrot;

import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * Mandelbrot parameters.
 * @author Martin Luerssen
 */
public class MandelSetting implements Serializable {
    
    private String name;
    private double min_real;
    private double max_real;
    private double min_imag;
    private double max_imag;
    private int max_iter;
    private int width;
    private int height;
    private BufferedImage rendered;
        
    public MandelSetting() {
        this("Default", -2, 1, -1.5, 1.5, 512, 512, 512);
    }
    
    public MandelSetting(String _name, double _min_real, double _max_real, double _min_imag, double _max_imag, int _max_iter, int _width, int _height) {
        name = _name;
        min_real = _min_real;
        max_real = _max_real;
        min_imag = _min_imag;
        max_imag = _max_imag;
        max_iter = _max_iter;
        width = _width;
        height = _height;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMinReal() {
        return min_real;
    }
    
    public void setMinReal(double min_real) {
        if (min_real != this.min_real) {
            this.min_real = min_real;
            rendered = null;
        }
    }
    
    public double getMaxReal() {
        return max_real;
    }

    public void setMaxReal(double max_real) {
        if (max_real != this.max_real) {
            this.max_real = max_real;
            rendered = null;
        }
    }
    
    public double getMinImaginary() {
        return min_imag;
    }

    public void setMinImaginary(double min_imag) {
        if (min_imag != this.min_imag) {
            this.min_imag = min_imag;
            rendered = null;
        }
    }
    
    public double getMaxImaginary() {
        return max_imag;
    }

    public void setMaxImaginary(double max_imag) {
        if (max_imag != this.max_imag) {
            this.max_imag = max_imag;
            rendered = null;
        }
    }

    public int getMaxIterations() {
        return max_iter;
    }

    public void setMaxIterations(int max_iter) {
        if (max_iter != this.max_iter) {
            this.max_iter = max_iter;
            rendered = null;
        }
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        if (width != this.width) {
            this.width = width;
            rendered = null;
        }
    }
    
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        if (height != this.height) {
            this.height = height;
            rendered = null;
        }
    }
    
    public BufferedImage getImage() {
        return rendered;
    }
    
    public void setImage(BufferedImage img) {
        rendered = img;
    }
    
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        name = (String)in.readObject();
        min_real = in.readDouble();
        max_real = in.readDouble();
        min_imag = in.readDouble();
        max_imag = in.readDouble();
        max_iter = in.readInt();
        width = in.readInt();
        height = in.readInt();
    }
    
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.writeObject(name);
        out.writeDouble(min_real);
        out.writeDouble(max_real);
        out.writeDouble(min_imag);
        out.writeDouble(max_imag);
        out.writeInt(max_iter);
        out.writeInt(width);
        out.writeInt(height);
    }
        
    /**
     * For the formatting of individual cells in the parameter variations list...
     */
    static class SettingListCellRenderer extends JLabel implements ListCellRenderer {
         final static ImageIcon tickIcon = new ImageIcon("Assets/tick.gif");
         final static ImageIcon incmpltIcon = new ImageIcon("Assets/incomplete.gif");

         @Override
         public Component getListCellRendererComponent(
           JList list,
           Object value,
           int index,
           boolean isSelected,
           boolean cellHasFocus)
         {
             MandelSetting m = (MandelSetting)value;
             
             if (m.getImage() != null) {
                setIcon(tickIcon);
             } else {
                setIcon(incmpltIcon);
             }
             setText(m.getName());
             if (isSelected) {
                 setBackground(list.getSelectionBackground());
                 setForeground(list.getSelectionForeground());
             } else {
                 setBackground(list.getBackground());
                 setForeground(list.getForeground());
             }
             setEnabled(list.isEnabled());
             setFont(list.getFont());
             setOpaque(true);
             return this;
         }
     }
}
