package assigment2.t2_4;

import java.util.ArrayDeque;
import java.util.Queue;

public class Main {
    public static void main(String[] args) {
        XControl xcontrol = new XControl();

        XCoasterCar car1 = new XCoasterCar(xcontrol, "A", 4000);
        XCoasterCar car2 = new XCoasterCar(xcontrol, "B", 4000);

        Queue<XCoasterCar> carQueue = new ArrayDeque<>();
        carQueue.add(car1);
        carQueue.add(car2);
        xcontrol.setCars(carQueue);

        new Thread(car1).start();
        new Thread(car2).start();
        new Thread(new XTurnstile(xcontrol, "Turnstile", 1000)).start();
    }
}