import java.util.ArrayList;
import java.util.List;

public class BookingStorage {
    private static List<String> bookings = new ArrayList<>();

    static {
        bookings.add(
                "Booking Summary:\nName: John Doe\nEmail: john.doe@example.com\nPhone: 123-456-7890\nDate of Stay: 2024-12-01\nBranch: CityHotel\nRoom Type: Suite\nAdd-ons: Meals, High-Speed WiFi\nTotal Price: RM 230\n");
        bookings.add(
                "Booking Summary:\nName: Jane Smith\nEmail: jane.smith@example.com\nPhone: 987-654-3210\nDate of Stay: 2024-11-15\nBranch: BeachResort\nRoom Type: Deluxe\nAdd-ons: Laundry\nTotal Price: RM 165\n");
        bookings.add(
                "Booking Summary:\nName: Alice Johnson\nEmail: alice.johnson@example.com\nPhone: 555-123-4567\nDate of Stay: 2024-10-20\nBranch: CityHotel\nRoom Type: Standard\nAdd-ons: None\nTotal Price: RM 100\n");
        bookings.add(
                "Booking Summary:\nName: Bob Brown\nEmail: bob.brown@example.com\nPhone: 444-555-6666\nDate of Stay: 2024-09-25\nBranch: BeachResort\nRoom Type: Suite\nAdd-ons: Meals\nTotal Price: RM 250\n");
        bookings.add(
                "Booking Summary:\nName: Charlie Davis\nEmail: charlie.davis@example.com\nPhone: 333-444-5555\nDate of Stay: 2024-08-30\nBranch: CityHotel\nRoom Type: Deluxe\nAdd-ons: High-Speed WiFi\nTotal Price: RM 160\n");
        bookings.add(
                "Booking Summary:\nName: David Wilson\nEmail: david.wilson@example.com\nPhone: 222-333-4444\nDate of Stay: 2024-07-15\nBranch: BeachResort\nRoom Type: Standard\nAdd-ons: None\nTotal Price: RM 100\n");
        bookings.add(
                "Booking Summary:\nName: Emily Clark\nEmail: emily.clark@example.com\nPhone: 111-222-3333\nDate of Stay: 2024-06-10\nBranch: CityHotel\nRoom Type: Suite\nAdd-ons: Meals, Laundry\nTotal Price: RM 265\n");
        bookings.add(
                "Booking Summary:\nName: Frank Miller\nEmail: frank.miller@example.com\nPhone: 666-777-8888\nDate of Stay: 2024-05-05\nBranch: BeachResort\nRoom Type: Deluxe\nAdd-ons: High-Speed WiFi\nTotal Price: RM 175\n");
        bookings.add(
                "Booking Summary:\nName: Grace Lee\nEmail: grace.lee@example.com\nPhone: 999-888-7777\nDate of Stay: 2024-04-20\nBranch: CityHotel\nRoom Type: Standard\nAdd-ons: None\nTotal Price: RM 100\n");
        bookings.add(
                "Booking Summary:\nName: Henry Taylor\nEmail: henry.taylor@example.com\nPhone: 888-999-0000\nDate of Stay: 2024-03-15\nBranch: BeachResort\nRoom Type: Suite\nAdd-ons: Meals\nTotal Price: RM 250\n");
        bookings.add(
                "Booking Summary:\nName: Isabella Martinez\nEmail: isabella.martinez@example.com\nPhone: 777-666-5555\nDate of Stay: 2024-02-10\nBranch: CityHotel\nRoom Type: Deluxe\nAdd-ons: High-Speed WiFi\nTotal Price: RM 175\n");
    }

    public static void addBooking(String bookingInfo) {
        bookings.add(bookingInfo);
    }

    public static List<String> getBookings() {
        return bookings;
    }

    public static String getBooking(int index) {
        if (index >= 0 && index < bookings.size()) {
            return bookings.get(index);
        }
        return null;
    }
}
