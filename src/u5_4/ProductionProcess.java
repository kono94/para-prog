package u5_4;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class ProductionProcess implements Runnable{
    private String prodName;
    private CyclicBarrier barrier;
    private int numberOfParts;

    ProductionProcess(String prodName, int numberOfParts){
        this.prodName = prodName;
        this.numberOfParts = numberOfParts;
    }

    void setBarrier(CyclicBarrier cb){
        barrier = cb;
    }

    @Override
    public void run() {
        for(int i=0; i< numberOfParts; ++i){
            produce();
            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

    private void produce(){
        System.out.printf("Producing %s...\n", prodName);
        try {
            Thread.sleep((int) (Math.random()*5000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("Fertig mit der Produktion von %s!\n", prodName);
    }
}
