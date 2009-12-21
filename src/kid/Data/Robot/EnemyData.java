package kid.Data.Robot;

import robocode.*;
import java.util.*;
import kid.Utils;
import kid.Data.*;
import kid.Data.Virtual.*;
import kid.Data.PatternMatching.*;
import kid.Data.Virtual.*;
import kid.Targeting.*;
import kid.Colors;

public class EnemyData extends TeammateData implements java.io.Serializable {

    //***** THIS ROBOTS INFO *****//
    private String NAME;
    private String ALIAS;
    private String SCANNER;

    private double X;
    private double Y;

    private double ENERGY;
    private double DELTA_ENERGY;

    private double HEADING;
    private double DELTA_HEADING;

    private double VELOCITY;

    private long TIME;
    private int DELTA_TIME;

    private boolean FIRE_BULLET = false;
    private transient List BULLETS = new ArrayList();

    private final int AvgLength = 5;
    private int arrayByte = 0;
    private double[] AVG_HEADING = new double[AvgLength];
    private double[] AVG_VELOCITY = new double[AvgLength];

    private transient AdvancedRobot MyRobot = null;
    private transient kid.Data.MyRobotsInfo i = null;

    //***** STATISTICAL DATA *****//
    private boolean GUESS_FACTOR = false;
    private static final int NUM_OF_SECTORS = 31;
    private transient double AVG_OFFSET = 0.0;
    private transient int AVG_OFFSET_TIME = 0;
    private Sector[] SECTORS = null;
    private transient List WAVES = new ArrayList();

    //***** VIRTUAL GUNS *****//
    private boolean VIRTUAL_GUNS = false;
    private VirtualGun[] VirtualGuns = null;

    //***** PATTERN MACHING *****//
    private boolean PATTERN_MATCHING = false;
    private transient Pattern[] POLAR_PATTERN;
    private transient Pattern[] SYMBOL_PATTERN;
    private transient PatternMatcher PATTERN_MACHER_POLAR;
    private transient PatternMatcher PATTERN_MACHER_SYMBOL;
    private boolean SCAN_THIS_TURN = false;
    private int arrayInt = 0;

    public EnemyData() {
        this(new String(), 0.0, 0.0, 0.0, 0.0, 0, Long.MAX_VALUE);
        ALIAS = DEAD;
    }

    public EnemyData(String name, double x, double y, double energy, double heading, double velocity, long time) {
        NAME = name;
        updateItemFromFile(x, y, energy, heading, velocity, time);
    }

    public void addGuessFactor(AdvancedRobot MyRobot) {
        this.MyRobot = MyRobot;
        i = new MyRobotsInfo(MyRobot);
        MyRobot.addCustomEvent(new WaveWhatcher());
        WAVES = new ArrayList();
        if (GUESS_FACTOR && SECTORS.length == EnemyData.NUM_OF_SECTORS)
            return;
        MyRobot.out.println("redoing sectors");
        GUESS_FACTOR = true;
        SECTORS = new Sector[NUM_OF_SECTORS];
        for (int s = 0; s < SECTORS.length; s++)
            SECTORS[s] = new Sector();
    }

    public void addPatternMatching() {
        PATTERN_MATCHING = true;
        POLAR_PATTERN = new Pattern[Short.MAX_VALUE];
        SYMBOL_PATTERN = new Pattern[Short.MAX_VALUE];
        PATTERN_MACHER_POLAR = new PatternMatcher(POLAR_PATTERN);
        PATTERN_MACHER_SYMBOL = new PatternMatcher(SYMBOL_PATTERN);
    }

    public void addVirtualGuns(AdvancedRobot MyRobot, Targeting[] Targeting) {
        VIRTUAL_GUNS = true;
        this.MyRobot = MyRobot;
        i = new MyRobotsInfo(MyRobot);
        VirtualGuns = new VirtualGun[Targeting.length];
        for (int i = 0; i < Targeting.length; i++) {
            VirtualGuns[i] = new VirtualGun(MyRobot, this, Targeting[i]);
        }
    }

    public void addVirtualGuns(TeamRobot MyRobot, Targeting[] Targeting) {
        addVirtualGuns((AdvancedRobot) MyRobot, Targeting);
    }


    public void updateItem(double x, double y, double energy, double heading, double velocity, long time) {
        if (time < TIME) {
            updateItemFromFile(NAME, x, y, energy, heading, velocity, time);
        } else if (time != TIME) {
            X = x;
            Y = y;
            DELTA_ENERGY = energy - ENERGY;
            ENERGY = energy;
            DELTA_HEADING = Utils.relative(heading - HEADING);
            HEADING = heading;
            AVG_HEADING[(arrayByte == AvgLength ? arrayByte = 0 : arrayByte)] = DELTA_HEADING;
            VELOCITY = velocity;
            AVG_VELOCITY[(arrayByte == AvgLength ? arrayByte = 0 : arrayByte)] = VELOCITY;
            arrayByte++;
            DELTA_TIME = (int) (time - TIME);
            TIME = time;
            updatePatterns();
            FIRE_BULLET = fireBullet();
        }
    }

    public void updateItemFromFile(String name, double x, double y, double energy, double heading, double velocity,
                                   long time) {
        ALIAS = ALIVE;
        NAME = name;
        X = x;
        Y = y;
        ENERGY = energy;
        HEADING = heading;
        AVG_HEADING = new double[AvgLength];
        VELOCITY = velocity;
        AVG_VELOCITY = new double[AvgLength];
        TIME = time;
        arrayByte = 0;
    }


    public void updateGuessFatorGun(double firePower) {
        if (GUESS_FACTOR)
            WAVES.add(new DataWave(MyRobot, this, firePower));
    }

    public void updateVirtualGuns(double firePower) {
        if (VIRTUAL_GUNS)
            for (int g = 0; g < VirtualGuns.length; g++)
                VirtualGuns[g].fireVirtualBullet(firePower);
    }


    public void setDeath() {
        ENERGY = ENERGY_DEAD;
        setAlias(DEAD);
    }

    public void setAlias(String a) {
        if (ALIAS != DEAD)
            ALIAS = a;
    }

    public void setScanner(String s) {
        if (SCANNER != DEAD)
            SCANNER = s;
    }

    public VirtualGun[] getVirtualGuns() {
        VirtualGun[] guns = new VirtualGun[VirtualGuns.length];
        for (int i = 0; i < VirtualGuns.length; i++)
            guns[i] = VirtualGuns[i];
        return guns;
    }

    public VirtualGun getTopVirtualGun() {
        if (MyRobot == null)
            return null;
        double hitRate = 0.0;
        int guni = -1;
        for (int i = 0; i < VirtualGuns.length; i++) {
            double GunHitRate = VirtualGuns[i].getHitRate();
            if (GunHitRate > hitRate) {
                guni = i;
                hitRate = GunHitRate;
            }
        }
        if (guni != -1)
            return VirtualGuns[guni];
        else
            return new VirtualGun(MyRobot);
        /*   OLD
         VirtualGun TopGun = null;
         if (MyRobot != null) {
            TopGun = new VirtualGun(MyRobot);
            double hitRate = 0.0;
            for (int i = 0; i < NumVirtualGuns; i++) {
                VirtualGun Gun = VirtualGuns[i];
                if (Gun.getHitRate() > hitRate) {
                    TopGun = Gun;
                    hitRate = Gun.getHitRate();
                }
            }
          }
          return TopGun;
         */
    }

    public void printVirtualGunStatus() {
        System.out.println("This Is The Status For The VirtualGuns Of " + NAME);
        for (int i = 0; i < VirtualGuns.length; i++) {
            VirtualGuns[i].printStatus();
        }
    }


    public String getName() {
        return NAME;
    }

    public String getAlias() {
        return ALIAS;
    }

    public String getScanner() {
        return SCANNER;
    }

    public double getX() {
        return X;
    }

    public double getY() {
        return Y;
    }

    public double getEnergy() {
        return ENERGY;
    }

    public double getDeltaEnergy() {
        return DELTA_ENERGY;
    }

    public boolean didFireBullet() {
        return FIRE_BULLET;
    }

    public double getHeading() {
        return HEADING;
    }

    public double getDeltaHeading() {
        return DELTA_HEADING;
    }

    public double getAverageHeading() {
        double avg = 0.0;
        for (int i = 0; i < AVG_HEADING.length; i++) {
            avg += AVG_HEADING[i];
        }
        return avg / AvgLength;
    }

    public double getVelocity() {
        return VELOCITY;
    }

    public double getAverageVelocity() {
        double avg = 0.0;
        for (int i = 0; i < AVG_VELOCITY.length; i++) {
            avg += AVG_VELOCITY[i];
        }
        return avg / AvgLength;
    }

    public long getTime() {
        return TIME;
    }

    public int getDeltaTime() {
        return DELTA_TIME;
    }

    public Sector[] getSectors() {
        return SECTORS;
    }

    public List getWaves() {
        return WAVES;
    }

    public void drawVirtualData(kid.GraphicsDrawer g) {
        if (VirtualGuns != null) {
            for (int gun = 0; gun < VirtualGuns.length; gun++) {
                VirtualGuns[gun].drawBullets(g);
            }
        }
    }

    public void drawGuessFactorData(kid.GraphicsDrawer g) {
        if (isDead())
            return;
        double angle = Utils.getAngle(MyRobot.getX(), MyRobot.getY(), getX(), getY());
        double dist = Utils.getDist(MyRobot.getX(), MyRobot.getY(), getX(), getY()) - MyRobotsInfo.WIDTH;
        double d = (Utils.sin(getHeading() - angle) * getVelocity() < 0 ? 1 : -1);
        int bestindex = 15;
        for (int i = 0; i < SECTORS.length; i++) {
            if (SECTORS[bestindex].getHits() < SECTORS[i].getHits())
                bestindex = i;
        }
        for (int s = 0; s < SECTORS.length; s++) {
            double guessfactor = (double) (s - (SECTORS.length - 1) / 2) / ((SECTORS.length - 1) / 2);
            double angleOffset = d * guessfactor * Utils.asin(MyRobotsInfo.MAX_VELOCITY / Utils.bulletVelocity(2));
            if (s == (SECTORS.length - 1) / 2) {
                g.setColor(Colors.YELLOW);
            } else if (Utils.sign(d) == 1 && s < (SECTORS.length - 1) / 2) {
                g.setColor(Colors.GREEN);
            } else if (Utils.sign(d) == -1 && s < (SECTORS.length - 1) / 2) {
                g.setColor(Colors.GREEN);
            } else {
                g.setColor(Colors.RED);
            }
            g.fillOval((int) Utils.getX(MyRobot.getX(), dist, angle + angleOffset),
                       (int) Utils.getY(MyRobot.getY(), dist, angle + angleOffset),
                       (int) (Math.max(((double) SECTORS[s].getHits() / SECTORS[bestindex].getHits()) * 10, 1)),
                       (int) (Math.max(((double) SECTORS[s].getHits() / SECTORS[bestindex].getHits()) * 10, 1)));
        }
    }

    public void drawGuessFactorWaves(kid.GraphicsDrawer g) {
        g.setColor(Colors.DARK_GRAY);
        for (int w = 0; w < WAVES.size(); w++) {
            DataWave wave = (DataWave) WAVES.get(w);
            g.drawOval((int) wave.getStartX(), (int) wave.getStartY(), (int) (wave.getDist(MyRobot.getTime()) * 2),
                       (int) (wave.getDist(MyRobot.getTime()) * 2));
        }
    }


    public double getAvgAngleOffSet() {
        return AVG_OFFSET / AVG_OFFSET_TIME;
    }

    public Pattern getPolarPattern(long t) {
        try {
            Pattern p = POLAR_PATTERN[(short) t];
            return p;
        } catch (Exception e) {
            return new Polar();
        }
    }

    public Pattern[] getPolarPattern(int RecentMovmentLength) {
        return PATTERN_MACHER_POLAR.MatchPattern(arrayInt, RecentMovmentLength);
    }

    public Pattern getSymbolPattern(long t) {
        try {
            Pattern p = SYMBOL_PATTERN[(int) t];
            return p;
        } catch (Exception e) {
            return new Symbol();
        }
    }

    public Pattern[] getSymbolPattern(int RecentMovmentLength) {
        return PATTERN_MACHER_SYMBOL.MatchPattern(arrayInt, RecentMovmentLength);
    }

    private void updatePatterns() {
        if (DELTA_TIME > 0 && PATTERN_MATCHING) {
            for (int i = 0; i < DELTA_TIME; i++) {
                if (i == DELTA_TIME - 1)
                    SCAN_THIS_TURN = true;
                POLAR_PATTERN[arrayInt] = new Polar((DELTA_HEADING / DELTA_TIME), VELOCITY, SCAN_THIS_TURN);
                SYMBOL_PATTERN[arrayInt] = new Symbol((DELTA_HEADING / DELTA_TIME), VELOCITY, SCAN_THIS_TURN);
                arrayInt++;
                SCAN_THIS_TURN = false;
            }
        }
    }

    private boolean fireBullet() {
        if (DELTA_ENERGY >= -3.0 && DELTA_ENERGY <= -0.1 && DELTA_TIME == 1) {
            return true;
        }
        return false;
    }

    private EnemyData getThis() {
        return this;
    }

    private class WaveWhatcher extends Condition {
        public boolean test() {
            for (int w = 0; w < WAVES.size(); w++) {
                DataWave currentWave = (DataWave) WAVES.get(w);
                if (currentWave.testHit(getRobot(), getTime())) {
                    int index = Utils.getIndex(currentWave.getGuessFactor(getRobot()), SECTORS.length);
                    AVG_OFFSET += Utils.getAngleOffset(getThis(), currentWave);
                    AVG_OFFSET_TIME++;
                    if (getEnergy() != 0.0)
                        SECTORS[index].addHit();
                    WAVES.remove(w);
                    w--;
                }
            }
            return false;
        }
    }

}
