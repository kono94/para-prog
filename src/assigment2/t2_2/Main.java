package assigment2.t2_2;

public class Main {
    public static void main(String[] args) {
        RollerCoasterMonitor control = new Control();

        new Thread(new CoasterCar(control, 6000)).start();
        new Thread(new Turnstile(control, 1000)).start();
    }
}
