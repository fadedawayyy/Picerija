package lvtPicerija;

import javax.swing.*;
import java.awt.Color;

public class Main {
    public static void main(String[] args) {
        try { 
          
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); 
        } catch (Exception ignored) {}

       
        

        SwingUtilities.invokeLater(() -> {
            AppFrame f = new AppFrame();
            f.setVisible(true);
        });
    }
}  