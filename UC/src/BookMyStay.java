import java.util.*;

abstract class Room {
    private String type;
    private int beds;
    private double price;

    public Room(String type, int beds, double price) {
        this.type = type;
        this.beds = beds;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public int getBeds() {
        return beds;
    }

    public double getPrice() {
        return price;
    }
}

class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 1000);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 1800);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 3000);
    }
}

class RoomInventory {
    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decrement(String roomType) {
        inventory.put(roomType, getAvailability(roomType) - 1);
    }
}

class Reservation {
    private String guestName;
    private String roomType;
    private String reservationId;

    public Reservation(String guestName, String roomType, String reservationId) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.reservationId = reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getReservationId() {
        return reservationId;
    }
}

class BookingQueue {
    private Queue<Reservation> queue;

    public BookingQueue() {
        queue = new LinkedList<>();
    }

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getNext() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

class BookingHistory {
    private List<Reservation> history;

    public BookingHistory() {
        history = new ArrayList<>();
    }

    public void add(Reservation r) {
        history.add(r);
    }

    public List<Reservation> getAll() {
        return history;
    }
}

class BookingService {
    private RoomInventory inventory;
    private HashMap<String, Set<String>> allocatedRooms;
    private int counter = 1;
    private BookingHistory history;

    public BookingService(RoomInventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
        allocatedRooms = new HashMap<>();
    }

    public void processQueue(BookingQueue queue) {
        while (!queue.isEmpty()) {
            Reservation r = queue.getNext();
            String type = r.getRoomType();

            if (inventory.getAvailability(type) > 0) {
                String roomId = type.replace(" ", "") + counter++;
                allocatedRooms.putIfAbsent(type, new HashSet<>());
                allocatedRooms.get(type).add(roomId);
                inventory.decrement(type);

                Reservation confirmed = new Reservation(r.getGuestName(), type, roomId);
                history.add(confirmed);

                System.out.println("Booking Confirmed: " + confirmed.getGuestName() + " -> " + roomId);
            } else {
                System.out.println("Booking Failed: " + r.getGuestName());
            }
        }
    }
}

class BookingReportService {
    public void displayAll(List<Reservation> reservations) {
        for (Reservation r : reservations) {
            System.out.println(r.getGuestName() + " | " + r.getRoomType() + " | " + r.getReservationId());
        }
    }

    public void summary(List<Reservation> reservations) {
        HashMap<String, Integer> countMap = new HashMap<>();

        for (Reservation r : reservations) {
            countMap.put(r.getRoomType(), countMap.getOrDefault(r.getRoomType(), 0) + 1);
        }

        System.out.println("\nBooking Summary:");
        for (String type : countMap.keySet()) {
            System.out.println(type + ": " + countMap.get(type));
        }
    }
}

public class BookMyStay {
    public static void main(String[] args) {
        RoomInventory inventory = new RoomInventory();
        BookingQueue queue = new BookingQueue();
        BookingHistory history = new BookingHistory();

        queue.addRequest(new Reservation("Aman", "Single Room", ""));
        queue.addRequest(new Reservation("Riya", "Double Room", ""));
        queue.addRequest(new Reservation("Karan", "Single Room", ""));

        BookingService service = new BookingService(inventory, history);
        service.processQueue(queue);

        BookingReportService reportService = new BookingReportService();

        System.out.println("\nBooking History:");
        reportService.displayAll(history.getAll());

        reportService.summary(history.getAll());
    }
}