package kid.Managers;

import java.io.*;
import java.util.zip.*;
import java.io.EOFException;

import kid.*;
import kid.Communication.*;
import kid.Data.*;
import kid.Data.Robot.*;
import kid.Data.Virtual.*;
import kid.Targeting.*;
import robocode.*;

/**
 * <p>Title: DataManager</p>
 *
 * <p>Description: </p>
 *
 * @todo (4) make virtual guns really fast, they are skip about 1.1% per gun per enemy
 * @author KID
 * @version 0.1b
 */
public class DataManager {

    private static MyRobotsInfo i;
    private static AdvancedRobot MyRobot = null;

    private static int NumOfRobots;
    private static int NumOfEnemys;
    private static int NumOfTeammates;

    private static final int RobotNotFound = -1;
    private static int EnemyCount = 0;
    private static EnemyData[] EnemyData;
    private static int TeammateCount = 0;
    private static TeammateData[] TeammateData;

    private VirtualBullet[] Bullets;

    private static boolean isGuessFactor = false;

    private static boolean isPatternMatching = false;

    private static boolean areVirtualGuns = false;
    private static Targeting[] VirtualGuns = null;


    /**
     * Saved from getRobots()
     */
    private RobotData[] Robots;
    private long Time_Robots = -1;

    /**
     * Saved from getEnemys()
     */
    private EnemyData[] Enemys;
    private long Time_Enemys = -1;

    /**
     * Saved from getTeammates()
     */
    private TeammateData[] Teammates;
    private long Time_Teammates = -1;

    /**
     * Saved from getColsestEnemy()
     */
    private EnemyData ColsestEnemy = new EnemyData();
    private long Time_ColsestEnemy = -1;

    /**
     * Saved from getSmallestRiskEnemy()
     */
    private EnemyData SmallestRiskEnemy = new EnemyData();
    private long Time_SmallestRiskEnemy = -1;


    public DataManager() {}

    public DataManager(Robot MyRobot) {
        this(MyRobot, null, null);
    }

    public DataManager(AdvancedRobot MyRobot) {
        this(null, MyRobot, null);
        this.MyRobot = MyRobot;
    }

    public DataManager(TeamRobot MyRobot) {
        this(null, null, MyRobot);
        this.MyRobot = MyRobot;
    }

    public DataManager(Robot robot, AdvancedRobot advancedRobot, TeamRobot teamRobot) {
        i = new MyRobotsInfo(robot, advancedRobot, teamRobot);
        if (robot != null || advancedRobot != null || teamRobot != null)
            setUpDataManager();
    }


    public void UpDateRobot(Robot MyRobot) {
        i = new MyRobotsInfo(MyRobot);
        setUpDataManager();
    }

    public void UpDateRobot(AdvancedRobot MyRobot) {
        if (this.MyRobot == null || this.MyRobot.getName() != MyRobot.getName()) {
            i = new MyRobotsInfo(MyRobot);
            this.MyRobot = MyRobot;
            setUpDataManager();
        }
    }

    public void UpDateRobot(TeamRobot MyRobot) {
        if (this.MyRobot == null || this.MyRobot.getName() != MyRobot.getName()) {
            i = new MyRobotsInfo(MyRobot);
            this.MyRobot = MyRobot;
            setUpDataManager();
        }
    }


    private void setUpDataManager() {
        NumOfRobots = i.getOthers();
        NumOfEnemys = i.getEnemys();
        NumOfTeammates = i.getTeammates();

        EnemyData = new EnemyData[NumOfEnemys];
        EnemyCount = 0;
        for (int b = 0; b < EnemyData.length; b++)
            EnemyData[b] = new EnemyData();
        TeammateData = new TeammateData[NumOfTeammates];
        TeammateCount = 0;
        for (int b = 0; b < TeammateData.length; b++)
            TeammateData[b] = new TeammateData();
    }

    public void updateEnemyDataFromFile(ScannedRobotEvent SRE) throws Exception {
        if (!(isGuessFactor || isPatternMatching || areVirtualGuns))
            throw new Exception();
        try {
            // sumtime from here to...
            ObjectInputStream in;
            String name = SRE.getName();
            int end = name.indexOf(" ");
            if (end != -1) {
                name = name.substring(0, end);
            }
            in = new ObjectInputStream(new FileInputStream(i.getDataFile(name + ".dat")));
            //zipin.getNextEntry();
            EnemyData[EnemyCount] = (EnemyData) in.readObject();
            EnemyData[EnemyCount].updateItemFromFile(SRE.getName(),
                    Utils.getX(i.getX(), SRE.getDistance(), SRE.getBearing() + i.getRobotFrontHeading()),
                    Utils.getY(i.getY(), SRE.getDistance(), SRE.getBearing() + i.getRobotFrontHeading()), SRE.getEnergy(),
                    SRE.getHeading(), SRE.getVelocity(), SRE.getTime());
            in.close();
            // here, the Exception gets throwen and my energy drops to zero.
            MyRobot.out.println("Loaded " + SRE.getName() + " from file");
        } catch (java.io.FileNotFoundException Exception) {
            throw Exception;
        } catch (java.io.EOFException Exception) {
            throw Exception;
        }
    }

    public void saveEnemyDataToFile() {
        if (!(isGuessFactor || isPatternMatching || areVirtualGuns))
            return;
        ObjectOutputStream out = null;
        for (int e = 0; e < EnemyData.length; e++) {
            saveEnemyToFile(EnemyData[e], out);
        }
    }

    private void saveEnemyToFile(EnemyData enemy, ObjectOutputStream out) {
        try {
            String name = enemy.getName();
            int end = name.indexOf(" ");
            if (end != -1) {
                name = name.substring(0, end);
            }
            out = new ObjectOutputStream(new RobocodeFileOutputStream(i.getDataFile(name + ".dat")));
            out.writeObject(enemy);
            out.flush();
            out.close();
            MyRobot.out.println("Saved " + enemy.getName() + " to file");
        } catch (java.io.EOFException Exception) {
        } catch (IOException ex) {}
    }

    public void inEvent(Event e) {
        if (e instanceof ScannedRobotEvent) {
            ScannedRobotEvent SRE = (ScannedRobotEvent) e;
            if (i.isTeammate(SRE.getName())) {
                try {
                    TeammateData[getTeammateNum(SRE.getName())].updateItem(Utils.getX(i.getX(), SRE.getDistance(),
                            SRE.getBearing() + i.getRobotFrontHeading()),
                            Utils.getY(i.getY(), SRE.getDistance(), SRE.getBearing() + i.getRobotFrontHeading()),
                             SRE.getEnergy(), SRE.getHeading(), SRE.getVelocity(), SRE.getTime());
                } catch (ArrayIndexOutOfBoundsException AE) {
                    TeammateData[TeammateCount++] = new TeammateData(SRE.getName(),
                            Utils.getX(i.getX(), SRE.getDistance(), SRE.getBearing() + i.getRobotFrontHeading()),
                            Utils.getY(i.getY(), SRE.getDistance(), SRE.getBearing() + i.getRobotFrontHeading()),
                            SRE.getEnergy(), SRE.getHeading(), SRE.getVelocity(), SRE.getTime());
                }
            } else {
                try {
                    EnemyData[getEnemyNum(SRE.getName())].updateItem(Utils.getX(i.getX(), SRE.getDistance(),
                            SRE.getBearing() + i.getRobotFrontHeading()),
                            Utils.getY(i.getY(), SRE.getDistance(), SRE.getBearing() + i.getRobotFrontHeading()),
                            SRE.getEnergy(), SRE.getHeading(), SRE.getVelocity(), SRE.getTime());
                } catch (ArrayIndexOutOfBoundsException AE) {
                    try {
                        updateEnemyDataFromFile(SRE);
                    } catch (Exception Exception) {
                        EnemyData[EnemyCount] = new EnemyData(SRE.getName(),
                                Utils.getX(i.getX(), SRE.getDistance(), SRE.getBearing() + i.getRobotFrontHeading()),
                                Utils.getY(i.getY(), SRE.getDistance(), SRE.getBearing() + i.getRobotFrontHeading()),
                                SRE.getEnergy(), SRE.getHeading(), SRE.getVelocity(), SRE.getTime());
                    }
                    if (isGuessFactor) {
                        EnemyData[EnemyCount].addGuessFactor(MyRobot);
                    }
                    if (areVirtualGuns) {
                        EnemyData[EnemyCount].addVirtualGuns(MyRobot, VirtualGuns);
                    }
                    if (isPatternMatching) {
                        EnemyData[EnemyCount].addPatternMatching();
                    }
                    EnemyCount++;
                }
            }
        } else if (e instanceof RobotDeathEvent) {
            RobotDeathEvent RDE = (RobotDeathEvent) e;
            getRobot(RDE.getName()).setDeath();
        } else if (e instanceof MessageEvent) {
            MessageEvent ME = (MessageEvent) e;
            if (ME.getMessage() instanceof Data) {
                Data Message = (Data) ME.getMessage();
                try {
                    TeammateData[getTeammateNum(Message.getTeammate().getName())].updateItemFromTeammate(Message.
                            getTeammate());
                } catch (ArrayIndexOutOfBoundsException AE) {
                    TeammateData[TeammateCount++] = Message.getTeammate();
                }
            }
        } else if (e instanceof DeathEvent) {
            DeathEvent DE = (DeathEvent) e;
            saveEnemyDataToFile();
        } else if (e instanceof WinEvent) {
            WinEvent WE = (WinEvent) e;
            saveEnemyDataToFile();
        }
    }

    public void AddGuessFactor() {
        isGuessFactor = true;
        for (int b = 0; b < EnemyCount; b++)
            EnemyData[b].addGuessFactor(MyRobot);
    }

    public void UpdateGuessFactor(double FirePower) {
        if (isGuessFactor)
            for (int b = 0; b < EnemyCount; b++)
                if (!EnemyData[b].isDead())
                    EnemyData[b].updateGuessFatorGun(FirePower);
    }


    public void AddPatternMatching() {
        isPatternMatching = true;
        for (int b = 0; b < EnemyCount; b++)
            EnemyData[b].addPatternMatching();
    }


    public void AddVirtualGuns(Targeting[] guns) {
        if (guns != VirtualGuns) {
            areVirtualGuns = true;
            VirtualGuns = guns;
            for (int b = 0; b < EnemyCount; b++)
                EnemyData[b].addVirtualGuns(MyRobot, guns);
        }
    }

    public void UpdateVirtualGuns(double FirePower) {
        if (areVirtualGuns)
            for (int b = 0; b < EnemyCount; b++)
                if (!EnemyData[b].isDead())
                    EnemyData[b].updateVirtualGuns(FirePower);
    }


    public void drawTargetingNames(kid.GraphicsDrawer g) {
        for (int gun = 0; gun < VirtualGuns.length; gun++) {
            g.setColor(VirtualGuns[gun].getTargetingColor());
            g.drawRect(50, 7 + 12 * gun + 1 * gun, 100, 12);
            g.fillOval(5, 7 + 12 * gun + 1 * gun, 5, 5);
            g.setColor(Colors.WHITE);
            g.drawString(VirtualGuns[gun].getNameOfTargeting(), 10,
                         (int) MyRobot.getBattleFieldHeight() - (12 * gun + 1 * gun + 3));
        }
    }

    public void drawEnemys(kid.GraphicsDrawer g) {
        for (int e = 0; e < EnemyCount; e++) {
            g.setColor(Colors.GREEN);
            EnemyData enemy = EnemyData[e];
            if (enemy.isDead())
                continue;
            g.drawRect((int) enemy.getX(), (int) enemy.getY(), (int) MyRobotsInfo.WIDTH, (int) MyRobotsInfo.HEIGHT);
        }
    }

    public void drawColsestEnemy(kid.GraphicsDrawer g) {
        if (!getColsestEnemy().isDead()) {
            g.setColor(Colors.RED);
            g.drawRect((int) getColsestEnemy().getX(), (int) getColsestEnemy().getY(), (int) MyRobotsInfo.WIDTH,
                       (int) MyRobotsInfo.HEIGHT);
        }
    }


    public EnemyData getColsestEnemy() {
        if (i.getTime() != Time_ColsestEnemy) {
            double dist = Double.POSITIVE_INFINITY;
            for (int b = 0; b < EnemyCount; b++) {
                EnemyData bot = EnemyData[b];
                if (bot.isDead())
                    continue;
                double distToBot = i.DistToSq(bot);
                if (distToBot < dist) {
                    dist = distToBot;
                    ColsestEnemy = bot;
                }
            }
            Time_ColsestEnemy = i.getTime();
        }
        return ColsestEnemy;
    }

    public EnemyData getSmallestRiskEnemy() {
        if (i.getTime() != Time_SmallestRiskEnemy) {
            double risk = Double.POSITIVE_INFINITY;
            for (int b = 0; b < EnemyCount; b++) {
                EnemyData bot = EnemyData[b];
                if (bot.isDead())
                    continue;
                double riskOfBot = bot.getEnergy() / i.getEnergy();
                riskOfBot = 1 / ((riskOfBot > 2 ? 2 : riskOfBot) * i.DistToSq(bot.getX(), bot.getY()));
                if (riskOfBot < risk) {
                    risk = riskOfBot;
                    SmallestRiskEnemy = bot;
                }
            }
            Time_SmallestRiskEnemy = i.getTime();
        }
        return SmallestRiskEnemy;
    }


    public RobotData[] getRobots() {
        if (i.getTime() == Time_Robots) {
            return Robots;
        } else {
            RobotData[] robots = new RobotData[EnemyCount + TeammateCount];
            int bots = 0;
            for (int b = 0; b < EnemyCount; bots++, b++)
                robots[bots] = EnemyData[b];
            for (int b = 0; b < TeammateCount; bots++, b++)
                robots[bots] = TeammateData[b];
            Robots = robots;
            Time_Robots = i.getTime();
            return robots;
        }
    }

    public EnemyData[] getEnemys() {
        if (i.getTime() == Time_Enemys) {
            return Enemys;
        } else {
            EnemyData[] enemys = new EnemyData[EnemyCount];
            for (int b = 0; b < EnemyCount; b++)
                enemys[b] = EnemyData[b];
            Enemys = enemys;
            Time_Enemys = i.getTime();
            return enemys;
        }
    }

    public TeammateData[] getTeammates() {
        if (i.getTime() == Time_Teammates) {
            return Teammates;
        } else {
            TeammateData[] teammates = new TeammateData[TeammateCount];
            for (int b = 0; b < TeammateCount; b++)
                teammates[b] = TeammateData[b];
            Teammates = teammates;
            Time_Teammates = i.getTime();
            return teammates;
        }
    }


    private int getRobotNum(String name) {
        if (getEnemyNum(name) != RobotNotFound)
            return getEnemyNum(name);
        else
            return getTeammateNum(name);
    }

    private int getEnemyNum(String name) {
        for (int b = 0; b < EnemyCount; b++) {
            if (name.equals(EnemyData[b].getName()))
                return b;
        }
        return RobotNotFound;
    }

    private int getTeammateNum(String name) {
        for (int b = 0; b < TeammateCount; b++) {
            if (name.equals(TeammateData[b].getName()))
                return b;
        }
        return RobotNotFound;
    }


    public RobotData getRobot(String name) {
        if (getRobotNum(name) != RobotNotFound) {
            if (i.isTeammate(name))
                return getTeammate(name);
            return getEnemy(name);
        }
        return new EnemyData();
    }

    public EnemyData getEnemy(String name) {
        if (getEnemyNum(name) != RobotNotFound)
            return EnemyData[getEnemyNum(name)];
        return new EnemyData();
    }

    public TeammateData getTeammate(String name) {
        if (getTeammateNum(name) != RobotNotFound)
            return TeammateData[getTeammateNum(name)];
        return new TeammateData();
    }

    public int getNumEnemys() {
        return NumOfEnemys;
    }


    private class UpdateVirtualGuns extends Condition {
        public boolean test() {
            return false;
        }
    }

}
