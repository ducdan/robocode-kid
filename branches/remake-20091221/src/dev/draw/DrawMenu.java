package dev.draw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.HashMap;

public class DrawMenu {

   private static int                   startX      = 0;
   private static int                   startY      = 1;

   private static int                   recWidth    = 71;
   private static int                   recHeight   = 13;

   private static int                   stringX     = 2;
   private static int                   stringY     = 2;

   private static Color                 colorOpen   = Color.BLUE;
   private static Color                 colorClosed = Color.RED;

   private static boolean               open        = false;
   private static HashMap<String, Menu> menus       = new HashMap<String, Menu>();
   private static String                longest     = "Draw Menu";


   public static boolean getValue(String item, String menu) {
      Menu m = menus.get(menu);
      if (m == null) {
         menus.put(menu, m = new Menu());
         longest = (longest.length() > item.length() ? longest : item);
      }
      return m.getValue(item);
   }

   public static void inMouseEvent(MouseEvent e) {
      if (e.getID() == MouseEvent.MOUSE_CLICKED) {
         if (open) {
            boolean found = false;

            // finds item
            for (int i = 0; i < menus.size() && !found; i++) {
               found = menus.get(i).inMouseEvent(e, startX + recWidth, startY + (i + 1) * recHeight);
            }

            // System.out.println(found);

            if (!found) {
               double x = e.getX() - startX;
               double y = e.getY() - startY;
               if (x <= recWidth && x >= 0.0D && y >= recHeight) {
                  for (int i = 0; i < menus.size() && !found; i++) {
                     if (y <= (i + 2) * recHeight) {
                        Menu menu = menus.get(i);
                        if (menu.isOpen()) {
                           menu.close();
                        } else {
                           for (Menu m : menus.values())
                              m.close();
                           menu.open();
                        }
                        found = true;
                     }
                  }
               }

               if (!found) {
                  open = false;
                  for (Menu m : menus.values())
                     m.close();
               }
            }
         } else {
            double x = e.getX() - startX;
            double y = e.getY() - startY;
            if (x <= recWidth && y <= recHeight && y >= 0.0D && x >= 0.0D)
               open = true;
         }
      }
   }

   public static void draw(Graphics graphics) {
      recWidth = (int) graphics.getFontMetrics().getStringBounds(longest, graphics).getWidth() + 15;
      recHeight = (int) graphics.getFontMetrics().getStringBounds(longest, graphics).getHeight() + 2;

      if (open) {
         int i = 1;
         for (String key : menus.keySet()) {
            Menu menu = menus.get(key);
            graphics.setColor(menu.isOpen() ? colorOpen : colorClosed);
            graphics.drawRect(startX, startY + i * recHeight, recWidth - 1, recHeight - 1);
            graphics.drawString(key, startX + stringX, startY + stringY + i * recHeight);
            menu.draw(graphics, startX + recWidth, startY + i * recHeight);
            i++;
         }
      }
      graphics.setColor(open ? colorOpen : colorClosed);
      graphics.drawRect(startX, startY, recWidth - 1, recHeight - 1);
      graphics.drawString("Draw Menu", startX + stringX, startY + stringY);
   }



   private static class Menu {

      public static final Color        ITEM_ON  = Color.GREEN;
      public static final Color        ITEM_OFF = Color.RED;

      private boolean                  open     = false;
      private HashMap<String, Boolean> items    = new HashMap<String, Boolean>();
      private String                   longest  = " ";


      public boolean getValue(String item) {
         Boolean value = items.get(item);
         if (value == null) {
            items.put(item, value = false);
            longest = (longest.length() > item.length() ? longest : item);
         }
         return value;
      }

      public boolean isOpen() {
         return open;
      }

      public void open() {
         open = true;
      }

      public void close() {
         open = false;
      }

      public boolean inMouseEvent(MouseEvent e, double startX, double startY) {
         if (open && e.getID() == MouseEvent.MOUSE_CLICKED) {
            double x = e.getX() - startX;
            double y = e.getY() - startY;
            if (x <= recWidth && x >= 0.0D && y >= 0.0D) {
               for (int i = 0; i < items.keySet().size(); i++) {
                  if (y <= (i + 1) * recHeight) {
                     String s = (String) (items.keySet().toArray())[i];
                     items.put(s, !items.get(s));
                     return true;
                  }
               }
            }
         }
         return false;
      }

      public void draw(Graphics graphics, int startX, int startY) {
         if (open) {
            int recWidth = (int) graphics.getFontMetrics().getStringBounds(longest, graphics).getWidth() + 15;

            int i = 0;
            for (String key : items.keySet()) {
               graphics.setColor(items.get(key) ? ITEM_ON : ITEM_OFF);
               graphics.drawRect(startX, startY + i * recHeight, recWidth - 1, recHeight - 1);
               graphics.drawString(key, startX + stringX, startY + stringY + i * recHeight);
               i++;
            }
         }
      }

   }

}
