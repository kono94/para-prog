package u8_1a;

public class TestSafePoint {
    public static void main(String[] args) {
        final SafePoint[] notSoSavePoint = {new SafePoint(0, 0)};
        SafePoint baitPoint = new SafePoint(0, 0);

        Thread a = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                notSoSavePoint[0] = new SafePoint(baitPoint);
                if (notSoSavePoint[0].get()[0] != notSoSavePoint[0].get()[0]) {
                    throw new RuntimeException(notSoSavePoint[0].toString());
                }
                System.out.println("dont optimize");
            }
        });

        Thread b = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                int t = (int) (Math.random() * 1000);
                baitPoint.set(t, t);
            }
        });

        a.start();
        b.start();

        try {
            a.join();
            b.join();
            System.out.println(notSoSavePoint[0]);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
