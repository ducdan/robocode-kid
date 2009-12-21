package kid.Movement.OneOnOne;

import kid.Data.Robot.*;
import kid.Movement.*;
import kid.Movement.OneOnOne.*;
import robocode.*;
import robocode.Robot;

public class Perpendicular extends OneOnOneMovement {

    private double SafeDist;

    public Perpendicular(Robot MyRobot) {
        super(MyRobot);
        SafeDist = Math.min(MyRobot.getBattleFieldHeight(), MyRobot.getBattleFieldWidth()) / 1.5;
    }

    public Perpendicular(AdvancedRobot MyRobot) {
        super(MyRobot);
        SafeDist = Math.min(MyRobot.getBattleFieldHeight(), MyRobot.getBattleFieldWidth()) / 1.5;
    }

    public Perpendicular(TeamRobot MyRobot) {
        super(MyRobot);
        SafeDist = Math.min(MyRobot.getBattleFieldHeight(), MyRobot.getBattleFieldWidth()) / 1.5;
    }

    public void doMovement(EnemyData EnemyData) {
        if (MyRobot instanceof AdvancedRobot) {
            if (EnemyData != null)
                Movement.setAhead(Double.POSITIVE_INFINITY *
                                  Movement.setTurnPerpenToRobotwBFwDC(EnemyData, safeDist(SafeDist, EnemyData)));
            Movement.setTurnToSmoothWalls(); // makes robot skip 1.5% of turns
        } else {
            //if (EnemyData != null)
            //    Movement.setAhead(Double.POSITIVE_INFINITY * Movement.setTurnPerpenToRobotwBFwDC(EnemyData, SafeDist));
            //Movement.setTurnToSmoothWalls(); // makes robot skip 1.5% of turns
        }
    }

    public void inEvent(Event e) {}

    private double safeDist(double SafeDist, EnemyData Enemy) {
        double EnergyFacter = Enemy.getEnergy() / MyRobot.getEnergy();
        EnergyFacter = (EnergyFacter > 1.2 ? 1.2 : EnergyFacter);
        EnergyFacter = (EnergyFacter < 0.8 ? 0.8 : EnergyFacter);
        return EnergyFacter * SafeDist;
    }

}
