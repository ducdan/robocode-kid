package dev.cluster;

import dev.data.RobotData;

public class Component extends Number {

   private static final long serialVersionUID = -6860639131489438731L;


   private double            component;

   public Component(Scale s, RobotData view, RobotData reference) {
      component = s.value(view, reference);
   }


   @Override
   public double doubleValue() {
      return component;
   }

   @Override
   public float floatValue() {
      return (float) component;
   }

   @Override
   public int intValue() {
      return (int) component;
   }

   @Override
   public long longValue() {
      return (long) component;
   }

}
