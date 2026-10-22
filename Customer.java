package hotelreservation.model;

public class Customer {
    private final String id;
    private final String name;
    private final String phone;
    private int loyaltyPoints = 0;

    public Customer(String id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public int getLoyaltyPoints() { return loyaltyPoints; }

    public void addLoyaltyPoints(int points) { loyaltyPoints += points; }
    public void deductLoyaltyPoints(int points) { loyaltyPoints = Math.max(0, loyaltyPoints - points); }

    // Regular customers: no discount, no loyalty points (overridden by PremiumCustomer)
    public String getCategory() { return "Regular"; }
    public double getDiscountPercent() { return 0.0; }
    public int calculatePoints(double amount) { return 0; }
}
