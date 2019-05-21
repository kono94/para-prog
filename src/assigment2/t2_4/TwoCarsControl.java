package assigment2.t2_4;

import assigment2.t2_2.Constants;
import assigment2.t2_2.RollerCoasterMonitor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class TwoCarsControl implements RollerCoasterMonitor {
    private Map<Long, CarInfo> cars;
    private CarInfo car1;
    private CarInfo car2;

    public TwoCarsControl(){
    }

    public synchronized void passenger() throws InterruptedException {
        while(!((car1.passengerCount + car2.passengerCount) < Constants.COASTER_CAR_MAX_PASSENGERS * 2)) wait();

        if(Math.random()>0.5 && car1.passengerCount < 5){
            car1.passengerCount++;
        }else{
            if (car2.passengerCount < 5) {
                car2.passengerCount++;
            } else {
                car1.passengerCount++;
            }
        }
        System.out.printf("%s. passenger passed the Turnstile\n", car1.passengerCount + car2.passengerCount);
        notifyAll();
    }

    public synchronized void departure() throws InterruptedException {
        System.out.println(cars.get(Thread.currentThread().getId()).passengerCount);
        while(!(cars.get(Thread.currentThread().getId()).isFirst && cars.get(Thread.currentThread().getId()).passengerCount == Constants.COASTER_CAR_MAX_PASSENGERS)) wait();
        System.out.println("Coaster Car is allowed to start!");
    }

    public synchronized void entrance(){
        cars.get(Thread.currentThread().getId()).passengerCount = 0;
        for(CarInfo info: cars.values()){
            info.isFirst = !info.isFirst;
        }
        notifyAll();
    }

    public void initCars(long car1ID, long car2ID){
        car1 = new CarInfo( true);
        car2 = new CarInfo(false);

        cars = new HashMap<>();
        cars.put(car1ID, car1);
        cars.put(car2ID, car2);
    }

    private class CarInfo{
        private int passengerCount;
        private boolean isFirst;

        private CarInfo(boolean isFirst){
            this.isFirst = isFirst;
            passengerCount = 0;
        }
    }

}
