package assigment2.t2_2;

public class Main {
    public static void main(String[] args) {
        // Initializing the monitor
        RollerCoasterMonitor control = new Control();

        // starting the coaster car thread
        new Thread(new CoasterCar(control, 6000)).start();
        // starting the turnstile thread
        new Thread(new Turnstile(control, 1000)).start();
    }
}
