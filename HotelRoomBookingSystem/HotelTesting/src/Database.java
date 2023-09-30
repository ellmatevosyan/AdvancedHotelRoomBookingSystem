import java.io.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Database {
    private static final String DATABASE_FILE = "db.txt";
    private List<Customer> customers;
    private int nextCustomerId;
    private int nextRoomId;
    private List<Room> rooms;
    private List<Room> singleRooms;
    private List<Room> doubleRooms;
    private List<Room> deluxeRooms;
    private HashMap<Integer, RoomType> roomTypeChoice;
    private HashMap<RoomType, List<Room>> roomsHash;

    public Database() {
        customers = new ArrayList<>();
        rooms = new ArrayList<>();
        singleRooms = new ArrayList<>();
        doubleRooms = new ArrayList<>();
        deluxeRooms = new ArrayList<>();

        initializeHashMaps();
        loadDatabase();
    }

    public void addCustomer(String name, String email) {
        this.nextCustomerId = ++nextCustomerId;
        System.out.println(nextCustomerId);

        Customer customer = new Customer(name, email, nextCustomerId);
        customers.add(customer);
        System.out.println("Customer added " + customer);
    }

    public void displayCustomers() {
        System.out.println("List of customers: ");
        for (Customer customer : customers) {
            System.out.println(customer.toString());
        }
    }

    public void addRoom(Integer option) {
        RoomType selectedRoomType = roomTypeChoice.get(option);
        this.nextRoomId = ++nextRoomId;
        Room room = new Room(selectedRoomType, nextRoomId);
        rooms.add(room);

        // Add the room to the appropriate room type list
        roomsHash.get(selectedRoomType).add(room);
    }

    public void displayRoomDetails(RoomType roomType) {
        System.out.println("Room ID: " + nextRoomId);
        System.out.println("Room Type: " + roomType.getDescription());
        System.out.println("Facilities: " + roomType.getFacilities());
        System.out.println("Price per Day: $" + roomType.getPricePerDay());
    }

    public void displayRooms() {
        System.out.println("List of rooms: ");

        // Display Single Rooms
        System.out.println("Single Rooms:");
        displayRoomsByType(singleRooms);

        // Display Double Rooms
        System.out.println("\nDouble Rooms:");
        displayRoomsByType(doubleRooms);

        // Display Deluxe Rooms
        System.out.println("\nDeluxe Rooms:");
        displayRoomsByType(deluxeRooms);
    }

    private void displayRoomsByType(List<Room> roomList) {
        for (Room room : roomList) {
            System.out.println(room.toString());
        }
    }

    public void printRoomsArrayList() {
        System.out.println("Added rooms in the rooms arraylist.");
        for (Room r : rooms) {
            System.out.println(r.toString());
        }
    }

    public void bookRoom(Customer customer, RoomType roomType, LocalDate startDate, LocalDate endDate) {
        Room room = roomsHash.get(roomType).get(0);
        room.book(customer, startDate, endDate);
        Booking booking = new Booking(customer, room, startDate, endDate);
        customer.addBooking(booking);
    }

    public Customer findCustomerByName(String name) {
        for (Customer customer : customers) {
            if (customer.getName().equalsIgnoreCase(name)) {
                return customer;
            }
        }
        return null; // No such customer
    }

    public void saveDatabase() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("db.txt"))) {
            oos.writeObject(customers);
            oos.writeInt(nextCustomerId);
            oos.writeObject(rooms);
            oos.writeObject(singleRooms);
            oos.writeObject(doubleRooms);
            oos.writeObject(deluxeRooms);
            oos.writeInt(nextRoomId);
            //Save customer booking history
            for (Customer customer : customers) {
                List<Booking> bookingHistory = customer.getBookingHistory();
                oos.writeObject(bookingHistory != null ? bookingHistory : Collections.emptyList());
            }
            System.out.println("Database saved");


        } catch (IOException e) {
            System.out.println("Error saving database: " + e.getMessage());
        }
    }

    public void loadDatabase() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("db.txt"))) {
            customers = (List<Customer>) ois.readObject();
            nextCustomerId = ois.readInt();
            rooms = (List<Room>) ois.readObject();
            singleRooms = (List<Room>) ois.readObject();
            doubleRooms = (List<Room>) ois.readObject();
            deluxeRooms = (List<Room>) ois.readObject();
            nextRoomId = ois.readInt();

            roomsHash.put(RoomType.SINGLE, singleRooms);
            roomsHash.put(RoomType.DOUBLE, doubleRooms);
            roomsHash.put(RoomType.DELUXE, deluxeRooms);

            //Load customer booking history
            for (Customer customer : customers) {
                List<Booking> customerBookingHistory = (List<Booking>) ois.readObject();
                customer.setCustomerBookingHistory(customerBookingHistory);
            }
            System.out.println("Database loaded.");

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading database: " + e.getMessage());
        }
    }

    int findAvailableRooms(LocalDate start, LocalDate end) {
        HashMap<RoomType, Integer> availableRoomTypes = new HashMap<>();
        int count = 0;

        for (RoomType roomType : RoomType.values()) {
            availableRoomTypes.put(roomType, 0); // Initialize counts to 0 for each room type
        }

        for (Room room : rooms) {
            if (room.isAvailable(start, end)) {
                count++;
                RoomType roomType = room.getRoomType();
                availableRoomTypes.put(roomType, availableRoomTypes.get(roomType) + 1); // Increment count for the available room type
            }
        }
        if (count == 0) {
            System.out.println("There are no available rooms");
            return count;
        }

        System.out.println("Available room types between " + start + " and " + end + ":");
        for (RoomType roomType : RoomType.values()) {
            int availableCount = availableRoomTypes.get(roomType);
            if (availableCount > 0) {
                System.out.println(roomType + " (Available: " + availableCount + ")");
            }
        }

        return count;
    }

    public double billCalculator(RoomType roomType, LocalDate startDate, LocalDate endDate) {
        final double TAX_RATE = 0.20;      // 20% tax rate
        final double SERVICE_FEE_RATE = 0.10;  // 10% service fee rate

        //Get price per day for the specified room type
        double pricePerDay = roomType.getPricePerDay();

        //Calculate the number of days for the booking period
        long numberOfDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;

        //Calculate room charge (net)
        double roomCharge = pricePerDay * numberOfDays;

        double taxAmount = roomCharge * TAX_RATE;
        double serviceFee = (roomCharge + taxAmount) * SERVICE_FEE_RATE;
        double totalAmount = roomCharge + taxAmount + serviceFee;
        return totalAmount;

    }

    private void initializeHashMaps() {
        roomTypeChoice = new HashMap<>();
        roomTypeChoice.put(1, RoomType.SINGLE);
        roomTypeChoice.put(2, RoomType.DOUBLE);
        roomTypeChoice.put(3, RoomType.DELUXE);

        roomsHash = new HashMap<>();
        roomsHash.put(RoomType.SINGLE, singleRooms);
        roomsHash.put(RoomType.DOUBLE, doubleRooms);
        roomsHash.put(RoomType.DELUXE, deluxeRooms);
    }

}
