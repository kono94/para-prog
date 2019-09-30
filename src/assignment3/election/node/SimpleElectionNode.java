package assignment3.election.node;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import assignment3.util.ColorConstants;

public class SimpleElectionNode extends ElectionNodeAbstract {

    private final static Logger Log = Logger.getLogger(SimpleElectionNode.class.getName());

    private int electionCount = 0;
    private ElectionNode callBackTo;
    private boolean receivedFirstElectionCall = false;
    private volatile boolean won;
    private boolean isAllowedToStartElection;
    private boolean isEchoInitiator;

    public SimpleElectionNode(String name, int identity, boolean isInitiator) {
        super(name, identity, isInitiator);
        this.won = false;
        isAllowedToStartElection = true;
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
    public synchronized void electionCall(ElectionNode neighbour, int identity) {
        System.out.println(this + ": Got election-call from: " + neighbour + " with identity " + identity);

        // received a "bigger wave", sending electionCalls to all neighbours once again
        // with the identity just received
        if (currStrongestIdentity < identity) {
            System.out.println(this + ": received higher identity wave from " + neighbour + " (" + currStrongestIdentity + " < " + identity + ")");
            callBackTo = neighbour;
            currStrongestIdentity = identity;
            receivedFirstElectionCall = true;
            electionCount = 0;

            // receiving a higher identity as previously saved means the node cannot be an
            // initiator anymore. The only case this is possible is, if the node sent an election call
            // to itself while starting an election process on its one
            if (isInitiator && currStrongestIdentity != this.identity) {
                System.out.printf("%s %s is not part of the election anymore %s\n", ColorConstants.ANSI_YELLOW, this, ColorConstants.ANSI_RESET);
                isInitiator = false;
            }

        }

        if (currStrongestIdentity > identity) {
            // "wave" gets lost
            System.out.println(this + ": current strongest identity is bigger than from " + neighbour + " (" + currStrongestIdentity + " > " + identity + ") Ignoring election call");

        }else{
            // do not increase the count in case the node called itself
            // because count represents the number of neighbours that already called the node
            if( this != neighbour){
                electionCount++;
            }
        }
        System.out.println(this + " count: " + electionCount + " / " + this.neighbours.size());
        try {
            Thread.sleep((int)(Math.random()*1 + 0.5)* 300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        notify();
    }

    // Data structure to capsule all necessary fields as in "SimpleNode" from the echo algorithm,
    // so multiple echo algorithms are able to run at the same time
    private class EchoData {
        private ElectionNode wokeUpBy = null;
        private boolean receivedFirstWakeUpCall = false;
        private int count = 0;
        private StringBuilder echoResult = new StringBuilder();

        private void clear() {
            wokeUpBy = null;
            receivedFirstWakeUpCall = false;
            count = 0;
            echoResult = new StringBuilder();
        }
    }

    private Map<Integer, EchoData> identityToEchoData = new HashMap<>();

    @Override
    public synchronized void wakeup(ElectionNode neighbour, int identity) {
        // System.out.println(this + ": Got wakeup-call from: " + neighbour);
        if (identityToEchoData.get(identity) == null) {
            identityToEchoData.put(identity, new EchoData());
        }

        EchoData data = identityToEchoData.get(identity);
        data.count++;

        if (data.wokeUpBy == null) {
            // System.out.println(this + ": Woke-up from: " + neighbour);
            data.wokeUpBy = neighbour;
            data.receivedFirstWakeUpCall = true;
        } else {
            // System.out.println(this + ": Ignoring wakeup-call from: " + neighbour);
        }
        try {
            Thread.sleep((int)(Math.random()*1 + 0.5)* 300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        notify();
    }

    @Override
    public synchronized void echo(ElectionNode neighbour, int identity, Object data) {
        EchoData echoData = identityToEchoData.get(identity);
        // System.out.println(this + ": Got echo-call from: " + neighbour);
        echoData.count++;
        echoData.echoResult.append(neighbour).append(" <-> ").append(this).append("\n").append(data);
        notify();
    }

    @Override
    public synchronized void run() {
        ExecutorService executorService = Executors.newFixedThreadPool(neighbours.size());

        // Mechanism for nodes to start the election once again
        new Thread(() -> {
            while (true) {
                try {
                    // necessary synchronized?
                    synchronized (SimpleElectionNode.this) {
                        if (isAllowedToStartElection && !isInitiator && currStrongestIdentity == Integer.MIN_VALUE && !isEchoInitiator) {
                            isInitiator = true;
                            // prevent node from starting another election when previous election is not finished yet (the one it started itself)
                            isAllowedToStartElection = false;
                            System.out.printf("%s %s started the election and wants to be leader %s\n", ColorConstants.ANSI_GREEN, this, ColorConstants.ANSI_RESET);
                            // node starts the election by sending an election call to itself
                            electionCall(this, identity);
                        }
                    }
                    Thread.sleep((int) (Math.random() * 3000 * identity));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        while (true) {
            try {
                if (receivedFirstElectionCall) {
                    System.out.println(this + ": waking up neighbours");
                    // async
                    neighbours.forEach(e -> {
                        if (e != callBackTo) {
                            int tmp = currStrongestIdentity;
                            executorService.submit(() -> e.electionCall(SimpleElectionNode.this, tmp));
                        }
                    });
                    receivedFirstElectionCall = false;
                }

                if (electionCount == getNeighbourCount()) {
                    if (isInitiator || identity == this.currStrongestIdentity) {
                        System.out.printf("%s %s won the election process! Result: %s\n", ColorConstants.ANSI_RED, this, ColorConstants.ANSI_RESET);
                        // for testing purposes
                        won = true;
                        // start echo algorithm after winning the election
                        if (!identityToEchoData.containsKey(identity)) {
                            identityToEchoData.put(identity, new EchoData());
                        }
                        System.out.println("ECHO ALGORITHM IN PROGRESS...");
                        isEchoInitiator = true;
                        EchoData echoData = identityToEchoData.get(identity);
                        // starting echo algorithm by sending a wakeup call to itself
                        echoData.receivedFirstWakeUpCall = true;
                        echoData.wokeUpBy = this;
                    } else {
                        // similar to the "callback" in "echo", send an election call back to the first node
                        // which sent the strongestIdentity
                        final ElectionNode d = callBackTo;
                        final int tmp = this.currStrongestIdentity;
                        executorService.submit(() -> {
                            // System.out.println("submitting fake echo wakeup  from " + this + " to " + d);
                            d.electionCall(this, tmp);
                        });
                    }
                    // reset parameters after node finished its part in the election process
                    isInitiator = false;
                    isAllowedToStartElection = false;
                    callBackTo = null;
                    currStrongestIdentity = Integer.MIN_VALUE;
                    electionCount = 0;
                }

                // Optional to process multiple echo algorithms at once
                // by using a map for every identity possible from the leader
                for (Map.Entry<Integer, EchoData> data : identityToEchoData.entrySet()) {
                    final int identity = data.getKey();
                    EchoData echoData = data.getValue();

                    // Same logic as in taskA
                    if (echoData.receivedFirstWakeUpCall) {
                        //  System.out.println(this + " waking up neighbours");
                        neighbours.forEach(e -> {
                            if (e != echoData.wokeUpBy) {
                                executorService.submit(() -> e.wakeup(SimpleElectionNode.this, identity));
                            }
                        });
                        echoData.receivedFirstWakeUpCall = false;
                    }
                    if (echoData.count >= getNeighbourCount()) {
                        if (isEchoInitiator) {
                            System.out.println("Finished! Result: \n" + echoData.echoResult);
                            isEchoInitiator = false;
                        } else {
                            //  System.out.println("Echo: " + this + " sent echo and finished");
                            echoData.wokeUpBy.echo(this, identity, echoData.echoResult);
                        }
                        // after a node completed its part in the echo process, it is able to start an election again
                        isAllowedToStartElection = true;
                        echoData.clear();
                    }
                }
                // System.out.println(this + " is sleeping again");
                wait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

    public ElectionNode getCallBackTo() {
        return callBackTo;
    }

    public int getElectionCount() {
        return electionCount;
    }

    public boolean isReceivedFirstElectionCall() {
        return receivedFirstElectionCall;
    }

}
