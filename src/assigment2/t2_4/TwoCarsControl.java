package assigment2.t2_4;

import assigment2.t2_2.Constants;
import assigment2.t2_2.RollerCoasterMonitor;
import u5_4.ColorConstants;

import java.util.HashMap;
import java.util.Map;


public class TwoCarsControl implements RollerCoasterMonitor {
    private Map<Long, CarInfo> cars;
    private CarInfo car1;
    private CarInfo car2;

    public TwoCarsControl(){
    }

    public synchronized void passenger() throws InterruptedException {
        while(!((car1.passengerCount < Constants.COASTER_CAR_MAX_PASSENGERS) || (car2.passengerCount < Constants.COASTER_CAR_MAX_PASSENGERS))) wait();
        boolean enteredCar1;
        if(car1.passengerCount == Constants.COASTER_CAR_MAX_PASSENGERS){
            car2.passengerCount++;
            enteredCar1 = false;
        }else if(car2.passengerCount == Constants.COASTER_CAR_MAX_PASSENGERS){
            car1.passengerCount++;
            enteredCar1 = true;
        }else{
            if (Math.random() > 0.5) {
                car1.passengerCount++;
                enteredCar1 = true;
            } else {
                car2.passengerCount++;
                enteredCar1 = false;
            }
        }

        System.out.printf("Passenger passed the turnstile into car %s. %sNEW COUNT [%s]%s\n", enteredCar1 ? car1.name : car2.name, enteredCar1 ?  ColorConstants.ANSI_PURPLE : ColorConstants.ANSI_CYAN, enteredCar1 ? car1.passengerCount : car2.passengerCount, ColorConstants.ANSI_RESET);
        notifyAll();
    }

    public synchronized void departure() throws InterruptedException {
        while(!(cars.get(Thread.currentThread().getId()).first && cars.get(Thread.currentThread().getId()).passengerCount == Constants.COASTER_CAR_MAX_PASSENGERS)) wait();
        System.out.printf("%sCar %s is starting his ride%s\n",  ColorConstants.ANSI_GREEN, cars.get(Thread.currentThread().getId()).name, ColorConstants.ANSI_RESET);
    }

    public synchronized void entrance(){
        System.out.printf("%sCar %s has returned from its ride%s\n", ColorConstants.ANSI_RED, cars.get(Thread.currentThread().getId()).name, ColorConstants.ANSI_RESET);
        cars.get(Thread.currentThread().getId()).passengerCount = 0;
        for(CarInfo info: cars.values()){
            info.first = !info.first;
        }
        notifyAll();
    }

    public void initCars(long car1ID, long car2ID){
        car1 = new CarInfo( "A", true);
        car2 = new CarInfo("B", false);

        cars = new HashMap<>();
        cars.put(car1ID, car1);
        cars.put(car2ID, car2);
    }

    private class CarInfo{
        private int passengerCount;
        private boolean first;
        private String name;

        private CarInfo(String name, boolean first){
            this.name = name;
            this.first = first;
            passengerCount = 0;
        }
    }

}
