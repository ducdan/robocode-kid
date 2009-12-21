package kid.Movement.Melee;

import kid.Data.Robot.*;
import kid.Movement.Melee.*;
import robocode.*;
import java.awt.geom.Point2D;
import kid.Utils;
import java.awt.geom.Rectangle2D;

public class MinimumRiskPoint extends MeleeMovement {

    Point2D nextPosition;
    Point2D lastPosition;

    public MinimumRiskPoint(Robot MyRobot) {
        super(MyRobot);
        nextPosition = new Point2D.Double(MyRobot.getX(), MyRobot.getY());
        lastPosition = new Point2D.Double(MyRobot.getX(), MyRobot.getY());
    }

    public MinimumRiskPoint(AdvancedRobot MyRobot) {
        super(MyRobot);
        nextPosition = new Point2D.Double(MyRobot.getX(), MyRobot.getY());
        lastPosition = new Point2D.Double(MyRobot.getX(), MyRobot.getY());
    }

    public MinimumRiskPoint(TeamRobot MyRobot) {
        super(MyRobot);
        nextPosition = new Point2D.Double(MyRobot.getX(), MyRobot.getY());
        lastPosition = new Point2D.Double(MyRobot.getX(), MyRobot.getY());
    }

    public void doMovement(EnemyData[] EnemyData) {
        Robot.setMoveToPoint(getMinimumRiskPoint(EnemyData));
    }

    public void inEvent(Event e) {}

    public Point2D getMinimumRiskPoint(EnemyData[] EnemyData) {
        double myX = MyRobot.getX();
        double myY = MyRobot.getY();
        double distanceToNextDestinationSq = nextPosition.distanceSq(myX, myY);
        if (distanceToNextDestinationSq < 250) {
            Rectangle2D.Double battleField = new Rectangle2D.Double(30, 30, MyRobot.getBattleFieldWidth() - 60,
                    MyRobot.getBattleFieldHeight() - 60);
            Point2D.Double testPoint;
            double evaluate = Double.POSITIVE_INFINITY;
            for (int i = 0; i < 200; i++) {
                double dist = 100 + 200 * Math.random();
                double angle = 360.0 * Math.random();
                testPoint = new Point2D.Double(Utils.getX(myX, dist, angle), Utils.getY(myY, dist, angle));
                double evaluateOfTestPoint = getPointRisk(testPoint, EnemyData);
                if (battleField.contains(testPoint) && evaluateOfTestPoint < evaluate) {
                    nextPosition = testPoint;
                    evaluate = evaluateOfTestPoint;
                }
            }
            lastPosition = new Point2D.Double(myX, myY);
        }
        return nextPosition;
    }

    private double getPointRisk(Point2D p, EnemyData[] EnemyData) {
        double risk = 0.0;
        for (int b = 0; b < EnemyData.length; b++) {
            EnemyData Enemy = EnemyData[b];
            if (!Enemy.isDead())
                risk += Enemy.getEnergy() / p.distanceSq(Enemy.getX(), Enemy.getY());
        }
        return risk;
    }

}
