import java.time.LocalDateTime;
import java.util.ArrayList;

public class AuctionProduct extends Product {
    private double startPrice;
    private double currentHighestBid;
    private LocalDateTime endTime;
    private boolean auctionActive;
    private ArrayList<Bid> bids;
    private Buyer winner;

    public AuctionProduct(int id, String name, String description, double basePrice,
                          Seller seller, double startPrice, LocalDateTime endTime, Category category) {
        super(id, name, description, basePrice, seller, category);
        this.startPrice = startPrice;
        this.currentHighestBid = startPrice;
        this.endTime = endTime;
        this.auctionActive = true;
        this.bids = new ArrayList<>();
        this.winner = null;
    }

    public boolean placeBid(Bid bid) {
        if (bid.getAmount() > currentHighestBid) {
            currentHighestBid = bid.getAmount();
            bids.add(bid);
            winner = bid.getBuyer();
            return true;
        }
        return false;
    }

    public void endAuction() {
        this.auctionActive = false;
    }

    @Override
    public String toString() {
        return "Açık Artırma -> " + getName() + " | Başlangıç: " + startPrice + 
               " | En Yüksek Teklif: " + currentHighestBid;
    }

    public double getStartPrice() { return startPrice; }
    public double getCurrentHighestBid() { return currentHighestBid; }
    public LocalDateTime getEndTime() { return endTime; }
    public boolean isAuctionActive() { return auctionActive; }
    public Buyer getWinner() { return winner; }
}