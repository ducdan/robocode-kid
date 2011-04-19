package test.data;

import junit.framework.TestCase;

import org.junit.Test;

import test.MockRobot;
import test.MockScannedRobotEvent;
import dev.robots.RobotData;
import dev.robots.RobotManager;

public class RobotManagerTest extends TestCase {

   @Test
   public void test_getInstance() {
      MockRobot robot = new MockRobot();

      RobotManager robots = RobotManager.getInstance();
      assertTrue(robots == null);

      robots = RobotManager.getInstance(robot);
      assertTrue(robots != null);

      robots = RobotManager.getInstance();
      assertTrue(robots != null);
   }

   @Test
   public void test_ScannedRobotEvent() {
      MockScannedRobotEvent dummy1a = new MockScannedRobotEvent(1, "test.dummy1", 100.0, Math.PI / 16.0, 300.0,
            Math.PI, 8.0);
      MockScannedRobotEvent dummy1b = new MockScannedRobotEvent(2, "test.dummy1", 98.0, Math.PI / 16.0, 295.0,
            17.0 / 16.0 * Math.PI, 6.0);
      MockScannedRobotEvent dummy2a = new MockScannedRobotEvent(1, "test.dummy2", 30.0, Math.PI / 4.0, 300.0, Math.PI,
            8.0);

      RobotManager robots = RobotManager.getInstance();

      robots.inEvent(dummy1a);
      RobotData dummy1 = robots.getRobot("test.dummy1");
      assertTrue(!dummy1.isDead());
      assertEquals(100.0, dummy1.getEnergy());
      assertEquals(Math.PI, dummy1.getHeading());
      assertEquals(8.0, dummy1.getVelocity());

      robots.inEvent(dummy2a);
      RobotData dummy2 = robots.getRobot("test.dummy2");
      assertTrue(!dummy2.isDead());
      assertEquals(30.0, dummy2.getEnergy());
      assertEquals(Math.PI, dummy2.getHeading());
      assertEquals(8.0, dummy2.getVelocity());

      robots.inEvent(dummy1b);
      dummy1 = robots.getRobot("test.dummy1");
      assertTrue(!dummy1.isDead());
      assertEquals(98.0, dummy1.getEnergy());
      assertEquals(-2.0, dummy1.getDeltaEnergy());
      assertEquals(17.0 / 16.0 * Math.PI, dummy1.getHeading());
      assertEquals(Math.PI / 16.0, dummy1.getDeltaHeading());
      assertEquals(6.0, dummy1.getVelocity());
      assertEquals(-2.0, dummy1.getDeltaVelocity());

   }

}
