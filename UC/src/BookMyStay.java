import java.util.*;
abstract class Room {
    private String roomType;
    private int beds;
    private double size;
    private double price;

    public Room(String roomType, int beds, double size, double price) {
        this.roomType = roomType;
        this.beds = beds;
        this.size = size;
        this.price = price;
    }
    public String getRoomType() {
        return roomType;
    }
    public int getBeds() {
        return beds;
    }
    public double getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }

    public abstract void displayDetails();
}

class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 200, 1500);
    }

    public void displayDetails() {
        System.out.println("Room: " + getRoomType());
        System.out.println("Beds: " + getBeds());
        System.out.println("Size: " + getSize() + " sq.ft");
        System.out.println("Price: ₹" + getPrice());
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 350, 2500);
    }

    public void displayDetails() {
        System.out.println("Room: " + getRoomType());
        System.out.println("Beds: " + getBeds());
        System.out.println("Size: " + getSize() + " sq.ft");
        System.out.println("Price: ₹" + getPrice());
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 600, 5000);
    }

    public void displayDetails() {
        System.out.println("Room: " + getRoomType());
        System.out.println("Beds: " + getBeds());
        System.out.println("Size: " + getSize() + " sq.ft");
        System.out.println("Price: ₹" + getPrice());
    }
}

class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 0); // Example: unavailable
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public Map<String, Integer> getAllAvailability() {
        return inventory;
    }
}

class RoomSearchService {

    public void searchAvailableRooms(List<Room> rooms, RoomInventory inventory) {

        System.out.println("=== Available Rooms ===\n");

        boolean found = false;

        for (Room room : rooms) {
            int available = inventory.getAvailability(room.getRoomType());


            if (available > 0) {
                room.displayDetails();
                System.out.println("Available: " + available);
                System.out.println("----------------------------");
                found = true;
            }
        }

        if (!found) {
            System.out.println("No rooms available at the moment.");
        }
    }
}

public class BookMyStay {

    public static void main(String[] args) {

        List<Room> rooms = new ArrayList<>();
        rooms.add(new SingleRoom());
        rooms.add(new DoubleRoom());
        rooms.add(new SuiteRoom());

        RoomInventory inventory = new RoomInventory();

        RoomSearchService searchService = new RoomSearchService();

        searchService.searchAvailableRooms(rooms, inventory);
    }
}