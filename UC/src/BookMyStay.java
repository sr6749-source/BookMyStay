
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
        int current = getAvailability(roomType);
        if (current <= 0) {
            throw new RuntimeException("No availability");
        }
        inventory.put(roomType, current - 1);
    }

    public void increment(String roomType) {
        inventory.put(roomType, getAvailability(roomType) + 1);
    }

    public boolean isValidRoomType(String roomType) {
        return inventory.containsKey(roomType);
    }
}

class Reservation {
    private String guestName;
    private String roomType;
    private String reservationId;
    private boolean active;

    public Reservation(String guestName, String roomType, String reservationId) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.reservationId = reservationId;
        this.active = true;
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

    public boolean isActive() {
        return active;
    }

    public void cancel() {
        this.active = false;
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

    public Reservation findById(String id) {
        for (Reservation r : history) {
            if (r.getReservationId().equals(id)) {
                return r;
            }
        }
        return null;
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

    public HashMap<String, Set<String>> getAllocatedRooms() {
        return allocatedRooms;
    }
}

class CancellationService {
    private RoomInventory inventory;
    private BookingHistory history;
    private HashMap<String, Set<String>> allocatedRooms;
    private Stack<String> rollbackStack;

    public CancellationService(RoomInventory inventory, BookingHistory history, HashMap<String, Set<String>> allocatedRooms) {
        this.inventory = inventory;
        this.history = history;
        this.allocatedRooms = allocatedRooms;
        rollbackStack = new Stack<>();
    }

    public void cancel(String reservationId) {
        Reservation r = history.findById(reservationId);

        if (r == null) {
            System.out.println("Cancellation Failed: Reservation not found");
            return;
        }

        if (!r.isActive()) {
            System.out.println("Cancellation Failed: Already cancelled");
            return;
        }

        String type = r.getRoomType();

        if (allocatedRooms.containsKey(type) && allocatedRooms.get(type).contains(reservationId)) {
            allocatedRooms.get(type).remove(reservationId);
            rollbackStack.push(reservationId);
            inventory.increment(type);
            r.cancel();

            System.out.println("Cancellation Successful: " + reservationId);
        } else {
            System.out.println("Cancellation Failed: Invalid state");
        }
    }

    public void showRollbackStack() {
        System.out.println("Rollback Stack:");
        for (String id : rollbackStack) {
            System.out.println(id);
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

        BookingService bookingService = new BookingService(inventory, history);
        bookingService.processQueue(queue);

        List<Reservation> all = history.getAll();

        if (!all.isEmpty()) {
            String idToCancel = all.get(0).getReservationId();

            CancellationService cancelService = new CancellationService(
                    inventory,
                    history,
                    bookingService.getAllocatedRooms()
            );

            cancelService.cancel(idToCancel);
            cancelService.showRollbackStack();
        }
    }
}