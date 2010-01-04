package dev.cluster.scales;

import dev.cluster.Scale;
import dev.data.RobotData;

public class DeltaHeading extends Scale {

   @Override
   public double value(RobotData view, RobotData reference) {
      return view.getDeltaHeading();
   }

   public String toString() {
      return "Delta Heading";
   }
}
