package kid;

public class RobotItem {

    private String n;
    private double x;
    private double y;
    private double e;
    private double h;
    private double v;
    private long dt;
    private long t;

    public RobotItem() {
        this(null, 0.0, 0.0, 0.0, 0.0, 0.0, 0);
    }

    public RobotItem(String n, double x, double y, double e, double h, double v, long t) {
        this.n = n;
        updateItem(x, y, e, h, v, t);
    }

    public void updateItem(double x, double y, double e, double h, double v, long t) {
        this.x = x;
        this.y = y;
        this.e = e;
        this.h = h;
        this.v = v;
        this.dt = (t - this.t);
        this.t = t;
    }

    public String getName() {
        return n;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getEnergy() {
        return e;
    }

    public double getHeading() {
        return h;
    }

    public double getVelocity() {
        return v;
    }

    public long getTime() {
        return t;
    }

    public long getDeltaTime() {
        return dt;
    }

}