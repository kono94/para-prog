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
                buffet.serveFood(this);
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

    public String getName() {
        return name;
    }
}
