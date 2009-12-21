package dev.data;

import robocode.Robot;

public class DataManager {

   private static DataManager instance;

   public static DataManager getInstance(Robot robot) {
      if (instance == null)
         instance = new DataManager(robot);
      return instance;
   }



   private DataManager(Robot robot) {

   }

}
