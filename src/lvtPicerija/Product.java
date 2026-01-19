package lvtPicerija;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;



enum ProductType { PIZZA, SNACK, DRINK }

class Extra {
    private final String name;
    private final double price;
    public Extra(String name, double price) { this.name = name; this.price = price; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    @Override public String toString() { return name + " (+" + String.format("%.2f", price) + ")"; }
}

class Product {
    private String id;
    private ProductType type;
    private String name;
    private double basePrice; // base price for 30cm for pizzas, or usual price for snack/drink
    private List<Extra> extras = new ArrayList<>(); // possible extras (for pizzas)
    private List<String> sauces = new ArrayList<>(); // possible sauces (strings)

    public Product() {}
    public Product(String id, ProductType type, String name, double basePrice) {
        this.id = id; this.type = type; this.name = name; this.basePrice = basePrice;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public ProductType getType() { return type; }
    public void setType(ProductType type) { this.type = type; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getBasePrice() { return basePrice; }
    public void setBasePrice(double basePrice) { this.basePrice = basePrice; }
    public List<Extra> getExtras() { return extras; }
    public List<String> getSauces() { return sauces; }

    @Override
    public String toString() { return name + " (" + id + ")"; }
}

class OrderItem {
    private String productId;
    private String name;
    private double unitPrice;
    private int quantity;
    private int size = 30; // for pizzas: 30/40/50
    private String sauce = "";
    private List<String> extras = new ArrayList<>();

    public OrderItem() {}
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
    public String getSauce() { return sauce; }
    public void setSauce(String sauce) { this.sauce = sauce; }
    public List<String> getExtras() { return extras; }
    public double getTotal() { return unitPrice * quantity; }
    @Override public String toString() {
        String e = extras.isEmpty() ? "" : " + " + String.join(",", extras);
        return name + " " + size + "cm " + sauce + " x" + quantity + e + " = " + String.format("%.2f", getTotal());
    }
}

