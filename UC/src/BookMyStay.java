import java.util.*;

class RoomInventory {
    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public synchronized int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public synchronized boolean allocate(String roomType) {
        int available = getAvailability(roomType);
        if (available > 0) {
            inventory.put(roomType, available - 1);
            return true;
        }
        return false;
    }
}

class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

class BookingQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public synchronized void addRequest(Reservation r) {
        queue.offer(r);
    }

    public synchronized Reservation getNext() {
        return queue.poll();
    }
}

class ConcurrentBookingProcessor implements Runnable {
    private BookingQueue queue;
    private RoomInventory inventory;

    public ConcurrentBookingProcessor(BookingQueue queue, RoomInventory inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    public void run() {
        while (true) {
            Reservation r;

            synchronized (queue) {
                r = queue.getNext();
            }

            if (r == null) break;

            boolean success = inventory.allocate(r.getRoomType());

            if (success) {
                System.out.println(Thread.currentThread().getName() + " Confirmed: " + r.getGuestName());
            } else {
                System.out.println(Thread.currentThread().getName() + " Failed: " + r.getGuestName());
            }
        }
    }
}

public class BookMyStay {
    public static void main(String[] args) {
        RoomInventory inventory = new RoomInventory();
        BookingQueue queue = new BookingQueue();

        queue.addRequest(new Reservation("Aman", "Single Room"));
        queue.addRequest(new Reservation("Riya", "Single Room"));
        queue.addRequest(new Reservation("Karan", "Single Room"));
        queue.addRequest(new Reservation("Neha", "Suite Room"));
        queue.addRequest(new Reservation("Arjun", "Suite Room"));

        Thread t1 = new Thread(new ConcurrentBookingProcessor(queue, inventory));
        Thread t2 = new Thread(new ConcurrentBookingProcessor(queue, inventory));
        Thread t3 = new Thread(new ConcurrentBookingProcessor(queue, inventory));

        t1.start();
        t2.start();
        t3.start();
    }
}