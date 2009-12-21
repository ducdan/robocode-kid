package dev.move;

import static dev.Format.twoDec;
import robocode.AdvancedRobot;
import robocode.Robot;
import robocode.Rules;
import dev.Trig;
import dev.Utils;

public class PsudoRobot {

   private double x;
   private double y;
   private double heading;
   private double velocity;
   private long time;
   private double forward;
   private double right;
   private double bfw;
   private double bfh;


   public PsudoRobot(double x, double y, double h, double v, long t, double bfw, double bfh) {
      this.x = x;
      this.y = y;
      this.heading = h;
      this.velocity = v;
      this.time = t;

      this.bfw = bfw;
      this.bfh = bfh;

      this.forward = 0.0;
      this.right = 0.0;
   }

   public PsudoRobot(Robot robot) {
      this(robot.getX(), robot.getY(), Math.toRadians(robot.getHeading()), robot.getVelocity(), robot.getTime(), robot
            .getBattleFieldWidth(), robot.getBattleFieldHeight());
      if (robot instanceof AdvancedRobot) {
         AdvancedRobot ar = (AdvancedRobot) robot;
         forward = ar.getDistanceRemaining();
         right = ar.getTurnRemainingRadians();
      }
   }

   public PsudoRobot(PsudoRobot robot) {
      this(robot.x, robot.y, robot.heading, robot.velocity, robot.time, robot.bfw, robot.bfh);
      this.forward = robot.forward;
      this.right = robot.right;
   }

   public PsudoRobot() {
      this(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, 0.0, 0.0, Long.MIN_VALUE, 0.0, 0.0);
   }


   public void setTurnRight(double radians) {
      this.right = radians;
   }

   public void setAhead(double distance) {
      this.forward = distance;
   }

   public void tick() {
      // TODO code: handle when the robot comes to a stop by finishing movement
      double turn = Utils.sign(right) * Math.min(Rules.getTurnRateRadians(Math.abs(velocity)), Math.abs(right));
      heading += turn;
      right -= turn;

      if (velocity * forward < 0.0) {
         velocity = Utils.sign(velocity) * (Math.abs(velocity) - Rules.DECELERATION);
         forward -= velocity;
      } else if (velocity == 0.0) {
         velocity = Utils.sign(forward) * Math.min(Rules.MAX_VELOCITY, Math.abs(velocity) + Rules.ACCELERATION);
         forward -= velocity;
      } else {
         velocity = Utils.sign(velocity) * Math.min(Rules.MAX_VELOCITY, Math.abs(velocity) + Rules.ACCELERATION);
         forward -= velocity;
      }

      double dx = velocity * Trig.t_sin(heading);
      double dy = velocity * Trig.t_cos(heading);

      x += dx;
      y += dy;

      double nx = Utils.limit(18.0, x, bfw - 18.0);
      double ny = Utils.limit(18.0, y, bfh - 18.0);

      if (x != nx) {
         velocity = 0.0;
         y -= dy * (x - nx) / dx;
         x = nx;
      } else if (y != ny) {
         velocity = 0.0;
         x -= dx * (y - ny) / dy;
         y = ny;
      }

      time++;
   }

   public void compare(Robot robot) {
      robot.out.println("PSUDO: (" + twoDec(x) + ", " + twoDec(y) + ") H:"
            + twoDec(Math.toDegrees(Utils.absolute(heading))) + " V:" + twoDec(velocity) + " F:" + twoDec(forward)
            + " T:" + twoDec(right) + " TIME:" + time);
      robot.out.print("ROBOT: (" + twoDec(robot.getX()) + ", " + twoDec(robot.getY()) + ") H:"
            + twoDec(robot.getHeading()) + " V:" + twoDec(robot.getVelocity()));
      if (robot instanceof AdvancedRobot) {
         AdvancedRobot ar = (AdvancedRobot) robot;
         robot.out.print(" F:" + twoDec(ar.getDistanceRemaining()) + " T:" + twoDec(ar.getTurnRemainingRadians()));
      }
      robot.out.println(" TIME:" + robot.getTime());
   }


   public double getX() {
      return x;
   }

   public double getY() {
      return y;
   }

   public double getHeading() {
      return heading;
   }

   public double getVelocity() {
      return velocity;
   }

   public long getTime() {
      return time;
   }

   public double getMovementRemaining() {
      return forward;
   }

   public double getTurnRemaining() {
      return right;
   }

   public PsudoRobot copy() {
      return new PsudoRobot(this);
   }

}
