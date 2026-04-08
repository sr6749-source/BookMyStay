abstract class Room {
    private String roomType;
    private int numberOfBeds;
    private double size;
    private double price;

    public Room(String roomType, int numberOfBeds, double size, double price) {
        this.roomType = roomType;
        this.numberOfBeds = numberOfBeds;
        this.size = size;
        this.price = price;
    }
    public String getRoomType() {
        return roomType;
    }
    public int getNumberOfBeds() {
        return numberOfBeds;
    }
    public double getSize() {
        return size;
    }
    public double getPrice() {
        return price;
    }
    public abstract void displayRoomDetails();
}
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 200.0, 1500.0);
    }
    @Override
    public void displayRoomDetails() {
        System.out.println("Room Type: " + getRoomType());
        System.out.println("Beds: " + getNumberOfBeds());
        System.out.println("Size: " + getSize() + " sq.ft");
        System.out.println("Price: ₹" + getPrice());
    }
}
class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 350.0, 2500.0);
    }
    @Override
    public void displayRoomDetails() {
        System.out.println("Room Type: " + getRoomType());
        System.out.println("Beds: " + getNumberOfBeds());
        System.out.println("Size: " + getSize() + " sq.ft");
        System.out.println("Price: ₹" + getPrice());
    }
}
class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 600.0, 5000.0);
    }
    @Override
    public void displayRoomDetails() {
        System.out.println("Room Type: " + getRoomType());
        System.out.println("Beds: " + getNumberOfBeds());
        System.out.println("Size: " + getSize() + " sq.ft");
        System.out.println("Price: ₹" + getPrice());
    }
}
public class BookMyStay {
    public static void main(String[] args) {
        Room singleRoom = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suiteRoom = new SuiteRoom();
        int singleRoomAvailability = 5;
        int doubleRoomAvailability = 3;
        int suiteRoomAvailability = 2;
        System.out.println("=== Room Details and Availability ===\n");

        singleRoom.displayRoomDetails();
        System.out.println("Available: " + singleRoomAvailability);
        System.out.println("-----------------------------------");

        doubleRoom.displayRoomDetails();
        System.out.println("Available: " + doubleRoomAvailability);
        System.out.println("-----------------------------------");

        suiteRoom.displayRoomDetails();
        System.out.println("Available: " + suiteRoomAvailability);
        System.out.println("-----------------------------------");
    }
}