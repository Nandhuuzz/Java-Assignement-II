package hotelreservation.transaction;

import java.time.LocalDate;
import hotelreservation.model.Customer;
import hotelreservation.model.Room;

public class Booking {
    public enum Status { CONFIRMED, CANCELLED }

    private static int counter = 0;

    private final String bookingId;
    private final Room room;
    private final Customer customer;
    private final int nights;
    private final LocalDate checkIn;
    private Status status = Status.CONFIRMED;
    private Bill bill;
    private Payment payment;
    private int pointsEarned = 0;

    public Booking(Room room, Customer customer, int nights) {
        if (!room.isAvailable()) {
            throw new IllegalStateException("Room " + room.getRoomNumber() + " is not available.");
        }
        this.bookingId = String.format("B%03d", ++counter);
        this.room = room;
        this.customer = customer;
        this.nights = nights;
        this.checkIn = LocalDate.now();
        room.setAvailable(false);                  // availability updated immediately
    }

    public String getBookingId() { return bookingId; }
    public Room getRoom() { return room; }
    public Customer getCustomer() { return customer; }
    public int getNights() { return nights; }
    public LocalDate getCheckIn() { return checkIn; }
    public LocalDate getCheckOut() { return checkIn.plusDays(nights); }
    public Status getStatus() { return status; }
    public Bill getBill() { return bill; }
    public Payment getPayment() { return payment; }
    public int getPointsEarned() { return pointsEarned; }
    public boolean isConfirmed() { return status == Status.CONFIRMED; }
    public double getSubtotal() { return nights * room.getPricePerNight(); }

    public void attachBilling(Bill bill, Payment payment, int pointsEarned) {
        this.bill = bill;
        this.payment = payment;
        this.pointsEarned = pointsEarned;
        customer.addLoyaltyPoints(pointsEarned);
    }

    public void cancel() {
        if (!isConfirmed()) {
            throw new IllegalStateException("Booking " + bookingId + " is already cancelled.");
        }
        status = Status.CANCELLED;
        room.setAvailable(true);                   // availability updated immediately
        customer.deductLoyaltyPoints(pointsEarned);
        if (payment != null) payment.refund();
    }

    public void printConfirmation() {
        System.out.println("\n========== RESERVATION CONFIRMATION ==========");
        System.out.println("Booking ID     : " + bookingId);
        System.out.println("Status         : " + status);
        System.out.println("Customer       : " + customer.getId() + " - " + customer.getName()
                + " (" + customer.getCategory() + ")");
        System.out.println("Room           : " + room.getRoomNumber() + " [" + room.getType() + "]");
        System.out.println("Check-in       : " + checkIn);
        System.out.println("Check-out      : " + getCheckOut());
        System.out.println("Nights         : " + nights);
        if (payment != null) {
            System.out.println("Payment        : " + payment.getPaymentId() + " via "
                    + payment.getMethod() + " - " + payment.getStatus());
        }
        if (customer.getDiscountPercent() > 0) {
            System.out.println("Loyalty points : +" + pointsEarned + " (balance " + customer.getLoyaltyPoints() + ")");
        } else {
            System.out.println("Loyalty points : Not applicable (regular customer)");
        }
        System.out.println("Room status    : BOOKED (not available)");
        System.out.println("==============================================");
    }

    public void printCancellation() {
        System.out.println("\n=========== CANCELLATION RECEIPT ===========");
        System.out.println("Booking ID     : " + bookingId);
        System.out.println("Status         : " + status);
        System.out.println("Customer       : " + customer.getName() + " (" + customer.getCategory() + ")");
        System.out.println("Room           : " + room.getRoomNumber() + " [" + room.getType() + "]");
        if (bill != null) {
            System.out.printf("Amount refunded: Rs %.2f (%s)%n", bill.getTotal(), payment.getStatus());
        }
        if (pointsEarned > 0) {
            System.out.println("Loyalty points : -" + pointsEarned + " (balance " + customer.getLoyaltyPoints() + ")");
        }
        System.out.println("Room status    : AVAILABLE again");
        System.out.println("============================================");
    }
}
