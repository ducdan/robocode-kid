package kid;

import robocode.*;
import java.awt.Color;

public class Gladiator extends TeamRobot {

    RobotInfo e;
    GravEngine g;

    static double maxFireDistance = 400.0;
    static double radarOffSet = 15.0;

    double lastX = 0;
    double lastY = 0;

    double lastEnemyHeading;
    long lastScanTime;
    int robotScannedCount;
    int timeSinceLastScan = 10;

    String trackedRobot = null;

    public void run() {

        e = new RobotInfo(this);
        g = new GravEngine(getBattleFieldWidth(), getBattleFieldHeight());

        if (getOthers() > 1) {
            g.addPoint(new GravPoint(getBattleFieldWidth() / 2, getBattleFieldHeight() / 2, -1));
            setEventPriority("RobotDeathEvent", 99);
        }
        g.addPoint(new GravPoint(0, 0, -100));
        g.addPoint(new GravPoint(0, getBattleFieldHeight(), -100));
        g.addPoint(new GravPoint(getBattleFieldWidth(), 0, -100));
        g.addPoint(new GravPoint(getBattleFieldWidth(), getBattleFieldHeight(), -100));

        setColors(new Color(137, 91, 46), Color.LIGHT_GRAY, new Color(117, 117, 208));

        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);

        while (true) {
            doScanner();
            moveLikeGrav();
            execute();
        }
    }

    public void onScannedRobot(ScannedRobotEvent SRE) {
        e.addRobotInfo(SRE.getName(), getX(SRE), getY(SRE), SRE.getEnergy(), SRE.getHeading(),
                       SRE.getVelocity(), SRE.getTime());
        g.addPoint(new GravPoint(getX(SRE), getY(SRE), -getGravForce(SRE.getName()), SRE.getTime(),
                                 e.getDeltaTime(SRE.getName()) + 2));
        setTrackingRobot(SRE.getName());
        fireCircularTargeting(trackedRobot);
    }

    public void onRobotDeath(RobotDeathEvent RDE) {
        e.setRobotDeath(RDE.getName());
        if (RDE.getName() == trackedRobot) {
            trackedRobot = null;
        }
    }

    public void onHitRobot(HitRobotEvent HRE) {
        g.addPoint(new GravPoint(e.getX(HRE.getName()), e.getY(HRE.getName()), -200, HRE.getTime(),
                                 2));
    }


    public void setTrackingRobot(String name) {
        timeSinceLastScan++;
        try {
            if (name.equals(trackedRobot)) {
                timeSinceLastScan = 0;
                robotScannedCount = 0;
            }
            double enemyRiskFactor = Double.POSITIVE_INFINITY;
            String enemyName = null;
            for (int i = 0; i < getOthers(); i++) {
                if (getRiskFactor(e.getName(i)) < enemyRiskFactor) {
                    enemyRiskFactor = getRiskFactor(enemyName = e.getName(i));
                }
            }
            trackedRobot = enemyName;
        } catch (ArrayIndexOutOfBoundsException e) {
            if (trackedRobot == null) {
                trackedRobot = name;
            }
        }
    }

    public void fireCircularTargeting(String name) {
        if (name != null) {
            double firePower = getFirePower(name);
            double w = relative(e.getHeading(name) - lastEnemyHeading) / (getTime() - lastScanTime);
            lastEnemyHeading = e.getHeading(name);
            lastScanTime = getTime();
            double eX = e.getX(name);
            double eY = e.getY(name);
            double db = 0;
            double ww = e.getHeading(name);
            double battleFieldHeight = getBattleFieldHeight(),
                    battleFieldWidth = getBattleFieldWidth();
            do {
                db += 20 - (3 * firePower);
                eX += e.getVelocity(name) * sin(ww);
                eY += e.getVelocity(name) * cos(ww);
                ww += w;
                if (eX < 18.0 || eY < 18.0 || eX > battleFieldWidth - 18.0 ||
                    eY > battleFieldHeight - 18.0) {
                    eX = Math.min(Math.max(18.0, eX), battleFieldWidth - 18.0);
                    eY = Math.min(Math.max(18.0, eY), battleFieldHeight - 18.0);
                    break;
                }
            } while (db < getDist(getX(), getY(), eX, eY));

            setTurnGunToXY(eX, eY);
            if (getGunHeat() == 0.0) {
                fireBullet(firePower);
            }
        }
    }

    public void moveLikeGrav() {
        int MOVING;
        g.addPoint(new GravPoint(lastX, lastY, -0.1, getTime(), 1));
        lastX = getX();
        lastY = getY();
        g.update(getX(), getY(), getTime());
        double theta = atan2((g.getXForce() + g.getXWallForce()), (g.getYForce() + g.getYWallForce()));
        if (theta > -90 && theta < 90) {
            setTurnToAngle(theta);
            MOVING = 1;
        } else {
            setTurnToAngle(theta - 180);
            MOVING = -1;
        }
        setAhead(Double.POSITIVE_INFINITY * MOVING);
    }

    public void doScanner() {
        timeSinceLastScan++;
        double radarArc = Double.POSITIVE_INFINITY;
        if (trackedRobot != null) {
            if (getOthers() == 1 && timeSinceLastScan < 4) {
                radarArc = relative(getRadarHeading() - e.getAbsBearing(trackedRobot));
                radarArc += sign(radarArc) * radarOffSet;
            } else if (getGunHeat() < 0.7 && timeSinceLastScan < 4) {
                radarArc = relative(getRadarHeading() - e.getAbsBearing(trackedRobot));
                radarArc += sign(radarArc) * radarOffSet;
            }
        }
        setTurnRadarLeft(radarArc);
    }

    public void setTurnGunToXY(double x, double y) {
        double theta = atan2(x - getX(), y - getY());
        setTurnGunRight(relative(theta - getGunHeading()));
    }

    public void setTurnToAngle(double a) {
        setTurnRight(relative(a - getHeading()));
    }

    public double getFirePower(String name) {
        double firePower = round(Math.min((maxFireDistance * 3) / e.getDistance(name), 3), .1);
        if (getEnergy() > 20) {
            return firePower;
        } else {
            if (e.getEnergy(name) > getEnergy()) {
                return firePower / 3;
            }
            return firePower / 2;
        }
    }

    public double getRiskFactor(String name) {
        return (e.getEnergy(name) / getEnergy() > 2 ? 2 : e.getEnergy(name) / getEnergy()) *
                e.getDistance(name);
    }

    public double getGravForce(String name) {
        return e.getEnergy(name);
    }

    public double getX(ScannedRobotEvent s) {
        return getX() + s.getDistance() * sin(s.getBearing() + getHeading());
    }

    public double getY(ScannedRobotEvent s) {
        return getY() + s.getDistance() * cos(s.getBearing() + getHeading());
    }

    public static double relative(double angle) {
        double relativeAngle = angle % 360;
        if (relativeAngle <= -180) {
            return 180 + (relativeAngle % 180);
        } else if (relativeAngle > 180) {
            return -180 + (relativeAngle % 180);
        } else {
            return relativeAngle;
        }
    }

    public static double getDist(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    public static double cos(double n) {
        return Math.cos(Math.toRadians(n));
    }

    public static double sin(double n) {
        return Math.sin(Math.toRadians(n));
    }

    public static double atan2(double dx, double dy) {
        return Math.toDegrees(Math.atan2(dx, dy));
    }

    public static int sign(double n) {
        return n < 0 ? -1 : 1;
    }

    public static double round(double n, double p) {
        return (Math.round(n * (1 / p)) / (1 / p));
    }

}