package assigment2.t2_4;
import assigment2.t2_2.Constants;

public class XTurnstile implements Runnable{
    private String name;
    private int avgSpped;
    private XControl control;

    public XTurnstile(XControl control, String name, int avgSpeed){
        this.name = name;
        this.avgSpped = avgSpeed;
        this.control = control;
    }

    @Override
    public void run() {
        for(int i = 0; i < Constants.VISITOR_COUNT; ++i){
            try {
                Thread.sleep(avgSpped);
                control.passenger(null);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
