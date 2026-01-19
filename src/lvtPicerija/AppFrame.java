package lvtPicerija;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Window;
import java.util.Comparator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;


public class AppFrame extends JFrame {
    private final Storage storage = Storage.getInstance();
    private final OrdersTableModel ordersModel = new OrdersTableModel();
    
    private final double DELIVERY_FEE = 4.00;
    private final double PIZZA_40_MULT = 1.27;
    private final double PIZZA_50_MULT = 1.56;
    private final double DRINK_1L_MULT = 1.80; 

    public AppFrame() {
        super("Pizzeria — Order Receiver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1160, 820);
        setLocationRelativeTo(null);

        Design.setupGlobalTheme();
        getContentPane().setBackground(Design.COLOR_BG);

        storage.init();

        add(new HeaderPanel(), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(Design.FONT_MAIN);
        tabs.setBackground(Design.COLOR_BG);
        
        tabs.addTab("Orders", buildOrdersPanel());
        tabs.addTab("Menu (Products)", buildProductsPanel());
        tabs.addTab("History", buildHistoryPanel());
        
        add(tabs, BorderLayout.CENTER);

        reloadOrders();
    }

    private void reloadOrders() {
        ordersModel.setOrders(storage.listOrders());
    }

   
    private JPanel buildOrdersPanel() {
        JPanel p = new JPanel(new BorderLayout(8,8));
        p.setBackground(Design.COLOR_BG);
        p.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        JTable table = new JTable(ordersModel);
        table.setRowHeight(36);
        table.setFont(Design.FONT_MAIN);
        
        table.getTableHeader().setBackground(Design.COLOR_BUTTON);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(Design.FONT_BOLD);
        table.setSelectionBackground(Design.COLOR_BUTTON.brighter());
        table.setSelectionForeground(Color.WHITE);

        JScrollPane sp = new JScrollPane(table);
        sp.getViewport().setBackground(Color.WHITE);
        p.add(sp, BorderLayout.CENTER);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT,8,8));
        top.setBackground(Design.COLOR_BG);
        
        JComboBox<String> filter = new JComboBox<>(new String[] {"Active", "Completed", "All"});
        JButton btnNew = new JButton("New Order");
        JButton btnView = new JButton("View");
        JButton btnMarkDelivered = new JButton("Mark Delivered");
        JButton btnRefresh = new JButton("Refresh");

        Design.styleButton(btnNew, true);
        Design.styleButton(btnView, false);
        Design.styleButton(btnMarkDelivered, false);
        Design.styleButton(btnRefresh, false);

        top.add(new JLabel("Show:")); top.add(filter);
        top.add(btnNew); top.add(btnView); top.add(btnMarkDelivered); top.add(btnRefresh);

        btnNew.addActionListener(e -> {
            OrderFormDialog dlg = new OrderFormDialog(this);
            dlg.setVisible(true);
            if (dlg.isSaved()) reloadOrders();
        });

        btnView.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r >= 0) {
                Order o = ordersModel.getOrderAt(r);
                new OrderDetailsDialog(AppFrame.this, o).setVisible(true);
            }
        });

        btnMarkDelivered.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r >= 0) {
                Order o = ordersModel.getOrderAt(r);
                storage.updateOrderStatus(o.getId(), OrderStatus.DELIVERED);
                reloadOrders();
            }
        });
        
        btnRefresh.addActionListener(e -> reloadOrders());

        filter.addActionListener(e -> {
            String f = (String) filter.getSelectedItem();
            ordersModel.setFilter(f);
        });

        p.add(top, BorderLayout.NORTH);
        return p;
    }

  
    // 2. PRODUCTS TAB
    
    private JPanel buildProductsPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Design.COLOR_BG);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        controls.setBackground(Design.COLOR_BG);
        JButton btnAdd = new JButton("Add New Product");
        JButton btnReload = new JButton("Refresh");
        
        Design.styleButton(btnAdd, true);
        Design.styleButton(btnReload, false);
        
        controls.add(btnAdd);
        controls.add(btnReload);

        JPanel gridPanel = new JPanel(new GridLayout(0, 2, 20, 20));
        gridPanel.setBackground(Design.COLOR_BG);
        gridPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        Runnable loadProducts = () -> {
            gridPanel.removeAll();
            List<Product> products = storage.listProducts();
            products.sort(Comparator.comparing(Product::getType).thenComparing(Product::getId));

            for (Product p : products) {
                gridPanel.add(createProductCard(p, () -> {
                    gridPanel.removeAll();
                    btnReload.doClick(); 
                }));
            }
            gridPanel.revalidate();
            gridPanel.repaint();
        };

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Design.COLOR_BG);
        wrapper.add(gridPanel, BorderLayout.NORTH);
        
        JScrollPane scrollPane = new JScrollPane(wrapper);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(Design.COLOR_BG);

        btnReload.addActionListener(e -> loadProducts.run());
        btnAdd.addActionListener(e -> showAddProductDialog(loadProducts));

        mainPanel.add(controls, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        loadProducts.run();
        return mainPanel;
    }

    private JPanel createProductCard(Product p, Runnable onUpdate) {
        JPanel card = new JPanel(new BorderLayout(15, 0));
        card.setBackground(Design.COLOR_CARD_BG); 
        card.setBorder(Design.createCardBorder());

        JPanel imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Design.drawProductIcon(g, p.getType());
            }
        };
        imagePanel.setPreferredSize(new Dimension(120, 120));
        imagePanel.setBackground(Design.COLOR_CARD_BG); 

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Design.COLOR_CARD_BG);

        JLabel lblTitle = new JLabel(p.getName().toUpperCase());
        lblTitle.setFont(Design.FONT_TITLE);
        lblTitle.setForeground(Design.COLOR_TEXT_MAIN);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea txtDesc = new JTextArea(generateDescription(p));
        txtDesc.setFont(Design.FONT_DESC);
        txtDesc.setForeground(Design.COLOR_DESC_TEXT);
        txtDesc.setWrapStyleWord(true);
        txtDesc.setLineWrap(true);
        txtDesc.setEditable(false);
        txtDesc.setOpaque(false);
        txtDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtDesc.setMaximumSize(new Dimension(300, 50));

        JLabel lblPrice = new JLabel();
        lblPrice.setFont(Design.FONT_PRICE);
        lblPrice.setForeground(Design.COLOR_PRICE_TEXT);
        lblPrice.setAlignmentX(Component.LEFT_ALIGNMENT);

        String green = String.format("#%02x%02x%02x", Design.COLOR_PRICE_GREEN.getRed(), Design.COLOR_PRICE_GREEN.getGreen(), Design.COLOR_PRICE_GREEN.getBlue());
        
        if (p.getType() == ProductType.PIZZA) {
            double p30 = p.getBasePrice();
            lblPrice.setText(String.format("<html><div style='margin-top:4px;'>30cm: <font color='%s'>€%.2f</font> | 40cm: <font color='%s'>€%.2f</font><br>50cm: <font color='%s'>€%.2f</font></div></html>", 
                    green, p30, green, p30 * PIZZA_40_MULT, green, p30 * PIZZA_50_MULT));
        } else if (p.getType() == ProductType.DRINK) {
            double p05 = p.getBasePrice();
            lblPrice.setText(String.format("<html><div style='margin-top:4px;'>0.5l: <font color='%s'>€%.2f</font> | 1l: <font color='%s'>€%.2f</font></div></html>", 
                    green, p05, green, p05 * DRINK_1L_MULT));
        } else {
            lblPrice.setText(String.format("Price: €%.2f", p.getBasePrice()));
        }

        JButton btnAction = new JButton("Edit");
        Design.styleButton(btnAction, false);
        btnAction.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnAction.addActionListener(e -> showEditDialog(p, onUpdate));

        infoPanel.add(lblTitle);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(txtDesc);
        infoPanel.add(Box.createVerticalStrut(8));
        infoPanel.add(lblPrice);
        infoPanel.add(Box.createVerticalStrut(8));
        infoPanel.add(btnAction);

        card.add(imagePanel, BorderLayout.WEST);
        card.add(infoPanel, BorderLayout.CENTER);

        return card;
    }

    
    // 3. LOGIC 
   

    private void showEditDialog(Product p, Runnable onSave) {
        JTextField tfName = new JTextField(p.getName());
        ((AbstractDocument) tfName.getDocument()).setDocumentFilter(new NameInputFilter());

        JTextField tfPrice = new JTextField(String.valueOf(p.getBasePrice()));
        ((AbstractDocument) tfPrice.getDocument()).setDocumentFilter(new PriceInputFilter());

        JTextField tfSecondary = new JTextField(8);
        tfSecondary.setEditable(false);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.setBackground(Design.COLOR_BG);
        panel.add(new JLabel("Product Name (No numbers):"));
        panel.add(tfName);

        if (p.getType() == ProductType.PIZZA) {
            panel.add(new JLabel("Price 30cm (€):"));
            panel.add(tfPrice);
            panel.add(new JLabel("Price 40cm (Auto):"));
            panel.add(tfSecondary);
        } else if (p.getType() == ProductType.DRINK) {
            panel.add(new JLabel("Price 0.5l (€):"));
            panel.add(tfPrice);
            panel.add(new JLabel("Price 1l (Auto):"));
            panel.add(tfSecondary);
        } else {
            panel.add(new JLabel("Price (€):"));
            panel.add(tfPrice);
        }

        DocumentListener calcListener = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { calc(); }
            public void removeUpdate(DocumentEvent e) { calc(); }
            public void changedUpdate(DocumentEvent e) { calc(); }
            void calc() {
                try {
                    double val = Double.parseDouble(tfPrice.getText().trim().replace(',', '.'));
                    if (p.getType() == ProductType.PIZZA) tfSecondary.setText(String.format("%.2f", val * PIZZA_40_MULT));
                    else if (p.getType() == ProductType.DRINK) tfSecondary.setText(String.format("%.2f", val * DRINK_1L_MULT));
                } catch(Exception ex) { tfSecondary.setText(""); }
            }
        };
        tfPrice.getDocument().addDocumentListener(calcListener);
        
        try {
            double v = Double.parseDouble(tfPrice.getText());
             if (p.getType() == ProductType.PIZZA) tfSecondary.setText(String.format("%.2f", v * PIZZA_40_MULT));
             else if (p.getType() == ProductType.DRINK) tfSecondary.setText(String.format("%.2f", v * DRINK_1L_MULT));
        } catch (Exception ignored){}
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Product: " + p.getId(), JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) { 
            try {
                String newName = tfName.getText().trim();
                double newPrice = Double.parseDouble(tfPrice.getText().trim().replace(',', '.'));
                if (!newName.isEmpty() && newPrice > 0) {
                    p.setName(newName);
                    p.setBasePrice(newPrice);
                    storage.saveProduct(p);
                    onSave.run();
                } else JOptionPane.showMessageDialog(this, "Invalid input.");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error saving."); }
        }
    }

    private void showAddProductDialog(Runnable onSave) {
        JDialog dlg = new JDialog(this, "Add New Product", true);
        dlg.setSize(400, 350);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());
        dlg.getContentPane().setBackground(Design.COLOR_BG);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Design.COLOR_BG);
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0; gbc.gridy = 0;

        JComboBox<ProductType> type = new JComboBox<>(ProductType.values());
        JTextField name = new JTextField(20);
        ((AbstractDocument) name.getDocument()).setDocumentFilter(new NameInputFilter()); 

        JTextField price = new JTextField(8); 
        ((AbstractDocument) price.getDocument()).setDocumentFilter(new PriceInputFilter());

        JTextField p30 = new JTextField(8); ((AbstractDocument) p30.getDocument()).setDocumentFilter(new PriceInputFilter());
        JTextField p40 = new JTextField(8); p40.setEditable(false);
        JTextField p50 = new JTextField(8); p50.setEditable(false);

        JTextField d05 = new JTextField(8); ((AbstractDocument) d05.getDocument()).setDocumentFilter(new PriceInputFilter());
        JTextField d1 = new JTextField(8); d1.setEditable(false);
        
        p30.getDocument().addDocumentListener(new SimpleCalcListener(p30, p40, p50, PIZZA_40_MULT, PIZZA_50_MULT));
        d05.getDocument().addDocumentListener(new SimpleCalcListener(d05, d1, null, DRINK_1L_MULT, 0));

        form.add(new JLabel("Type:"), gbc); gbc.gridx = 1; form.add(type, gbc);
        gbc.gridx = 0; gbc.gridy++;
        form.add(new JLabel("Name:"), gbc); gbc.gridx = 1; form.add(name, gbc);
        gbc.gridx = 0; gbc.gridy++;
        
        JPanel priceCards = new JPanel(new CardLayout());
        priceCards.setBackground(Design.COLOR_BG);
        
        JPanel stdP = new JPanel(new FlowLayout(FlowLayout.LEFT)); stdP.add(new JLabel("Price (€): ")); stdP.add(price); stdP.setBackground(Design.COLOR_BG);
        JPanel pizP = new JPanel(new GridLayout(3,2,5,5)); pizP.add(new JLabel("30cm:")); pizP.add(p30); pizP.add(new JLabel("40cm:")); pizP.add(p40); pizP.add(new JLabel("50cm:")); pizP.add(p50); pizP.setBackground(Design.COLOR_BG);
        JPanel drkP = new JPanel(new GridLayout(2,2,5,5)); drkP.add(new JLabel("0.5l:")); drkP.add(d05); drkP.add(new JLabel("1l:")); drkP.add(d1); drkP.setBackground(Design.COLOR_BG);
        
        priceCards.add(pizP, "PIZ"); priceCards.add(stdP, "STD"); priceCards.add(drkP, "DRK");
        gbc.gridwidth = 2; form.add(priceCards, gbc);

        type.addActionListener(e -> {
            CardLayout cl = (CardLayout) priceCards.getLayout();
            ProductType pt = (ProductType) type.getSelectedItem();
            if (pt == ProductType.PIZZA) cl.show(priceCards, "PIZ");
            else if (pt == ProductType.DRINK) cl.show(priceCards, "DRK");
            else cl.show(priceCards, "STD");
        });

        JButton btnSave = new JButton("Save");
        Design.styleButton(btnSave, true);
        btnSave.addActionListener(e -> {
            try {
                ProductType selType = (ProductType) type.getSelectedItem();
                String generatedId = generateNextId(selType);
                double basePrice = 0;
                if (selType == ProductType.PIZZA) basePrice = parsePrice(p30);
                else if (selType == ProductType.DRINK) basePrice = parsePrice(d05);
                else basePrice = parsePrice(price);
                
                Product pnew = new Product(generatedId, selType, name.getText().trim(), basePrice);
                if (selType == ProductType.PIZZA) {
                    pnew.getSauces().addAll(List.of("Tomato","Garlic","BBQ","Cream"));
                    pnew.getExtras().add(new Extra("Extra cheese", 1.5));
                } else if (selType == ProductType.SNACK) {
                    pnew.getSauces().addAll(List.of("Ketchup", "Mayo", "Garlic", "BBQ", "Cheese"));
                }
                storage.saveProduct(pnew);
                dlg.dispose();
                onSave.run();
            } catch (Exception ex) { JOptionPane.showMessageDialog(dlg, "Invalid Input"); }
        });

        dlg.add(form, BorderLayout.CENTER);
        JPanel bP = new JPanel(); bP.add(btnSave); bP.setBackground(Design.COLOR_BG);
        dlg.add(bP, BorderLayout.SOUTH);
        if (type.getSelectedItem() == ProductType.PIZZA) ((CardLayout)priceCards.getLayout()).show(priceCards, "PIZ");
        dlg.setVisible(true);
    }

    private JPanel buildHistoryPanel() {
        JPanel p = new JPanel(new BorderLayout(8,8));
        p.setBackground(Design.COLOR_BG);
        p.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
        DefaultListModel<String> lm = new DefaultListModel<>();
        JList<String> list = new JList<>(lm);
        p.add(new JScrollPane(list), BorderLayout.CENTER);
        JButton btnLoad = new JButton("Load Completed");
        Design.styleButton(btnLoad, false);
        btnLoad.addActionListener(e -> {
            lm.clear();
            for (Order o : storage.listOrders()) {
                if (o.getStatus() == OrderStatus.DELIVERED || o.getStatus() == OrderStatus.CANCELED) {
                    lm.addElement("#" + o.getId() + " " + o.getCustomerName() + " - " + o.getStatus() + " - " + String.format("€%.2f", o.getTotal()));
                }
            }
        });
        JPanel bot = new JPanel(); bot.setBackground(Design.COLOR_BG); bot.add(btnLoad);
        p.add(bot, BorderLayout.NORTH);
        return p;
    }

    private String generateNextId(ProductType type) {
        String prefix = type == ProductType.PIZZA ? "PIZ" : type == ProductType.DRINK ? "DRK" : "SNK";
        int max = 0;
        for (Product p : storage.listProducts()) {
            if (p.getId().startsWith(prefix)) {
                try { int num = Integer.parseInt(p.getId().split("-")[1]); if (num > max) max = num; } catch (Exception ignored) {}
            }
        }
        return String.format("%s-%03d", prefix, max + 1);
    }

    private String generateDescription(Product p) {
        StringBuilder sb = new StringBuilder();
        if (p.getSauces() != null) sb.append(String.join(", ", p.getSauces()));
        if (p.getType() == ProductType.PIZZA) {
            if (sb.length() > 0) sb.append(", ");
            sb.append("Mozzarella");
            String n = p.getName().toLowerCase();
            if (n.contains("pepperoni")) sb.append(", Pepperoni");
            if (n.contains("chicken")) sb.append(", Chicken");
        }
        if (sb.length() == 0) return p.getType().name();
        String res = sb.toString();
        return res.substring(0, 1).toUpperCase() + res.substring(1);
    }

    private double parsePrice(JTextField tf) {
        return Double.parseDouble(tf.getText().trim().replace(',', '.'));
    }

    }