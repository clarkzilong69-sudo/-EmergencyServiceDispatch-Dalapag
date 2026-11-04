
package emergencyservicedispatch2;



public class ManualPriorityQueue {

    private Incident[] queue;
    private int size;

    public ManualPriorityQueue(int capacity) {
        queue = new Incident[capacity];
        size = 0;
    }

    // ADD INCIDENT
    public void enqueue(Incident incident) {

        if (size == queue.length) {
            resize();
        }

        int position = size;

        // Arrange according to priority
        while (position > 0 &&
               queue[position - 1].getPriority()
               > incident.getPriority()) {

            queue[position] = queue[position - 1];
            position--;
        }

        queue[position] = incident;
        size++;
    }

    // REMOVE HIGHEST-PRIORITY INCIDENT
    public Incident dequeue() {

        if (isEmpty()) {
            return null;
        }

        Incident highest = queue[0];

        for (int i = 1; i < size; i++) {
            queue[i - 1] = queue[i];
        }

        queue[size - 1] = null;
        size--;

        return highest;
    }

    // VIEW NEXT INCIDENT
    public Incident peek() {

        if (isEmpty()) {
            return null;
        }

        return queue[0];
    }

    // GET INCIDENT BY INDEX
    public Incident getAt(int index) {

        if (index < 0 || index >= size) {
            return null;
        }

        return queue[index];
    }

    // GET QUEUE SIZE
    public int getSize() {
        return size;
    }

    // CHECK IF EMPTY
    public boolean isEmpty() {
        return size == 0;
    }

    // INCREASE ARRAY CAPACITY
    private void resize() {

        Incident[] newQueue =
            new Incident[queue.length * 2];

        for (int i = 0; i < size; i++) {
            newQueue[i] = queue[i];
        }

        queue = newQueue;
    }
}

