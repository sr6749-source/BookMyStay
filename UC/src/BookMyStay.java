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

class BookingService {
    private RoomInventory inventory;
    private HashMap<String, Set<String>> allocatedRooms;
    private int counter = 1;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        allocatedRooms = new HashMap<>();
    }

    public List<Reservation> processQueue(BookingQueue queue) {
        List<Reservation> confirmed = new ArrayList<>();

        while (!queue.isEmpty()) {
            Reservation r = queue.getNext();
            String type = r.getRoomType();

            if (inventory.getAvailability(type) > 0) {
                String roomId = type.replace(" ", "") + counter++;
                allocatedRooms.putIfAbsent(type, new HashSet<>());
                allocatedRooms.get(type).add(roomId);
                inventory.decrement(type);

                Reservation confirmedRes = new Reservation(r.getGuestName(), type, roomId);
                confirmed.add(confirmedRes);

                System.out.println("Booking Confirmed: " + r.getGuestName() + " -> " + roomId);
            } else {
                System.out.println("Booking Failed: " + r.getGuestName());
            }
        }

        return confirmed;
    }
}

class AddOnService {
    private String name;
    private double cost;

    public AddOnService(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }
}

class AddOnServiceManager {
    private HashMap<String, List<AddOnService>> serviceMap;

    public AddOnServiceManager() {
        serviceMap = new HashMap<>();
    }

    public void addService(String reservationId, AddOnService service) {
        serviceMap.putIfAbsent(reservationId, new ArrayList<>());
        serviceMap.get(reservationId).add(service);
    }

    public double calculateTotal(String reservationId) {
        double total = 0;
        List<AddOnService> services = serviceMap.getOrDefault(reservationId, new ArrayList<>());

        for (AddOnService s : services) {
            total += s.getCost();
        }

        return total;
    }

    public void displayServices(String reservationId) {
        List<AddOnService> services = serviceMap.getOrDefault(reservationId, new ArrayList<>());

        for (AddOnService s : services) {
            System.out.println(s.getName() + " - " + s.getCost());
        }
    }
}

public class BookMyStay {
    public static void main(String[] args) {
        RoomInventory inventory = new RoomInventory();
        BookingQueue queue = new BookingQueue();

        queue.addRequest(new Reservation("Aman", "Single Room", ""));
        queue.addRequest(new Reservation("Riya", "Double Room", ""));

        BookingService bookingService = new BookingService(inventory);
        List<Reservation> confirmed = bookingService.processQueue(queue);

        AddOnServiceManager manager = new AddOnServiceManager();

        if (!confirmed.isEmpty()) {
            Reservation r = confirmed.get(0);

            manager.addService(r.getReservationId(), new AddOnService("Breakfast", 200));
            manager.addService(r.getReservationId(), new AddOnService("Spa", 500));

            System.out.println("\nServices for " + r.getReservationId() + ":");
            manager.displayServices(r.getReservationId());

            System.out.println("Total Add-On Cost: " + manager.calculateTotal(r.getReservationId()));
        }
    }
}