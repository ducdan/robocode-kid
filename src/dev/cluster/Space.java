package dev.cluster;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import dev.Utils;
import dev.data.RobotData;

public class Space {

   public void print() {
      for (Dimension d : this.dimensions) {
         d.print();
      }
   }

   private LinkedList<Dimension> dimensions;
   private Collection<Scale>     scales;

   public Space(Collection<Scale> scales) {
      this.scales = scales;
      this.dimensions = new LinkedList<Dimension>();
      for (Scale s : scales)
         this.dimensions.add(new Dimension(s));
   }

   public Space(Scale[] scales) {
      this(Arrays.asList(scales));
   }


   public void add(RobotData view, RobotData reference) {
      Vector v = new Vector(this.scales, view, reference);
      for (Dimension d : this.dimensions)
         d.add(v);
   }

   public LinkedList<Vector> getClustor(RobotData view, RobotData reference, int size) {
      Vector center = new Vector(this.scales, view, reference);
      LinkedList<SortVector> sorted = new LinkedList<SortVector>();
      for (Dimension d : this.dimensions) {
         for (Vector v : d.getCluster(center, size)) {

            // TODO code: make so that one scale does not have precedence over another
            double sort = 0.0;
            for (Scale s : this.scales)
               sort += Utils.sqr(s.compare(center, v));

            // binary search
            int first = 0;
            int last = sorted.size();
            int mid = (last - first) / 2;
            while (first != last) {
               mid = (last - first) / 2;
               SortVector m = sorted.get(mid);
               int sign_m = Utils.signum(m.sort - sort);
               // smaller values at the beginning
               if (sign_m == 0) {
                  first = last = mid;
               } else if (sign_m == 1) {
                  first = mid;
               } else {
                  last = mid;
               }
            }

            sorted.add(mid, new SortVector(v, sort));

            // remove any vectors that make the cluster to large
            if (sorted.size() > size) {
               SortVector v1 = sorted.get(size);
               SortVector v2 = sorted.get(size - 1);

               // only remove vectors that don't tie for the last position
               if (v1.sort != v2.sort) {
                  while (sorted.size() > size) {
                     sorted.removeLast();
                  }
               }
            }
         }
      }

      LinkedList<Vector> cluster = new LinkedList<Vector>();
      for (SortVector sv : sorted)
         cluster.add(sv);
      return cluster;
   }


   private static class SortVector extends Vector {

      private Vector vector;
      private double sort;

      public SortVector(Vector v, double sort) {
         this.vector = v;
         this.sort = sort;
      }

      @Override
      public RobotData getView() {
         return this.vector.getView();
      }

      @Override
      public RobotData getReference() {
         return this.vector.getReference();
      }

      @Override
      protected Component getComponent(Scale s) {
         return this.vector.getComponent(s);
      }
   }

}
