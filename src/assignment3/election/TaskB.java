package assignment3.election;

import assignment3.election.node.ElectionCluster;

public class TaskB {
  public static void main(String[] args) {
    SimpleElectionNode a = new SimpleElectionNode("A", 4, true);
    SimpleElectionNode b = new SimpleElectionNode("B", 3, false);
    SimpleElectionNode c = new SimpleElectionNode("C", 2, false);
    SimpleElectionNode d = new SimpleElectionNode("D", 1, true);

    /*
     * a <-> b <-> d 
     * ^ 	   ^ 
     * |--> c <----|
     */

    /*
     * a.setupNeighbours(b, c); b.setupNeighbours(d); c.setupNeighbours(d);
     */

    a.setupNeighbours(b, c);
    b.setupNeighbours(a, d);
    c.setupNeighbours(a, d);
    d.setupNeighbours(b, c);

    ElectionCluster cluster = new ElectionCluster(a, b, c, d);
    cluster.printConnections();
    cluster.visualize();
    cluster.startNodes();
  }
}
