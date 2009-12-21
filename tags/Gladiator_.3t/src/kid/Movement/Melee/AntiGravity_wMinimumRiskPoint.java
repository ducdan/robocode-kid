package kid.Movement.Melee;

import robocode.*;
import kid.Data.Robot.EnemyData;
import java.awt.geom.Point2D;

public class AntiGravity_wMinimumRiskPoint extends AntiGravity {

    private MinimumRiskPoint MinimumRiskPoint;

    public AntiGravity_wMinimumRiskPoint(Robot MyRobot) {
        super(MyRobot);
        MinimumRiskPoint = new MinimumRiskPoint(MyRobot);
    }

    public AntiGravity_wMinimumRiskPoint(AdvancedRobot MyRobot) {
        super(MyRobot);
        MinimumRiskPoint = new MinimumRiskPoint(MyRobot);
    }

    public AntiGravity_wMinimumRiskPoint(TeamRobot MyRobot) {
        super(MyRobot);
        MinimumRiskPoint = new MinimumRiskPoint(MyRobot);
    }

    public void doMovement(EnemyData[] EnemyData) {
        Point2D point = new Point2D.Double();
        point.setLocation(MinimumRiskPoint.getMinimumRiskPoint(EnemyData));
        GravityEngine.addPoint(new kid.Data.Gravity.GravityPoint(point.getX(), point.getY(), 25.0, MyRobot.getTime(),
                10));
        super.doMovement(EnemyData);
    }

    public void inEvent(Event e) {
        MinimumRiskPoint.inEvent(e);
        super.inEvent(e);
    }

}
