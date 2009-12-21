package kid.Movement.Melee;

import robocode.*;
import kid.Data.*;
import kid.Data.Robot.*;
import kid.Movement.*;

public abstract class MeleeMovement {

    protected Robot MyRobot;
    protected MyRobotsInfo i;
    protected RobotMovement Robot;

    protected MeleeMovement(Robot MyRobot) {
        this.MyRobot = MyRobot;
        Robot = new RobotMovement(MyRobot);
        i = new MyRobotsInfo(MyRobot);
    }

    protected MeleeMovement(AdvancedRobot MyRobot) {
        this((Robot) MyRobot);
        Robot = new RobotMovement(MyRobot);
        i = new MyRobotsInfo(MyRobot);
    }

    protected MeleeMovement(TeamRobot MyRobot) {
        this((Robot) MyRobot);
        Robot = new RobotMovement(MyRobot);
        i = new MyRobotsInfo(MyRobot);
    }

    public abstract void doMovement(EnemyData[] EnemyData);

    public abstract void inEvent(Event e);

}
