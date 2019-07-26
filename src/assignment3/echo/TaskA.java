package assignment3.echo;


import assignment3.node.Cluster;

public class TaskA {
    public static void main(String[] args) {
        SimpleNode a = new SimpleNode("A", false);
        SimpleNode b = new SimpleNode("B", false);
        SimpleNode c = new SimpleNode("C", false);
        SimpleNode d = new SimpleNode("D", true);
        SimpleNode e = new SimpleNode("E", false);

        /*
            a <-> b <-> d
            ^           ^
            |--> c <----|
         */

        /*
        a.setupNeighbours(b, c);
        b.setupNeighbours(d);
        c.setupNeighbours(d);
         */

        a.setupNeighbours(b, e, c, d);
        b.setupNeighbours(a, c, d, c);
        c.setupNeighbours(b, d, e, a);
        d.setupNeighbours(a, b, c, e);
        e.setupNeighbours(a, b, c, d);


        Cluster cluster = new Cluster(a, b, c, d, e);
        cluster.printConnections();
        cluster.visualize();
        cluster.startNodes();
    }
}
