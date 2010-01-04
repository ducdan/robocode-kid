package dev.cluster.scales;

import dev.cluster.Scale;
import dev.data.RobotData;

public class DeltaVelocity extends Scale {

   @Override
   public double value(RobotData view, RobotData reference) {
      return view.getDeltaVelocity();
   }

   public String toString() {
      return "Delta Velocity";
   }
}
