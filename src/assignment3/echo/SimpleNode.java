package assignment3.echo;

import assignment3.echo.node.Node;
import assignment3.echo.node.NodeAbstract;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleNode extends NodeAbstract {
    private int count = 0;
    private Node wokeUpBy;
    private boolean receivedFirstWakeUpCall = false;
    private StringBuilder echoResult = new StringBuilder();

    public SimpleNode(String name, boolean isInitiator) {
        super(name, isInitiator);
    }

    @Override
    public void setupNeighbours(Node... neighbours) {
        for (Node n : neighbours) {
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
    public void hello(Node neighbour) {
        this.neighbours.add(neighbour);
    }

    @Override
    public synchronized void wakeup(Node neighbour) {
        System.out.println(this + ": Got wakeup-call from: " + neighbour);
        count++;
        if (wokeUpBy == null) {
            System.out.println(this + ": Woke-up from: " + neighbour);
            wokeUpBy = neighbour;
            receivedFirstWakeUpCall = true;
        } else {
            System.out.println(this + ": Ignoring wakeup-call from: " + neighbour);
        }
        notify();
    }

    @Override
    public synchronized void echo(Node neighbour, Object data) {
        System.out.println(this + ": Got echo-call from: " + neighbour);
        count++;
        echoResult.append(neighbour).append(" <-> ").append(this).append("\n").append(data);
        notify();
    }

    @Override
    public synchronized void run() {
        ExecutorService executorService = Executors.newFixedThreadPool(neighbours.size());

        if (isInitiator) {
            receivedFirstWakeUpCall = true;
            wokeUpBy = this;
            //neighbours.forEach(e -> executorService.submit(() -> e.wakeup(SimpleNode.this)));
        }

        while (true) {
            try {
                if (receivedFirstWakeUpCall) {
                    System.out.println(this + " waking up neighbours");
                    neighbours.forEach(e -> {
                        if (e != wokeUpBy) {
                            executorService.submit(() -> e.wakeup(SimpleNode.this));
                        }
                    });
                    receivedFirstWakeUpCall = false;
                }

                if (count == getNeighbourCount()) {
                    if (isInitiator) {
                        System.out.println("Finished! Result: \n" + echoResult);
                    } else {
                        System.out.println(this + " sent echo and finished");
                        wokeUpBy.echo(this, echoResult);
                    }
                    break;
                }
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
