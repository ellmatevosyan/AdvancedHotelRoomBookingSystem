import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Customer implements Serializable {
    private static final long serialVersionUID = 8883948304561792395L;
    private int customerId;
    private String name;
    private String email;
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern pattern = Pattern.compile(EMAIL_REGEX); //compiles EMAIL_REGEX
    //String to check if they conform to the email address pattern defined by EMAIL_REGEX
    private List<Booking> customerBookingHistory = new ArrayList<>();


    public static boolean isValid(String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public Customer(String name, String email, int customerId){
        this.customerId = customerId;
        this.name = name;
        if(isValid(email)){
            this.email = email;
        }else{
            System.out.println("Invalid email address: " + email);
        }
    }

 

    public int getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
    public List<Booking> getBookingHistory() {
        return customerBookingHistory;
    }

    public void setCustomerBookingHistory(List<Booking> customerBookingHistory) {
        this.customerBookingHistory = customerBookingHistory;
    }

    public void addBooking(Booking booking){
        customerBookingHistory.add(booking);
    }


    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", customerBookingHistory=" + customerBookingHistory +
                '}';
    }
}
