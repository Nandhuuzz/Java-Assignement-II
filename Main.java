package hotelreservation.app;

import java.util.Scanner;
import hotelreservation.model.Customer;
import hotelreservation.model.PremiumCustomer;
import hotelreservation.model.Room;
import hotelreservation.service.Hotel;
import hotelreservation.transaction.Booking;

/** Console driver for the Hotel Reservation System. */
public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final Hotel hotel = new Hotel("Grand Palace Hotel");

    public static void main(String[] args) {
        System.out.println("=== HOTEL RESERVATION SYSTEM - " + hotel.getName() + " ===");
        boolean running = true;
        while (running) {
            System.out.println("\n----------- MENU -----------");
            System.out.println("1. Add Room");
            System.out.println("2. Add Customer");
            System.out.println("3. Make Reservation");
            System.out.println("4. Cancel Reservation");
            System.out.println("5. Display Rooms");
            System.out.println("6. Exit");
            int choice = readInt("Enter choice: ", 1, 6);
            switch (choice) {
                case 1: addRoom(); break;
                case 2: addCustomer(); break;
                case 3: makeReservation(); break;
                case 4: cancelReservation(); break;
                case 5: displayRooms(); break;
                case 6: running = false; System.out.println("Thank you. Goodbye!"); break;
                default: break;
            }
        }
    }

    // ---------- menu actions ----------
    private static void addRoom() {
        int number = readInt("Room number: ", 1, 9999);
        if (hotel.findRoom(number) != null) {
            System.out.println("Error: room " + number + " already exists.");
            return;
        }
        int t = readInt("Type (1 = Single, 2 = Double, 3 = Suite): ", 1, 3);
        String type = t == 1 ? "Single" : t == 2 ? "Double" : "Suite";
        double price = readDouble("Price per night (Rs): ");
        hotel.addRoom(new Room(number, type, price));
        System.out.println("Room added successfully.");
    }

    private static void addCustomer() {
        String id = readText("Customer ID: ");
        if (hotel.findCustomer(id) != null) {
            System.out.println("Error: a customer with ID " + id + " already exists.");
            return;
        }
        String name = readText("Name: ");
        String phone = readText("Phone: ");
        int cat = readInt("Category (1 = Regular, 2 = Premium): ", 1, 2);
        hotel.addCustomer(cat == 2 ? new PremiumCustomer(id, name, phone) : new Customer(id, name, phone));
        System.out.println("Customer added successfully."
                + (cat == 2 ? " (10% discount + loyalty points)" : ""));
    }

    private static void makeReservation() {
        if (hotel.getRooms().isEmpty() || hotel.getCustomers().isEmpty()) {
            System.out.println("Error: add at least one room and one customer first.");
            return;
        }
        Customer c = hotel.findCustomer(readText("Customer ID: "));
        if (c == null) { System.out.println("Error: customer not found."); return; }
        System.out.println("Available rooms: " + hotel.getAvailableRoomNumbers());
        Room r = hotel.findRoom(readInt("Room number: ", 1, 9999));
        if (r == null) { System.out.println("Error: room not found."); return; }
        if (!r.isAvailable()) {
            System.out.println("Error: room " + r.getRoomNumber() + " is not available. Reservation not confirmed.");
            return;
        }
        int nights = readInt("Number of nights: ", 1, 60);
        int m = readInt("Payment method (1 = Cash, 2 = Card, 3 = UPI): ", 1, 3);
        String method = m == 1 ? "Cash" : m == 2 ? "Card" : "UPI";
        try {
            Booking b = hotel.makeReservation(c.getId(), r.getRoomNumber(), nights, method);
            b.printConfirmation();
            b.getBill().print();
        } catch (IllegalArgumentException | IllegalStateException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private static void cancelReservation() {
        if (hotel.getActiveBookingIds().isEmpty()) {
            System.out.println("No active reservations.");
            return;
        }
        System.out.println("Active reservations: " + hotel.getActiveBookingIds());
        try {
            Booking b = hotel.cancelReservation(readText("Booking ID: "));
            b.printCancellation();
        } catch (IllegalArgumentException | IllegalStateException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private static void displayRooms() {
        if (hotel.getRooms().isEmpty()) { System.out.println("No rooms added yet."); return; }
        System.out.println("\n" + String.format("%-8s %-8s %-14s %s",
                "Room", "Type", "Price/night", "Status"));
        System.out.println("-".repeat(44));
        for (Room r : hotel.getRooms()) {
            System.out.println(String.format("%-8d %-8s Rs %-11.2f %s",
                    r.getRoomNumber(), r.getType(), r.getPricePerNight(),
                    r.isAvailable() ? "Available" : "Booked"));
        }
    }

    // ---------- validated input ----------
    private static String readText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            if (!s.isEmpty()) return s;
            System.out.println("Input cannot be empty. Try again.");
        }
    }

    private static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int n = Integer.parseInt(sc.nextLine().trim());
                if (n >= min && n <= max) return n;
                System.out.println("Invalid input: enter a number between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: please enter a whole number.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double d = Double.parseDouble(sc.nextLine().trim());
                if (d > 0) return d;
                System.out.println("Invalid input: value must be greater than 0.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: please enter a number.");
            }
        }
    }
}
