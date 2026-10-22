package hotelreservation.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import hotelreservation.model.Customer;
import hotelreservation.model.Room;
import hotelreservation.transaction.Bill;
import hotelreservation.transaction.Booking;
import hotelreservation.transaction.Payment;

/** Manages rooms, customers and bookings. */
public class Hotel {
    private final String name;
    private final List<Room> rooms = new ArrayList<>();
    private final List<Customer> customers = new ArrayList<>();
    private final Map<String, Booking> bookings = new LinkedHashMap<>();

    public Hotel(String name) { this.name = name; }

    public String getName() { return name; }
    public List<Room> getRooms() { return rooms; }
    public List<Customer> getCustomers() { return customers; }

    public void addRoom(Room room) {
        if (findRoom(room.getRoomNumber()) != null) {
            throw new IllegalArgumentException("Room " + room.getRoomNumber() + " already exists.");
        }
        rooms.add(room);
    }

    public void addCustomer(Customer customer) {
        if (findCustomer(customer.getId()) != null) {
            throw new IllegalArgumentException("Customer " + customer.getId() + " already exists.");
        }
        customers.add(customer);
    }

    public Room findRoom(int number) {
        for (Room r : rooms) if (r.getRoomNumber() == number) return r;
        return null;
    }

    public Customer findCustomer(String id) {
        for (Customer c : customers) if (c.getId().equalsIgnoreCase(id)) return c;
        return null;
    }

    public Booking findBooking(String id) { return bookings.get(id.toUpperCase()); }

    public List<String> getActiveBookingIds() {
        List<String> ids = new ArrayList<>();
        for (Booking b : bookings.values()) if (b.isConfirmed()) ids.add(b.getBookingId());
        return ids;
    }

    public List<Integer> getAvailableRoomNumbers() {
        List<Integer> nums = new ArrayList<>();
        for (Room r : rooms) if (r.isAvailable()) nums.add(r.getRoomNumber());
        return nums;
    }

    /** Confirms a reservation only if the room is available; bills and records payment. */
    public Booking makeReservation(String customerId, int roomNumber, int nights, String method) {
        Customer customer = findCustomer(customerId);
        if (customer == null) throw new IllegalArgumentException("Customer not found.");
        Room room = findRoom(roomNumber);
        if (room == null) throw new IllegalArgumentException("Room not found.");

        Booking booking = new Booking(room, customer, nights);   // throws if not available
        Bill bill = new Bill(booking);
        Payment payment = new Payment(booking, bill.getTotal(), method);
        int points = customer.calculatePoints(bill.getTaxableAmount());
        booking.attachBilling(bill, payment, points);
        bookings.put(booking.getBookingId(), booking);
        return booking;
    }

    public Booking cancelReservation(String bookingId) {
        Booking booking = findBooking(bookingId);
        if (booking == null) throw new IllegalArgumentException("Booking not found.");
        booking.cancel();
        return booking;
    }
}
