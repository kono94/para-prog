package assignment3.election;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import assignment3.echo.SimpleNode;
import assignment3.election.node.ElectionNode;
import assignment3.election.node.ElectionNodeAbstract;
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
      //  System.out.println(this + ": Got wakeup-call from: " + neighbour + " with identity " + identity);
        if (currStrongestIdentity < identity) {
            System.out.println(this + ": received higher identity wave from " + neighbour + " (" + currStrongestIdentity + " < " + identity + ")");
            callBackTo = neighbour;
            currStrongestIdentity = identity;
            receivedFirstElectionCall = true;
            electionCount = 0;

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
            electionCount++;
        } else {
       //     System.out.println(this + ": Ignoring wakeup-call from: " + neighbour);
        }

       // System.out.println(this + " count: " + electionCount);
        try {
            Thread.sleep(600);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        notify();
    }

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
        //    System.out.println(this + ": Woke-up from: " + neighbour);
            data.wokeUpBy = neighbour;
            data.receivedFirstWakeUpCall = true;
        } else {
     //       System.out.println(this + ": Ignoring wakeup-call from: " + neighbour);
        }
        try {
            Thread.sleep(600);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        notify();
    }

    @Override
    public synchronized void echo(ElectionNode neighbour, int identity, Object data) {
        EchoData echoData = identityToEchoData.get(identity);
      //  System.out.println(this + ": Got echo-call from: " + neighbour);
        echoData.count++;
        echoData.echoResult.append(neighbour).append(" <-> ").append(this).append("\n").append(data);
        notify();
    }

    @Override
    public synchronized void run() {
        ExecutorService executorService = Executors.newFixedThreadPool(neighbours.size());

        new Thread(() -> {
            while (true) {
                try {
                    synchronized (SimpleElectionNode.this) {
                        if (isAllowedToStartElection && !isInitiator && currStrongestIdentity == Integer.MIN_VALUE) {
                            isInitiator = true;
                            isAllowedToStartElection = false;
                            System.out.printf("%s %s started the election and wants to be leader %s\n", ColorConstants.ANSI_GREEN, this, ColorConstants.ANSI_RESET);
                            electionCall(this, identity);
                        }
                    }
                    Thread.sleep((int) (1000 * identity + 2000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        while (true) {
            try {
                if (receivedFirstElectionCall) {
                    // System.out.println(this + ": waking up neighbours");
                    // async
                    neighbours.forEach(e -> {
                        if (e != callBackTo) {
                            int tmp = currStrongestIdentity;
                            executorService.submit(() -> {
                                e.electionCall(SimpleElectionNode.this, tmp);
                            });
                        }
                    });
                    receivedFirstElectionCall = false;
                }

                if (electionCount == getNeighbourCount()) {
                    if (isInitiator) {
                        System.out.printf("%s %s won the election process! Result: %s\n", ColorConstants.ANSI_RED, this, ColorConstants.ANSI_RESET);
                        won = true;
                        //start echo
                        if (!identityToEchoData.containsKey(identity)) {
                            identityToEchoData.put(identity, new EchoData());
                        }
                        System.out.println("ECHO ALGORITHM IN PROGRESS...");
                        isEchoInitiator = true;
                        EchoData echoData = identityToEchoData.get(identity);
                        echoData.receivedFirstWakeUpCall = true;
                        echoData.wokeUpBy = this;
                    } else {
                        final ElectionNode d = callBackTo;
                        final int tmp = this.currStrongestIdentity;
                        executorService.submit(() -> {
                            //  System.out.println("submitting fake echo wakeup  from " + this + " to " + d);
                            d.electionCall(this, tmp);
                        });
                    }
                    isInitiator = false;
                    isAllowedToStartElection = false;
                    callBackTo = null;
                    currStrongestIdentity = Integer.MIN_VALUE;
                    electionCount = 0;
                }

                for (Map.Entry<Integer, EchoData> data : identityToEchoData.entrySet()) {
                    final int identity = data.getKey();
                    EchoData echoData = data.getValue();

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
