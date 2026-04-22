import java.time.LocalDateTime;
import java.util.ArrayList;

public class MARKETPLACE {

    private ArrayList<User> users;
    private ArrayList<Product> products;
    private ArrayList<Order> orders;

    private User loggedInUser;

    private int nextUserId;
    private int nextProductId;
    private int nextOrderId;

    public MARKETPLACE() {
        users = new ArrayList<>();
        products = new ArrayList<>();
        orders = new ArrayList<>();

        loggedInUser = null;

        nextUserId = 1;
        nextProductId = 1;
        nextOrderId = 1;
    }

    // =========================
    // USER OPERATIONS
    // =========================

    public Seller registerSeller(String name, String email, String password) {
        if (findUserByEmail(email) != null) {
            System.out.println("Bu email ile kayıtlı kullanıcı zaten var.");
            return null;
        }

        Seller seller = new Seller(nextUserId++, name, email, password);
        users.add(seller);
        System.out.println("Seller kaydı başarılı: " + name);
        return seller;
    }

    public Buyer registerBuyer(String name, String email, String password) {
        if (findUserByEmail(email) != null) {
            System.out.println("Bu email ile kayıtlı kullanıcı zaten var.");
            return null;
        }

        Buyer buyer = new Buyer(nextUserId++, name, email, password);
        users.add(buyer);
        System.out.println("Buyer kaydı başarılı: " + name);
        return buyer;
    }

    public boolean login(String email, String password) {
        for (User user : users) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                loggedInUser = user;
                System.out.println("Giriş başarılı. Hoş geldin, " + user.getName());
                return true;
            }
        }

        System.out.println("Email veya şifre hatalı.");
        return false;
    }

    public void logout() {
        if (loggedInUser != null) {
            System.out.println(loggedInUser.getName() + " çıkış yaptı.");
            loggedInUser = null;
        } else {
            System.out.println("Şu anda giriş yapan kullanıcı yok.");
        }
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void listUsers() {
        if (users.isEmpty()) {
            System.out.println("Kayıtlı kullanıcı yok.");
            return;
        }

        System.out.println("=== KULLANICILAR ===");
        for (User user : users) {
            System.out.println(user.toString());
        }
    }

    public User findUserByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }
        return null;
    }

    public User findUserById(int id) {
        for (User user : users) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    // =========================
    // PRODUCT OPERATIONS
    // =========================

    // Method Overloading - tam parametreli versiyon
    public Product addProduct(String name, String description, double price, Category category) {
        if (!(loggedInUser instanceof Seller)) {
            System.out.println("Ürün eklemek için seller olarak giriş yapmalısın.");
            return null;
        }

        Seller seller = (Seller) loggedInUser;
        Product product = new Product(nextProductId++, name, description, price, seller, category);
        products.add(product);

        System.out.println("Normal ürün eklendi: " + product.getName());
        return product;
    }

    // Method Overloading - kısa versiyon
    public Product addProduct(String name, double price) {
        Category defaultCategory = new Category(0, "Genel");
        return addProduct(name, "", price, defaultCategory);
    }

    public AuctionProduct addAuctionProduct(String name, String description, double basePrice,
                                            double startPrice, LocalDateTime endTime, Category category) {
        if (!(loggedInUser instanceof Seller)) {
            System.out.println("Açık artırmalı ürün eklemek için seller olarak giriş yapmalısın.");
            return null;
        }

        Seller seller = (Seller) loggedInUser;

        AuctionProduct auctionProduct = new AuctionProduct(
                nextProductId++, name, description, basePrice, seller, startPrice, endTime, category
        );

        products.add(auctionProduct);
        System.out.println("Açık artırmalı ürün eklendi: " + auctionProduct.getName());
        return auctionProduct;
    }

    public void listAllProducts() {
        if (products.isEmpty()) {
            System.out.println("Sistemde ürün yok.");
            return;
        }

        System.out.println("=== TÜM ÜRÜNLER ===");
        for (Product product : products) {
            printProductDetails(product);
        }
    }

    public void listAvailableProducts() {
        boolean found = false;
        System.out.println("=== SATIN ALINABİLİR / AKTİF ÜRÜNLER ===");

        for (Product product : products) {
            if (product instanceof AuctionProduct) {
                AuctionProduct auctionProduct = (AuctionProduct) product;
                if (auctionProduct.isAuctionActive()) {
                    printProductDetails(product);
                    found = true;
                }
            } else {
                if (!product.isSold()) {
                    printProductDetails(product);
                    found = true;
                }
            }
        }

        if (!found) {
            System.out.println("Uygun ürün bulunamadı.");
        }
    }

    public void listProductsOfSeller(Seller seller) {
        boolean found = false;
        System.out.println("=== SELLER ÜRÜNLERİ: " + seller.getName() + " ===");

        for (Product product : products) {
            if (product.getSeller().getId() == seller.getId()) {
                printProductDetails(product);
                found = true;
            }
        }

        if (!found) {
            System.out.println("Bu satıcıya ait ürün yok.");
        }
    }

    public Product findProductById(int productId) {
        for (Product product : products) {
            if (product.getId() == productId) {
                return product;
            }
        }
        return null;
    }

    public void printProductDetails(Product product) {
        System.out.println("-----------------------------------");
        System.out.println("ID: " + product.getId());
        System.out.println("Ad: " + product.getName());
        System.out.println("Açıklama: " + product.getDescription());
        System.out.println("Satıcı: " + product.getSeller().getName());
        System.out.println("Kategori: " + product.getCategory().getName());

        if (product instanceof AuctionProduct) {
            AuctionProduct auctionProduct = (AuctionProduct) product;
            System.out.println("Tür: Açık Artırma Ürünü");
            System.out.println("Başlangıç Fiyatı: " + auctionProduct.getStartPrice());
            System.out.println("En Yüksek Teklif: " + auctionProduct.getCurrentHighestBid());
            System.out.println("Bitiş Zamanı: " + auctionProduct.getEndTime());
            System.out.println("Aktif mi?: " + auctionProduct.isAuctionActive());
        } else {
            System.out.println("Tür: Normal Ürün");
            System.out.println("Fiyat: " + product.getPrice());
            System.out.println("Satıldı mı?: " + product.isSold());
        }
        System.out.println("-----------------------------------");
    }

    // =========================
    // BUY PRODUCT
    // =========================

    public boolean buyProduct(int productId) {
        if (!(loggedInUser instanceof Buyer)) {
            System.out.println("Satın alma işlemi için buyer olarak giriş yapmalısın.");
            return false;
        }

        Product product = findProductById(productId);

        if (product == null) {
            System.out.println("Ürün bulunamadı.");
            return false;
        }

        if (product instanceof AuctionProduct) {
            System.out.println("Bu ürün açık artırmalı. Direkt satın alınamaz.");
            return false;
        }

        if (product.isSold()) {
            System.out.println("Bu ürün zaten satılmış.");
            return false;
        }

        Buyer buyer = (Buyer) loggedInUser;
        product.setSold(true);

        Order order = new Order(nextOrderId++, buyer, product, product.getPrice());
        orders.add(order);

        System.out.println("Satın alma başarılı. Ürün: " + product.getName());
        return true;
    }

    // =========================
    // CART OPERATIONS
    // =========================

    public Cart createCart() {
        if (!(loggedInUser instanceof Buyer)) {
            System.out.println("Sepet oluşturmak için buyer olarak giriş yapmalısın.");
            return null;
        }
        return new Cart((Buyer) loggedInUser);
    }

    public void checkoutCart(Cart cart) {
        if (cart == null || cart.getItems().isEmpty()) {
            System.out.println("Sepet boş.");
            return;
        }

        System.out.println("=== SEPET ÖDEMESİ ===");
        for (Product product : cart.getItems()) {
            product.setSold(true);
            Order order = new Order(nextOrderId++, cart.getBuyer(), product, product.getPrice());
            orders.add(order);
            System.out.println("Satın alındı: " + product.getName());
        }
        System.out.println("Toplam: " + cart.getTotalPrice() + " TL");
        cart.getItems().clear();
    }

    // =========================
    // AUCTION OPERATIONS
    // =========================

    public boolean placeBid(int productId, double amount) {
        if (!(loggedInUser instanceof Buyer)) {
            System.out.println("Teklif vermek için buyer olarak giriş yapmalısın.");
            return false;
        }

        Product product = findProductById(productId);

        if (product == null) {
            System.out.println("Ürün bulunamadı.");
            return false;
        }

        if (!(product instanceof AuctionProduct)) {
            System.out.println("Bu ürün açık artırmalı değil.");
            return false;
        }

        AuctionProduct auctionProduct = (AuctionProduct) product;
        Buyer buyer = (Buyer) loggedInUser;

        try {
            if (!auctionProduct.isAuctionActive()) {
                throw new AuctionExpiredException("Açık artırma aktif değil.");
            }

            if (LocalDateTime.now().isAfter(auctionProduct.getEndTime())) {
                throw new AuctionExpiredException("Açık artırma süresi dolmuş.");
            }

            Bid bid = new Bid(buyer, amount);
            boolean success = auctionProduct.placeBid(bid);

            if (success) {
                System.out.println("Teklif başarılı: " + amount);
                return true;
            } else {
                throw new InvalidBidException("Teklif geçersiz. Daha yüksek bir teklif vermelisin.");
            }
        } catch (AuctionExpiredException | InvalidBidException e) {
            System.out.println("Hata: " + e.getMessage());
            return false;
        }
    }

    public void endAuctionManually(int productId) {
        Product product = findProductById(productId);

        if (product == null) {
            System.out.println("Ürün bulunamadı.");
            return;
        }

        if (!(product instanceof AuctionProduct)) {
            System.out.println("Bu ürün açık artırmalı değil.");
            return;
        }

        AuctionProduct auctionProduct = (AuctionProduct) product;

        if (!(loggedInUser instanceof Seller)) {
            System.out.println("Açık artırmayı bitirmek için seller olarak giriş yapmalısın.");
            return;
        }

        Seller seller = (Seller) loggedInUser;

        if (auctionProduct.getSeller().getId() != seller.getId()) {
            System.out.println("Sadece ürünü ekleyen seller açık artırmayı bitirebilir.");
            return;
        }

        finalizeAuction(auctionProduct);
    }

    public void checkAndCloseExpiredAuctions() {
        for (Product product : products) {
            if (product instanceof AuctionProduct) {
                AuctionProduct auctionProduct = (AuctionProduct) product;
                if (auctionProduct.isAuctionActive() &&
                    LocalDateTime.now().isAfter(auctionProduct.getEndTime())) {
                    finalizeAuction(auctionProduct);
                }
            }
        }
    }

    private void finalizeAuction(AuctionProduct auctionProduct) {
        if (!auctionProduct.isAuctionActive()) {
            System.out.println("Bu açık artırma zaten bitmiş.");
            return;
        }

        auctionProduct.endAuction();

        Buyer winner = auctionProduct.getWinner();

        if (winner == null) {
            System.out.println("Açık artırma bitti fakat teklif verilmedi: " + auctionProduct.getName());
            return;
        }

        auctionProduct.setSold(true);

        Order order = new Order(
                nextOrderId++,
                winner,
                auctionProduct,
                auctionProduct.getCurrentHighestBid()
        );
        orders.add(order);

        System.out.println("Açık artırma sona erdi.");
        System.out.println("Kazanan: " + winner.getName());
        System.out.println("Kazanan teklif: " + auctionProduct.getCurrentHighestBid());
    }

    public void showAuctionWinner(int productId) {
        Product product = findProductById(productId);

        if (product == null) {
            System.out.println("Ürün bulunamadı.");
            return;
        }

        if (!(product instanceof AuctionProduct)) {
            System.out.println("Bu ürün açık artırmalı değil.");
            return;
        }

        AuctionProduct auctionProduct = (AuctionProduct) product;

        if (auctionProduct.isAuctionActive()) {
            System.out.println("Açık artırma henüz bitmemiş.");
            return;
        }

        if (auctionProduct.getWinner() == null) {
            System.out.println("Kazanan yok. Teklif verilmemiş.");
            return;
        }

        System.out.println("Kazanan: " + auctionProduct.getWinner().getName());
        System.out.println("Kazanan teklif: " + auctionProduct.getCurrentHighestBid());
    }

    // =========================
    // ORDER OPERATIONS
    // =========================

    public void listAllOrders() {
        if (orders.isEmpty()) {
            System.out.println("Henüz sipariş yok.");
            return;
        }

        System.out.println("=== TÜM SİPARİŞLER ===");
        for (Order order : orders) {
            printOrderDetails(order);
        }
    }

    public void listOrdersOfBuyer(Buyer buyer) {
        boolean found = false;
        System.out.println("=== BUYER SİPARİŞLERİ: " + buyer.getName() + " ===");

        for (Order order : orders) {
            if (order.getBuyer().getId() == buyer.getId()) {
                printOrderDetails(order);
                found = true;
            }
        }

        if (!found) {
            System.out.println("Bu kullanıcıya ait sipariş yok.");
        }
    }

    public void printOrderDetails(Order order) {
        System.out.println("-----------------------------------");
        System.out.println("Order ID: " + order.getId());
        System.out.println("Buyer: " + order.getBuyer().getName());
        System.out.println("Product: " + order.getProduct().getName());
        System.out.println("Total Price: " + order.getTotalPrice());
        System.out.println("Order Date: " + order.getOrderDate());
        System.out.println("-----------------------------------");
    }

    

    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }
}