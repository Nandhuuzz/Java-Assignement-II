# Hotel Reservation System (Java Console Application)

**Name:** Nandhana G J
**Roll No:** 717825E140

## Problem Statement
Design and implement a Java console application for a Hotel Reservation System
to manage rooms, customers, bookings, and billing.

**Class Requirements:** Room, Customer, PremiumCustomer, Booking, Payment, Bill, Hotel

**Business Rules:**
1. Bookings can only be confirmed if rooms are available.
2. Room availability updates immediately after booking or cancellation.
3. Premium customers may receive discounts or loyalty points.
4. Billing includes taxes and optional discounts.
5. Each booking links directly to a room and customer.

**Menu Options:** Add Room / Add Customer / Make Reservation / Cancel Reservation / Display Rooms / Exit

**Expected Output:**
- Show reservation confirmation with details.
- Display updated room availability.
- Show bills with discounts and taxes.

## Rules Used
| Rule | Behaviour |
|---|---|
| Reservation | Confirmed only if the room is available; room becomes Booked immediately |
| Cancellation | Room becomes Available immediately; payment refunded; loyalty points reversed |
| Premium customer | 10% discount on room charges + 1 loyalty point per Rs 100 (after discount, before tax) |
| Regular customer | No discount, no loyalty points |
| Bill | Room charges - discount = taxable amount; GST 12% added; total |

## Project Structure
```
src/hotelreservation/model        Room, Customer, PremiumCustomer
src/hotelreservation/transaction  Booking, Payment, Bill
src/hotelreservation/service      Hotel
src/hotelreservation/app          Main
```

## Compile and Run
```bash
mkdir out
javac -d out src/hotelreservation/model/*.java src/hotelreservation/transaction/*.java src/hotelreservation/service/*.java src/hotelreservation/app/*.java
java -cp out hotelreservation.app.Main
```
In Eclipse: File > New > Java Project, copy the `src` folders in, then run `Main.java`.
