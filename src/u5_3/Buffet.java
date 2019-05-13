package u5_3;

import static u5_3.Main.MAX_BUFFET_CAPCITY;

public class Buffet {
    private int currentFood = MAX_BUFFET_CAPCITY;

    public Buffet() {
    }

    synchronized void serveFood() throws InterruptedException {
        while (!(currentFood > 0)) wait();
        --currentFood;
    }

    synchronized void fill() throws InterruptedException {
        while (!(currentFood < MAX_BUFFET_CAPCITY)) wait();
        currentFood = MAX_BUFFET_CAPCITY;
        notifyAll();
    }
}
