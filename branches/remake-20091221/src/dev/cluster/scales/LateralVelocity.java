package dev.cluster.scales;

import dev.Trig;
import dev.Utils;
import dev.cluster.Scale;
import dev.data.RobotData;
import dev.data.RobotDataAccesser;

public class LateralVelocity extends Scale {

   @Override
   public double value(RobotData view, RobotData reference) {
      RobotDataAccesser viewAccess = new RobotDataAccesser(view);
      RobotDataAccesser referenceAccess = new RobotDataAccesser(reference);
      return Math.abs(viewAccess.getVelocity()
            * Trig.sin(viewAccess.getHeading()
                  - Utils.angle(referenceAccess.getX(), referenceAccess.getY(), viewAccess.getX(), viewAccess.getY())));
   }

   @Override
   public String toString() {
      return "Lateral Velocity";
   }
}
