package dev.cluster;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import dev.data.RobotData;

public class Vector<E> {

   private E                      data;
   private HashMap<Scale, Double> components;

   public Vector(Collection<Scale> scales, RobotData view, RobotData reference, E data) {
      this.data = data;
      components = new HashMap<Scale, Double>(scales.size());
      for (Scale s : scales)
         components.put(s, s.value(view, reference));
   }

   protected Vector() {
   }

   protected double getComponent(Scale s) {
      return components.get(s);
   }

   public E getData() {
      return data;
   }

   @Override
   public String toString() {
      String str = "(";
      Iterator<Double> iter = components.values().iterator();
      while (iter.hasNext()) {
         str += iter.next();
         if (iter.hasNext())
            str += ", ";
      }
      return str + ")";
   }

}
