package assigment2.t2_2;

public class Turnstile implements Runnable{
    private String name;
    private int avgSpped;
    private Controller control;

    public Turnstile(Controller control, String name, int avgSpeed){
        this.name = name;
        this.avgSpped = avgSpeed;
        this.control = control;
    }

    @Override
    public void run() {
        for(int i=0; i < Constants.VISITOR_COUNT; ++i){
            try {
                Thread.sleep(avgSpped);
                control.passenger();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
