package dev.cluster.scales;

import dev.cluster.Scale;
import dev.data.RobotData;
import dev.data.RobotDataAccesser;

public class BulletFirePower extends Scale {

   @Override
   public double value(RobotData view, RobotData reference) {
      return Math.abs(new RobotDataAccesser(reference).getDeltaEnergy());
   }

   @Override
   public String toString() {
      return "Bullet Fire Power";
   }
}
