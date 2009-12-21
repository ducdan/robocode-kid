package kid.Data;

import kid.Utils;

public class Sector implements java.io.Serializable {

    int hits = 0;

    public void addHit() {
        hits++;
    }

    public int getHits() {
        return hits;
    }

}
