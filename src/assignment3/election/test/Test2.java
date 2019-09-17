package assignment3.election.test;

import assignment3.election.SimpleElectionNode;
import assignment3.election.node.ElectionCluster;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Test2 {

  private SimpleElectionNode a;
  private SimpleElectionNode b;
  private SimpleElectionNode c;
  private SimpleElectionNode d;
  
  @BeforeEach
  void setUp() throws Exception {
    a = new SimpleElectionNode("A", 3, false);
    b = new SimpleElectionNode("B", 2, true);
    c = new SimpleElectionNode("C", 1, false);
    d = new SimpleElectionNode("D", 4, true);
    a.setupNeighbours(b, c, d);
    b.setupNeighbours(a, c, d);
    c.setupNeighbours(a, b, d);
    d.setupNeighbours(a, b, c);
  }

  @AfterEach
  void tearDown() throws Exception {
    assertFalse(a.hasWon());
    assertFalse(b.hasWon());
    assertFalse(c.hasWon());
    assertTrue(d.hasWon());
  }

  @Test
  @RepeatedTest(value = 99)
  void test() {
    ElectionCluster cluster = new ElectionCluster(a, b, c, d);
    cluster.printConnections();
    cluster.startNodes();
    
    int count = 0;
    while (!d.hasWon()) {
      try {
	if (count++ == 7000) {
	  break;
	}
	Thread.sleep(1);
      } catch (InterruptedException e) {
	e.printStackTrace();
      }
    }
  }

}
