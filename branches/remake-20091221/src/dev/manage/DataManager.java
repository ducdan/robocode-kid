package dev.manage;

import dev.team.Message;
import robocode.Event;

public interface DataManager {

   public void inEvent(Event e);

   public void inMessage(Message m);

}
