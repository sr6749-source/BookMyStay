import java.util.LinkedList;
import java.util.Queue;

// Reservation class representing a guest's booking intent
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

    @Override
    public String toString() {
        return "Reservation [Guest=" + guestName + ", RoomType=" + roomType + "]";
    }
}

// Booking Request Queue Manager
class BookingRequestQueue {
    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    // Accept booking request (enqueue)
    public void submitRequest(Reservation reservation) {
        requestQueue.offer(reservation);
        System.out.println("Request added to queue: " + reservation);
    }

    // View all queued requests (without processing)
    public void viewQueue() {
        System.out.println("\nCurrent Booking Request Queue:");
        if (requestQueue.isEmpty()) {
            System.out.println("Queue is empty.");
            return;
        }

        for (Reservation r : requestQueue) {
            System.out.println(r);
        }
    }

    // Get next request (for future processing stage)
    public Reservation getNextRequest() {
        return requestQueue.peek(); // No removal (no allocation yet)
    }
}

// Main class
public class BookMyStay {
    public static void main(String[] args) {

        BookingRequestQueue queue = new BookingRequestQueue();

        // Simulating multiple booking requests
        Reservation r1 = new Reservation("Alice", "Deluxe");
        Reservation r2 = new Reservation("Bob", "Suite");
        Reservation r3 = new Reservation("Charlie", "Standard");

        // Step 1: Guests submit requests
        queue.submitRequest(r1);
        queue.submitRequest(r2);
        queue.submitRequest(r3);

        // Step 2: View queue (FIFO order preserved)
        queue.viewQueue();

        // Step 3: Show next request to be processed (no removal)
        System.out.println("\nNext request to process (peek): " + queue.getNextRequest());
    }
}