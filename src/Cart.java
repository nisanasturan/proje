import java.util.ArrayList;

public class Cart {
    private Buyer buyer;
    private ArrayList<Product> items;

    public Cart(Buyer buyer) {
        this.buyer = buyer;
        this.items = new ArrayList<>();
    }

    public void addItem(Product product) throws InsufficientStockException {
        if (product.isSold()) {
            throw new InsufficientStockException("Ürün stokta yok: " + product.getName());
        }
        items.add(product);
        System.out.println("Sepete eklendi: " + product.getName());
    }

    public void removeItem(Product product) {
        items.remove(product);
        System.out.println("Sepetten çıkarıldı: " + product.getName());
    }

    public void listCart() {
        if (items.isEmpty()) {
            System.out.println("Sepet boş.");
            return;
        }
        System.out.println("=== SEPET ===");
        for (Product p : items) {
            System.out.println("- " + p.getName() + " | Fiyat: " + p.getPrice());
        }
    }

    public double getTotalPrice() {
        double total = 0;
        for (Product p : items) {
            total += p.getPrice();
        }
        return total;
    }

    public ArrayList<Product> getItems() {
        return items;
    }

    public Buyer getBuyer() {
        return buyer;
    }
}