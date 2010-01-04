package dev.team;

public abstract class Message {

   protected long time;

   public Message(long time) {
      this.time = time;
   }

   public long getTime() {
      return time;
   }

}
