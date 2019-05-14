package u5_4;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CyclicBarrier;

public class Production {
    public static void main(String[] args) {
        final int numberOfParts = 25;
        Set<ProductionProcess> productionProcesses = new HashSet<>();

        productionProcesses.add(new ProductionProcess("A", numberOfParts));
        productionProcesses.add(new ProductionProcess("B", numberOfParts));
        productionProcesses.add(new ProductionProcess("C", numberOfParts));

        // Size of barrier equals productions threads and the assembly thread;
        // Assembly thread is waiting for the other threads to call .await()
        CyclicBarrier cyclicBarrier = new CyclicBarrier(productionProcesses.size() + 1);

        // Passing barrier to all processes
        for (ProductionProcess c: productionProcesses) c.setBarrier(cyclicBarrier);

        // Starting all production processes
        for (ProductionProcess c: productionProcesses) new Thread(c).start();

        // Starting Assembly process
        new Thread(new Assembly(cyclicBarrier)).start();
    }
}
