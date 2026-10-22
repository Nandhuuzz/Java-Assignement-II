package hotelreservation.model;

/** Premium customers get a 10% discount and 1 loyalty point for every Rs 100 spent. */
public class PremiumCustomer extends Customer {
    private static final double DISCOUNT = 10.0;

    public PremiumCustomer(String id, String name, String phone) {
        super(id, name, phone);
    }

    @Override public String getCategory() { return "Premium"; }
    @Override public double getDiscountPercent() { return DISCOUNT; }
    @Override public int calculatePoints(double amount) { return (int) (amount / 100); }
}
