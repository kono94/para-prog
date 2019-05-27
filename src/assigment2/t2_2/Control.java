package assigment2.t2_2;

import u5_4.ColorConstants;

public class Control implements RollerCoasterMonitor {
    private int passengersPassed;


    public Control(){
        passengersPassed = 0;
    }

    /**
     * Gets called by the turnstile thread.
     * "A passenger is going passed the turnstile and is taking a seat in the coaster car"
     * <p>
     * Represents the guardian "when(n<M)"
     *
     * @throws InterruptedException
     */
    @Override
    public synchronized void passenger() throws InterruptedException {
        while(!(passengersPassed < Constants.COASTER_CAR_MAX_PASSENGERS)) wait();
        ++passengersPassed;
        System.out.printf("%s. passenger passed the Turnstile\n", passengersPassed);
        notifyAll();
    }

    /**
     * Gets called by the coaster car thread.
     * The coaster car is allowed to start his if the maximum amount
     * of passengers took a seat in the car.
     *
     * Represents the guardian "when(n==M)"
     *
     * @throws InterruptedException
     */
    @Override
    public synchronized void departure() throws InterruptedException {
        while(!(passengersPassed == Constants.COASTER_CAR_MAX_PASSENGERS)) wait();
        System.out.printf("%sCar is starting his ride%s\n",  ColorConstants.ANSI_GREEN,  ColorConstants.ANSI_RESET);
    }

    /**
     * Gets called by the coaster car thread.
     * Is only called if the coaster car previously took a ride.
     */
    @Override
    public synchronized void entrance(){
        System.out.printf("%sCar has returned %s\n",  ColorConstants.ANSI_RED,  ColorConstants.ANSI_RESET);
        passengersPassed = 0;
        notifyAll();
    }
}
