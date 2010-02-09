package dev.cluster.scales;

import dev.cluster.Scale;
import dev.data.RobotData;
import dev.data.RobotDataAccesser;

public class DeltaHeading extends Scale {

   @Override
   public double value(RobotData view, RobotData reference) {
      return new RobotDataAccesser(view).getDeltaHeading();
   }

   @Override
   public String toString() {
      return "Delta Heading";
   }
}
