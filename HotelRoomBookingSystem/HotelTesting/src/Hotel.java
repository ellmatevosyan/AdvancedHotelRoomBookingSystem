import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Hotel {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Database cd = new Database();
        cd.loadDatabase();
        while (true) {
            System.out.println("Options:");
            System.out.println("1. Add a customer.");
            System.out.println("2. Display customers.");
            System.out.println("3. Add a room.");
            System.out.println("4. Book a room.");
            System.out.println("5. Display rooms. ");
            System.out.println("6. Save database.");
            System.out.println("7. Exit. ");
            System.out.print("Select an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    System.out.println("Name: ");
                    String name = scanner.nextLine();
                    while (true) {
                        System.out.println("Enter the email: ");
                        String inputEmail = scanner.nextLine();
                        if (Customer.isValid(inputEmail)) {
                            cd.addCustomer(name, inputEmail);
                            break;
                        } else {
                            System.out.println("Invalid email address. Please try again.");
                        }
                    }
                    break;
                case 2:
                    cd.displayCustomers();
                    break;
                case 3:
                    System.out.println("Available room types:");
                    for (RoomType roomType : RoomType.values()) {
                        System.out.println(roomType.ordinal() + 1 + ". " + roomType.getDescription());
                    }
                    System.out.println("Select a room type (1-" + RoomType.values().length + "): ");
                    int roomTypeChoice1 = scanner.nextInt();
                    scanner.nextLine();

                    //Check whether the selected room type is valid
                    if (roomTypeChoice1 < 1 || roomTypeChoice1 > RoomType.values().length) {
                        System.out.println("Invalid room type selection.");
                        break;
                    }
                    cd.addRoom(roomTypeChoice1);
                    System.out.println("Room is added.");
                    break;


                case 4:
                    System.out.println("Room booking:");
                    System.out.println("Enter the name of the customer: ");
                    String customerName = scanner.nextLine();
                    Customer customer = cd.findCustomerByName(customerName);

                    if (customer == null) {
                        System.out.println("Customer not found. Please register first.");
                        break;
                    }

                    LocalDate startDate;
                    LocalDate endDate;
                    while (true) {
                        System.out.println("Enter booking start date (yyyy-MM-dd)");
                        String startDateStr = scanner.nextLine();
                        try {
                            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            startDate = LocalDate.parse(startDateStr, dateFormat);
                            LocalDate currentDate = LocalDate.now();

                            //Check if the start date is in the past
                            if (startDate.isBefore(currentDate)) {
                                System.out.println("Invalid start date. Please choose a current or future date");
                                continue; //Ask for input again
                            }
                            break;
                        } catch (DateTimeParseException e) {
                            System.out.println("Invalid date format. Please use yyyy-MM-dd.");
                        }
                    }

                    while (true) {
                        System.out.println("Enter booking end date (yyyy-MM-dd)");
                        String endDateStr = scanner.nextLine();
                        try {
                            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            endDate = LocalDate.parse(endDateStr, dateFormat);

                            //Get the current date
                            LocalDate currentDate = LocalDate.now();


                            // Check if the end date is after the start date
                            if (endDate.isBefore(startDate)) {
                                System.out.println("Invalid date range. End date should be after the start date.");
                                continue; // Ask for input again
                            }
                            break;

                        } catch (DateTimeParseException e) {
                            System.out.println("Invalid date format. Please use yyyy-MM-dd.");
                        }
                    }

                    try {

                        if (cd.findAvailableRooms(startDate, endDate) > 0) {
                            System.out.println("Select a room type (1-" + RoomType.values().length + "): ");
                            for (RoomType roomType : RoomType.values()) {
                                System.out.println(roomType.ordinal() + 1 + ". " + roomType.getDescription());
                            }
                            int roomTypeChoice = scanner.nextInt();
                            scanner.nextLine();

                            //Check whether the selected room type is valid
                            if (roomTypeChoice < 1 || roomTypeChoice > RoomType.values().length) {
                                System.out.println("Invalid room type selection.");
                                break;
                            }


                            RoomType selectedRoomType = RoomType.values()[roomTypeChoice - 1];


                            //Displaying room details
                            cd.displayRoomDetails(selectedRoomType);
                            double totalAmount = cd.billCalculator(selectedRoomType, startDate, endDate);
                            System.out.println("Total amount of the room is: " + totalAmount);
                            System.out.println("Do you agree with this booking conditions? (yes/no) ");
                            String response = scanner.nextLine();
                            if (response.equalsIgnoreCase("yes")) {
                                cd.bookRoom(customer, selectedRoomType, startDate, endDate);
                                System.out.println("The room is booked successfully");
                                break;
                            } else if (response.equalsIgnoreCase("no")) {
                                System.out.println("Select another room type (1-" + RoomType.values().length + "): ");

                                for (RoomType roomType : RoomType.values()) {
                                    System.out.println(roomType.ordinal() + 1 + ". " + roomType.getDescription());
                                }
                                int roomTypeChoice2 = scanner.nextInt();
                                scanner.nextLine();

                                //Check whether the selected room type is valid
                                if (roomTypeChoice2 < 1 || roomTypeChoice2 > RoomType.values().length) {
                                    System.out.println("Invalid room type selection.");
                                    continue;
                                }
                                RoomType selectedRoomType2 = RoomType.values()[roomTypeChoice2 - 1];
                                cd.displayRoomDetails(selectedRoomType2);
                                double totalAmount2 = cd.billCalculator(selectedRoomType2, startDate, endDate);
                                System.out.println("Total amount of the room is: " + totalAmount2);
                                cd.bookRoom(customer, selectedRoomType2, startDate, endDate);
                                System.out.println("The room is booked successfully");


                            } else {
                                System.out.println("Invalid response. Please enter 'yes' or 'no'.");
                                continue;
                            }
                        }

                    } catch (DateTimeParseException e) {
                        System.out.println("Invalid date format. Please use yyyy-MM-dd.");
                    }

                    break;
                case 5:
                    cd.displayRooms();
                    break;
                case 6:
                    cd.saveDatabase();
                    break;
                case 7:
                    cd.saveDatabase();
                    System.out.println("Exiting.");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option.");

            }
        }
    }
}
