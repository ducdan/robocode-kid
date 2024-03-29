package kid.robot;

import java.awt.geom.Point2D;
import java.io.PrintStream;
import java.io.Serializable;

import kid.data.Drawable;
import kid.data.Printable;
import kid.graphics.Colors;
import kid.graphics.RGraphics;
import kid.utils.Utils;
import robocode.RobocodeFileOutputStream;

// TODO document class

public class RobotVector implements Cloneable, Serializable, Printable, Drawable {

   /**
    * Determines if a de-serialized file is compatible with this class.<BR>
    * <BR>
    * Maintainers must change this value if and only if the new version of this class is not compatible with old
    * versions.
    */
   private static final long serialVersionUID = 7415604949876623460L;

   private double x;
   private double y;
   private double deltaX;
   private double deltaY;
   private double heading;
   private double velocity;

   private boolean updatedDeltaX = true;
   private boolean updatedDeltaY = true;
   private boolean updatedHeading = true;
   private boolean updatedVelocity = true;

   public RobotVector() {
      init(-1.0D, -1.0D, 0.0D, 0.0D);
   }

   public RobotVector(final double heading, final double velocity) {
      init(-1.0D, -1.0D, heading, velocity);
   }

   public RobotVector(final double x, final double y, final double heading, final double velocity) {
      init(x, y, heading, velocity);
   }

   public RobotVector(final Point2D point, final double heading, final double velocity) {
      init(point.getX(), point.getY(), heading, velocity);
   }

   private RobotVector(final RobotVector vector) {
      init(vector.getX(), vector.getY(), vector.getHeading(), vector.getVelocity());
   }

   private void init(final double x, final double y, final double heading, final double velocity) {
      this.x = x;
      this.y = y;
      this.deltaX = Utils.getDeltaX(velocity, heading);
      this.deltaY = Utils.getDeltaY(velocity, heading);
      this.heading = heading;
      this.velocity = velocity;
   }

   public double getX() {
      return x;
   }

   public double getY() {
      return y;
   }

   public double getDeltaX() {
      if (!updatedDeltaX)
         deltaX = Utils.getDeltaX(getVelocity(), getHeading());
      updatedDeltaX = true;
      return deltaX;
   }

   public double getDeltaY() {
      if (!updatedDeltaY)
         deltaY = Utils.getDeltaY(getVelocity(), getHeading());
      updatedDeltaY = true;
      return deltaY;
   }

   public double getHeading() {
      if (!updatedHeading)
         heading = Utils.angle(0.0D, 0.0D, getDeltaX(), getDeltaY());
      updatedHeading = true;
      return heading;
   }

   public double getVelocity() {
      if (!updatedVelocity)
         velocity = Utils.dist(0.0D, 0.0D, getDeltaX(), getDeltaY());
      updatedVelocity = true;
      return velocity;
   }

   public void add(final RobotVector vector) {
      deltaX += vector.getDeltaX();
      deltaY += vector.getDeltaY();
      updatedHeading = false;
      updatedVelocity = false;
   }

   public void rotate(final double turn) {
      this.heading = getHeading() + turn;
      updatedDeltaX = false;
      updatedDeltaY = false;
   }

   public void velocity(final double newVelocity) {
      this.velocity = newVelocity;
      updatedDeltaX = false;
      updatedDeltaY = false;
   }

   public static void add(final Point2D point, final RobotVector vector) {
      point.setLocation(point.getX() + vector.getDeltaX(), point.getY() + vector.getDeltaY());
   }

   public RobotVector startNew() {
      return new RobotVector(getX() + getDeltaX(), getY() + getDeltaY(), getHeading(), 0.0D);
   }

   public void print(final PrintStream console) {
      // TODO Method Stub
   }

   public void print(final RobocodeFileOutputStream output) {
      // TODO Method Stub
   }

   public void draw(final RGraphics grid) {
      if (getX() > 0.0D && getY() > 0.0D) {
         grid.setColor(Colors.DIRT_GREEN);
         grid.drawLine(getX(), getY(), getX() + getDeltaX(), getY() + getDeltaY());
      }
   }

   @Override
   public Object clone() {
      return new RobotVector(this);
   }

   @Override
   public boolean equals(final Object obj) {
      if (obj instanceof RobotVector) {
         RobotVector vector = (RobotVector) obj;
         return (vector.getDeltaX() == getDeltaX()) && (vector.getDeltaY() == getDeltaY()) && (vector.getX() == getX()) && (vector.getY() == getY());
      }
      return false;
   }

   @Override
   public String toString() {
      // TODO Method Stub
      return new String();
   }

   @Override
   public void finalize() {
      // TODO Method Stub
   }

}
