package lvtPicerija;

import java.io.IOException;
import java.nio.file.*;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class Storage {
    private static final Storage INSTANCE = new Storage();
    private final Path productsFile = Paths.get("products.txt");
    private final Path ordersFile = Paths.get("orders.txt");
    private final DateTimeFormatter fmt = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    private Storage() {}

    public static Storage getInstance() { return INSTANCE; }

    public void init() {
        try {
            if (Files.notExists(productsFile)) Files.createFile(productsFile);
            if (Files.notExists(ordersFile)) Files.createFile(ordersFile);
            
         
            List<String> seed = Arrays.asList(
               
                "PIZ-001|PIZZA|Classic Margherita|10.40|Extra cheese:1.50;Olives:1.00|Tomato;Garlic",
                "PIZ-002|PIZZA|Pepperoni Blast|11.10|Extra cheese:1.50;Chili:0.50|Tomato;BBQ",
                "PIZ-003|PIZZA|BBQ Chicken Supreme|11.60|Extra chicken:2.00;Onion:0.60|BBQ;Garlic",
                "PIZ-004|PIZZA|Four Cheese Delight|11.50|Blue cheese:1.80;Honey:1.00|Tomato;Cream",
                "PIZ-005|PIZZA|Meat Lovers Feast|11.90|Bacon:1.50;Ham:1.50|Tomato;BBQ",
                "PIZ-006|PIZZA|Hawaiian Sunset|11.00|Pineapple:1.00;Ham:1.50|Tomato;Garlic",
                "PIZ-007|PIZZA|Spicy Diablo|11.70|Jalapeno:1.00;Chili sauce:0.50|Tomato;Sriracha",
                "PIZ-008|PIZZA|Veggie Garden|10.80|Corn:0.80;Peppers:0.80|Tomato;Garlic",
                "PIZ-009|PIZZA|Seafood Paradise|11.90|Shrimp:2.50;Mussels:2.00|Tomato;Cream",
                "PIZ-010|PIZZA|Truffle Mushroom|11.80|Truffle oil:2.00;Mushrooms:1.20|Cream;Garlic",

          
                "SNK-001|SNACK|Garlic Breadsticks|4.30||Garlic;Cheese",
                "SNK-002|SNACK|Mozzarella Sticks|5.20||Cranberry;Tomato",
                "SNK-003|SNACK|Chicken Wings|5.80||BBQ;Blue Cheese",
                "SNK-004|SNACK|Loaded Potato Wedges|4.90||Cheese;Bacon",

              
                "DRK-001|DRINK|Classic Cola|1.50||",
                "DRK-002|DRINK|Fresh Lemonade|3.00||",
                "DRK-003|DRINK|Sparkling Water|1.00||",
                "DRK-004|DRINK|Iced Tea|1.90||",
                "DRK-005|DRINK|Orange Juice|1.50||"
            );
            
            Files.write(productsFile, seed, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
            
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    // PRODUCTS
    public synchronized List<Product> listProducts() {
        List<Product> out = new ArrayList<>();
        try {
            for (String ln : Files.readAllLines(productsFile)) {
                if (ln.trim().isEmpty()) continue;
                String[] p = ln.split("\\|", -1);
                if (p.length < 4) continue;
                Product prod = new Product();
                prod.setId(p[0]);
                prod.setType(ProductType.valueOf(p[1]));
                prod.setName(p[2]);
                prod.setBasePrice(Double.parseDouble(p[3]));
                if (p.length >= 5 && !p[4].isEmpty()) {
                    String[] extras = p[4].split(";");
                    for (String e : extras) {
                        String[] kv = e.split(":",2);
                        if (kv.length==2) prod.getExtras().add(new Extra(kv[0], Double.parseDouble(kv[1])));
                    }
                }
                if (p.length >= 6 && !p[5].isEmpty()) {
                    String[] sauces = p[5].split(";");
                    for (String s : sauces) if (!s.isEmpty()) prod.getSauces().add(s);
                }
                out.add(prod);
            }
        } catch (IOException ex) { ex.printStackTrace(); }
        return out;
    }

    public synchronized void saveProduct(Product prod) {
        try {
            List<String> lines = new ArrayList<>();
            boolean replaced = false;
            for (String ln : Files.readAllLines(productsFile)) {
                if (ln.startsWith(prod.getId() + "|")) {
                    lines.add(formatProductLine(prod));
                    replaced = true;
                } else lines.add(ln);
            }
            if (!replaced) lines.add(formatProductLine(prod));
            Files.write(productsFile, lines, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
        } catch (IOException ex) { ex.printStackTrace(); }
    }

    private String formatProductLine(Product p) {
        StringBuilder sb = new StringBuilder();
        sb.append(p.getId()).append("|").append(p.getType().name()).append("|").append(safe(p.getName())).append("|").append(p.getBasePrice()).append("|");
        boolean first = true;
        for (Extra e : p.getExtras()) {
            if (!first) sb.append(";");
            sb.append(safe(e.getName())).append(":").append(e.getPrice());
            first = false;
        }
        sb.append("|");
        first = true;
        for (String s : p.getSauces()) {
            if (!first) sb.append(";");
            sb.append(safe(s));
            first = false;
        }
        return sb.toString();
    }

    // ORDERS
    public synchronized List<Order> listOrders() {
        List<Order> out = new ArrayList<>();
        try {
            for (String ln : Files.readAllLines(ordersFile)) {
                if (ln.trim().isEmpty()) continue;
                Order o = parseOrderLine(ln);
                if (o != null) out.add(o);
            }
        } catch (IOException e) { e.printStackTrace(); }
        return out;
    }

    public synchronized void createOrder(Order order) {
        try {
            long id = nextOrderId();
            order.setId(id);
            order.setCreatedAt(OffsetDateTime.now());
            Files.writeString(ordersFile, formatOrderLine(order) + System.lineSeparator(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    public synchronized boolean updateOrderStatus(long id, OrderStatus st) {
        try {
            List<String> lines = new ArrayList<>();
            boolean found = false;
            for (String ln : Files.readAllLines(ordersFile)) {
                if (ln.startsWith(id + "|")) {
                    Order o = parseOrderLine(ln);
                    if (o != null) {
                        o.setStatus(st);
                        lines.add(formatOrderLine(o));
                        found = true;
                    } else lines.add(ln);
                } else lines.add(ln);
            }
            Files.write(ordersFile, lines, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
            return found;
        } catch (IOException e) { e.printStackTrace(); return false; }
    }

    private long nextOrderId() throws IOException {
        long max = 0;
        if (Files.notExists(ordersFile)) return 1;
        for (String ln : Files.readAllLines(ordersFile)) {
            if (ln.trim().isEmpty()) continue;
            String[] p = ln.split("\\|", -1);
            try { long id = Long.parseLong(p[0]); if (id > max) max = id; } catch (Exception ignored) {}
        }
        return max + 1;
    }

    private Order parseOrderLine(String ln) {
        try {
            String[] parts = ln.split("\\|", 9);
            if (parts.length < 8) return null;
            Order o = new Order();
            o.setId(Long.parseLong(parts[0]));
            o.setCustomerName(parts[1]);
            o.setPhone(parts[2]);
            o.setAddress(parts[3]);
            o.setMethod(parts[4]);
            try { o.setStatus(OrderStatus.valueOf(parts[5])); } catch (Exception ex) { o.setStatus(OrderStatus.NEW); }
            if (!parts[6].isEmpty()) o.setCreatedAt(OffsetDateTime.parse(parts[6], fmt));
            o.setDeliveryFee(parts.length >= 8 && !parts[7].isEmpty() ? Double.parseDouble(parts[7]) : 0.0);
            if (parts.length >= 9 && !parts[8].isEmpty()) {
                String[] items = parts[8].split(";");
                for (String it : items) {
                    String[] ip = it.split("\\^", -1);
                    if (ip.length >= 6) {
                        OrderItem oi = new OrderItem();
                        oi.setProductId(ip[0]);
                        oi.setName(ip[1]);
                        oi.setUnitPrice(Double.parseDouble(ip[2]));
                        oi.setQuantity(Integer.parseInt(ip[3]));
                        oi.setSize(Integer.parseInt(ip[4]));
                        oi.setSauce(ip[5]);
                        if (ip.length >= 7 && !ip[6].isEmpty()) {
                            String[] ex = ip[6].split(",");
                            for (String e : ex) if (!e.isEmpty()) oi.getExtras().add(e);
                        }
                        o.getItems().add(oi);
                    }
                }
            }
            return o;
        } catch (Exception e) { e.printStackTrace(); return null; }
    }

    private String formatOrderLine(Order o) {
        StringBuilder sb = new StringBuilder();
        sb.append(o.getId()).append("|");
        sb.append(safe(o.getCustomerName())).append("|");
        sb.append(safe(o.getPhone())).append("|");
        sb.append(safe(o.getAddress())).append("|");
        sb.append(safe(o.getMethod())).append("|");
        sb.append(o.getStatus().name()).append("|");
        sb.append(o.getCreatedAt() == null ? "" : o.getCreatedAt().format(fmt)).append("|");
        sb.append(o.getDeliveryFee()).append("|");
        boolean firstItem = true;
        for (OrderItem it : o.getItems()) {
            if (!firstItem) sb.append(";");
            sb.append(safe(it.getProductId())).append("^")
              .append(safe(it.getName())).append("^")
              .append(it.getUnitPrice()).append("^")
              .append(it.getQuantity()).append("^")
              .append(it.getSize()).append("^")
              .append(safe(it.getSauce())).append("^");
            boolean firstE = true;
            for (String e : it.getExtras()) {
                if (!firstE) sb.append(",");
                sb.append(safe(e));
                firstE = false;
            }
            firstItem = false;
        }
        return sb.toString();
    }

    private String safe(String s) { return s == null ? "" : s.replace("|"," ").replace("^"," ").replace(";"," ").replace(","," "); }
}