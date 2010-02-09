package dev.cluster.scales;

import java.awt.geom.Rectangle2D;

import dev.Utils;
import dev.cluster.Scale;
import dev.data.RobotData;
import dev.data.RobotDataAccesser;

public class WallDistance extends Scale {

   private Rectangle2D battleFeild;

   public WallDistance(Rectangle2D battleFeild) {
      this.battleFeild = battleFeild;
   }

   @Override
   public double value(RobotData view, RobotData reference) {
     RobotDataAccesser viewAccess = new RobotDataAccesser(view);
      return Utils.min(viewAccess.getX(), viewAccess.getY(), this.battleFeild.getMaxX() - viewAccess.getX(), this.battleFeild.getMaxY()
            - viewAccess.getY());
   }

   @Override
   public String toString() {
      return "Wall Distance";
   }

}
