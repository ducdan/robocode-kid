package kid;

import java.util.*;
import robocode.*;

public class RobotInfo {

    public static final int robotNotFound = -1;
    public static final double ROBOT_HEIGHT = 36;
    public static final double ROBOT_WIDTH = 36;

    private TeamRobot r;
    private Vector RobotInfo;

    public RobotInfo(TeamRobot myRobot) {
        RobotInfo = new Vector();
        r = myRobot;
    }


    public void addRobotInfo(String name, double x, double y, double energy, double heading,
                             double velocity, long time) {
        int idx = findRobotByName(name);
        if (idx == robotNotFound) {
            RobotInfo.add(new RobotItem(name, x, y, energy, heading, velocity, time));
        } else {
            RobotItem i = (RobotItem) RobotInfo.elementAt(idx);
            i.updateItem(x, y, energy, heading, velocity, time);
        }
    }

    public void setRobotDeath(String name) {
        int idx = findRobotByName(name);
        if (idx != robotNotFound) {
            RobotInfo.removeElementAt(idx);
        }
    }


    public String getName(int idx) {
        RobotItem i = (RobotItem) RobotInfo.elementAt(idx);
        return (i.getName());
    }


    public double getX(String name) {
        try {
            RobotItem i = (RobotItem) RobotInfo.elementAt(findRobotByName(name));
            return (i.getX());
        } catch (ArrayIndexOutOfBoundsException e) {}
        return 0.0;
    }

    public double getY(String name) {
        try {
            RobotItem i = (RobotItem) RobotInfo.elementAt(findRobotByName(name));
            return (i.getY());
        } catch (ArrayIndexOutOfBoundsException e) {}
        return 0.0;
    }


    public double getBearing(String name) {
        try {
            RobotItem i = (RobotItem) RobotInfo.elementAt(findRobotByName(name));
            return relative(getBearing(r.getX(), r.getY(), i.getX(), i.getY()) - r.getHeading());
        } catch (ArrayIndexOutOfBoundsException e) {}
        return 0.0;
    }

    public double getAbsBearing(String name) {
        try {
            RobotItem i = (RobotItem) RobotInfo.elementAt(findRobotByName(name));
            return (getBearing(r.getX(), r.getY(), i.getX(), i.getY()));
        } catch (ArrayIndexOutOfBoundsException e) {}
        return 0.0;
    }


    public double getDistance(String name) {
        try {
            RobotItem i = (RobotItem) RobotInfo.elementAt(findRobotByName(name));
            return (getDist(r.getX(), r.getY(), i.getX(), i.getY()));
        } catch (ArrayIndexOutOfBoundsException e) {}
        return 0.0;
    }

    public double getDistance(String name, double x, double y) {
        try {
            RobotItem i = (RobotItem) RobotInfo.elementAt(findRobotByName(name));
            return getDist(i.getX(), i.getY(), x, y);
        } catch (ArrayIndexOutOfBoundsException e) {}
        return 0.0;
    }

    public double getDistance(String name1, String name2) {
        try {
            RobotItem i1 = (RobotItem) RobotInfo.elementAt(findRobotByName(name1));
            RobotItem i2 = (RobotItem) RobotInfo.elementAt(findRobotByName(name2));
            return getDist(i1.getX(), i1.getY(), i2.getX(), i2.getY());
        } catch (ArrayIndexOutOfBoundsException e) {}
        return 0.0;
    }


    public double getHeading(String name) {
        try {
            RobotItem i = (RobotItem) RobotInfo.elementAt(findRobotByName(name));
            return (i.getHeading());
        } catch (ArrayIndexOutOfBoundsException e) {}
        return 0.0;
    }

    public double getVelocity(String name) {
        try {
            RobotItem i = (RobotItem) RobotInfo.elementAt(findRobotByName(name));
            return (i.getVelocity());
        } catch (ArrayIndexOutOfBoundsException e) {}
        return 0.0;
    }


    public double getEnergy(String name) {
        try {
            RobotItem i = (RobotItem) RobotInfo.elementAt(findRobotByName(name));
            return (i.getEnergy());
        } catch (ArrayIndexOutOfBoundsException e) {}
        return 0.0;
    }


    public long getDeltaTime(String name) {
        try {
            RobotItem i = (RobotItem) RobotInfo.elementAt(findRobotByName(name));
            return (i.getDeltaTime());
        } catch (ArrayIndexOutOfBoundsException e) {}
        return 0;
    }


    private int findRobotByName(String name) {
        for (int p = 0; p < RobotInfo.size(); p++) {
            RobotItem I = (RobotItem) RobotInfo.elementAt(p);
            if (I.getName() == name) {
                return p;
            }
        }
        return robotNotFound;
    }

    private double getDist( double x1, double y1, double x2, double y2 ) {
        return Math.sqrt( ( x1 - x2 ) * ( x1 - x2 ) + ( y1 - y2 ) * ( y1 - y2 ) );
    }

    private double getBearing( double x1, double y1, double x2, double y2 ) {
        double xo = x2 - x1;
        double yo = y2 - y1;
        return Math.toDegrees( Math.atan2( xo, yo ) );
    }

    private double relative( double angle ) {
        double relativeAngle = angle % 360;
        if ( relativeAngle <= -180 ) {
            return 180 + ( relativeAngle % 180 );
        } else if ( relativeAngle > 180 ) {
            return -180 + ( relativeAngle % 180 );
        } else {
            return relativeAngle;
        }
    }

}