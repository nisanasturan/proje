public class Bid {
    private Buyer buyer;
    private double amount;

    public Bid(Buyer buyer, double amount) {
        this.buyer = buyer;
        this.amount = amount;
    }

    public Buyer getBuyer() { return buyer; }
    public double getAmount() { return amount; }
}