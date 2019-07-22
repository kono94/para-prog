package u8_1b;

import u8_1a.SafePoint;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * b) Ist diese Implementierung thread-safe?
 * A: Natürlich. Die Implementierung in der Vorlesung mit ConcurrentHashMap
 * und "Point" (unveränderbar) ist bereits thread-safe. Nun wird statt
 * "Point" und dem Erzeugen von neuen Points bei jeder Veränderung eine thread-safe
 * mutable Variante "SafePoint" verwendet.
 * Frage: muss es überhaupt eine ConcurrentHashMap sein, reicht nicht auch eine
 * normale Hashmap, weil SafePoint bereits thread-safe ist und die Klasse nur Map.get()
 * benutzt?!
 * Recherche: ConcurrentMap locked nicht bei .get(). In der Klasse wird nur .get() benutzt,
 * also hätte auch eine normale Map gereicht.
 * <p>
 * <p>
 * ANMERKUNG:
 * Collections.unmodifiableMap() sorgt nur partiell für Unveränderbarkeit.
 * Im Beispiel aus der Vorlesung war "Point" immutable. SafePoint ist aber veränderbar durch die
 * .set()-Methode. Besorgt sich ein Thread mit .getLocations() die unmodifiableMap, so
 * ist er in der Lage map.get().set(x,y) aufzurufen und somit die Punkte zu verändern!
 * <p>
 * <p>
 * Ist die obige Deklaration des Members locations so richtig, oder muss es wie im letzten
 * Beispiel in der Vorlesung
 * private final ConcurrentMap<String, Point> locations;
 * heißen?
 * A: Würde auch mit Map<String, Point> funktionieren, weil die Klasse ConcurrentHashMap
 * die default Methoden vom Map-Interface überschreibt, aber wenn man auf der sicheren
 * Seite sein möchte, dann benutzt man lieber das ConcurrentMap Interface.
 * <p>
 * <p>
 * <p>
 * Sind Änderungen in der Map bzw. in der Punkten sofort für alle Threads sichtbar?
 * A: "Memory consistency effects: As with other concurrent collections, actions
 * in a thread prior to placing an object into a ConcurrentMap as a key or value
 * happen-before actions subsequent to the access or removal of that object
 * from the ConcurrentMap in another thread." - Hat das mit dem "happen-before" zutun.
 * <p>
 * Sonst:
 * Ja, weil die Referenzen auf die SafePoints in der Map gespeichert werden.
 */

public class PublishingVehicleTracker {
    private final Map<String, SafePoint> locations;
    private final Map<String, SafePoint> unmodifiableMap;

    public PublishingVehicleTracker
            (Map<String, SafePoint> locations) {
        this.locations =
                new ConcurrentHashMap<>(locations);
        this.unmodifiableMap =
                Collections.unmodifiableMap(this.locations);
    }

    public Map<String, SafePoint> getLocations() {
        return unmodifiableMap;
    }

    public SafePoint getLocation(String id) {
        return locations.get(id);
    }

    public void setLocation(String id, int x, int y) {
        if (!locations.containsKey(id))
            throw new IllegalArgumentException("...");
        locations.get(id).set(x, y);
    }
}