package kid;

import java.util.*;

public class GravEngine {

    protected Vector gravPoints = new Vector();

    protected double wallForce = -50.0;

    protected double width;
    protected double height;

    protected double xForce = 0.0;
    protected double yForce = 0.0;
    protected double xWallForce = 0.0;
    protected double yWallForce = 0.0;

    protected double pointDropoff = 2.0;
    protected double wallDropoff = 2.0;
    protected double bulletDropoff = 2.0;

    public GravEngine() {
        this( 0, 0 );
    }

    public GravEngine( double width, double height ) {
        this.width = width;
        this.height = height;
    }

    public void addPoint( GravPoint g ) {
        gravPoints.add( g );
    }

    public boolean removePoint( GravPoint g ) {
        return gravPoints.remove( g );
    }

    public void update( double curX, double curY, long time ) {
        xForce = 0.0;
        yForce = 0.0;
        xWallForce = 0.0;
        yWallForce = 0.0;
        Vector deadPoints = new Vector();
        GravPoint g;
        double force;
        double angle;
        for ( int i = 0; i < gravPoints.size(); i++ ) {
            g = ( GravPoint ) gravPoints.elementAt( i );
            if ( g.update( time ) ) {
                deadPoints.add( g );
                continue;
            }
            force = g.strength / Math.pow( getDist( curX, curY, g.x, g.y ), pointDropoff );
            angle = Math.toRadians( getBearing( curX, curY, g.x, g.y ) );
            xForce += force * Math.sin( angle );
            yForce += force * Math.cos( angle );
        }
        xWallForce -= wallForce / Math.pow( curX, wallDropoff );
        xWallForce += wallForce / Math.pow( width - curX, wallDropoff );
        yWallForce += wallForce / Math.pow( height - curY, wallDropoff );
        yWallForce -= wallForce / Math.pow( curY, wallDropoff );
        for ( int i = 0; i < deadPoints.size(); i++ ) {
            gravPoints.remove( deadPoints.elementAt( i ) );
        }
    }

    public double getXForce() {
        return xForce;
    }

    public double getYForce() {
        return yForce;
    }

    public double getXWallForce() {
        return xWallForce;
    }

    public double getYWallForce() {
        return yWallForce;
    }

    private double getDist( double x1, double y1, double x2, double y2 ) {
        return Math.sqrt( ( x1 - x2 ) * ( x1 - x2 ) + ( y1 - y2 ) * ( y1 - y2 ) );
    }

    private double getBearing( double x1, double y1, double x2, double y2 ) {
        double xo = x2 - x1;
        double yo = y2 - y1;
        return Math.toDegrees( Math.atan2( xo, yo ) );
    }
}