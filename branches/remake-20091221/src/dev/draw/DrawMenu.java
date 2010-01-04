package dev.draw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.HashMap;

public class DrawMenu {

   public static int                    START_X         = 0;
   public static int                    START_Y         = 1;

   public static final int              REC_WIDTH       = 71;
   public static final int              REC_DRAW_WIDTH  = REC_WIDTH - 1;
   public static final int              REC_HEIGHT      = 13;
   public static final int              REC_DRAW_HEIGHT = REC_HEIGHT - 1;

   public static final int              STRING_OFFSET_X = 2;
   public static final int              STRING_OFFSET_Y = 2;

   public static final Color            MENU_OPEN       = Color.BLUE;
   public static final Color            MENU_CLOSED     = Color.RED;

   public static final Color            ITEM_ON         = Color.GREEN;
   public static final Color            ITEM_OFF        = Color.RED;


   private static boolean               open            = false;
   private static HashMap<String, Menu> menus           = new HashMap<String, Menu>();


   public static boolean getValue(String item, String menu) {
      Menu m = menus.get(menu);
      if (m == null)
         menus.put(menu, m = new Menu());
      return m.getValue(item);
   }

   public static void inMouseEvent(MouseEvent e) {
      if (e.getID() == MouseEvent.MOUSE_CLICKED) {
         if (open) {
            boolean found = false;

            // finds item
            for (int i = 0; i < menus.size() && !found; i++) {
               found = menus.get(i).inMouseEvent(e, START_X + REC_WIDTH, START_Y + (i + 1) * REC_HEIGHT);
            }

            if (!found) {
               double x = e.getX() - START_X;
               double y = e.getY() - START_Y;
               if (x <= REC_WIDTH && x >= 0.0D && y >= REC_HEIGHT) {
                  for (int i = 0; i < menus.size() && !found; i++) {
                     if (y <= (i + 2) * REC_HEIGHT) {
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
            double x = e.getX() - START_X;
            double y = e.getY() - START_Y;
            if (x <= REC_WIDTH && y <= REC_HEIGHT && y >= 0.0D && x >= 0.0D)
               open = true;
         }
      }
   }

   public static void draw(Graphics grid) {
      if (open) {
         int i = 1;
         for (String key : menus.keySet()) {
            Menu menu = menus.get(key);
            grid.setColor(menu.isOpen() ? MENU_OPEN : MENU_CLOSED);
            grid.drawRect(START_X, START_Y + i * REC_HEIGHT, REC_DRAW_WIDTH, REC_DRAW_HEIGHT);
            grid.drawString(key, START_X + STRING_OFFSET_X, START_Y + STRING_OFFSET_Y + i * REC_HEIGHT);
            menu.draw(grid, START_X + REC_WIDTH, START_Y + i * REC_HEIGHT);
            i++;
         }
      }
      grid.setColor(open ? MENU_OPEN : MENU_CLOSED);
      grid.drawRect(START_X, START_Y, REC_DRAW_WIDTH, REC_DRAW_HEIGHT);
      grid.drawString("Draw Menu", START_X + STRING_OFFSET_X, START_Y + STRING_OFFSET_Y);
   }



   private static class Menu {

      private boolean                  open  = false;
      private HashMap<String, Boolean> items = new HashMap<String, Boolean>();


      public boolean getValue(String item) {
         Boolean value = items.get(item);
         if (value == null)
            items.put(item, value = false);
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
            if (x <= REC_WIDTH && x >= 0.0D && y >= 0.0D) {
               for (int i = 0; i < items.keySet().size(); i++) {
                  if (y <= (i + 1) * REC_HEIGHT) {
                     String s = (String) (items.keySet().toArray())[i];
                     items.put(s, !items.get(s));
                     return true;
                  }
               }
            }
         }
         return false;
      }

      public void draw(Graphics grid, int startX, int startY) {
         if (open) {
            int i = 0;
            for (String key : items.keySet()) {
               grid.setColor(items.get(key) ? ITEM_ON : ITEM_OFF);
               grid.drawRect(startX, startY + i * REC_HEIGHT, REC_DRAW_WIDTH, REC_DRAW_HEIGHT);
               grid.drawString(key, startX + STRING_OFFSET_X, startY + STRING_OFFSET_Y + i * REC_HEIGHT);
               i++;
            }
         }
      }

   }

}
