package assignment3.election;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import assignment3.election.node.ElectionNode;
import assignment3.election.node.ElectionNodeAbstract;
import assignment3.util.ColorConstants;

public class SimpleElectionNode extends ElectionNodeAbstract {

    private final static Logger Log = Logger.getLogger(SimpleElectionNode.class.getName());

    private int count = 0;
    private ElectionNode wokeUpBy;
    private boolean receivedFirstWakeUpCall = false;
    private StringBuilder echoResult = new StringBuilder();
    private volatile boolean won;

    public SimpleElectionNode(String name, int identity, boolean isInitiator) {
        super(name, identity, isInitiator);
        this.won = false;

    }

    @Override
    public void setupNeighbours(ElectionNode... neighbours) {
        for (ElectionNode n : neighbours) {
            this.neighbours.add(n);
            n.hello(this);
        }
    }

    public void printNeighbours() {
        System.out.print(name + " Neighbours:[");
        neighbours.forEach(node -> System.out.print(node + ","));
        System.out.println("]");
    }

    @Override
    public void hello(ElectionNode neighbour) {
        this.neighbours.add(neighbour);
    }

    @Override
    public synchronized void wakeup(ElectionNode neighbour, int identity) {
        System.out.println(this + ": Got wakeup-call from: " + neighbour + " with identity " + identity);

        if (currStrongestIdentity < identity) {
            System.out.println(this + ": received higher identity wave from " + neighbour + " (" + currStrongestIdentity + " < " + identity + ")");
            wokeUpBy = neighbour;
            System.out.println("setting wokeup to " + wokeUpBy + " curre " + this);
            currStrongestIdentity = identity;
            receivedFirstWakeUpCall = true;
            count = 0;

            if (isInitiator && currStrongestIdentity != this.identity) {
                System.out.printf("%s %s is not part of the election anymore %s\n", ColorConstants.ANSI_YELLOW, this, ColorConstants.ANSI_RESET);
                isInitiator = false;
            }
        } else if (currStrongestIdentity > identity) {
            System.out.println(this + ": current strongest identity is bigger than from " + neighbour + " (" + currStrongestIdentity + " > " + identity + ")");
        }

        // Not working with previous condition " identity == currStrongestIdentity && !isInitiator"
        /*
        case: A is initiator and sends wakeups to B,C and D
        B and C received those calls instantly, D not
        B wakes up D before A does, so
        in the end A receives a wakeup call from D although A queued wakeups to B,C and D
         */
        if (identity == currStrongestIdentity && this != neighbour) {
            count++;
        }else{
            System.out.println(this + ": Ignoring wakeup-call from: " + neighbour);
        }

        System.out.println(this + " count: " + count);
        try {
            Thread.sleep(111);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        notify();
    }

    @Override
    public synchronized void echo(ElectionNode neighbour, Object data) {
        System.out.println(this + ": Got echo-call from: " + neighbour);
        count++;
        echoResult.append(neighbour).append(" <-> ").append(this).append("\n").append(data);
        System.out.println(this + " count: " + count);
        notify();
    }

    @Override
    public synchronized void run() {
        ExecutorService executorService = Executors.newFixedThreadPool(neighbours.size());

        new Thread(() -> {
            while (true) {
                try {
                    synchronized (SimpleElectionNode.this){
                        if (!isInitiator && currStrongestIdentity == Integer.MIN_VALUE) {
                            isInitiator = true;
                            System.out.printf("%s %s started the election and wants to be leader %s\n", ColorConstants.ANSI_GREEN, this, ColorConstants.ANSI_RESET);
                            wakeup(this, identity);
                        }
                    }
                    Thread.sleep((int) (Math.random() * 1000 * identity + 400));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        while (true) {
            try {
                if (receivedFirstWakeUpCall) {
                    System.out.println(this + ": waking up neighbours");
                    // async
                    neighbours.forEach(e -> {
                        if (e != wokeUpBy) {
                            int tmp = currStrongestIdentity;
                            executorService.submit(() -> {
                                e.wakeup(SimpleElectionNode.this, tmp);
                            });
                        }
                    });
                    receivedFirstWakeUpCall = false;
                }

                if (count == getNeighbourCount()) {
                    if (isInitiator) {
                        String tmp = echoResult.toString();
                        System.out.printf("%s %s won the election process! Result: %s %s\n", ColorConstants.ANSI_RED, this, tmp, ColorConstants.ANSI_RESET);
                        won = true;
                    } else {
                        ElectionNode d = wokeUpBy;
                        int t =  this.currStrongestIdentity;
                        executorService.submit(() -> {
                            System.out.println("submitting fake echo wakup  from " + this + " to " + d);
                            d.wakeup(this, t);
                        });
                    }
                    wokeUpBy = null;
                    isInitiator = false;
                    currStrongestIdentity = Integer.MIN_VALUE;
                    count = 0;
                    echoResult = new StringBuilder();
                }
                System.out.println(this + " is sleeping again");
                wait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //  executorService.shutdown();
    }

    private int getNeighbourCount() {
        return neighbours.size();
    }

    public boolean hasWon() {
        return won;
    }

    public int getStrongestIdentity() {
        return currStrongestIdentity;
    }

    public ElectionNode getWokeUpBy() {
        return wokeUpBy;
    }

    public int getCount() {
        return count;
    }

    public boolean isReceivedFirstWakeUpCall() {
        return receivedFirstWakeUpCall;
    }

}
