package dev.utils;

import java.awt.geom.Point2D;

public final class Utils {

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
      return x + d * Trig.sin(a);
   }

   public static final double getY(double y, double d, double a) {
      return y + d * Trig.cos(a);
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

   public static final int sign(double n) {
      return (n < 0.0 ? -1 : 1);
   }

   public static final int signum(double n) {
      return (n == 0.0 ? 0 : (n > 0.0 ? 1 : -1));
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

   public static final double absMax(double a, double b) {
      return (abs(a) < abs(b) ? b : a);
   }

   public static final double min(double a, double b) {
      return (a < b ? a : b);
   }

   public static final double absMin(double a, double b) {
      return (abs(a) > abs(b) ? b : a);
   }

   public static final double max(double a, double b, double c) {
      return max(c, max(a, b));
   }

   public static final double absMax(double a, double b, double c) {
      return absMax(a, absMax(b, c));
   }

   public static final double min(double a, double b, double c) {
      return min(c, min(a, b));
   }

   public static final double absMin(double a, double b, double c) {
      return absMin(a, absMin(b, c));
   }

   public static final double max(double a, double b, double c, double d) {
      return max(max(c, d), max(a, b));
   }

   public static final double absMax(double a, double b, double c, double d) {
      return absMax(absMax(a, b), absMax(c, d));
   }

   public static final double min(double a, double b, double c, double d) {
      return min(min(c, d), min(a, b));
   }

   public static final double absMin(double a, double b, double c, double d) {
      return absMin(absMin(a, b), absMin(c, d));
   }

   public static final double limit(double low, double n, double high) {
      return max(low, min(n, high));
   }

   public static final double round(double value, double nearest) {
      return round(value / nearest) * nearest;
   }

   public static final double angle(Point2D start, Point2D finish) {
      return Trig.atan2(finish.getX() - start.getX(), finish.getY() - start.getY());
   }

   public static final double angle(double startX, double startY, double finishX, double finishY) {
      return Trig.atan2(finishX - startX, finishY - startY);
   }


   public static final double distSq(double x1, double y1, double x2, double y2) {
      return sqr(x2 - x1) + sqr(y2 - y1);
   }

   public static final double distSq(Point2D p1, Point2D p2) {
      return sqr(p2.getX() - p1.getX()) + sqr(p2.getY() - p1.getY());
   }

   public static final double dist(double x1, double y1, double x2, double y2) {
      return StrictMath.sqrt(sqr(x2 - x1) + sqr(y2 - y1));
   }

   public static final double dist(Point2D p1, Point2D p2) {
      return StrictMath.sqrt(sqr(p2.getX() - p1.getX()) + sqr(p2.getY() - p1.getY()));
   }


   public static final boolean inRange(double low, double n, double high) {
      return low <= n && n <= high;
   }

   public static final boolean isNear(double n1, double n2, double percent) {
      return abs(2.0 * (n1 - n2) / (n1 + n2)) < percent;
   }

}
