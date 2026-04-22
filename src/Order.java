import java.time.LocalDateTime;

public class Order {
    private int id;
    private Buyer buyer;
    private Product product;
    private double totalPrice;
    private LocalDateTime orderDate;

    public Order(int id, Buyer buyer, Product product, double totalPrice) {
        this.id = id;
        this.buyer = buyer;
        this.product = product;
        this.totalPrice = totalPrice;
        this.orderDate = LocalDateTime.now();
    }

    public int getId() { return id; }
    public Buyer getBuyer() { return buyer; }
    public Product getProduct() { return product; }
    public double getTotalPrice() { return totalPrice; }
    public LocalDateTime getOrderDate() { return orderDate; }
}