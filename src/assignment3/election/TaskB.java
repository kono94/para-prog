package assignment3.election;

import assignment3.election.node.ElectionCluster;

public class TaskB {
  public static void main(String[] args) {
    SimpleElectionNode a = new SimpleElectionNode("A", 3, false);
    SimpleElectionNode b = new SimpleElectionNode("B", 4, false);
    SimpleElectionNode c = new SimpleElectionNode("C", 1, false);
    SimpleElectionNode d = new SimpleElectionNode("D", 2, false);

    /*
     * a <-> b <-> d 
     * ^ 	       ^
     * |--> c <----|
     */

    /*
     * a.setupNeighbours(b, c); b.setupNeighbours(d); c.setupNeighbours(d);
     */

    /*
    a <-> b
      <-> c
     */
    a.setupNeighbours(b, c, d );
    b.setupNeighbours(a, c, d);
    c.setupNeighbours(a, b, d);
    d.setupNeighbours(a,b,c);
    ElectionCluster cluster = new ElectionCluster(a, b, c, d);
    cluster.printConnections();
    cluster.visualize();
    cluster.startNodes();
  }
}
