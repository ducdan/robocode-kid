package dev.manage;

import java.util.List;

import robocode.Event;

public interface EventHandler {

   public void inEvent(Event e);

   public void inEvents(List<Event> events);

}
