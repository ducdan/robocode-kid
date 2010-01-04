package dev.cluster;

import dev.data.RobotData;

public abstract class Scale {

   /**
    * Returns a positive double if <code>v1</code> is greater than <code>v2</code>, a negative double if <code>v1</code>
    * is less than <code>v2</code>, and a zero if they are equal. Another way to think about this is <code>v1</code> -
    * <code>v2</code> or <code>v2</code> subtracted from <code>v1</code>.
    * 
    * @param v1
    * @param v2
    * @return a double representing the relationship between <code>v1</code> and <code>v2</code>: positive, negative, or
    *         zero.
    */
   public double compare(Vector v1, Vector v2) {
      Component c1 = v1.getComponent(this);
      Component c2 = v2.getComponent(this);
      return c1.doubleValue() - c2.doubleValue();
   }

   public abstract double value(RobotData view, RobotData reference);

}
