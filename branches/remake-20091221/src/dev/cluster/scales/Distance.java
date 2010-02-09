package dev.cluster.scales;

import dev.Utils;
import dev.cluster.Scale;
import dev.data.RobotData;
import dev.data.RobotDataAccesser;

public class Distance extends Scale {

   @Override
   public double value(RobotData view, RobotData reference) {
      RobotDataAccesser viewAccess = new RobotDataAccesser(view);
      RobotDataAccesser referenceAccess = new RobotDataAccesser(reference);
      return Utils.dist(referenceAccess.getX(), referenceAccess.getY(), viewAccess.getX(), viewAccess.getY());
   }

   @Override
   public String toString() {
      return "Distance";
   }
}
