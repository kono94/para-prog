package assigment2.t2_2;

public class Turnstile implements Runnable{
    private int avgSpeed;
    private RollerCoasterMonitor control;

    public Turnstile(RollerCoasterMonitor control, int avgSpeed){
        this.avgSpeed = avgSpeed;
        this.control = control;
    }

    @Override
    public void run() {
        for(int i=0; i < Constants.VISITOR_COUNT; ++i){
            try {
                Thread.sleep((int)(avgSpeed*Math.random()));
                control.passenger();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
