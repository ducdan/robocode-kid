package dev.cluster.scales;

import java.awt.geom.Rectangle2D;

import dev.Utils;
import dev.cluster.Scale;
import dev.data.RobotData;

public class WallDistance extends Scale {

   private Rectangle2D battleFeild;

   public WallDistance(Rectangle2D battleFeild) {
      this.battleFeild = battleFeild;
   }

   @Override
   public double value(RobotData view, RobotData reference) {
      return Utils.min(view.getX(), view.getY(), battleFeild.getMaxX() - view.getX(), battleFeild.getMaxY()
            - view.getY());
   }

   @Override
   public String toString() {
      return "Wall Distance";
   }

}
