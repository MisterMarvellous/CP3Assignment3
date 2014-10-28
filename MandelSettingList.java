import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javax.swing.AbstractListModel;
import javax.swing.JOptionPane;

/**
 * Document model for the parameter variations list.
 * @author Martin Luerssen
 */
public class MandelSettingList extends AbstractListModel {
    
    private ArrayList<MandelSetting> settings;
    private int currentIndex = 0;
    private MandelBatchGUI gui;
    private String filename;
    
    public MandelSettingList(MandelBatchGUI gui, String filename) {
        this.gui = gui;
        this.filename = filename;
        loadFromFile();
    }
   
    void createSetting() {
        MandelSetting setting = new MandelSetting();
        settings.add(setting);
        currentIndex = settings.size()-1;
        gui.updateFromSetting(getCurrentSetting());
        fireIntervalAdded(this, currentIndex, currentIndex);
        saveToFile();
    }
    
    int getCurrentIndex() {
        return currentIndex >= 0 ? currentIndex : 0;
    }
    
    MandelSetting getCurrentSetting() {
        if (currentIndex >= 0 && currentIndex < settings.size()) {
            return settings.get(currentIndex);
        } else {
            return null;
        }
    }
    
    void notifyContentChanged() {
        fireContentsChanged(this, currentIndex, currentIndex);
        gui.refreshImage();
        saveToFile();
    }

    boolean selectSetting(int index) {
        if (index >= 0 && index < settings.size()) {
            currentIndex = index;
        } else {
            currentIndex = -1;
        }
        gui.updateFromSetting(getCurrentSetting());
        gui.refreshImage();
        return currentIndex != -1;
    }
    
    void removeCurrentSetting() {
         if (currentIndex >= 0 && currentIndex < settings.size()) {
             settings.remove(currentIndex--);
             gui.updateFromSetting(getCurrentSetting());
             fireIntervalRemoved(this, currentIndex+1, currentIndex+1);
             saveToFile();
         }
    }

    void loadFromFile() {
        try {
           ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)));
           System.out.println("Reading configuration from file: " + filename);
           settings = (ArrayList<MandelSetting>)(in.readObject());
           in.close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(gui, "Warning: can't read configuration file: " + filename);
            settings = new ArrayList<MandelSetting>();
        } catch (ClassNotFoundException ex) {
        }
    }
    
    void saveToFile() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filename)));
            System.out.println("Saving configuration to file: " + filename);
            out.writeObject(settings);
            out.close();
        } catch (IOException ex) {
            System.err.println("Warning: can't write configuration to file: " + filename);
        }
    }
        
    // AbstractListModel overrides ---
    
    @Override
    public int getSize() {
        return settings.size();
    }

    @Override
    public Object getElementAt(int index) {
        return settings.get(index);
    }
    
}
