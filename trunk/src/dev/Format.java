package dev;

import java.text.DecimalFormat;

public final class Format {

   public static final DecimalFormat NO_DECIMAL_PLACES = new DecimalFormat("#0;-#0");
   public static final DecimalFormat ONE_DECIMAL_PLACES = new DecimalFormat("#0.0;-#0.0");
   public static final DecimalFormat TWO_DECIMAL_PLACES = new DecimalFormat("#0.00;-#0.00");
   public static final DecimalFormat THREE_DECIMAL_PLACES = new DecimalFormat("#0.000;-#0.000");

   private Format() {
   }


   public static final String noDec(double n) {
      return NO_DECIMAL_PLACES.format(n);
   }

   public static final String oneDec(double n) {
      return ONE_DECIMAL_PLACES.format(n);
   }

   public static final String twoDec(double n) {
      return TWO_DECIMAL_PLACES.format(n);
   }

   public static final String threeDec(double n) {
      return THREE_DECIMAL_PLACES.format(n);
   }

}
