package kid.Movement.Melee;

import kid.Data.Robot.*;
import robocode.*;
import java.awt.geom.Point2D;
import kid.Utils;
import java.awt.geom.Rectangle2D;
import kid.Data.Virtual.VirtualBullet;
import kid.RobocodeGraphicsDrawer;
import java.awt.Color;

public class MinimumRiskPoint extends MeleeMovement {

    private Point2D nextPosition;
    // private Point2D lastPosition;
    private Rectangle2D battleField;

    private int NUM_OF_GENERATED_POINTS = 50;


    public MinimumRiskPoint(Robot MyRobot) {
        super(MyRobot);
        nextPosition = new Point2D.Double(MyRobot.getX(), MyRobot.getY());
        // lastPosition = new Point2D.Double(MyRobot.getX(), MyRobot.getY());
        battleField = new Rectangle2D.Double(30, 30, MyRobot.getBattleFieldWidth() - 60,
                MyRobot.getBattleFieldHeight() - 60);
    }

    public MinimumRiskPoint(AdvancedRobot MyRobot) {
        super(MyRobot);
        nextPosition = new Point2D.Double(MyRobot.getX(), MyRobot.getY());
        // lastPosition = new Point2D.Double(MyRobot.getX(), MyRobot.getY());
        battleField = new Rectangle2D.Double(30, 30, MyRobot.getBattleFieldWidth() - 60,
                MyRobot.getBattleFieldHeight() - 60);
    }

    public MinimumRiskPoint(TeamRobot MyRobot) {
        super(MyRobot);
        nextPosition = new Point2D.Double(MyRobot.getX(), MyRobot.getY());
        // lastPosition = new Point2D.Double(MyRobot.getX(), MyRobot.getY());
        battleField = new Rectangle2D.Double(30, 30, MyRobot.getBattleFieldWidth() - 60,
                MyRobot.getBattleFieldHeight() - 60);
    }


    public void doMovement(EnemyData[] EnemyData) {
        Robot.setMoveToPoint(getMinimumRiskPoint(EnemyData));
    }

    public void doMovement(EnemyData[] EnemyData, TeammateData[] TeammateData, VirtualBullet[] VirtualBullets) {
        Robot.setMoveToPoint(getMinimumRiskPoint(EnemyData, TeammateData, VirtualBullets));
    }

    public void inEvent(Event e) {
    }


    public Point2D getMinimumRiskPoint(EnemyData[] EnemyData) {
        double mx = MyRobot.getX(), my = MyRobot.getY();
        double enemydist = Double.POSITIVE_INFINITY;
        for (int b = 0; b < EnemyData.length; b++) {
            EnemyData bot = EnemyData[b];
            if (bot.isDead())
                continue;
            double botdist = i.DistToSq(bot);
            if (botdist < enemydist) {
                enemydist = botdist;
            }
        }
        enemydist = Math.sqrt(enemydist);
        double nextdist = nextPosition.distanceSq(mx, my);

        Point2D point;
        double pointrisk = Double.POSITIVE_INFINITY;
        if (nextdist > 20 * 20 && enemydist > 200)
            pointrisk = getPointRisk(nextPosition, EnemyData);// * .75;

        for (int p = 0; p < NUM_OF_GENERATED_POINTS; p++) {
            double dist = Utils.random(enemydist / 4, enemydist);
            double angle = 360 * Math.random();
            point = new Point2D.Double(Utils.getX(mx, dist, angle), Utils.getY(my, dist, angle));
            double risk = getPointRisk(point, EnemyData);
            if (battleField.contains(point) && risk < pointrisk) {
                nextPosition = point;
                pointrisk = risk;
            }
        }
        return nextPosition;
    }

    public Point2D getMinimumRiskPoint(EnemyData[] EnemyData, TeammateData[] TeammateData,
            VirtualBullet[] VirtualBullets) {
        double myX = MyRobot.getX();
        double myY = MyRobot.getY();
        double distto = Double.POSITIVE_INFINITY;
        for (int b = 0; b < EnemyData.length; b++) {
            EnemyData bot = EnemyData[b];
            if (bot.isDead())
                continue;
            double distToBot = i.DistToSq(bot);
            if (distToBot < distto) {
                distto = distToBot;
            }
        }
        for (int b = 0; b < TeammateData.length; b++) {
            TeammateData bot = TeammateData[b];
            if (bot.isDead())
                continue;
            double distToBot = i.DistToSq(bot);
            if (distToBot < distto) {
                distto = distToBot;
            }
        }
        if (VirtualBullets != null) {
            for (int b = 0; b < VirtualBullets.length; b++) {
                VirtualBullet vb = VirtualBullets[b];
                if (vb.testActive(i.getTime()))
                    continue;
                double distToBot = i.DistToSq(vb);
                if (distToBot < distto) {
                    distto = distToBot;
                }
            }
        }

        distto = Math.sqrt(distto);
        double distToNextDestinationSq = nextPosition.distanceSq(myX, myY);

        Point2D testPoint;
        double evaluate = Double.POSITIVE_INFINITY;

        if (distToNextDestinationSq > 20 * 20 && distto > 200) {
            testPoint = nextPosition;
            evaluate = getPointRisk(testPoint, EnemyData, TeammateData, VirtualBullets);// *
            // .75;
        }

        for (int p = 0; p < NUM_OF_GENERATED_POINTS; p++) {
            double dist = Utils.random(distto / 2, distto);
            double angle = 360 * Math.random();

            testPoint = new Point2D.Double(Utils.getX(myX, dist, angle), Utils.getY(myY, dist, angle));
            double evaluateOfTestPoint = getPointRisk(testPoint, EnemyData, TeammateData, VirtualBullets);
            if (battleField.contains(testPoint) && evaluateOfTestPoint < evaluate) {
                nextPosition = testPoint;
                // lastPosition = new Point2D.Double(myX, myY);
                evaluate = evaluateOfTestPoint;
            }
        }
        return nextPosition;

    }


    private double getPointRisk(Point2D p, EnemyData[] EnemyData) {
        double mx = i.getX(), my = i.getY();
        double angle = Utils.getAngle(mx, my, p.getX(), p.getY());
        double risk = 0.0;
        for (int b = 0; b < EnemyData.length; b++) {
            EnemyData Enemy = EnemyData[b];
            if (!Enemy.isDead()) {
                double botrisk = 100 / p.distanceSq(Enemy.getX(), Enemy.getY());
                botrisk *= (1 + Math.abs(Utils.cos(angle - Utils.getAngle(mx, my, Enemy.getX(), Enemy.getY()))));
                risk += botrisk;
            }
        }
        risk += 2 / p.distanceSq(i.getBattleFieldWidth(), i.getBattleFieldHeight());
        risk += 2 / p.distanceSq(0, i.getBattleFieldHeight());
        risk += 2 / p.distanceSq(0, 0);
        risk += 2 / p.distanceSq(i.getBattleFieldWidth(), 0);

        // risk *= Math.abs(Utils.sin(angle - i.getRobotFrontHeading())) + 1;
        // int d = (Math.abs(Utils.relative(angle - i.getRobotFrontHeading())) < Math.abs(Utils.relative(angle - i.getRobotBackHeading())) ? 1 : -1);
        // risk *= (d * i.getRobotMovingSign() == 1 ? 1 : 1.2);
        return risk;
    }

    private double getPointRisk(Point2D p, EnemyData[] EnemyData, TeammateData[] TeammateData,
            VirtualBullet[] VirtualBullets) {
        double mx = i.getX(), my = i.getY();
        double angle = Utils.getAngle(mx, my, p.getX(), p.getY());
        double risk = 0.0;
        for (int b = 0; b < EnemyData.length; b++) {
            EnemyData Enemy = EnemyData[b];
            if (!Enemy.isDead()) {
                double botrisk = 100 / p.distanceSq(Enemy.getX(), Enemy.getY());
                botrisk *= (1 + Math.abs(Utils.cos(angle - Utils.getAngle(mx, my, Enemy.getX(), Enemy.getY()))));
                risk += botrisk;
            }
        }
        for (int b = 0; b < TeammateData.length; b++) {
            TeammateData Teammate = TeammateData[b];
            if (!Teammate.isDead()) {
                double botrisk = 75 / p.distanceSq(Teammate.getX(), Teammate.getY());
                // botrisk *= (1 + Math.abs(Utils.cos(angle - Utils.getAngle(mx,
                // my, Teammate.getX(), Teammate.getY()))));
                risk += botrisk;
            }
        }
        if (VirtualBullets != null) {
            long t = i.getTime();
            for (int b = 0; b < VirtualBullets.length; b++) {
                VirtualBullet VirtualBullet = VirtualBullets[b];
                if (!VirtualBullet.testActive(i.getTime())) {
                    double botrisk = 90 / p.distanceSq(VirtualBullet.getX(t), VirtualBullet.getY(t));
                    // botrisk *= (1 + Math.abs(Utils.cos(angle
                    // - Utils.getAngle(mx, my, VirtualBullet.getX(t),
                    // VirtualBullet.getY(t)))));
                    risk += botrisk;
                }
            }
        }
        risk += 3 / p.distanceSq(i.getBattleFieldWidth(), i.getBattleFieldHeight());
        risk += 3 / p.distanceSq(0, i.getBattleFieldHeight());
        risk += 3 / p.distanceSq(0, 0);
        risk += 3 / p.distanceSq(i.getBattleFieldWidth(), 0);

        // risk *= Math.abs(Utils.sin(angle - i.getRobotFrontHeading())) + 1;
        int d = (Math.abs(Utils.relative(angle - i.getRobotFrontHeading())) < Math.abs(Utils.relative(angle
                - i.getRobotBackHeading())) ? 1 : -1);
        risk *= (d * i.getRobotMovingSign() == 1 ? 1 : 1.2);
        return risk;
    }


    public void drawMovement(RobocodeGraphicsDrawer g, EnemyData[] EnemyData) {
        g.setColor(Color.GREEN);
        getMinimumRiskPoint(EnemyData);
        g.fillOval((int) nextPosition.getX(), (int) nextPosition.getY(), 5, 5);
    }

    public void drawMovement(RobocodeGraphicsDrawer g, EnemyData[] EnemyData, TeammateData[] TeammateData,
            VirtualBullet[] VirtualBullets) {
        g.setColor(Color.GREEN);
        getMinimumRiskPoint(EnemyData, TeammateData, VirtualBullets);
        g.fillOval((int) nextPosition.getX(), (int) nextPosition.getY(), 5, 5);
    }

}