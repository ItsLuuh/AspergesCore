package core.luuh.aspergescore.utils;

import java.text.DecimalFormat;

public class NumberFormatter {

    public static String formatNumber(double number) {
        String formattedNumber;

        if (number >= 1_000_000_000) {
            formattedNumber = String.format("%.0fB", number / 1_000_000_000);
        } else if (number >= 1_000_000) {
            formattedNumber = String.format("%.0fM", number / 1_000_000);
        } else if (number >= 100_000) {
            formattedNumber = String.format("%.0fk", number / 1_000);
        } else {
            formattedNumber = String.valueOf(Math.round(number));
        }

        return formattedNumber;
    }

    public static String formatNumberCommas(long number) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(number);
    }

    public static boolean isLong(String input) {
        try {
            Long.parseLong(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException | NullPointerException e) {
            return false;
        }

        return true;
    }

    public static boolean isInt(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException | NullPointerException e) {
            return false;
        }

        return true;
    }


}
