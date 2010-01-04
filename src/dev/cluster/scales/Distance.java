package dev.cluster.scales;

import dev.Utils;
import dev.cluster.Scale;
import dev.data.RobotData;

public class Distance extends Scale {

   @Override
   public double value(RobotData view, RobotData reference) {
      return Utils.dist(reference.getX(), reference.getY(), view.getX(), view.getY());
   }

   public String toString() {
      return "Distance";
   }
}
