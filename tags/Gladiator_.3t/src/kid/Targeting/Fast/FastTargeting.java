package kid.Targeting.Fast;

import robocode.*;
import kid.Data.MyRobotsInfo;
import kid.Data.Robot.*;
import kid.Utils;
import kid.Targeting.Targeting;
import java.util.*;

public abstract class FastTargeting implements Targeting, java.io.Serializable {

    protected Robot MyRobot = null;

    protected List LastAngle = new ArrayList();
    protected long LastTime = -1;

    public FastTargeting(Robot MyRobot) {
        this.MyRobot = MyRobot;
    }

    public FastTargeting(AdvancedRobot MyRobot) {
        this((Robot) MyRobot);
    }

    public FastTargeting(TeamRobot MyRobot) {
        this((Robot) MyRobot);
    }

    public abstract double getTargetingAngle(EnemyData robot, double firePower);

    public abstract String getNameOfTargeting();

    public String getTypeOfTargeting() {
        return "FastTargeting";
    }


    protected double getAngle(EnemyData EnemyRobot, double firePower, double deltaHeading, double deltaVelocity) {
        if (MyRobot.getTime() == LastTime) {
            for (int b = 0; b < LastAngle.size(); b++) {
                RobotFireAngle a = (RobotFireAngle) LastAngle.get(b);
                if (a.getEnemy() == EnemyRobot && a.getFirePower() == firePower) {
                    return a.getFireAngle();
                }
            }
        } else {
            LastAngle = new ArrayList();
            LastTime = MyRobot.getTime();
        }
        if (EnemyRobot != null || !EnemyRobot.isDead()) {
            if (firePower < 0.1)
                firePower = 0.1;
            else if (firePower > 3.0)
                firePower = 3.0;
            double BulletVelocity = Utils.bulletVelocity(firePower);
            int MWD = (int) MyRobotsInfo.MIN_WALL_DIST;
            double mX = MyRobot.getX(), mY = MyRobot.getY();
            int battleFieldHeight = (int) (MyRobot.getBattleFieldHeight() - MWD),
                    battleFieldWidth = (int) (MyRobot.getBattleFieldWidth() - MWD);
            double eX = EnemyRobot.getX(), eY = EnemyRobot.getY(), eH = EnemyRobot.getHeading();
            for (int t = 0; Math.pow(t * BulletVelocity, 2) < Utils.getDistSq(mX, mY, eX, eY); t++) {
                eX += deltaVelocity * Utils.sin(eH);
                eY += deltaVelocity * Utils.cos(eH);
                eH += deltaHeading;
                if (eX < MWD || eY < MWD || eX > battleFieldWidth || eY > battleFieldHeight) {
                    eX = Math.min(Math.max(MWD, eX), battleFieldWidth);
                    eY = Math.min(Math.max(MWD, eY), battleFieldHeight);
                    break;
                }
            }
            double angle = Utils.atan2(eX - mX, eY - mY);
            LastAngle.add(new RobotFireAngle(EnemyRobot, firePower, angle, MyRobot.getTime()));
            return angle;
        }
        double GunHeading = Utils.relative(MyRobot.getGunHeading() + MyRobot.getGunHeading());
        LastAngle.add(new RobotFireAngle(EnemyRobot, firePower, GunHeading, MyRobot.getTime()));
        return GunHeading;
    }

}
