package assigment2.t2_3;

import assigment2.t2_2.CoasterCar;
import assigment2.t2_2.Turnstile;

public class Main {
    public static void main(String[] args) {
        // Initializing the monitor
        TwoCarsControl twoCarsControl = new TwoCarsControl();

        // Setting up two coaster cars
        CoasterCar car1 = new CoasterCar(twoCarsControl, 8000);
        CoasterCar car2 = new CoasterCar(twoCarsControl, 8000);

        Thread a = new Thread(car1);
        Thread b = new Thread(car2);

        // Passing the thread IDs of those cars to the monitor
        twoCarsControl.initCars(a.getId(), b.getId());

        // Starting the turnstile thread
        new Thread(new Turnstile(twoCarsControl, 3000)).start();
        // Starting the coaster car threads
        a.start();
        b.start();
    }
}