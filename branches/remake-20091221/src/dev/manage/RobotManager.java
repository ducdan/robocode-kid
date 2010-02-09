package dev.manage;

import java.util.HashMap;
import java.util.List;

import dev.data.EnemyData;
import dev.data.RobotData;
import dev.data.TeammateData;
import dev.team.Message;

import robocode.DeathEvent;
import robocode.Event;
import robocode.Robot;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;
import robocode.TeamRobot;
import robocode.WinEvent;

/**
 * A {@link DataManager} that stores information on the enemies and teammates that are in the current match. Allows the
 * user to easily access the {@link EnemyData} or {@link TeammateData} objects that store the information on the robots
 * and to also update them through the {@link #inEvent(Event) inEvent(Event)} method. This class also uses the singleton
 * pattern because only one instance of this class should ever be used at a time.
 * 
 * @author Brian Norman
 * @version 0.0.1 alpha
 */
public class RobotManager implements DataManager {

   /**
    * Singleton instance. Only want one {@link RobotManager} floating around so we used the singleton pattern to achieve
    * this.
    */
   private static RobotManager instance;

   /**
    * Creates or returns the singleton instance. If a {@link RobotManager} was created for another {@link Robot} than
    * that {@link RobotManager} will be returned.
    * 
    * @param robot
    *           the {@link Robot} this {@link DataManger} is for
    * @return the singleton instance of this {@link RobotManager}
    */
   public static RobotManager getInstance(Robot robot) {
      if (instance == null)
         instance = new RobotManager(robot);
      return instance;
   }



   /**
    * The {@link Robot} this {@link RobotManager} is for. Provides information on number of enemies, size of
    * battlefield, etc.
    */
   protected Robot                         robot;


   /**
    * A {@link HashMap} containing {@link RobotData} entries representing all the robots that are on the battlefield.
    */
   protected HashMap<String, RobotData>    robots;

   /**
    * A {@link HashMap} containing {@link EnemyData} entries representing all the enemies that are on the battlefield.
    */
   protected HashMap<String, EnemyData>    enemies;

   /**
    * A {@link HashMap} containing {@link TeammateData} entries representing all the teammates that are on the
    * battlefield.
    */
   protected HashMap<String, TeammateData> teammates;


   /**
    * Creates a new {@link RobotManager} for the given {@link Robot}. This constructor should only ever be called once.
    * If it is called a second time, a runtime error will be thrown.
    * 
    * @param robot
    */
   private RobotManager(Robot robot) {
      if (instance != null)
         throw new RuntimeException("Singleton class, connot create more than one instance.");

      this.robot = robot;
      this.robots = new HashMap<String, RobotData>(robot.getOthers());
      this.enemies = new HashMap<String, EnemyData>(robot.getOthers());
      this.teammates = new HashMap<String, TeammateData>(0);

      if (robot instanceof TeamRobot) {
         TeamRobot tRobot = (TeamRobot) robot;
         if (tRobot.getTeammates() != null) {
            this.teammates = new HashMap<String, TeammateData>(tRobot.getTeammates().length);
            this.enemies = new HashMap<String, EnemyData>(robot.getOthers() - tRobot.getTeammates().length);
         }
      }
   }


   /**
    * The way of passing information about the current round into the {@link RobotManager}. If certain events happen
    * during the round, they should be passed in using this method.<br>
    * <br>
    * The events that are currently handled are:<br>
    * - {@link ScannedRobotEvent}<br>
    * - {@link RobotDeathEvent}<br>
    * - {@link DeathEvent}<br>
    * - {@link WinEvent}<br>
    * <small>(Last updated 2009/12/26)</small>
    * 
    * @param e
    *           the current {@link Event} being processed.
    */
   public void inEvent(Event e) {
      if (e instanceof ScannedRobotEvent) {
         ScannedRobotEvent sre = (ScannedRobotEvent) e;
         handleScannedRobotEvent(sre);
      } else if (e instanceof RobotDeathEvent) {
         RobotDeathEvent rde = (RobotDeathEvent) e;
         handleRobotDeathEvent(rde);
      } else if (e instanceof DeathEvent) {
         DeathEvent de = (DeathEvent) e;
         handleDeathEvent(de);
      } else if (e instanceof WinEvent) {
         WinEvent we = (WinEvent) e;
         handleWinEvent(we);
      }
   }

   // TODO documentation: RobotManager - inEvents(List<Event> events)
   public void inEvents(List<Event> events) {
      for (Event e : events)
         inEvent(e);
   }

   /**
    * The way of passing in a {@link Message} from a teammate. To process multiple messages simply call this method once
    * for each message.
    * 
    * @param m
    *           a {@link Message} from a teammate
    */
   public void inMessage(Message m) {
   }

   /**
    * Properly processes a {@link ScannedRobotEvent} by add any unknown scanned robots or updating any known robots that
    * have been scanned.
    * 
    * @param sre
    *           {@link ScannedRobotEvent} to process
    */
   private void handleScannedRobotEvent(ScannedRobotEvent sre) {
      String name = sre.getName();
      if (robots.containsKey(name)) {
         RobotData r = robots.get(name);
         r.update(sre, robot);
      } else {
         if (robot instanceof TeamRobot && ((TeamRobot) robot).isTeammate(name)) {
            TeammateData t = new TeammateData(sre, robot);
            teammates.put(name, t);
            robots.put(name, t);
         } else {
            EnemyData e = new EnemyData(sre, robot);
            enemies.put(name, e);
            robots.put(name, e);
         }
      }
   }

   /**
    * Properly processes a {@link RobotDeathEvent} by updating the status of the robot that died.
    * 
    * @param rde
    *           {@link RobotDeathEvent} to process
    */
   private void handleRobotDeathEvent(RobotDeathEvent rde) {
      String name = rde.getName();
      if (robots.containsKey(name)) {
         RobotData r = robots.get(name);
         r.setDeath();
      }
   }

   /**
    * Properly processes a {@link DeathEvent} by updating the status of all the robots to be dead. This allows each
    * robot to be updated correctly at the beginning of each round.
    * 
    * @param de
    *           {@link DeathEvent} to process
    */
   private void handleDeathEvent(DeathEvent de) {
      for (RobotData r : robots.values()) {
         r.setDeath();
      }
   }

   /**
    * Properly processes a {@link WinEvent} by updating the status of all the robots to be dead. This allows each robot
    * to be updated correctly at the beginning of each round.
    * 
    * @param de
    *           {@link WinEvent} to process
    */
   private void handleWinEvent(WinEvent de) {
      for (RobotData r : robots.values()) {
         r.setDeath();
      }
   }

   /**
    * Returns the {@link RobotData} object that corresponds to the robot with the given name. A default
    * {@link RobotData} object is returned if no robot is found with that name.
    * 
    * @param name
    *           the identifier {@link String}
    * @return the corresponding {@link RobotData}
    */
   public RobotData getRobot(String name) {
      RobotData r = robots.get(name);
      return (r == null ? new RobotData() : null);
   }

   /**
    * Returns the {@link EnemyData} object that corresponds to the enemy with the given name. A default
    * {@link EnemyData} object is returned if no enemy is found with that name.
    * 
    * @param name
    *           the identifier {@link String}
    * @return the corresponding {@link EnemyData}
    */
   public EnemyData getEnemy(String name) {
      EnemyData e = enemies.get(name);
      return (e == null ? new EnemyData() : null);
   }

   /**
    * Returns the {@link TeammateData} object that corresponds to the teammate with the given name. A default
    * {@link TeammateData} object is return if no teammate is found with that name.
    * 
    * @param name
    *           the identifier {@link String}
    * @return the corresponding {@link TeammateData}
    */
   public TeammateData getTeammate(String name) {
      TeammateData t = teammates.get(name);
      return (t == null ? new TeammateData() : null);
   }

}
