package kid;

import java.awt.geom.*;
import kid.Data.Virtual.DataWave;
import kid.Data.Robot.EnemyData;
import robocode.Robot;
import kid.Data.Virtual.EnemyWave;
import kid.Data.MyRobotsInfo;

public class Utils {

    public static final double getGuessFactor(int index, int listlength) {
        return (index - ((double) listlength - 1) / 2) / ((listlength - 1) / 2);
    }

    public static final int getIndex(double guessfactor, int listlength) {
        return (int) round(((double) listlength - 1) / 2 * (guessfactor + 1), .1);
    }

    public static final double getAngleOffset(EnemyData enemy, DataWave wave) {
        return (Utils.sin(enemy.getHeading() - wave.getAngleOffset(enemy)) * sign(enemy.getVelocity())) *
                wave.getGuessFactor(enemy.getRobot()) * wave.maxEscapeAngle();
    }

    public static final double getAngleOffset(Robot MyRobot, EnemyWave wave) {
        if (MyRobot == null)
            return 0.0;
        return (Utils.sin(MyRobot.getHeading() - wave.getAngleOffset(MyRobot)) * sign(MyRobot.getVelocity())) *
                wave.getGuessFactor(MyRobot) * wave.maxEscapeAngle();
    }

    public static final double getAngleOffset(Robot MyRobot, EnemyData Enemy, double guessfactor, double FirePower) {
        return Utils.sin(MyRobot.getHeading() -
                         (Enemy.BearingToXY(MyRobot.getX(), MyRobot.getY()) + Enemy.getHeading()) *
                         sign(MyRobot.getVelocity())) * guessfactor *
                Utils.asin(MyRobotsInfo.MAX_VELOCITY / Utils.bulletVelocity(FirePower));
    }

    public static final double absMin(double value1, double value2) {
        return (Math.abs(value1) <= Math.abs(value2) ? value1 : value2);
    }

    public static final double rollingAvg(double value, double newEntry, double n, double weighting) {
        return (value * n + newEntry * weighting) / (n + weighting);
    }

    public static final double cos(double n) {
        return Math.cos(Math.toRadians(n));
    }

    public static final double sin(double n) {
        return Math.sin(Math.toRadians(n));
    }

    public static final double tan(double n) {
        return Math.tan(Math.toRadians(n));
    }

    public static final double acos(double n) {
        return Math.toDegrees(Math.acos(n));
    }

    public static final double asin(double n) {
        return Math.toDegrees(Math.asin(n));
    }

    public static final double atan(double n) {
        return Math.toDegrees(Math.atan(n));
    }

    public static final double atan2(double xDelta, double yDelta) {
        return Math.toDegrees(Math.atan2(xDelta, yDelta));
    }

    public static final byte sign(double n) {
        return n < 0 ? (byte) - 1 : 1;
    }

    public static final boolean sign_B(double n) {
        return (sign(n) == 1 ? true : false);
    }

    public static final double round(double n, double p) {
        return (Math.round(n * (1 / p)) / (1 / p));
    }

    public static final double roundUp(double n) {
        return (Math.round(n + 0.4999999999999999999999999999));
    }

    public static final double random(double n1, double n2) {
        return ((Math.max(n1, n2) - Math.min(n1, n2)) * Math.random()) + Math.min(n1, n2);
    }

    public static final double absolute(double angle) {
        while (angle < 0.0) angle += 360;
        return angle % 360;
    }

    public static final double relative(double angle) {
        if (angle > -180 && angle <= 180)
            return angle;
        double fixedAngle = angle;
        while (fixedAngle <= -180)
            fixedAngle += 360;
        while (fixedAngle > 180)
            fixedAngle -= 360;
        return fixedAngle;
    }


    public static final double oppositeRelative(double angle) {
        return relative(angle - 180);
    }

    public static final double oppositeAbsolute(double angle) {
        return absolute(angle - 180);
    }

    public static final double getDistSq(double x1, double y1, double x2, double y2) {
        return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
    }

    public static final double getDist(double x1, double y1, double x2, double y2) {
        return Math.sqrt(getDistSq(x1, y1, x2, y2));
    }

    public static final int getDistSq(int x1, int y1, int x2, int y2) {
        return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
    }

    public static final int getDist(int x1, int y1, int x2, int y2) {
        return (int) Math.sqrt(getDistSq(x1, y1, x2, y2));
    }

    public static final double getDistSq(Point2D p1, Point2D p2) {
        return getDistSq(p1.getX(), p1.getY(), p2.getX(), p2.getY());
    }

    public static final double getAngle(double x1, double y1, double x2, double y2) {
        return atan2(x2 - x1, y2 - y1);
    }

    public static final double getAngle(Point2D p1, Point2D p2) {
        return getAngle(p1.getX(), p1.getY(), p2.getX(), p2.getY());
    }

    public static final double getX(double x, double distance, double bearing) {
        return (x + (distance * sin(bearing)));
    }

    public final static double bulletDamage(double power) {
        double damage = 4 * power;
        if (power > 1)
            damage += 2 * (power - 1);
        return damage;
    }

    public static final double bulletVelocity(double power) {
        return 20 - 3 * power;
    }

    public static final double bulletFirePower(double velocity) {
        return (velocity - 20) / -3;
    }


    public static final double getY(double y, double distance, double bearing) {
        return (y + (distance * cos(bearing)));
    }

}
