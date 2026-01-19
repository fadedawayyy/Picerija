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

  
    }