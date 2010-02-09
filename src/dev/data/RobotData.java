package dev.data;

import java.io.PrintStream;
import java.io.Serializable;

import kid.info.RobotInfo;
import robocode.RobocodeFileOutputStream;
import robocode.Robot;
import robocode.ScannedRobotEvent;
import dev.Format;
import dev.Utils;
import dev.draw.DrawMenu;
import dev.draw.RobotColor;
import dev.draw.RobotGraphics;

public class RobotData implements Cloneable, Serializable {

   /**
    * Determines if a deserialized file is compatible with this class.<br>
    * <br>
    * Maintainers must change this value if and only if the new version of this class is not compatible with old
    * versions.
    */
   private static final long     serialVersionUID = -6829232010226649500L;


   /**
    * The weight that the original average cares when calculating the new average.
    */
   protected static final double AVERAGE_WEIGHT   = 5.0D;

   /**
    * The energy that a robot has when it is destroyed.
    */
   protected static final double DEAD_ENERGY      = -1.0D;



   /**
    * The name of the robot.
    */
   protected String              name;

   /**
    * The <code>x</code> coordinate of the robot.
    */
   protected double              x;

   /**
    * The <code>y</code> coordinate of the robot.
    */
   protected double              y;

   /**
    * The energy of the robot.
    */
   protected double              energy;

   /**
    * The difference of the robot's current energy and it's last known energy.
    */
   protected double              deltaEnergy;

   /**
    * The robot's heading in degrees.
    */
   protected double              heading;

   /**
    * The difference in degrees of the robot's current heading and it's last known heading.
    */
   protected double              deltaHeading;

   /**
    * The robot's average delta heading.
    * 
    * @see #deltaHeading
    */
   protected double              avgDeltaHeading;

   /**
    * The robot's speed in pixels per tick.
    */
   protected double              velocity;

   /**
    * The difference in speed of the robot's current velocity and it's last known velocity.
    */
   protected double              deltaVelocity;

   /**
    * The robot's average velocity.
    * 
    * @see #velocity
    */
   protected double              avgVelocity;

   /**
    * The robot's average delta velocity.
    * 
    * @see #avgDeltaVelocity
    */
   protected double              avgDeltaVelocity;

   /**
    * The last time at which the robot's information was updated.
    */
   protected long                time;



   public RobotData() {
      this.init(new String(), Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, DEAD_ENERGY, 0.0D, 0.0D, -1);
   }

   public RobotData(String n, double x, double y, double e, double h, double v, long t) {
      this.init(n, x, y, e, h, v, t);
   }

   public RobotData(ScannedRobotEvent e, Robot r) {
      double x = Utils.getX(r.getX(), e.getDistance(), Math.toRadians(r.getHeading() + e.getBearing()));
      double y = Utils.getY(r.getY(), e.getDistance(), Math.toRadians(r.getHeading() + e.getBearing()));
      this.init(e.getName(), x, y, e.getEnergy(), e.getHeadingRadians(), e.getVelocity(), e.getTime());
   }

   public RobotData(Robot r) {
      this.init(r.getName(), r.getX(), r.getY(), r.getEnergy(), Math.toRadians(r.getHeading()), r.getVelocity(), r.getTime());
   }

   protected RobotData(RobotData r) {
      this.init(r.name, r.x, r.y, r.energy, r.deltaEnergy, r.heading, r.deltaHeading, r.avgDeltaHeading, r.velocity,
            r.deltaVelocity, r.avgVelocity, r.avgDeltaVelocity, r.time);
   }

   protected void init(String n, double x, double y, double e, double h, double v, long t) {
      this.init(n, x, y, e, 0.0D, h, 0.0D, 0.0D, v, 0.0D, v, 0.0D, t);
   }

   protected void init(String n, double x, double y, double e, double dE, double h, double dH, double a_dH, double v,
         double dV, double a_V, double a_dV, long t) {
      this.name = n;
      this.x = x;
      this.y = y;
      this.energy = e;
      this.deltaEnergy = dE;
      this.heading = h;
      this.deltaHeading = dH;
      this.avgDeltaHeading = a_dH;
      this.velocity = v;
      this.deltaVelocity = dV;
      this.avgVelocity = a_V;
      this.avgDeltaVelocity = a_dV;
      this.time = t;
   }



   /**
    * Updates the <code>RobotData class</code> with the new given information. It assumes that the robot went straight
    * from its previous known position to the new position.<br>
    * <br>
    * This method should not be called directly but should be called through either
    * <code>{@link kid.robot.RobotData#update(ScannedRobotEvent, Robot) update(ScannedRobotEvent, Robot)}</code> or
    * <code>{@link kid.robot.RobotData#update(Robot) update(Robot)}</code>.
    * 
    * @param x
    *           - the <code>x</code> component of the robot's current position.
    * @param y
    *           - the <code>y</code> component of the robot's current position.
    * @param e
    *           - the robot's current energy.
    * @param h
    *           - the robot's current heading in degrees.
    * @param v
    *           - the robot's current velocity.
    * @param t
    *           - the time at which all these things were <code>true</code> of the robot.
    * @see #update(ScannedRobotEvent, Robot)
    * @see #update(Robot)
    */
   public void update(double x, double y, double e, double h, double v, long t) {
      long dT = (t - this.time);
      if (dT != 0) {
         this.x = x;
         this.y = y;
         this.energy = e;
         this.heading = h;
         this.velocity = v;
         this.time = t;
         if (dT < 0) {
            this.deltaEnergy = 0.0D;
            this.deltaHeading = 0.0D;
            this.avgDeltaHeading = 0.0D;
            this.deltaVelocity = 0.0D;
            this.avgDeltaVelocity = 0.0D;
            this.avgVelocity = 0.0D;
         } else if (dT > 0) {
            this.deltaEnergy = (e - this.energy);// / dT;
            this.deltaHeading = Utils.relative((h - this.heading));// / dT;
            this.avgDeltaHeading = Utils.avg(this.avgDeltaHeading, RobotData.AVERAGE_WEIGHT, this.deltaHeading, 1.0D);
            this.deltaVelocity = (v - this.velocity);// / dT;
            this.avgDeltaVelocity = Utils
                  .avg(this.avgDeltaVelocity, RobotData.AVERAGE_WEIGHT, this.deltaVelocity, 1.0D);
            this.avgVelocity = Utils.avg(this.avgVelocity, RobotData.AVERAGE_WEIGHT, this.velocity, 1.0D);
         }
      }
   }

   public void update(ScannedRobotEvent e, Robot r) {
      double curX = Utils.getX(r.getX(), e.getDistance(), Math.toRadians(r.getHeading() + e.getBearing()));
      double curY = Utils.getY(r.getY(), e.getDistance(), Math.toRadians(r.getHeading() + e.getBearing()));
      this.update(curX, curY, e.getEnergy(), e.getHeadingRadians(), e.getVelocity(), e.getTime());
   }

   public void update(Robot r) {
      this.update(r.getX(), r.getY(), r.getEnergy(), Math.toRadians(r.getHeading()), r.getVelocity(), r.getTime());
   }

   /**
    * Sets a robot as dead. Should be called as soon as a robot is destroyed.
    */
   public void setDeath() {
      this.energy = DEAD_ENERGY;
   }



   public void print(PrintStream console) {
      console.println(this.toString());
   }

   public void print(RobocodeFileOutputStream output) {
      PrintStream file = new PrintStream(output);
      file.println(this.getClass() + ": " + this.name + " at Time: " + this.time);
      file.println("Coordinates:            " + Format.corrdinateNoDec(this.x, this.y));
      file.println("Energy:                 " + this.energy);
      file.println("Heading:                " + this.heading);
      file.println("Delta Heading:          " + this.deltaHeading);
      file.println("Average Delta Heading:  " + this.avgDeltaHeading);
      file.println("Velocty:                " + this.velocity);
      file.println("Delta Velocity:         " + this.deltaVelocity);
      file.println("Average Delta Velocity: " + this.avgDeltaVelocity);
      file.println();
   }

   @Override
   public String toString() {
      return new String(this.getClass() + ": " + this.name + ": " + Format.corrdinateNoDec(this.x, this.y) + " "
            + this.energy + " " + this.heading + " " + this.velocity + " " + this.time);
   }

   public void draw(RobotGraphics grid) {
      if (this.energy == DEAD_ENERGY) {
         if (DrawMenu.getValue("Square", "Robot")) {
            grid.setColor(RobotColor.LIGHT_GRAY);
            grid.drawRectCenter(this.x, this.y, RobotInfo.WIDTH, RobotInfo.HEIGHT);
         }
      }
   }

   public RobotData copy() {
      return new RobotData(this);
   }

   @Override
   public Object clone() {
      return this.copy();
   }

   @Override
   public boolean equals(Object obj) {
      if (obj instanceof RobotData) {
         RobotData robot = (RobotData) obj;
         return robot.name.equals(this.name) && robot.time == this.time;
      }
      return super.equals(obj);
   }

}
