package assignment3.election.node;

/**
 * This interface defines the methods of each node participating in the echo
 * algorithm. These methods can be called by the neighbours of a node.
 */
public interface ElectionNode {

  /**
   * Greetings from a neighbour. Before starting the echo algorithm this message
   * is called within <code>setupNeighbours</code> once by every node for all its
   * initially known neighbours.
   * <p>
   * The main purpose is that <code>this</code> node gets the chance to add the
   * calling <code>neighbour</code> to its own list of neighbours (just in case
   * the neighbour was previously unknown to this node although the neighbour
   * itself knows this node).
   *
   * @param neighbour
   */
  void hello(ElectionNode neighbour);

  /**
   *
   * @param neighbour
   * @param identity
   */
  void electionCall(ElectionNode neighbour, int identity);

  /**
   * Incoming "wakeup" message from a neighbour with identity of the initializer.
   *
   * @param neighbour
   * @param identity
   */
  void wakeup(ElectionNode neighbour, int identity);

  /**
   * Incoming "echo" message from a neighbour. The neighbour can also send some
   * data with this echo message. The data object might e.g. contain information
   * about edges of the spanning tree known so far; if not needed, set data to
   * null.
   *
   * @param neighbour
   * @param data
   */
  void echo(ElectionNode neighbour, int identity, Object data);
}
