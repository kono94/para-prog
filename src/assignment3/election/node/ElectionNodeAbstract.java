package assignment3.election.node;

import java.util.HashSet;
import java.util.Set;

/**
 * Abstract implementation of the Node interface.
 */
public abstract class ElectionNodeAbstract extends Thread implements ElectionNode {

    /**
     * Name of this node
     */
    protected final String name;

    /**
     * Is this node the isInitiator of the echo algorithm?
     */
    protected volatile boolean isInitiator;

    /**
     * Collection of known neighbours of this node; only the methods of the
     * neighbours in this collection can be called.
     */
    protected final Set<ElectionNode> neighbours = new HashSet<>();

    /**
     * 
     */
    protected final int identity;
    
    /**
     * 
     */
    protected int currStrongestIdentity;
    
    /**
     * Abstract constructor of a node
     */
    public ElectionNodeAbstract(String name, int identity, boolean isInitiator) {
        super(name);
        this.name = name;
        this.identity = identity;
        this.currStrongestIdentity = Integer.MIN_VALUE;
        this.isInitiator = isInitiator;
        if (isInitiator) {
          this.currStrongestIdentity = identity;
        }
    }

    /**
     * Method to setup the list of initially known neighbours; the setup must be
     * complete in all nodes before any echo thread is started!
     * <p>
     * Be aware that the neighbour relationship is symmetric (if node "a" has node
     * "b" as its neighbour, also node "b" must have node "a" as its neighbour)!
     * Therefore call method <code>hello</code> of each neighbour here.
     */
    public abstract void setupNeighbours(ElectionNode... neighbours);
    
    public int getIdentity() {
      return identity;
    }

    /**
     * Utility method to print this node in a readable way
     */
    @Override
    public String toString() {
        return name;
    }

}
