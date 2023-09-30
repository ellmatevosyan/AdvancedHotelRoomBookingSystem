import java.io.Serializable;
import java.time.LocalDate;


public class Booking implements Serializable {
    private Customer customer;

    private Room room;
    private LocalDate startDate;
    private LocalDate endDate;

    public Booking(Customer customer, Room room, LocalDate startDate, LocalDate endDate) {
        this.customer = customer;
        this.room = room;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    boolean isDateOverlapping(LocalDate start, LocalDate end) {
        return (start.isEqual(startDate) || start.isAfter(startDate)) && start.isBefore(endDate)
                || end.isAfter(startDate) && (end.isBefore(endDate) || end.isEqual(endDate));
    }


}
