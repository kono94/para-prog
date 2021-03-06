package u8_2;

/**
 * Kriterien:
 * <p>
 * Threads werden über Sperrmechanismus synchronisiert
 * (Semaphore, Monitore, ...)
 * <p>
 * Threads werden zur Vorrangsteuerung in mehreren Prioritatsstufen
 * ausgeführt (z.B. in Java: setPriority(int prio))
 * <p>
 * Es sind weniger Prozessoren als Prozesse vorhanden
 */
public class Main {
    public static boolean RUN = false;

    public static void main(String[] args) {
        Slide slide = new Slide();

        Boy[] boys = new Boy[]{
                new Boy("Thomas", slide, 5),
                new Boy("Dennis", slide, 5),
                new Boy("Henrik", slide, 5),
                new Boy("Michael", slide, 5),
                new Boy("Peter", slide, 5),
                new Boy("Karin", slide, 5),
                new Boy("Ulrike", slide, 5),
                new Boy("Alfred", slide, 5),
                new Boy("Oliver", slide, 5),
                new Boy("Nadja", slide, 5),
        };

        /*
        Boy[] boys = new Boy[]{
                new Boy("Bob", slide, 1),
                new Boy("Jens", slide, 1),
                new Boy("Ulf", slide, 1),
                new Boy("Kelvin", slide, 2),
                new Boy("Hecht", slide, 2),
                new Boy("Ulrich", slide, 2),
                new Boy("Maxi", slide, 3),
                new Boy("Peter", slide, 3),
                new Boy("Thomas", slide, 3),
                new Boy("Oliver", slide, 3),
                new Boy("E", slide, 7),
                new Boy("A", slide, 7),
                new Boy("F", slide, 7),
                new Boy("G", slide, 8),
                new Boy("H", slide, 8),
                new Boy("L", slide, 8),
                new Boy("O", slide, 9),
                new Boy("Z", slide, 9),
                new Boy("T", slide, 9),
                new Boy("Q", slide, 9),
        };

         */

        RUN = true;
        for (Boy boy : boys) {
            boy.start();
        }

        try {
            Thread.sleep(5000);
            RUN = false;
            System.out.println("Letting all Threads end");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\n\n");
        for (Boy boy : boys) {
            System.out.println(boy);
        }
    }
}
