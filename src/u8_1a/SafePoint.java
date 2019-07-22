package u8_1a;

/**
 * a) Ist SafePoint thread-safe?
 * A: Ja, da die Interface-Methoden .get() und .set() explizit
 * synchronized deklariert sind.
 * <p>
 * Ist diese Klasse zur Speicherung von Koordinaten im Rahmen des Vehicle Trackers geeignet?
 * A: Ja, da die .set()-Methode synchronized ist und die .get() Methode "quasi" eine
 * Kopie der Werte als Array liefert, da der primitive Datentyp int, keine Referenzen zulässt.
 * <p>
 * <p>
 * Wie ist eine alternative Implementierung des Copy-Konstrukturs durch this(p.x, p.y)
 * statt this(p.get() zu bewerten?
 * A: Dürfte nicht Thread-safe sein, aber konnte den Effekt nicht produzieren.
 */

public class SafePoint {
    private int x, y;

    private SafePoint(int[] a) {
        this(a[0], a[1]);
    }

    public SafePoint(int x, int y) {
        this.set(x, y);
    }

    public SafePoint(SafePoint p) {
        this(p.get());
        //this(p.x, p.y);
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