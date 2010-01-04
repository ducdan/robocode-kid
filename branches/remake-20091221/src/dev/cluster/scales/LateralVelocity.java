package dev.cluster.scales;

import dev.Trig;
import dev.Utils;
import dev.cluster.Scale;
import dev.data.RobotData;

public class LateralVelocity extends Scale {

   @Override
   public double value(RobotData view, RobotData reference) {
      return Math.abs(view.getVelocity()
            * Trig.sin(view.getHeading() - Utils.angle(reference.getX(), reference.getY(), view.getX(), view.getY())));
   }

   public String toString() {
      return "Lateral Velocity";
   }
}
