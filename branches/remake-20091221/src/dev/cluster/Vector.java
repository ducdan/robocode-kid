package dev.cluster;

import java.util.Collection;
import java.util.HashMap;

import dev.data.RobotData;

public class Vector {

   private RobotData                 view;
   private RobotData                 reference;
   private HashMap<Scale, Component> components;

   public Vector(Collection<Scale> scales, RobotData view, RobotData reference) {
      this.view = view;
      this.reference = reference;
      components = new HashMap<Scale, Component>(scales.size());
      for (Scale s : scales)
         components.put(s, new Component(s, view, reference));
   }

   protected Vector() {
   }

   public RobotData getView() {
      return view;
   }

   public RobotData getReference() {
      return reference;
   }

   protected Component getComponent(Scale s) {
      return components.get(s);
   }

}
