
package emergencyservicedispatch1;


public class ManualPriorityQueue {
    private Incident[] queue;
    private int size;

    /**
     * Initializes the priority queue with a specified initial capacity.
     * @param capacity Initial size of the queue array
     */
    public ManualPriorityQueue(int capacity) {
        queue = new Incident[capacity];
        size = 0;
    }

    /**
     * INSERT INCIDENT BASED ON PRIORITY
     * Automatically shifts existing elements to maintain priority order
     * (Highest priority / lowest priority value comes first).
     */
    public void enqueue(Incident incident) {
        // Automatically resize internal array if capacity is reached
        if (size == queue.length) {
            resize();
        }

        int position = size;

        // Shift elements down to find the correct sorted position based on priority
        while (position > 0 &&
               queue[position - 1].getPriority()
               > incident.getPriority()) {

            queue[position] = queue[position - 1];
            position--;
        }

        queue[position] = incident;
        size++;
    }

    /**
     * REMOVE HIGHEST-PRIORITY INCIDENT
     * Dequeues and returns the element at the front of the queue,
     * then shifts remaining elements up.
     */
    public Incident dequeue() {
        if (isEmpty()) {
            return null;
        }

        Incident highestPriority = queue[0];

        // Shift all subsequent elements up by one index
        for (int i = 1; i < size; i++) {
            queue[i - 1] = queue[i];
        }

        queue[size - 1] = null;
        size--;

        return highestPriority;
    }

    // VIEW HIGHEST-PRIORITY INCIDENT
    public Incident peek() {
        if (isEmpty()) {
            return null;
        }
        return queue[0];
    }

    // GET INCIDENT AT A SPECIFIC INDEX
    public Incident getAt(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        return queue[index];
    }

    // GET NUMBER OF INCIDENTS
    public int getSize() {
        return size;
    }

    // CHECK IF QUEUE IS EMPTY
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * INCREASE ARRAY CAPACITY
     * Doubles the size of the underlying array when capacity is fully utilized.
     */
    private void resize() {
        Incident[] newQueue = new Incident[queue.length * 2];

        for (int i = 0; i < size; i++) {
            newQueue[i] = queue[i];
        }

        queue = newQueue;
    }

}
