package core.luuh.aspergescore.utils;

public class NumberFormatter {

    public static String formatNumber(double number) {
        String formattedNumber;

        if (number >= 1_000_000_000) {
            formattedNumber = String.format("%.0fB", number / 1_000_000_000);
        } else if (number >= 1_000_000) {
            formattedNumber = String.format("%.0fM", number / 1_000_000);
        } else if (number >= 1_000) {
            formattedNumber = String.format("%.0fk", number / 1_000);
        } else {
            formattedNumber = String.valueOf(number);
        }

        return formattedNumber;
    }

}
