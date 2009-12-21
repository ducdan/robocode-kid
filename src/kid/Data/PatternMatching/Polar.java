package kid.Data.PatternMatching;

public class Polar extends Pattern {

    private short HeadingChange;
    private byte Velocity;

    boolean DidScanThisTurn;

    private static final byte bump = 100;

    public Polar() {
        this(0.0, 0.0, false);
    }

    public Polar(double HeadingChange, double Velocity, boolean DidScanThisTurn) {
        this.HeadingChange = (short) (HeadingChange * bump);
        this.Velocity = (byte) Velocity;
        this.DidScanThisTurn = DidScanThisTurn;
    }

    public double getVelocity() {
        return Velocity;
    }

    public double getHeadingChange() {
        return ((double) HeadingChange) / bump;
    }

    public double difference(Pattern p) {
        return Math.abs(Velocity - p.getVelocity()) +
                Math.abs((HeadingChange - (short) (p.getHeadingChange() * bump)) / (double) bump);
    }

    public boolean didScanThisTurn() {
        return DidScanThisTurn;
    }

}
