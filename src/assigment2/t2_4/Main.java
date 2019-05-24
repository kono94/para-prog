package assigment2.t2_4;

import assigment2.t2_2.CoasterCar;
import assigment2.t2_2.Turnstile;


public class Main {
    public static void main(String[] args) {
        TwoCarsControl twoCarsControl = new TwoCarsControl();

        CoasterCar car1 = new CoasterCar(twoCarsControl, 8000);
        CoasterCar car2 = new CoasterCar(twoCarsControl, 8000);

        Thread a = new Thread(car1);
        Thread b = new Thread(car2);

        twoCarsControl.initCars(a.getId(), b.getId());
        new Thread(new Turnstile(twoCarsControl, 3000)).start();
        a.start();
        b.start();
    }
}