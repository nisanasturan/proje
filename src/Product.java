public class Product implements Purchasable {
    private int id;
    private String name;
    private String description;
    private double price;
    private Seller seller;
    private boolean sold;
    private Category category;

    public Product(int id, String name, String description, double price, Seller seller, Category category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.seller = seller;
        this.sold = false;
        this.category = category;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public Seller getSeller() { return seller; }
    public boolean isSold() { return sold; }
    public void setSold(boolean sold) { this.sold = sold; }
    public Category getCategory() { return category; }

    @Override
    public String toString() {
        return "Ürün: " + name + " | Fiyat: " + price + " | Kategori: " + category.getName();
    }
}