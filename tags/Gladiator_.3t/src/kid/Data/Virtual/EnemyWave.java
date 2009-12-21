package kid.Data.Virtual;

import robocode.*;
import kid.Data.Robot.*;
import kid.Data.MyRobotsInfo;
import java.awt.geom.Rectangle2D;
import kid.Utils;

public class EnemyWave extends VirtualWave {

    public EnemyWave(double Start_X, double Start_Y, double Heading, double FirePower, int Direction, long CreationTime) {
        super(Start_X, Start_Y, Heading, FirePower, Direction, CreationTime);
    }

    public EnemyWave(Robot MyRobot, EnemyData EnemyRobot) {
        super(EnemyRobot.getX(), EnemyRobot.getY(),
              Utils.getAngle(EnemyRobot.getX(), EnemyRobot.getY(), MyRobot.getX(), MyRobot.getY()),
              (EnemyRobot.didFireBullet() ? Math.abs(EnemyRobot.getDeltaEnergy()) : 0.0),
              (Utils.sin(MyRobot.getHeading() -
                         Utils.getAngle(EnemyRobot.getX(), EnemyRobot.getY(), MyRobot.getX(), MyRobot.getY())) *
               MyRobot.getVelocity() < 0 ? 1 : -1), MyRobot.getTime());
    }

    public boolean testHit(Robot MyRobot) {
        return testHit(new Rectangle2D.Double(MyRobot.getX() - MyRobotsInfo.WIDTH / 2,
                MyRobot.getY() + MyRobotsInfo.HEIGHT / 2, MyRobotsInfo.WIDTH, MyRobotsInfo.HEIGHT), MyRobot.getTime());
    }

    public double getGuessFactor(Robot MyRobot) {
        double desiredDirection = Utils.atan2(MyRobot.getX() - sX, MyRobot.getY() - sY);
        double angleOffset = Utils.relative(desiredDirection - h);
        double guessFactor = Math.max( -1, Math.min(1, angleOffset / maxEscapeAngle())) * d;
        return guessFactor;
    }

    public double getAngleOffset(Robot MyRobot) {
        double desiredDirection = Utils.atan2(MyRobot.getX() - sX, MyRobot.getY() - sY);
        double angleOffset = Utils.relative(desiredDirection - h);
        return angleOffset;
    }

    public double getFirePower() {
        return fp;
    }

}
