package kid.Data.PatternMatching;

import kid.Utils;

public class Symbol extends Pattern {

    char PatternSymbol;

    private byte HeadingChangeSign;
    private byte VelocitySign;

    boolean DidScanThisTurn;

    private static final byte bump = 100;

    public Symbol() {
        this(0.0, 0.0, false);
    }

    public Symbol(double HeadingChange, double Velocity, boolean DidScanThisTurn) {
        PatternSymbol = (char) (Math.abs((short) HeadingChange * bump) | Math.abs((byte) Velocity << 8));
        HeadingChangeSign = Utils.sign(HeadingChange);
        VelocitySign = Utils.sign(Velocity);
        this.DidScanThisTurn = DidScanThisTurn;
    }

    public double getVelocity() {
        return VelocitySign * (PatternSymbol >> 8);
    }

    public double getHeadingChange() {
        return HeadingChangeSign * ((double) (PatternSymbol & 255)) / bump;
    }

    public double getSymbol() {
        return HeadingChangeSign * ((double) (PatternSymbol & 255)) / bump;
    }

    public double difference(Pattern p) {
        if (p instanceof Symbol) {
            Symbol symbol = (Symbol) p;
            if (PatternSymbol == symbol.getSymbol())
                return 0;
            return 1;
        }
        char symbol = (char) (Math.abs((short) p.getHeadingChange() * bump) | Math.abs((byte) p.getVelocity() << 8));
        if (symbol == PatternSymbol)
            return 0;
        return 1;
    }

    public boolean didScanThisTurn() {
        return DidScanThisTurn;
    }

}
