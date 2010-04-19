package dev;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import robocode.AdvancedRobot;
import robocode.DeathEvent;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;
import robocode.WinEvent;
import dev.cluster.Scale;
import dev.cluster.Space;
import dev.cluster.Vector;
import dev.cluster.scales.Distance;
import dev.cluster.scales.LateralVelocity;
import dev.cluster.scales.Velocity;
import dev.data.RobotData;
import dev.draw.DrawMenu;
import dev.manage.RobotManager;

public class Test extends AdvancedRobot {

   private RobotManager         robots;
   private static final Scale[] scales = { new Distance(), new Velocity(), new LateralVelocity() };
   private static Space         space  = new Space(scales);

   @Override
   public void run() {
      this.robots = RobotManager.getInstance(this);

      setTurnRadarRightRadians(Double.POSITIVE_INFINITY);

      do {
         this.execute();
      } while (true);
   }

   @Override
   public void onScannedRobot(ScannedRobotEvent event) {
      this.robots.inEvent(event);
      if (setFireBullet(2.0) != null) {
         // Vector center = new Vector(Test.scales, robots.getRobot(event.getName()), new RobotData(this));
         // System.out.println("CENTER: " + center);

         LinkedList<Vector> vectors = space.getClustor(robots.getRobot(event.getName()), new RobotData(this), 10);
         String str = "CLUSTER:\n";
         for (Vector v : vectors) {
            str += v;
         }
         System.out.println(str);

         space.add(robots.getRobot(event.getName()), new RobotData(this));
      }
   }

   @Override
   public void onRobotDeath(RobotDeathEvent event) {
      this.robots.inEvent(event);
   }

   @Override
   public void onDeath(DeathEvent event) {
      this.robots.inEvent(event);
   }

   @Override
   public void onWin(WinEvent event) {
      this.robots.inEvent(event);
   }

   @Override
   public void onPaint(Graphics2D g) {
      DrawMenu.draw(g);

      DrawMenu.getValue("Hello", "Goodbye");
      DrawMenu.getValue("Hey", "Bye");
      DrawMenu.getValue("Sweet Dreams", "Night Time");
   }

   @Override
   public void onMouseClicked(MouseEvent e) {
      DrawMenu.inMouseEvent(e);
   }

}
