package dev.cluster.scales;

import java.awt.geom.Rectangle2D;

import dev.Trig;
import dev.cluster.Scale;
import dev.data.RobotData;
import dev.data.RobotDataAccesser;

public class WallDanger extends Scale {

   private Rectangle2D battleFeild;

   public WallDanger(Rectangle2D battleFeild) {
      this.battleFeild = battleFeild;
   }

   @Override
   public double value(RobotData view, RobotData reference) {
      double danger = Double.POSITIVE_INFINITY;
      if (this.battleFeild != null) {
         RobotDataAccesser viewAccess = new RobotDataAccesser(view);
         double min = Math.min(Math.min(viewAccess.getX(), viewAccess.getY()), Math.min(this.battleFeild.getMaxX()
               - viewAccess.getX(), this.battleFeild.getMaxY() - viewAccess.getY()));
         if (min == viewAccess.getX()) {
            danger = Math.abs(min / Trig.acos(viewAccess.getHeading() - 90));
         } else if (min == viewAccess.getY()) {
            danger = Math.abs(min / Trig.acos(viewAccess.getHeading()));
         } else if (min == this.battleFeild.getMaxX() - viewAccess.getX()) {
            danger = Math.abs(min / Trig.acos(viewAccess.getHeading() - 90));
         } else {
            danger = Math.abs(min / Trig.acos(viewAccess.getHeading()));
         }
      }
      return danger;
   }

   @Override
   public String toString() {
      return "Wall Danger";
   }
}
