package dev.cluster.scales;

import dev.cluster.Scale;
import dev.data.RobotData;
import dev.data.RobotDataAccesser;

public class Velocity extends Scale {

   @Override
   public double value(RobotData view, RobotData reference) {
      return Math.abs(new RobotDataAccesser(view).getVelocity());
   }

   @Override
   public String toString() {
      return "Velocity";
   }

}
