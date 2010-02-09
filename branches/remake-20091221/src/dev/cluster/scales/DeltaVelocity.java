package dev.cluster.scales;

import dev.cluster.Scale;
import dev.data.RobotData;
import dev.data.RobotDataAccesser;

public class DeltaVelocity extends Scale {

   @Override
   public double value(RobotData view, RobotData reference) {
      return new RobotDataAccesser(view).getDeltaVelocity();
   }

   @Override
   public String toString() {
      return "Delta Velocity";
   }
}
