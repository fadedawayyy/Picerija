package lvtPicerija;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;


public class Design {
    
    public static final Color COLOR_BG = new Color(184, 180, 145);
    public static final Color COLOR_BUTTON = new Color(113, 92, 128);
    public static final Color COLOR_BUTTON_HOVER = new Color(93, 72, 108);
    public static final Color COLOR_CARD_BG = Color.WHITE;
    public static final Color COLOR_TEXT_MAIN = Color.BLACK;
    public static final Color COLOR_TEXT_ON_BTN = Color.WHITE;
    public static final Color COLOR_BORDER = new Color(160, 156, 125);
    
    
    public static final Color COLOR_DESC_TEXT = Color.GRAY;
    public static final Color COLOR_PRICE_TEXT = Color.BLACK;
    public static final Color COLOR_PRICE_GREEN = new Color(0, 153, 0);

    
    private static final Color LOGO_DARK = new Color(75, 70, 50);
    private static final Color LOGO_ORANGE = new Color(230, 160, 50);
    private static final Color PIZZA_CRUST = new Color(225, 200, 140);
    private static final Color PIZZA_RED = new Color(210, 60, 60);
    private static final Color PIZZA_CHEESE = new Color(255, 220, 100);

    
    public static final Font FONT_BOLD = new Font("SansSerif", Font.BOLD, 13);
    public static final Font FONT_MAIN = new Font("SansSerif", Font.PLAIN, 14);
    public static final Font FONT_TITLE = new Font("SansSerif", Font.BOLD, 18);
    public static final Font FONT_DESC = new Font("SansSerif", Font.PLAIN, 12);
    public static final Font FONT_PRICE = new Font("SansSerif", Font.BOLD, 13);
    public static final Font FONT_FALLBACK_LOGO = new Font("SansSerif", Font.BOLD, 28);
    public static final Font FONT_LOGO = new Font("SansSerif", Font.BOLD, 42);

    
    public static void setupGlobalTheme() {
        UIManager.put("Panel.background", COLOR_BG);
        UIManager.put("Frame.background", COLOR_BG);
        UIManager.put("Label.foreground", COLOR_TEXT_MAIN);
        UIManager.put("TabbedPane.background", COLOR_BG);
        UIManager.put("ScrollPane.background", COLOR_BG);
        UIManager.put("Viewport.background", COLOR_BG);
        UIManager.put("OptionPane.background", COLOR_BG);
        UIManager.put("OptionPane.messageForeground", COLOR_TEXT_MAIN);
        UIManager.put("CheckBox.background", COLOR_BG);
        UIManager.put("RadioButton.background", COLOR_BG);
        UIManager.put("SplitPane.background", COLOR_BG);
        UIManager.put("Table.selectionBackground", COLOR_BUTTON.brighter());
        UIManager.put("Table.selectionForeground", Color.WHITE);
    }

    public static void styleButton(JButton btn, boolean primary) {
        btn.setFont(FONT_BOLD);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setMargin(new Insets(5, 10, 5, 10));
        btn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        btn.setBackground(COLOR_BUTTON);
        btn.setForeground(COLOR_TEXT_ON_BTN);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(COLOR_BUTTON_HOVER);
                btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(COLOR_BUTTON);
            }
        });
    }

    public static Border createCardBorder() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDER, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );
    }

   

    }