package u5_4b;

import u5_4.Assembly;
import u5_4.ProductionProcess;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

import static u5_4.ColorConstants.*;
import static u5_4.ColorConstants.ANSI_RESET;

public class Production {
    public static void main(String[] args) {
        final int numberOfParts = 24;
        Set<ProductionProcess> productionProcesses = new HashSet<>();

        productionProcesses.add(new ProductionProcess("A", numberOfParts));
        productionProcesses.add(new ProductionProcess("B", numberOfParts));
        productionProcesses.add(new ProductionProcess("C", numberOfParts));

        // Variables used in lambda should be final or effectively final
        AtomicInteger counter = new AtomicInteger(0);

        // Size of barrier equals productions threads.
        // No Assembly Thread needed.
        // BUT: Other production threads have to wait before producing again until assembly is complete!
        CyclicBarrier cyclicBarrier = new CyclicBarrier(productionProcesses.size(), new Assembly()::run2);

        // Passing barrier to all processes
        for (ProductionProcess c : productionProcesses) c.setBarrier(cyclicBarrier);

        // Starting all production processes
        for (ProductionProcess c : productionProcesses) new Thread(c).start();
    }
}
