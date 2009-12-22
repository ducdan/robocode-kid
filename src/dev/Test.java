package dev;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

import robocode.AdvancedRobot;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.Rules;
import robocode.ScannedRobotEvent;
import dev.move.PsudoRobot;

public class Test extends AdvancedRobot {
   private PsudoRobot psudo;

   boolean movingForward;

   @Override
   public void run() {
      rr2d = new RoundRectangle2D.Double(18.0, 18.0, getBattleFieldWidth() - 36.0, getBattleFieldHeight() - 36.0,
            radius, radius);

      psudo = new PsudoRobot(this);

      while (true) {
         setAhead(40000);
         psudo.setAhead(40000);
         movingForward = true;
         setTurnRight(90);
         psudo.setTurnRight(Math.toRadians(90));

         // psudo.compare(this);

         while (getTurnRemaining() != 0.0) {
            psudo.tick();
            execute();
            // psudo.compare(this);
         }

         setTurnLeft(180);
         psudo.setTurnRight(Math.toRadians(-180));
         // ... and wait for the turn to finish ...
         while (getTurnRemaining() != 0.0) {
            psudo.tick();
            execute();
            // psudo.compare(this);
         }

         setTurnRight(180);
         psudo.setTurnRight(Math.toRadians(180));
         // .. and wait for that turn to finish.
         while (getTurnRemaining() != 0.0) {
            psudo.tick();
            execute();
            // psudo.compare(this);
         }
      }
   }

   public void reverseDirection() {
      if (movingForward) {
         setBack(40000);
         psudo.setAhead(-40000);
         movingForward = false;
      } else {
         setAhead(40000);
         psudo.setAhead(40000);
         movingForward = true;
      }
   }

   double radius = Rules.MAX_VELOCITY / Math.sin(Rules.getTurnRateRadians(Rules.MAX_VELOCITY));
   RoundRectangle2D rr2d;

   @Override
   public void onPaint(Graphics2D g) {
      super.onPaint(g);
      g.setColor(Color.BLUE);

      g.draw(rr2d);

      PsudoRobot r = new PsudoRobot(this);
      for (int i = 0; i < 20; i++) {
         r.tick();
         g.fillOval((int) r.getX(), (int) r.getY(), 2, 2);
      }
   }

   @Override
   public void onHitWall(HitWallEvent e) {
      reverseDirection();
   }

   @Override
   public void onScannedRobot(ScannedRobotEvent e) {
      fire(1);
   }

   @Override
   public void onHitRobot(HitRobotEvent e) {
      if (e.isMyFault()) {
         reverseDirection();
      }
   }
}
