package assigment2.t2_4;

import assigment2.t2_2.Constants;
import java.util.Queue;

public class XControl {
    private int passengersPassed;
    private Queue<XCoasterCar> cars;

    public XControl(){
        passengersPassed = 0;
    }

    public void setCars(Queue<XCoasterCar> cars){
        this.cars = cars;
    }

    public synchronized void passenger(XCoasterCar car) throws InterruptedException {
        while(!(passengersPassed < Constants.COASTER_CAR_MAX_PASSENGERS * cars.size())) wait();
        ++passengersPassed;
        car.addPassenger();
        System.out.printf("%s. passenger passed the Turnstile\n", passengersPassed);
        notifyAll();
    }

    public synchronized void departure(XCoasterCar car) throws InterruptedException {
        while(!(cars.peek() == car)) wait();
        cars.poll();
        System.out.println("Coaster Car is allowed to start!");
    }

    public synchronized void entrance(XCoasterCar car){
        cars.add(car);
        passengersPassed = 0;
        notifyAll();
    }
}
