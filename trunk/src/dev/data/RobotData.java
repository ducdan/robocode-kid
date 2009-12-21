package dev.data;

import java.awt.geom.Rectangle2D;
import java.io.PrintStream;
import java.io.Serializable;

import kid.data.Drawable;
import kid.data.Printable;
import kid.graphics.Colors;
import kid.graphics.DrawMenu;
import kid.graphics.RGraphics;
import kid.info.RobotInfo;
import robocode.RobocodeFileOutputStream;
import robocode.Robot;
import robocode.ScannedRobotEvent;
import dev.Utils;

public class RobotData implements Cloneable, Serializable, Printable, Drawable {

   /**
    * Determines if a deserialized file is compatible with this class.<br>
    * <br>
    * Maintainers must change this value if and only if the new version of this class is not
    * compatible with old versions.
    */
   private static final long serialVersionUID = -6829232010226649500L;


   /**
    * The weight that the original average cares when calculating the new average.
    */
   protected static final double AVERAGE_WEIGHT = 5.0D;

   /**
    * The energy that a robot has when it is destroyed.
    */
   protected static final double DEAD_ENERGY = -1.0D;



   /**
    * The name of the robot.
    */
   protected String name;

   /**
    * The <code>x</code> coordinate of the robot.
    */
   protected double x;

   /**
    * The <code>y</code> coordinate of the robot.
    */
   protected double y;

   /**
    * The energy of the robot.
    */
   protected double energy;

   /**
    * The difference of the robot's current energy and it's last known energy.
    */
   protected double deltaEnergy;

   /**
    * The robot's heading in degrees.
    */
   protected double heading;

   /**
    * The difference in degrees of the robot's current heading and it's last known heading.
    */
   protected double deltaHeading;

   /**
    * The robot's average delta heading.
    * 
    * @see #deltaHeading
    */
   protected double avgDeltaHeading;

   /**
    * The robot's speed in pixels per tick.
    */
   protected double velocity;

   /**
    * The difference in speed of the robot's current velocity and it's last known velocity.
    */
   protected double deltaVelocity;

   /**
    * The robot's average velocity.
    * 
    * @see #velocity
    */
   protected double avgVelocity;

   /**
    * The robot's average delta velocity.
    * 
    * @see #avgDeltaVelocity
    */
   protected double avgDeltaVelocity;

   /**
    * The last time at which the robot's information was updated.
    */
   protected long time;



   public RobotData() {
      init(new String(), Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, DEAD_ENERGY, 0.0D, 0.0D, -1);
   }

   public RobotData(String n, double x, double y, double e, double h, double v, long t) {
      init(n, x, y, e, h, v, t);
   }

   public RobotData(ScannedRobotEvent e, Robot r) {
      double x = Utils.getX(r.getX(), e.getDistance(), Math.toRadians(r.getHeading() + e.getBearing()));
      double y = Utils.getY(r.getY(), e.getDistance(), Math.toRadians(r.getHeading() + e.getBearing()));
      init(e.getName(), x, y, e.getEnergy(), e.getHeadingRadians(), e.getVelocity(), e.getTime());
   }

   public RobotData(Robot r) {
      init(r.getName(), r.getX(), r.getY(), r.getEnergy(), Math.toRadians(r.getHeading()), r.getVelocity(), r.getTime());
   }

   public RobotData(RobotData r) {
      init(r.getName(), r.getX(), r.getY(), r.getEnergy(), r.getDeltaEnergy(), r.getHeading(), r.getDeltaHeading(), r
            .getAvgDeltaHeading(), r.getVelocity(), r.getDeltaVelocity(), r.getAvgVelocity(), r.getAvgDeltaVelocity(),
            r.getTime());
   }

   protected void init(String n, double x, double y, double e, double h, double v, long t) {
      init(n, x, y, e, 0.0D, h, 0.0D, 0.0D, v, 0.0D, v, 0.0D, t);
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
    * Updates the <code>RobotData class</code> with the new given information. It assumes that the
    * robot went straight from its previous known position to the new position.<br>
    * <br>
    * This method should not be called directly but should be called through either
    * <code>{@link kid.robot.RobotData#update(ScannedRobotEvent, Robot) update(ScannedRobotEvent, Robot)}</code>
    * or <code>{@link kid.robot.RobotData#update(Robot) update(Robot)}</code>.
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
      update(curX, curY, e.getEnergy(), e.getHeadingRadians(), e.getVelocity(), e.getTime());
   }

   public void update(Robot r) {
      update(r.getX(), r.getY(), r.getEnergy(), Math.toRadians(r.getHeading()), r.getVelocity(), r.getTime());
   }



   /**
    * Returns the name of the robot.
    * 
    * @return the robot's name.
    */
   public String getName() {
      return this.name;
   }

   /**
    * Returns the x value of the robot's current coordinate.
    * 
    * @return the robot's x coordinate.
    */
   public double getX() {
      return this.x;
   }

   /**
    * Returns the y value of the robot's current coordinate.
    * 
    * @return the robot's y coordinate.
    */
   public double getY() {
      return this.y;
   }

   /**
    * Returns a <code>Rectangle2D</code> that is the height, width and at the current position of
    * the robot.
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
      return this.energy;
   }

   /**
    * Returns the robot's delta energy.
    * 
    * @return the robot's delta energy.
    */
   public double getDeltaEnergy() {
      return this.deltaEnergy;
   }

   /**
    * Sets a robot as dead. Should be called as soon as a robot is destroyed.
    */
   public void setDeath() {
      this.energy = DEAD_ENERGY;
   }

   /**
    * Returns if the robot is dead or not.
    * 
    * @return if the robot is dead or not.
    */
   public boolean isDead() {
      return (this.getEnergy() == DEAD_ENERGY);
   }

   /**
    * Returns the robot's current heading.
    * 
    * @return the robot's heading.
    */
   public double getHeading() {
      return this.heading;
   }

   /**
    * Returns the robot's delta heading.
    * 
    * @return the robot's delta heading.
    * @see #deltaHeading
    */
   public double getDeltaHeading() {
      return this.deltaHeading;
   }

   /**
    * Returns the robot's average delta heading.
    * 
    * @return the robot's average delta heading.
    * @see #avgDeltaHeading
    */
   public double getAvgDeltaHeading() {
      return this.avgDeltaHeading;
   }

   /**
    * Returns the robot's current velocity.
    * 
    * @return the robot's velocity.
    */
   public double getVelocity() {
      return this.velocity;
   }

   /**
    * Returns the robot's delta velocity.
    * 
    * @return the robot's delta velocity.
    * @see #deltaVelocity
    */
   public double getDeltaVelocity() {
      return this.deltaVelocity;
   }

   /**
    * Returns the robot's average velocity.
    * 
    * @return the robot's average velocity
    * @see #avgVelocity
    */
   public double getAvgVelocity() {
      return this.avgVelocity;
   }

   /**
    * Returns the robot's average delta velocity.
    * 
    * @return the robot's average delta velocity.
    * @see #avgDeltaVelocity
    */
   public double getAvgDeltaVelocity() {
      return this.avgDeltaVelocity;
   }

   /**
    * Returns the time at which the robot was scanned.
    * 
    * @return the time at which the robot was scanned.
    */
   public long getTime() {
      return this.time;
   }


   @Override
   public void print(PrintStream console) {
      console.println(this.toString());
   }

   @Override
   public void print(RobocodeFileOutputStream output) {
      PrintStream file = new PrintStream(output);
      file.println(getClass() + ": " + this.getName() + " at Time: " + this.getTime());
      file.println("Coordinates:           (" + Math.round(this.getX()) + ", " + Math.round(this.getY()) + ")");
      file.println("Energy:                 " + this.getEnergy());
      file.println("Heading:                " + this.getHeading());
      file.println("Delta Heading:          " + this.getDeltaHeading());
      file.println("Average Delta Heading:  " + this.getAvgDeltaHeading());
      file.println("Velocty:                " + this.getVelocity());
      file.println("Delta Velocity:         " + this.getDeltaVelocity());
      file.println("Average Delta Velocity: " + this.getAvgDeltaVelocity());
      file.println();
   }

   @Override
   public String toString() {
      return new String(getClass() + ": " + this.getName() + ": (" + this.getX() + ", " + this.getY() + ") "
            + this.getEnergy() + " " + this.getHeading() + " " + this.getVelocity() + " " + this.getTime());
   }

   @Override
   public void draw(RGraphics grid) {
      if (!this.isDead()) {
         if (DrawMenu.getValue("Square", "Robot")) {
            grid.setColor(Colors.LIGHT_GRAY);
            grid.draw(this.getRectangle());
         }
      }
   }

   public RobotData copy() {
      return new RobotData(this);
   }

   @Override
   public Object clone() {
      return copy();
   }

   @Override
   public boolean equals(Object obj) {
      if (obj instanceof RobotData) {
         RobotData robot = (RobotData) obj;
         return robot.getName().equals(this.getName()) && robot.getTime() == this.getTime();
      }
      return super.equals(obj);
   }

}
