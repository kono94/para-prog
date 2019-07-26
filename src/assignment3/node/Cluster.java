package assignment3.node;


import assignment3.echo.SimpleNode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Cluster {
    Set<SimpleNode> nodes;

    public Cluster(SimpleNode... nodes) {
        this.nodes = new HashSet<>(Arrays.asList(nodes));
    }

    public void visualize() {
        StringBuilder sb = new StringBuilder();
        sb.append("@startuml \ndigraph G {\n concentrate=true \n");
        nodes.forEach(sn -> {
            if (sn.isInitiator) sb.append(sn).append(" [color=red]");

            sn.neighbours.forEach(neighbour -> sb.append(sn).append(" -> ").append(neighbour).append(";\n"));
        });
        sb.append("} \n @enduml");
        try {
            Files.write(Paths.get("src/assignment3/output.puml"), sb.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startNodes() {
        nodes.forEach(Thread::start);
    }

    public void printConnections() {
        nodes.forEach(SimpleNode::printNeighbours);
    }
}
