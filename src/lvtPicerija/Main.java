package lvtPicerija;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        try { 
          
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); 
        } catch (Exception ignored) {}

       
        Design.setupGlobalTheme();

        SwingUtilities.invokeLater(() -> {
            AppFrame f = new AppFrame();
            f.setVisible(true);
        });
    }
}  