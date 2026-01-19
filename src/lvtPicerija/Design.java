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

   

    public static void drawHeader(Graphics g, int w, int h) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(COLOR_BG);
        g2.fillRect(0, 0, w, h);

        int iconSize = 80;
        int gap = 20;
        
        g2.setFont(FONT_LOGO);
        FontMetrics fm = g2.getFontMetrics();
        int textW = fm.stringWidth("LVT PIZZA");
        int totalW = iconSize + gap + textW;
        
        int startX = (w - totalW) / 2;
        int centerY = h / 2;
        int iconY = centerY - (iconSize / 2);

        
        g2.setColor(LOGO_DARK);
        g2.fillOval(startX, iconY, iconSize, iconSize);
        g2.setColor(PIZZA_CRUST);
        g2.fillOval(startX + 4, iconY + 4, iconSize - 8, iconSize - 8);
        g2.setColor(PIZZA_RED);
        g2.fillOval(startX + 8, iconY + 8, iconSize - 16, iconSize - 16);
        g2.setColor(PIZZA_CHEESE);
        g2.fillOval(startX + 15, iconY + 15, iconSize - 30, iconSize - 30); 
        g2.setColor(new Color(200, 60, 60));
        g2.fillOval(startX + 25, iconY + 25, 12, 12);
        g2.fillOval(startX + 45, iconY + 20, 12, 12);
        g2.fillOval(startX + 30, iconY + 45, 12, 12);
        g2.fillOval(startX + 50, iconY + 40, 12, 12);

       
        int textX = startX + iconSize + gap;
        int textBaseline = centerY + (fm.getAscent() / 2) - 5;

        g2.setColor(LOGO_DARK);
        g2.fillRect(textX, centerY - 25, textW + 10, 4);

        g2.drawString("LVT", textX, textBaseline);
        int widthLVT = fm.stringWidth("LVT ");
        g2.setColor(LOGO_ORANGE);
        g2.drawString("PIZZA", textX + widthLVT, textBaseline);

        int yLineBottom = centerY + 22;
        g2.setColor(LOGO_DARK);
        g2.fillRect(textX, yLineBottom, widthLVT - 5, 4);
        g2.setColor(LOGO_ORANGE);
        g2.fillRect(textX + widthLVT, yLineBottom, fm.stringWidth("PIZZA") + 10, 4);

        g2.dispose();
    }

    public static void drawProductIcon(Graphics g, ProductType type) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (type == ProductType.PIZZA) {
            g2.setColor(new Color(230, 150, 50)); 
            g2.fillOval(5, 5, 110, 110);
            g2.setColor(new Color(255, 200, 80));
            g2.fillOval(10, 10, 100, 100);
            g2.setColor(new Color(200, 50, 50));
            g2.fillOval(30, 30, 20, 20);
            g2.fillOval(70, 40, 20, 20);
            g2.fillOval(45, 80, 20, 20);
        } else if (type == ProductType.DRINK) {
            g2.setColor(new Color(220, 220, 220));
            g2.fillOval(30, 100, 60, 15);
            g2.setColor(new Color(50, 150, 250));
            g2.fillRoundRect(35, 40, 50, 75, 10, 10);
            g2.fillRect(48, 15, 24, 30);
            g2.setColor(new Color(200, 50, 50));
            g2.fillRect(46, 10, 28, 8);
            g2.setColor(new Color(200, 0, 0)); 
            g2.fillRect(35, 65, 50, 20);
        } else {
            g2.setColor(new Color(255, 215, 0)); 
            g2.fillRoundRect(55, 20, 7, 70, 3, 3);
            AffineTransform old = g2.getTransform();
            drawFry(g2, old, -10, 50, 50, 45, 25, 7, 60);
            drawFry(g2, old, 10, 70, 50, 65, 25, 7, 60);
            drawFry(g2, old, -20, 40, 60, 35, 35, 7, 50);
            drawFry(g2, old, 20, 80, 60, 75, 35, 7, 50);
            g2.setColor(new Color(220, 20, 20)); 
            Path2D box = new Path2D.Double();
            box.moveTo(35, 110);
            box.lineTo(85, 110);
            box.lineTo(95, 60);
            box.lineTo(25, 60);
            box.closePath();
            g2.fill(box);
            g2.setColor(new Color(180, 0, 0));
            g2.setStroke(new BasicStroke(1));
            g2.draw(box);
        }
    }

    private static void drawFry(Graphics2D g2, AffineTransform old, double rot, int anchorX, int anchorY, int x, int y, int w, int h) {
        g2.rotate(Math.toRadians(rot), anchorX, anchorY);
        g2.fillRoundRect(x, y, w, h, 3, 3);
        g2.setTransform(old);
    }
}