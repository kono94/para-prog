package assigment2.t2_2;

import u5_4.ColorConstants;

public class Control implements RollerCoasterMonitor {
    private int passengersPassed;


    public Control(){
        passengersPassed = 0;
    }

    @Override
    public synchronized void passenger() throws InterruptedException {
        while(!(passengersPassed < Constants.COASTER_CAR_MAX_PASSENGERS)) wait();
        ++passengersPassed;
        System.out.printf("%s. passenger passed the Turnstile\n", passengersPassed);
        notifyAll();
    }

    @Override
    public synchronized void departure() throws InterruptedException {
        while(!(passengersPassed == Constants.COASTER_CAR_MAX_PASSENGERS)) wait();
        System.out.printf("%sCar is starting his ride%s\n",  ColorConstants.ANSI_GREEN,  ColorConstants.ANSI_RESET);
    }

    @Override
    public synchronized void entrance(){
        System.out.printf("%sCar has returned %s\n",  ColorConstants.ANSI_RED,  ColorConstants.ANSI_RESET);
        passengersPassed = 0;
        notifyAll();
    }
}
