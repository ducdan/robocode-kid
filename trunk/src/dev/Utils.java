package dev;

import java.awt.geom.Point2D;

public final class Utils {

   public static final double NEAR_DELTA = 0.000001;


   private Utils() {
   }



   public static final double absolute(double n) {
      if (!Double.isInfinite(n) && !Double.isNaN(n)) {
         n %= Trig.CIRCLE;
         if (n < 0.0)
            n += Trig.CIRCLE;
      }
      return n;
   }

   public static final double relative(double n) {
      if (!Double.isInfinite(n) && !Double.isNaN(n)) {
         n %= Trig.CIRCLE;
         if (n <= -Trig.HALF_CIRCLE)
            n += Trig.CIRCLE;
         else if (n > Trig.HALF_CIRCLE)
            n -= Trig.CIRCLE;
      }
      return n;
   }

   public static final double getX(double x, double d, double a) {
      return x + d * Trig.t_sin(a);
   }

   public static final double getY(double y, double d, double a) {
      return y + d * Trig.t_cos(a);
   }

   public static final Point2D project(Point2D p, double d, double a) {
      return new Point2D.Double(getX(p.getX(), d, a), getY(p.getY(), d, a));
   }

   public static final double avg(double n1, double w1, double n2, double w2) {
      return (n1 * w1 + n2 * w2) / (w1 + w2);
   }


   public static final double round(double n) {
      return ((int) (n + 0.5));
   }

   public static final double floor(double n) {
      return ((int) n);
   }

   public static final double ceil(double n) {
      return ((int) (n + 1.0));
   }

   public static final double sign(double n) {
      return (n < 0.0 ? -1.0 : 1.0);
   }

   public static final double signum(double n) {
      return (n == 0.0 ? 0.0 : (n > 0.0 ? 1.0 : -1.0));
   }

   public static final double abs(double n) {
      return (n < 0 ? -n : n);
   }

   public static final double sqr(double n) {
      return n * n;
   }

   public static final double cbr(double n) {
      return n * n * n;
   }

   public static final double qur(double n) {
      return n * n * n * n;
   }

   public static final double max(double a, double b) {
      return (a > b ? a : b);
   }

   public static final double min(double a, double b) {
      return (a < b ? a : b);
   }

   public static final double max(double a, double b, double c) {
      return max(c, max(a, b));
   }

   public static final double min(double a, double b, double c) {
      return min(c, min(a, b));
   }

   public static final double max(double a, double b, double c, double d) {
      return max(max(c, d), max(a, b));
   }

   public static final double min(double a, double b, double c, double d) {
      return min(min(c, d), min(a, b));
   }

   public static final double limit(double low, double n, double high) {
      return max(low, min(n, high));
   }

   public static final double round(double value, double nearest) {
      return round(value / nearest) * nearest;
   }

   public static final double getAngle(Point2D start, Point2D finish) {
      return Trig.atan2(finish.getY() - start.getX(), finish.getY() - start.getY());
   }

   public static final boolean inRange(double low, double n, double high) {
      return low <= n && n <= high;
   }

   public static final boolean isNear(double n1, double n2) {
      return (abs(n1 - n2) < NEAR_DELTA);
   }

}
