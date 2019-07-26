package assignment3.echo;


import assignment3.node.Cluster;

public class TaskA {
    public static void main(String[] args) {
        SimpleNode a = new SimpleNode("A", true);
        SimpleNode b = new SimpleNode("B", false);
        SimpleNode c = new SimpleNode("C", false);
        SimpleNode d = new SimpleNode("D", false);

        /*
            a <-> b <-> d
            ^           ^
            |--> c <----|
         */

        a.setupNeighbours(b, c);
        b.setupNeighbours(d);
        c.setupNeighbours(d);

        Cluster cluster = new Cluster(a, b, c, d);
        cluster.printConnections();
        cluster.visualize();
        cluster.startNodes();
    }
}
