package u5_3;


import java.util.HashSet;
import java.util.Set;

public class Main {
    public static final int MAX_BUFFET_CAPCITY = 5;

    public static void main(String[] args) {
        Buffet buffet = new Buffet();
        Set<Runnable> ppl = new HashSet<>();

        ppl.add(new Cannibal("Olaf", buffet, 6000));
        ppl.add(new Cannibal("Hans", buffet, 7000));
        ppl.add(new Cannibal("Josef", buffet, 7000));
        ppl.add(new Chef("Harald", buffet, 3000, 2000));
        //ppl.add(new Chef("Max", buffet, 3000, 2000));
        for (Runnable r : ppl) new Thread(r).start();
    }
}
