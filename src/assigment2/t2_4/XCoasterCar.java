package assigment2.t2_4;

import assigment2.t2_2.Constants;
import u5_4.ColorConstants;

public class XCoasterCar implements Runnable{
    private XControl xControl;
    private int roundTime;
    private String name;
    private int passengers;

    public XCoasterCar(XControl control, String name, int roundTime) {
        this.xControl = control;
        this.roundTime = roundTime;
        this.passengers = 0;
        this.name = name;
    }

   @Override
    public void run() {
        while (true) {
            try {
                if (passengers == Constants.COASTER_CAR_MAX_PASSENGERS) {
                    xControl.departure(this);
                    System.out.printf("%sCoaster car is starting the drive!%s\n", ColorConstants.ANSI_RED, ColorConstants.ANSI_RESET);
                    Thread.sleep(roundTime);
                    xControl.entrance(this);
                    System.out.printf("%sCoasterCar has returned. All passengers leaving%s\n", ColorConstants.ANSI_GREEN, ColorConstants.ANSI_RESET);

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void addPassenger() {
        ++passengers;
        System.out.printf("%s. passenger took a seat in car %s", passengers,name);
    }

    public void resetPassengers() {
        passengers = 0;
        System.out.printf("All passengers left car %s",name);
    }
}
