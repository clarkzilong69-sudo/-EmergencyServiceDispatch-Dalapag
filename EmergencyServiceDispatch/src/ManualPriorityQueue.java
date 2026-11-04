
package emergencyservicedispatch2;



public class ManualPriorityQueue {

    private Incident[] queue;
    private int size;

    public ManualPriorityQueue(int capacity) {
        queue = new Incident[capacity];
        size = 0;
    }

    
    public void enqueue(Incident incident) {

        if (size == queue.length) {
            resize();
        }

        int position = size;

        while (position > 0 &&
               queue[position - 1].getPriority()
               > incident.getPriority()) {

            queue[position] = queue[position - 1];
            position--;
        }

        queue[position] = incident;
        size++;
    }

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

    public Incident peek() {

        if (isEmpty()) {
            return null;
        }

        return queue[0];
    }

    public Incident getAt(int index) {

        if (index < 0 || index >= size) {
            return null;
        }

       

