package dev.cluster;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import dev.data.RobotData;

public class Vector {

   // private RobotData view;
   // private RobotData reference;
   private HashMap<Scale, Double> components;

   public Vector(Collection<Scale> scales, RobotData view, RobotData reference) {
      // this.view = view;
      // this.reference = reference;
      components = new HashMap<Scale, Double>(scales.size());
      for (Scale s : scales)
         components.put(s, s.value(view, reference));
   }

   protected Vector() {
   }

   // public RobotData getView() {
   // return view;
   // }
   //
   // public RobotData getReference() {
   // return reference;
   // }

   protected Double getComponent(Scale s) {
      return components.get(s);
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
