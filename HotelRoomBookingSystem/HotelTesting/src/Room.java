import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Room implements Serializable {
    private static final long serialVersionUID = -7329539896133364720L;
    private int roomId;
    private RoomType roomType;
    private List<Booking> roomBookingHistory = new ArrayList<>();

    public Room(RoomType roomType, int roomId) {
        this.roomId = roomId;
        this.roomType = roomType;

    }
    public void book(Customer customer, LocalDate startDate, LocalDate endDate) {
        // Check if the room is already booked for the specified date range

        if (!this.isAvailable(startDate, endDate)) {
            System.out.println("The room is already booked for the specified dates.");
            return; // Room is already booked for this date range
        }


        // If the loop completes without finding a conflicting booking, add the new booking
        Booking booking = new Booking(customer, this, startDate, endDate);
        roomBookingHistory.add(booking);
    }

    boolean isAvailable(LocalDate start, LocalDate end) {
        for (Booking booking : roomBookingHistory) {
            if (booking.isDateOverlapping(start, end)) {
                return false;
            }
        }
        return true;
    }

    public List<Booking> getBookingHistory() {
        return roomBookingHistory;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomId=" + roomId +
                ", roomType=" + roomType +
                '}';
    }
}








