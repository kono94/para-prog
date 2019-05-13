package u5_3;

import static u5_4.ColorConstants.ANSI_RED;
import static u5_4.ColorConstants.ANSI_RESET;

public class Cannibal implements Runnable {
    private int eatingPace;
    private Buffet buffet;
    private String name;

    public Cannibal(String name, Buffet buffet, int eatingPace) {
        this.name = name;
        this.eatingPace = eatingPace;
        this.buffet = buffet;
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.printf("%s is going to the buffet looking for food.\n", name);
                buffet.serveFood();
                System.out.printf("%s%s is eating!%s\n", ANSI_RED, name, ANSI_RESET);
                Thread.sleep(eatingPace);
                feelGood();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void feelGood() {
        System.out.printf("%s is feeling good now!\n", name);
    }
}
