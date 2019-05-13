package u5_4;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import static u5_4.ColorConstants.*;

public class Assembly implements Runnable{
    private CyclicBarrier barrier;

    public Assembly(CyclicBarrier barrier) {
        this.barrier = barrier;
    }

    @Override
    public void run() {
        int counter = 0;
        while(true){
            try{
                barrier.await();
                System.out.printf("%s Beginne Montage... %s\n", ANSI_RED, ANSI_RESET);
                Thread.sleep((int)(Math.random()*6000));
                System.out.printf("%s Montiert! %d. Teil fertig !!!%s\n" ,ANSI_GREEN, ++counter, ANSI_RESET);
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }
}
