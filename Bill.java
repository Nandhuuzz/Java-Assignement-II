package hotelreservation.transaction;

import hotelreservation.model.Customer;
import hotelreservation.model.Room;

public class Bill {
    public static final double TAX_RATE = 12.0;    // GST %

    private final Booking booking;
    private final double subtotal;
    private final double discount;
    private final double taxableAmount;
    private final double tax;
    private final double total;

    public Bill(Booking booking) {
        this.booking = booking;
        this.subtotal = booking.getSubtotal();
        this.discount = subtotal * booking.getCustomer().getDiscountPercent() / 100.0;
        this.taxableAmount = subtotal - discount;
        this.tax = taxableAmount * TAX_RATE / 100.0;
        this.total = taxableAmount + tax;
    }

    public double getSubtotal() { return subtotal; }
    public double getDiscount() { return discount; }
    public double getTaxableAmount() { return taxableAmount; }
    public double getTax() { return tax; }
    public double getTotal() { return total; }

    public void print() {
        Customer c = booking.getCustomer();
        Room r = booking.getRoom();

        System.out.println("\n=================== BILL ===================");
        System.out.println("Booking ID     : " + booking.getBookingId());
        System.out.println("Customer       : " + c.getName() + " (" + c.getCategory() + ")");
        System.out.println("Room           : " + r.getRoomNumber() + " [" + r.getType() + "]");
        System.out.println("--------------------------------------------");
        System.out.printf("Room charges   : Rs %10.2f (%d night(s) x Rs %.2f)%n",
                subtotal, booking.getNights(), r.getPricePerNight());
        if (c.getDiscountPercent() > 0) {
            System.out.printf("Premium disc.  : -Rs %9.2f (%.0f%%)%n", discount, c.getDiscountPercent());
        } else {
            System.out.println("Discount       : None (regular customer)");
        }
        System.out.printf("Taxable amount : Rs %10.2f%n", taxableAmount);
        System.out.printf("GST (%.0f%%)      : Rs %10.2f%n", TAX_RATE, tax);
        System.out.println("--------------------------------------------");
        System.out.printf("TOTAL PAID     : Rs %10.2f%n", total);
        System.out.println("============================================");
    }
}
