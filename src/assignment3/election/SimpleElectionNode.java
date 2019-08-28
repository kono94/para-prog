package assignment3.election;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import assignment3.election.node.ElectionNode;
import assignment3.election.node.ElectionNodeAbstract;

public class SimpleElectionNode extends ElectionNodeAbstract {
    private int count = 0;
    private ElectionNode wokeUpBy;
    private boolean receivedFirstWakeUpCall = false;
    private StringBuilder echoResult = new StringBuilder();

    public SimpleElectionNode(String name, int identity, boolean isInitiator) {
        super(name, identity, isInitiator);
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
        if (isInitiator && identity > this.identity) {
            isInitiator = false;
        }
        if (currStrongestIdentity < identity) {
            System.out.println(this + ": received higher identity wave from " + neighbour + " (" + currStrongestIdentity + " < " + identity + ")");
            wokeUpBy = neighbour;
            currStrongestIdentity = identity;
            receivedFirstWakeUpCall = true;
            count = 0;
        } else {
            if (currStrongestIdentity > identity) {
                System.out.println(this + ": current strongest identity is bigger than from " + neighbour + " (" + currStrongestIdentity + " > " + identity + ")");
            } else {
                System.out.println(this + ": Ignoring wakeup-call from: " + neighbour);
            }
        }
        // Not working with previous condition " identity == currStrongestIdentity && !isInitiator"
        /*
        case: A is initiator and sends to wakeup to B,C and D
        B and C received those calls instantly, D not
        B wakes up D before A does, so
        in the end A receives a wakeup call from D altough A queued wakeups to B,C and D
         */
        if (identity == currStrongestIdentity) {
            count++;
        }

        System.out.println(this + " count: " + count);
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

        if (isInitiator) {
            System.out.println(this + " started the election and wants to be leader");
            receivedFirstWakeUpCall = true;
            wokeUpBy = this;
            //neighbours.forEach(e -> executorService.submit(() -> e.wakeup(SimpleElectionNode.this, identity)));
        }

        while (true) {
            try {
                if (receivedFirstWakeUpCall) {
                    System.out.println(this + ": waking up neighbours");
                    neighbours.forEach(e -> {
                        if (e != wokeUpBy) {
                            executorService.submit(() -> e.wakeup(SimpleElectionNode.this, currStrongestIdentity));
                        }
                    });
                    receivedFirstWakeUpCall = false;
                }

                if (count == getNeighbourCount()) {
                    if (isInitiator) {
                        System.out.println(this + ": WON Finished! Result: \n" + echoResult);
                    } else {
                        wokeUpBy.echo(this, echoResult);
                    }
                    wokeUpBy = null;
                    isInitiator = false;
                    break;
                   // wait((int) (Math.random()* 10000));
                }
                System.out.println(this + " is sleeping again");
                wait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        executorService.shutdownNow();
    }

    private int getNeighbourCount() {
        return neighbours.size();
    }

}
