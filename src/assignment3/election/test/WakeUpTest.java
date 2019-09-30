package assignment3.election.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import assignment3.election.node.SimpleElectionNode;

class WakeUpTest {
  
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
  }

  @Test
  @RepeatedTest(value = 99)
  void test() {
    assertEquals(Integer.MIN_VALUE, c.getStrongestIdentity());
    
    c.wakeup(a, 3);
    assertEquals(a.getIdentity(), c.getStrongestIdentity());
    assertEquals(a, c.getCallBackTo());
    
    c.wakeup(b, 2);
    assertEquals(a.getIdentity(), c.getStrongestIdentity());
    assertEquals(a, c.getCallBackTo());

    c.wakeup(d, 4);
    assertEquals(d.getIdentity(), c.getStrongestIdentity());
    assertEquals(d, c.getCallBackTo());
  
  }

}
