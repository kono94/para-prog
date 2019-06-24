package u8_1a;


public class SafePoint {
    private int x, y;

    private SafePoint(int[] a) {
        this(a[0], a[1]);
    }

    public SafePoint(int x, int y) {
        this.set(x, y);
    }

    public SafePoint(SafePoint p) {
        //this(p.get());
        this(p.x, p.y);
    }

    public synchronized int[] get() {
        return new int[]{x, y};
    }

    public synchronized void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "x: " + x + " y:" + y;
    }
}