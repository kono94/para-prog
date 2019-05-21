package assigment2.t2_2;

public class Control implements RollerCoasterMonitor {
    private int passengersPassed;


    public Control(){
        passengersPassed = 0;
    }

    public synchronized void passenger() throws InterruptedException {
        while(!(passengersPassed < Constants.COASTER_CAR_MAX_PASSENGERS)) wait();
        ++passengersPassed;
        System.out.printf("%s. passenger passed the Turnstile\n", passengersPassed);
        notifyAll();
    }

    public synchronized void departure() throws InterruptedException {
        while(!(passengersPassed == Constants.COASTER_CAR_MAX_PASSENGERS)) wait();
        System.out.println("Coaster Car is allowed to start!");
    }

    public synchronized void entrance(){
        passengersPassed = 0;
        notifyAll();
    }
}
