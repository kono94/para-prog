package assignment3.election;

import assignment3.election.node.ElectionCluster;

public class TaskB {
  public static void main(String[] args) {
    SimpleElectionNode a = new SimpleElectionNode("A", 1, false);
    SimpleElectionNode b = new SimpleElectionNode("B", 2, false);
    SimpleElectionNode c = new SimpleElectionNode("C", 3, false);
    SimpleElectionNode d = new SimpleElectionNode("D", 4, false);

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
    a.setupNeighbours(b, c);
    b.setupNeighbours();
    c.setupNeighbours(d,a);
    d.setupNeighbours(c);
    ElectionCluster cluster = new ElectionCluster(a, b, c, d);
    cluster.printConnections();
    cluster.visualize();
    cluster.startNodes();
  }
}
