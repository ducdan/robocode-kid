package kid;

public class GravPoint {
    public double x;
    public double y;
    public double strength;
    public long ctime;
    public long life;

    public GravPoint() {
        this(0, 0, 0, 0, 0);
    }

    public GravPoint(double x, double y, double strength) {
        this(x, y, strength, 0, 0);
    }

    public GravPoint(double x, double y, double strength, long ctime, long life) {
        this.x = x;
        this.y = y;
        this.strength = strength;
        this.ctime = ctime;
        this.life = life;
    }

    public boolean update(long time) {
        return ((life != 0) && (ctime + life) < time);
    }

}