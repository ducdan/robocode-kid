package kid.Movement.Melee;

import robocode.*;
import kid.*;
import kid.Movement.*;
import kid.Data.Robot.EnemyData;
import kid.Data.Gravity.*;
import kid.Movement.Melee.MeleeMovement;

public class AntiGravity extends MeleeMovement {

    protected GravityEngine GravityEngine;

    public AntiGravity(Robot MyRobot) {
        super(MyRobot);
        GravityEngine = new GravityEngine(MyRobot.getBattleFieldWidth(), MyRobot.getBattleFieldHeight());
        GravityEngine.addPoint(new GravityPoint(0, 0, -100));
        GravityEngine.addPoint(new GravityPoint(0, MyRobot.getBattleFieldHeight(), -100));
        GravityEngine.addPoint(new GravityPoint(MyRobot.getBattleFieldWidth(), 0, -100));
        GravityEngine.addPoint(new GravityPoint(MyRobot.getBattleFieldWidth(), MyRobot.getBattleFieldHeight(), -100));
    }

    public AntiGravity(AdvancedRobot MyRobot) {
        super(MyRobot);
        GravityEngine = new GravityEngine(MyRobot.getBattleFieldWidth(), MyRobot.getBattleFieldHeight());
        GravityEngine.addPoint(new GravityPoint(0, 0, -100));
        GravityEngine.addPoint(new GravityPoint(0, MyRobot.getBattleFieldHeight(), -100));
        GravityEngine.addPoint(new GravityPoint(MyRobot.getBattleFieldWidth(), 0, -100));
        GravityEngine.addPoint(new GravityPoint(MyRobot.getBattleFieldWidth(), MyRobot.getBattleFieldHeight(), -100));
    }

    public AntiGravity(TeamRobot MyRobot) {
        this((AdvancedRobot) MyRobot);
    }


    public void doMovement(EnemyData[] EnemyData) {
        if (MyRobot instanceof AdvancedRobot) {
            GravityEngine.update(MyRobot);
            Robot.setAhead(Double.POSITIVE_INFINITY *
                              Robot.setTurnToXYwBF(GravityEngine.getXForce() + MyRobot.getX(),
                    GravityEngine.getYForce() + MyRobot.getY()));
        } else {
        }
    }

    /*
         public Movement getMovement(GravityEngine GravityEngine) {
        GravityEngine.update(MyRobot);
        double theta = RobotInfo.RobotBearingTo(GravityEngine.getXForce() + MyRobot.getX(),
                GravityEngine.getYForce() + MyRobot.getY());
        return new Movement.Robot(Double.POSITIVE_INFINITY * (Math.abs(theta) < 90 ? 1 : -1),
                                  (Math.abs(theta) < 90 ? theta : Utils.oppositeRelative(theta)));
         }

         public void doMovement(Robot MyRobot, GravityEngine GravityEngine) {
             GravityEngine.update(MyRobot);
             double theta = RobotInfo.RobotBearingTo(GravityEngine.getXForce() + MyRobot.getX(),
             GravityEngine.getYForce() + MyRobot.getY());
          }
     */


    /**
     * Enter all ScannedRobotEvents. Adds GravPoints to the GravityEngine. <br>
     * <P>
     * Example
     * <PRE>
     *   public void onScannedRobot(ScannedRobotEvent e) {<br>
     *     Movement.inEvent(e);<br>
     *   }
     * </PRE>
     * @param e Event - ScannedRobotEvent
     */
    public void inEvent(Event e) {
        if (e instanceof ScannedRobotEvent) {
            ScannedRobotEvent SRE = (ScannedRobotEvent) e;
            GravityEngine.addPoint(new GravityPoint(Utils.getX(MyRobot.getX(), SRE.getDistance(),
                    SRE.getBearing() + MyRobot.getHeading()),
                    Utils.getY(MyRobot.getY(), SRE.getDistance(), SRE.getBearing() + MyRobot.getHeading()),
                    -SRE.getEnergy(), SRE.getTime(), 4));
        }
    }

}
