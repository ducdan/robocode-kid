package kid;

import kid.Data.Robot.*;
import kid.Managers.*;
import kid.Movement.*;
import kid.Movement.Melee.*;
import kid.Movement.OneOnOne.*;
import kid.Targeting.*;
import kid.Targeting.Fast.*;
import robocode.*;

// Version .3

// Tracking, added to robot choser, set the robot and int in the for loop
// AntiGrav, changed my last position, smaller power and doesn't add it if not OneOnOne
// Firering, changed the way it fires, setFire not fireBullet
// Radar, changed when it locks it's trackRobot with GunHeat, 0.5 not 0.6

public class Gladiator extends AdvancedRobot {

    DataManager Data = new DataManager();
    MovementManager Movement;
    StatisticsManager Statistics;

    GunMovement Gun;
    RadarMovement Radar;

    Targeting Targeting;

    final static double maxFireDistance = 200.0;

    public void run() {

        Data.UpDateRobot(this);
        Movement = new MovementManager(this, new Perpendicular(this), new AntiGravity_wMinimumRiskPoint(this));
        Statistics = new StatisticsManager(this);

        Gun = new GunMovement(this);
        Radar = new RadarMovement(this);

        Targeting = new Circular(this);

        setColors(Colors.BROWN, Colors.LIGHT_GRAY, Colors.LIGHT_BLUE);
        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);

        while (true) {
            Movement.doMovement(Data);
            Gun.setTo(Targeting, Data.getColsestEnemy(), getFirePower(Data.getColsestEnemy()));
            if (!Data.getColsestEnemy().isDead())
                setFire(2);
            if (getOthers() != 1)
                Radar.Melee_TickScan_GunHeat(Data.getColsestEnemy(), 45 / 3, 4, .6);
            else
                Radar.OneOnOne_RadarControl_TickScan(Data.getColsestEnemy(), 4);
            execute();
        }
    }

    private double getFirePower(EnemyData Enemy) {
        double firePower = Utils.round(Math.min((maxFireDistance * 3) / Enemy.DistToXY(getX(), getY()), 3), .1);
        if (getEnergy() > 20) {
            return firePower;
        } else {
            if (Enemy.getEnergy() > getEnergy()) {
                return firePower / 3;
            }
            return firePower / 2;
        }
    }

    public void onPaint(java.awt.Graphics2D g) {
        try {
            kid.GraphicsDrawer Graphics = new GraphicsDrawer(this, g);
            Data.drawEnemys(Graphics);
            Data.drawColsestEnemy(Graphics);
        } catch (Exception e) {}
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        Data.inEvent(e);
        Movement.inEvent(e);
        Statistics.inEvent(e);
    }

    public void onRobotDeath(RobotDeathEvent e) {
        Data.inEvent(e);
        Movement.inEvent(e);
        Statistics.inEvent(e);
    }

    public void onHitRobot(HitRobotEvent e) {
        Data.inEvent(e);
        Movement.inEvent(e);
        Statistics.inEvent(e);
    }

    public void onBulletHit(BulletHitEvent e) {
        Data.inEvent(e);
        Movement.inEvent(e);
        Statistics.inEvent(e);
    }

    public void onBulletMissed(BulletMissedEvent e) {
        Data.inEvent(e);
        Movement.inEvent(e);
        Statistics.inEvent(e);
    }

    public void onBulletHitBullet(BulletHitBulletEvent e) {
        Data.inEvent(e);
        Movement.inEvent(e);
        Statistics.inEvent(e);
    }

    public void onSkippedTurn(SkippedTurnEvent e) {
        Data.inEvent(e);
        Movement.inEvent(e);
        Statistics.inEvent(e);
    }

    public void onHitByBullet(HitByBulletEvent e) {
        Data.inEvent(e);
        Movement.inEvent(e);
        Statistics.inEvent(e);
    }

    public void onHitWall(HitWallEvent e) {
        Data.inEvent(e);
        Movement.inEvent(e);
        Statistics.inEvent(e);
    }

    public void onWin(WinEvent e) {
        Data.inEvent(e);
        Movement.inEvent(e);
        Statistics.inEvent(e);
        Statistics.outStatistics();
    }

    public void onDeath(DeathEvent e) {
        Data.inEvent(e);
        Movement.inEvent(e);
        Statistics.inEvent(e);
        Statistics.outStatistics();
    }

}
