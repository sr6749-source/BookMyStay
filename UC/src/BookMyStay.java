import java.io.*;
import java.util.*;

class RoomInventory implements Serializable {
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
        int val = getAvailability(roomType);
        if (val > 0) inventory.put(roomType, val - 1);
    }

    public HashMap<String, Integer> getAll() {
        return inventory;
    }
}

class Reservation implements Serializable {
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

class BookingHistory implements Serializable {
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

class PersistenceService {
    private static final String FILE = "hotel_state.dat";

    public void save(RoomInventory inventory, BookingHistory history) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE))) {
            out.writeObject(inventory);
            out.writeObject(history);
            System.out.println("State Saved");
        } catch (Exception e) {
            System.out.println("Save Failed");
        }
    }

    public Object[] load() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE))) {
            RoomInventory inventory = (RoomInventory) in.readObject();
            BookingHistory history = (BookingHistory) in.readObject();
            System.out.println("State Restored");
            return new Object[]{inventory, history};
        } catch (Exception e) {
            System.out.println("No valid saved state found, starting fresh");
            return new Object[]{new RoomInventory(), new BookingHistory()};
        }
    }
}

public class BookMyStay {
    public static void main(String[] args) {
        PersistenceService persistence = new PersistenceService();

        Object[] data = persistence.load();
        RoomInventory inventory = (RoomInventory) data[0];
        BookingHistory history = (BookingHistory) data[1];

        Reservation r1 = new Reservation("Aman", "Single Room", "R1");
        Reservation r2 = new Reservation("Riya", "Double Room", "R2");

        if (inventory.getAvailability(r1.getRoomType()) > 0) {
            inventory.decrement(r1.getRoomType());
            history.add(r1);
        }

        if (inventory.getAvailability(r2.getRoomType()) > 0) {
            inventory.decrement(r2.getRoomType());
            history.add(r2);
        }

        System.out.println("Current Bookings:");
        for (Reservation r : history.getAll()) {
            System.out.println(r.getGuestName() + " -> " + r.getRoomType());
        }

        persistence.save(inventory, history);
    }
}