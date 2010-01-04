package dev.cluster.scales;

import java.awt.geom.Rectangle2D;

import dev.Trig;
import dev.cluster.Scale;
import dev.data.RobotData;

public class WallDanger extends Scale {

   private Rectangle2D battleFeild;

   public WallDanger(Rectangle2D battleFeild) {
      this.battleFeild = battleFeild;
   }

   @Override
   public double value(RobotData view, RobotData reference) {
      double danger = Double.POSITIVE_INFINITY;
      if (battleFeild != null) {
         double min = Math.min(Math.min(view.getX(), view.getY()), Math.min(battleFeild.getMaxX() - view.getX(),
               battleFeild.getMaxY() - view.getY()));
         if (min == view.getX()) {
            danger = Math.abs(min / Trig.acos(view.getHeading() - 90));
         } else if (min == view.getY()) {
            danger = Math.abs(min / Trig.acos(view.getHeading()));
         } else if (min == battleFeild.getMaxX() - view.getX()) {
            danger = Math.abs(min / Trig.acos(view.getHeading() - 90));
         } else {
            danger = Math.abs(min / Trig.acos(view.getHeading()));
         }
      }
      return danger;
   }

   @Override
   public String toString() {
      return "Wall Danger";
   }
}
