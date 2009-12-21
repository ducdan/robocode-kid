package kid.Targeting;

import kid.Data.Robot.EnemyData;
import java.awt.Color;

public interface Targeting {

    public abstract double getTargetingAngle(EnemyData EnemyRobot, double FirePower);

    public abstract String getNameOfTargeting();

    public abstract String getTypeOfTargeting();

    public abstract Color getTargetingColor();

}
