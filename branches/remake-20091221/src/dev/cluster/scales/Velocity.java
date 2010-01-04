package dev.cluster.scales;

import dev.cluster.Scale;
import dev.data.RobotData;

public class Velocity extends Scale {

   @Override
   public double value(RobotData view, RobotData reference) {
      return Math.abs(view.getVelocity());
   }

   public String toString() {
      return "Velocity";
   }

}
