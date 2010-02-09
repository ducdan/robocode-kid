package dev.data;

import java.awt.geom.Rectangle2D;

import kid.info.RobotInfo;

public class RobotDataAccesser {

   /**
    * Determines if a deserialized file is compatible with this class.<br>
    * <br>
    * Maintainers must change this value if and only if the new version of this class is not compatible with old
    * versions.
    */
   private static final long serialVersionUID = 7923723819188026501L;


   private RobotData         robot;


   public RobotDataAccesser(RobotData robot) {
      this.robot = robot;
   }


   /**
    * Returns the name of the robot.
    * 
    * @return the robot's name.
    */
   public String getName() {
      return this.robot.name;
   }

   /**
    * Returns the x value of the robot's current coordinate.
    * 
    * @return the robot's x coordinate.
    */
   public double getX() {
      return this.robot.x;
   }

   /**
    * Returns the y value of the robot's current coordinate.
    * 
    * @return the robot's y coordinate.
    */
   public double getY() {
      return this.robot.y;
   }

   /**
    * Returns a <code>Rectangle2D</code> that is the height, width and at the current position of the robot.
    * 
    * @return the robot's <code>Rectangle</code>.
    */
   public Rectangle2D getRectangle() {
      return new Rectangle2D.Double(this.getX() - (RobotInfo.WIDTH / 2.0D), this.getY() - (RobotInfo.HEIGHT / 2.0D),
            RobotInfo.WIDTH, RobotInfo.HEIGHT);
   }

   /**
    * Returns the robot's current energy.
    * 
    * @return the robot's energy.
    */
   public double getEnergy() {
      return this.robot.energy;
   }

   /**
    * Returns the robot's delta energy.
    * 
    * @return the robot's delta energy.
    */
   public double getDeltaEnergy() {
      return this.robot.deltaEnergy;
   }

   /**
    * Returns if the robot is dead or not.
    * 
    * @return if the robot is dead or not.
    */
   public boolean isDead() {
      return (this.getEnergy() == RobotData.DEAD_ENERGY);
   }

   /**
    * Returns the robot's current heading.
    * 
    * @return the robot's heading.
    */
   public double getHeading() {
      return this.robot.heading;
   }

   /**
    * Returns the robot's delta heading.
    * 
    * @return the robot's delta heading.
    * @see #deltaHeading
    */
   public double getDeltaHeading() {
      return this.robot.deltaHeading;
   }

   /**
    * Returns the robot's average delta heading.
    * 
    * @return the robot's average delta heading.
    * @see #avgDeltaHeading
    */
   public double getAvgDeltaHeading() {
      return this.robot.avgDeltaHeading;
   }

   /**
    * Returns the robot's current velocity.
    * 
    * @return the robot's velocity.
    */
   public double getVelocity() {
      return this.robot.velocity;
   }

   /**
    * Returns the robot's delta velocity.
    * 
    * @return the robot's delta velocity.
    * @see #deltaVelocity
    */
   public double getDeltaVelocity() {
      return this.robot.deltaVelocity;
   }

   /**
    * Returns the robot's average velocity.
    * 
    * @return the robot's average velocity
    * @see #avgVelocity
    */
   public double getAvgVelocity() {
      return this.robot.avgVelocity;
   }

   /**
    * Returns the robot's average delta velocity.
    * 
    * @return the robot's average delta velocity.
    * @see #avgDeltaVelocity
    */
   public double getAvgDeltaVelocity() {
      return this.robot.avgDeltaVelocity;
   }

   /**
    * Returns the time at which the robot was scanned.
    * 
    * @return the time at which the robot was scanned.
    */
   public long getTime() {
      return this.robot.time;
   }

}
