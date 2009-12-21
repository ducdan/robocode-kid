package kid.Managers;

import robocode.*;
import kid.Movement.OneOnOne.OneOnOneMovement;
import kid.Movement.Melee.MeleeMovement;
import kid.Data.MyRobotsInfo;
import kid.Movement.RobotMovement;
import kid.RobocodeGraphicsDrawer;

/**
 * <p>Title: MovementManager</p>
 *
 * <p>Description: </p>
 *
 * @author KID
 * @version 0.1b
 */
public class MovementManager {

    OneOnOneMovement OneOnOne;
    MeleeMovement Melee;

    RobotMovement Robot;

    MyRobotsInfo i;

    public MovementManager(Robot MyRobot) {
        this(MyRobot, null, null, null, null);
    }

    public MovementManager(AdvancedRobot MyRobot) {
        this(null, MyRobot, null, null, null);
    }

    public MovementManager(TeamRobot MyRobot) {
        this(null, null, MyRobot, null, null);
    }

    public MovementManager(Robot MyRobot, OneOnOneMovement OneOnOneMovement) {
        this(MyRobot, null, null, OneOnOneMovement, null);
    }

    public MovementManager(AdvancedRobot MyRobot, OneOnOneMovement OneOnOneMovement) {
        this(null, MyRobot, null, OneOnOneMovement, null);
    }

    public MovementManager(TeamRobot MyRobot, OneOnOneMovement OneOnOneMovement) {
        this(null, null, MyRobot, OneOnOneMovement, null);
    }

    public MovementManager(Robot MyRobot, OneOnOneMovement OneOnOneMovement, MeleeMovement MeleeMovement) {
        this(MyRobot, null, null, OneOnOneMovement, MeleeMovement);
    }

    public MovementManager(AdvancedRobot MyRobot, OneOnOneMovement OneOnOneMovement, MeleeMovement MeleeMovement) {
        this(null, MyRobot, null, OneOnOneMovement, MeleeMovement);
    }

    public MovementManager(TeamRobot MyRobot, OneOnOneMovement OneOnOneMovement, MeleeMovement MeleeMovement) {
        this(null, null, MyRobot, OneOnOneMovement, MeleeMovement);
    }

    private MovementManager(Robot Robot, AdvancedRobot AdvancedRobot, TeamRobot TeamRobot,
                            OneOnOneMovement OneOnOneMovement, MeleeMovement MeleeMovement) {
        this.Robot = new RobotMovement(Robot, AdvancedRobot, TeamRobot);
        i = new MyRobotsInfo(Robot, AdvancedRobot, TeamRobot);
        OneOnOne = OneOnOneMovement;
        Melee = MeleeMovement;
    }

    public void inEvent(Event e) {
        Melee.inEvent(e);
        OneOnOne.inEvent(e);
    }

    public void doMovement(DataManager Data) {
        if (isMelee())
            if (i.getTeammates() == 0)
                Melee.doMovement(Data.getEnemys());
            else
                Melee.doMovement(Data.getEnemys(), Data.getTeammates(), Data.getBullets());
        else {
            OneOnOne.doMovement(Data.getColsestEnemy());
        }
    }

    public void drawMovement(RobocodeGraphicsDrawer g, DataManager Data) {
        if (isMelee()) {
            if (i.getTeammates() == 0) {
                Melee.drawMovement(g, Data.getEnemys());
            } else
                Melee.drawMovement(g, Data.getEnemys(), Data.getTeammates(), Data.getBullets());
        } else {
            OneOnOne.drawMovement(g, Data.getColsestEnemy());
        }
    }


    public boolean isMelee() {
        return i.getEnemys() > 1;
    }

    public boolean isOneOnOme() {
        return i.getEnemys() == 1;
    }

}
