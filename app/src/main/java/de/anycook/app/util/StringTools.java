package de.anycook.app.util;

import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jan Graßegger<jan@anycook.de>
 */
public final class StringTools {
    private static final Logger LOGGER;
    private static final Pattern HAS_UNIT_PATTERN;
    private static final Pattern NUMBER_PATTERN;
    private static final NumberFormat NUMBER_FORMAT;

    static {
        LOGGER = LoggerManager.getLogger();
        HAS_UNIT_PATTERN = Pattern.compile("(\\d+|\\d+\\.\\d+) ([a-zA-ZÄÜÖäüöß]+)");
        NUMBER_PATTERN = Pattern.compile("(\\d+|\\d+\\.\\d+)");
        NUMBER_FORMAT = new DecimalFormat("0.##");
    }

    private StringTools() { }

    public static String formatAmount(String input) {
        if (input.length() == 0) { return input; }
        StringBuilder output = new StringBuilder();
        output.append(input.charAt(0));

        for (int i = 0; i < input.length() - 1; i++) {
            char c = input.charAt(i);
            if (Character.isDigit(c) && Character.isLetter(input.codePointAt(i + 1))) { output.append(' '); }

            if (input.charAt(i + 1) == '.') {
                output.append(',');
            } else  {
                output.append(input.charAt(i + 1));
            }
        }

        LOGGER.v("Input: '%s' Output: '%s", input, output);

        return output.toString().trim();
    }

    public static String mergeAmounts(String amount1, String amount2) {
        if (amount1.length() == 0) { return amount2; }
        if (amount2.length() == 0) { return amount1; }

        amount1 = amount1.replace(',', '.');
        amount2 = amount2.replace(',', '.');

        String newAmount = null;

        Matcher amount1UnitMatcher = HAS_UNIT_PATTERN.matcher(amount1);
        Matcher amount2UnitMatcher = HAS_UNIT_PATTERN.matcher(amount2);

        Matcher amount1NumberMatcher = NUMBER_PATTERN.matcher(amount1);
        Matcher amount2NumberMatcher = NUMBER_PATTERN.matcher(amount2);

        if (amount1UnitMatcher.matches() && amount2UnitMatcher.matches()) {
            String unit1 = amount1UnitMatcher.group(2);
            String unit2 = amount2UnitMatcher.group(2);

            if (unit1.equals(unit2)) {
                float number1 = Float.parseFloat(amount1UnitMatcher.group(1));
                float number2 = Float.parseFloat(amount2UnitMatcher.group(1));

                newAmount = String.format("%s %s", NUMBER_FORMAT.format(number1 + number2), unit1);
            }
        } else if (amount1NumberMatcher.matches() && amount2NumberMatcher.matches()) {
            float number1 = Float.parseFloat(amount1);
            float number2 = Float.parseFloat(amount2);

            newAmount = NUMBER_FORMAT.format(number1 + number2);
        }

        if (newAmount == null) { newAmount = String.format("%s + %s", amount1, amount2); }

        return newAmount.replace('.', ',');
    }

    public static String multiplyAmount(String amount, int recipePersons, int newPersons) {
        amount = amount.replaceAll(",", ".");

        float factor = (float) newPersons / (float) recipePersons;
        Matcher numberMatcher = NUMBER_PATTERN.matcher(amount);

        StringBuilder newAmount = new StringBuilder();

        int start, end = 0;
        while (numberMatcher.find(end)) {
            start = numberMatcher.start();

            // append non-matching
            if (end < start) { newAmount.append(amount.substring(end, start)); }

            end = numberMatcher.end();

            float number = Float.parseFloat(amount.substring(start, end));
            newAmount.append(NUMBER_FORMAT.format(factor * number));
        }

        newAmount.append(amount.substring(end, amount.length()));

        return newAmount.toString().replaceAll("\\.", ",");
    }
}
