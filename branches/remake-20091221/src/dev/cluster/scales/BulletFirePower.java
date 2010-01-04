package dev.cluster.scales;

import dev.cluster.Scale;
import dev.data.RobotData;

public class BulletFirePower extends Scale {

   @Override
   public double value(RobotData view, RobotData reference) {
      return Math.abs(reference.getDeltaEnergy());
   }

   public String toString() {
      return "Bullet Fire Power";
   }
}
