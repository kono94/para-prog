package u8_2;

public class Slide implements Monitor {
    private boolean isOccupied;

    public Slide() {
    }

    public synchronized void slide() throws InterruptedException {
        while (isOccupied) wait();
        isOccupied = true;
        notifyAll();
    }

    public synchronized void exit() {
        isOccupied = false;
        notifyAll();
    }
}
