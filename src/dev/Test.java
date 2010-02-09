package dev;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;
import dev.cluster.Scale;
import dev.cluster.Space;
import dev.cluster.scales.Distance;
import dev.cluster.scales.Velocity;
import dev.data.RobotData;
import dev.draw.DrawMenu;
import dev.manage.RobotManager;

public class Test extends AdvancedRobot {

   private RobotManager robots;
   private Space        space = new Space(new Scale[] { new Distance(), new Velocity() });

   @Override
   public void run() {
      this.robots = RobotManager.getInstance(this);

      setTurnRadarRightRadians(Double.POSITIVE_INFINITY);

      do {
         this.robots.inEvents(this.getAllEvents());
         for (ScannedRobotEvent sre : this.getScannedRobotEvents())
            handleScannedRobot(sre);
         this.execute();
      } while (true);
   }

   public void handleScannedRobot(ScannedRobotEvent event) {
      if (setFireBullet(2.0) != null) {
         space.add(robots.getRobot(event.getName()), new RobotData(this));
      }
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
