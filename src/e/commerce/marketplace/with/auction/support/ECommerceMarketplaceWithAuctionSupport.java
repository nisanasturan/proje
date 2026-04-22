import java.time.LocalDateTime;

public class ECommerceMarketplaceWithAuctionSupport {

    public static void main(String[] args) {
        MARKETPLACE marketplace = new MARKETPLACE();

        // Kategoriler
        Category elektronik = new Category(1, "Elektronik");
        Category giyim = new Category(2, "Giyim");

        // === SELLER KAYIT VE GİRİŞ ===
        marketplace.registerSeller("Ali", "ali@mail.com", "123");
        marketplace.login("ali@mail.com", "123");

        // === ÜRÜN EKLEME ===
        marketplace.addProduct("Laptop", "Gaming laptop", 15000, elektronik);
        marketplace.addProduct("Tişört", 250);
        marketplace.addAuctionProduct("Telefon", "iPhone 14", 5000, 1000,
                LocalDateTime.now().plusMinutes(1), elektronik);

        marketplace.logout();

        // === BUYER KAYIT VE GİRİŞ (Ayşe) ===
        marketplace.registerBuyer("Ayşe", "ayse@mail.com", "123");
        marketplace.login("ayse@mail.com", "123");

        marketplace.listAvailableProducts();

        // Sepet ile satın alma
        Cart cart = marketplace.createCart();
        Product laptop = marketplace.findProductById(1);
        try {
            cart.addItem(laptop);
        } catch (InsufficientStockException e) {
            System.out.println("Hata: " + e.getMessage());
        }
        cart.listCart();
        marketplace.checkoutCart(cart);

        // Açık artırmaya teklif
        marketplace.placeBid(3, 2000);
        marketplace.logout();

        //buyer için kayıt-giriş
        marketplace.registerBuyer("Mehmet", "mehmet@mail.com", "123");
        marketplace.login("mehmet@mail.com", "123");

        marketplace.placeBid(3, 3000);
        marketplace.logout();

        // açık arttırmayı bitir
        marketplace.login("ali@mail.com", "123");
        marketplace.endAuctionManually(3);
        marketplace.showAuctionWinner(3);
        marketplace.logout();

       
        marketplace.listAllOrders();

        //tüm kullanıcılar
        marketplace.listUsers();
    }
}