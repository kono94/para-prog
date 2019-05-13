package u5_3;

import static u5_3.Main.MAX_BUFFET_CAPCITY;
import static u5_4.ColorConstants.*;

public class Buffet {
    private int currentFood = MAX_BUFFET_CAPCITY;

    public Buffet() {
    }

    synchronized void serveFood(Cannibal cannibal) throws InterruptedException {
        System.out.printf("%s is going to the buffet looking for food.\n", cannibal.getName());
        while (!(currentFood > 0)) wait();
        --currentFood;
        System.out.printf("%s%s is eating! - Food left: %d%s\n", ANSI_RED, cannibal.getName(), currentFood, ANSI_RESET);
        notifyAll();
    }

    synchronized void fill(Chef chef) throws InterruptedException {
        while (!(currentFood == 0)) wait();
        currentFood = MAX_BUFFET_CAPCITY;
        System.out.printf("%s%s filled the Buffet\n%s", ANSI_GREEN, chef.getName(), ANSI_RESET);
        notifyAll();
    }
}
