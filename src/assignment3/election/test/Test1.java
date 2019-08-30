package assignment3.election.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import assignment3.election.SimpleElectionNode;
import assignment3.election.node.ElectionCluster;

public class Test1 {

  private SimpleElectionNode a;
  private SimpleElectionNode b;
  private SimpleElectionNode c;
  private SimpleElectionNode d;

  @Before
  public void setUp() throws Exception {
    a = new SimpleElectionNode("A", 3, false);
    b = new SimpleElectionNode("B", 2, true);
    c = new SimpleElectionNode("C", 1, false);
    d = new SimpleElectionNode("D", 4, true);
    a.setupNeighbours(b, c, d);
    b.setupNeighbours(a, c, d);
    c.setupNeighbours(a, b, d);
    d.setupNeighbours(a, b, c);
  }

  @After
  public void tearDown() throws Exception {
    assertEquals(false, a.hasWon());
    assertEquals(false, b.hasWon());
    assertEquals(false, c.hasWon());
    assertEquals(true, d.hasWon());
  }

  @Test
  public void test() {
    ElectionCluster cluster = new ElectionCluster(a, b, c, d);
    cluster.printConnections();
    cluster.visualize();
    cluster.startNodes();
    
    int count = 0;
    while (!d.hasWon()) {
      try {
	if (count++ == 2000) {
	  break;
	}
	Thread.sleep(1);
      } catch (InterruptedException e) {
	e.printStackTrace();
      }
    }
  }

}
