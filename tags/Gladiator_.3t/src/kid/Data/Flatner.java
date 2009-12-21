package kid.Data;

import robocode.*;
import java.util.*;
import kid.Data.Robot.EnemyData;
import kid.Data.Virtual.EnemyWave;
import kid.Managers.DataManager;
import kid.*;

public class Flatner {

    private AdvancedRobot MyRobot = null;
    private DataManager Data;
    private List Waves = new ArrayList();

    private final int NumOfSectors = 51;
    private Sector[] Sectors = new Sector[NumOfSectors];


    public Flatner(AdvancedRobot MyRobot) {
        this.MyRobot = MyRobot;
        for (int s = 0; s < Sectors.length; s++)
            Sectors[s] = new Sector();
    }

    public Flatner(TeamRobot MyRobot) {
        this.MyRobot = MyRobot;
        for (int s = 0; s < Sectors.length; s++)
            Sectors[s] = new Sector();
    }


    public void startDataProsseser(DataManager Data) {
        this.Data = Data;
        MyRobot.addCustomEvent(new DataProsseser());
    }


    public Sector[] getSectors() {
        return Sectors;
    }


    public EnemyWave getMaxRiskBulletWave() {
        EnemyWave wave;
        for (int w = 0; w < Waves.size(); w++) {
            wave = (EnemyWave) Waves.get(w);
            if (wave.getFirePower() != 0.0)
                return wave;
        }
        return null;
    }

    public void drawBulletWaves(GraphicsDrawer g) {
        g.setColor(Colors.RED);
        EnemyWave wave;
        long t = MyRobot.getTime();
        for (int w = 0; w < Waves.size(); w++) {
            wave = (EnemyWave) Waves.get(w);
            if (wave.getFirePower() != 0.0) {
                int dist = (int) (wave.getDist(t) + wave.getDist(t));
                g.drawOval((int) wave.getStartX(), (int) wave.getStartY(), dist, dist);
                g.setColor(Colors.GREEN);
            }
        }
    }


    public double getBestAngleOffset(EnemyData Enemy) {
        int bestindex = (NumOfSectors - 1) / 2;
        int besthits = Sectors[bestindex].getHits();
        int hits;
        for (int s = 0; s < Sectors.length; s++) {
            hits = Sectors[s].getHits();
            if (hits < besthits) {
                bestindex = s;
                besthits = hits;
            }
        }
        double guessfactor = Utils.getGuessFactor(bestindex, Sectors.length);
        return Utils.getAngleOffset(MyRobot, Enemy, guessfactor,
                                    (Enemy.didFireBullet() ? Math.abs(Enemy.getDeltaEnergy()) : 2.0));
    }

    public double getWorstAngleOffset(EnemyData Enemy) {
        int bestindex = (NumOfSectors - 1) / 2;
        int besthits = Sectors[bestindex].getHits();
        int hits;
        for (int s = 0; s < Sectors.length; s++) {
            hits = Sectors[s].getHits();
            if (hits > besthits) {
                bestindex = s;
                besthits = hits;
            }
        }
        double guessfactor = Utils.getGuessFactor(bestindex, Sectors.length);
        return Utils.getAngleOffset(MyRobot, Enemy, guessfactor,
                                    (Enemy.didFireBullet() ? Math.abs(Enemy.getDeltaEnergy()) : 2.0));
    }

    public void drawWorstAngleOffset(kid.GraphicsDrawer g, EnemyData robot) {
        double riskangle = Utils.getAngle(robot.getX(), robot.getY(), MyRobot.getX(), MyRobot.getY()) +
                           getWorstAngleOffset(robot);
        double dist = Utils.getDist(robot.getX(), robot.getY(), MyRobot.getX(), MyRobot.getY());
        g.drawLine((int) robot.getX(), (int) robot.getY(), (int) Utils.getX(robot.getX(), dist, riskangle),
                   (int) Utils.getY(robot.getY(), dist, riskangle));
    }

    private class DataProsseser extends Condition {
        public boolean test() {
            EnemyData[] EnemyData = Data.getEnemys();
            if (EnemyData != null) {
                for (int b = 0; b < EnemyData.length; b++) {
                    if (EnemyData[b] != null && !EnemyData[b].isDead())
                        Waves.add(new EnemyWave(MyRobot, EnemyData[b]));
                }
            }
            for (int w = 0; w < Waves.size(); w++) {
                EnemyWave wave = (EnemyWave) Waves.get(w);
                if (wave.testHit(MyRobot)) {
                    Sectors[Utils.getIndex(wave.getGuessFactor(MyRobot), Sectors.length)].addHit();
                    Waves.remove(w);
                    w--;
                }
            }
            return false;
        }
    }

}
