package kid.Data.Virtual;

import robocode.*;
import kid.Utils;
import kid.Data.Robot.*;

public class DataWave extends VirtualWave {

    public DataWave(double Start_X, double Start_Y, double Heading, double FirePower, int Direction, long CreationTime) {
        super(Start_X, Start_Y, Heading, FirePower, Direction, CreationTime);
    }

    public DataWave(Robot MyRobot, EnemyData EnemyRobot, double FirePower) {
        super(MyRobot.getX(), MyRobot.getY(),
              Utils.getAngle(MyRobot.getX(), MyRobot.getY(), EnemyRobot.getX(), EnemyRobot.getY()), FirePower,
              (Utils.
               sin(EnemyRobot.getHeading() -
                   Utils.getAngle(MyRobot.getX(), MyRobot.getY(), EnemyRobot.getX(), EnemyRobot.getY())) *
               EnemyRobot.getVelocity() < 0 ? 1 : -1), MyRobot.getTime());
    }

    public boolean testHit(EnemyData EnemyRobot, long t) {
        return testHit(EnemyRobot.getRobot(), t);
    }

    public double getGuessFactor(EnemyData enemy) {
        return getGuessFactor(enemy.getRobot());
    }

    public double getAngleOffset(EnemyData EnemyRobot) {
        double desiredDirection = Utils.atan2(EnemyRobot.getX() - sX, EnemyRobot.getY() - sY);
        double angleOffset = Utils.relative(desiredDirection - h);
        return angleOffset;
    }

}
