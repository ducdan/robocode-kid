package dev.cluster;

import java.util.LinkedList;
import java.util.ListIterator;

import dev.Utils;

public class Dimension {

   private Scale              scale;
   private LinkedList<Vector> vectors;

   public Dimension(Scale s) {
      this.scale = s;
      vectors = new LinkedList<Vector>();
   }

   public Scale getScale() {
      return scale;
   }

   public void add(Vector v) {
      if (v != null)
         vectors.add(binarySearch(v), v);
   }

   public LinkedList<Vector> getCluster(Vector center, int size) {
      LinkedList<Vector> cluster = new LinkedList<Vector>();

      int index = binarySearch(center);
      ListIterator<Vector> iterN = vectors.listIterator(index);
      ListIterator<Vector> iterP = vectors.listIterator(index);

      if (iterN.hasNext()) {
         if (iterP.hasPrevious()) {
            Vector vN = iterN.next();
            Vector vP = iterP.previous();
            double cN = Utils.abs(scale.compare(center, vN));
            double cP = Utils.abs(scale.compare(center, vP));

            do {
               if (vN == null) {
                  // fill with iterP
                  cluster.add(vP);
                  while (cluster.size() < size && iterP.hasPrevious()) {
                     cluster.add(iterP.previous());
                  }
               } else if (vP == null) {
                  // fill with iterN
                  cluster.add(vN);
                  while (cluster.size() < size && iterN.hasNext()) {
                     cluster.add(iterN.next());
                  }
               } else {
                  if (cN <= cP) {
                     // vN is closer or equal to vP
                     cluster.add(vN);
                     if (iterN.hasNext()) {
                        vN = iterN.next();
                        cN = Utils.abs(scale.compare(center, vN));
                     } else {
                        vN = null;
                        cN = Double.NaN;
                     }
                  }
                  if (cN >= cP) {
                     // vP is closer or equal to vN
                     cluster.add(vP);
                     if (iterP.hasPrevious()) {
                        vP = iterP.previous();
                        cP = Utils.abs(scale.compare(center, vP));
                     } else {
                        vP = null;
                        cP = Double.NaN;
                     }
                  }
               }
            } while ((cluster.size() < size || cN == cP) && (iterN.hasNext() || iterP.hasPrevious()));
         } else {
            // fill with iterN
            do {
               cluster.add(iterN.next());
            } while (cluster.size() < size && iterN.hasNext());
         }
      } else if (iterP.hasPrevious()) {
         // fill with iterP
         do {
            cluster.add(iterP.previous());
         } while (cluster.size() < size && iterP.hasPrevious());
      } // else leave empty
      return cluster;
   }


   /**
    * Returns the index of the element that the {@link Vector} should be put before.
    * 
    * @param v
    * @param vectors
    * @return
    */
   private int binarySearch(Vector v) {
      int first = 0;
      int last = vectors.size();
      int mid = (last - first) / 2;

      while (first != last) {
         mid = (last - first) / 2;
         Vector m = vectors.get(mid);
         int sign_m = Utils.signum(scale.compare(v, m));
         if (sign_m == 0) {
            first = last = mid;
         } else if (sign_m == 1) {
            last = mid;
         } else {
            first = mid;
         }
      }
      return mid;
   }
}
