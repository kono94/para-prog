package u8_2;

public class Boy extends Thread {
    private String name;
    private Slide slide;
    private int slideTimes;

    public Boy(String name, Slide slide, int priority) {
        this.setPriority(priority);
        this.name = name;
        this.slide = slide;
        slideTimes = 0;
    }

    @Override
    public void run() {
        while (Main.RUN) {
            try {
                slide.slide();
                slideTimes++;
                System.out.println(name + " is sliding for the " + slideTimes + " time; Priority: " + this.getPriority());
                // why this behaviour
                Thread.sleep(5);
                slide.exit();
                //Thread.yield();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        return name + " has been sliding " + slideTimes + " times; Priority: " + this.getPriority();
    }
}
