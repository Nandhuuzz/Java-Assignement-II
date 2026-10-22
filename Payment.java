package hotelreservation.transaction;

public class Payment {
    public enum Status { PAID, REFUNDED }

    private static int counter = 0;

    private final String paymentId;
    private final Booking booking;
    private final double amount;
    private final String method;
    private Status status = Status.PAID;

    public Payment(Booking booking, double amount, String method) {
        this.paymentId = String.format("P%03d", ++counter);
        this.booking = booking;
        this.amount = amount;
        this.method = method;
    }

    public String getPaymentId() { return paymentId; }
    public Booking getBooking() { return booking; }
    public double getAmount() { return amount; }
    public String getMethod() { return method; }
    public Status getStatus() { return status; }

    public void refund() { status = Status.REFUNDED; }
}
