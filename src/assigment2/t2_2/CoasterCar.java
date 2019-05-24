package assigment2.t2_2;

public class CoasterCar implements Runnable {
    private RollerCoasterMonitor control;
    private int roundTime;

    public CoasterCar(RollerCoasterMonitor control, int roundTime) {
        this.control = control;
        this.roundTime = roundTime;
    }

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
