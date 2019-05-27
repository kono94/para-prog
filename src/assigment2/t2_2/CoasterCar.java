package assigment2.t2_2;

public class CoasterCar implements Runnable {
    private RollerCoasterMonitor control;
    private int roundTime;

    public CoasterCar(RollerCoasterMonitor control, int roundTime) {
        this.control = control;
        this.roundTime = roundTime;
    }

    /**
     * The coaster car is always trying to departure. If the monitor
     * is not blocking the car thread anymore, the car is able to take the ride
     * (displayed by Thread.sleep());
     * It then returns to the station and all passengers are exiting the car.
     * <p>
     * Note: It is not possible for the passengers to exit the car before the ride,
     * even though the monitor would allow it.
     */
    @Override
    public void run() {
        while (true) {
            try {
                control.departure();
                Thread.sleep((int)(roundTime*Math.random()));
                control.entrance();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
