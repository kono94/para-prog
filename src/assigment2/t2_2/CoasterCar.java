package assigment2.t2_2;

import u5_4.ColorConstants;

public class CoasterCar implements Runnable{
    private Controller control;
    private int roundTime;

    public CoasterCar(Controller control, int roundTime) {
        this.control = control;
        this.roundTime = roundTime;
    }

    @Override
    public void run() {
        while (true){
            try {
                control.departure();
                System.out.printf("%sCoaster car is starting the drive!%s\n", ColorConstants.ANSI_RED, ColorConstants.ANSI_RESET);
                Thread.sleep(roundTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            control.entrance();
            System.out.printf("%sCoasterCar has returned. All passengers leaving%s\n", ColorConstants.ANSI_GREEN, ColorConstants.ANSI_RESET);
        }
    }
}
