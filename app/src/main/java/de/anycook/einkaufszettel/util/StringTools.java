package de.anycook.einkaufszettel.util;

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
        NUMBER_PATTERN = Pattern.compile("(\\d+/\\d+)|(\\d+)|(\\d+\\.\\d+)");
        HAS_UNIT_PATTERN = Pattern.compile("(\\d+/\\d+|\\d+|\\d+\\.\\d+) ([a-zA-ZÄÜÖäüöß]+)");

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

        Matcher amount1NumberMatcher = NUMBER_PATTERN.matcher(amount1);
        Matcher amount2NumberMatcher = NUMBER_PATTERN.matcher(amount2);

        Matcher amount1UnitMatcher = HAS_UNIT_PATTERN.matcher(amount1);
        Matcher amount2UnitMatcher = HAS_UNIT_PATTERN.matcher(amount2);

        if (amount1UnitMatcher.matches() && amount2UnitMatcher.matches()) {
            String unit1 = amount1UnitMatcher.group(2);
            String unit2 = amount2UnitMatcher.group(2);

            if (unit1.equals(unit2)) {
                String newNumber = mergeNumbers(amount1UnitMatcher.group(1), amount2UnitMatcher.group(1));
                return String.format("%s %s", newNumber, unit1);
            }
        }

        if (amount1NumberMatcher.matches() && amount2NumberMatcher.matches()) {
            return mergeNumbers(amount1, amount2);
        }

        return String.format("%s + %s", amount1, amount2);
    }

    private static String mergeNumbers(String number1, String number2) {
        if (number1.contains("/") && number2.contains("/")) {
            return mergeFractions(number1, number2);
        } else if (number1.contains("/")) {
            return mergeFractionAndDecimal(number1, number2);
        } else if (number2.contains("/")) {
            return  mergeFractionAndDecimal(number2, number1);
        }
        return mergeDecimals(number1, number2);
    }

    private static String mergeFractions(String fraction1, String fraction2) {
        String[] split1 = fraction1.split("/");
        String[] split2 = fraction2.split("/");

        int numerator1 = Integer.parseInt(split1[0]);
        int denominator1 = Integer.parseInt(split1[1]);

        int numerator2 = Integer.parseInt(split2[0]);
        int denominator2 = Integer.parseInt(split2[1]);

        return getFractionString(numerator1 * denominator2 + numerator2 * denominator1, denominator1 * denominator2);
    }

    private static String mergeDecimals(String numberString1, String numberString2) {
        float number1 = Float.parseFloat(numberString1);
        float number2 = Float.parseFloat(numberString2);
        return formatNumber(number1 + number2);
    }

    private static String mergeFractionAndDecimal(String fraction, String decimalString) {
        String[] split = fraction.split("/");
        float fractionDecimal = Float.parseFloat(split[0]) / Float.parseFloat(split[1]);
        return mergeDecimals(Float.toString(fractionDecimal), decimalString);
    }

    public static String multiplyAmount(String amount, int recipePersons, int newPersons) {
        amount = amount.replaceAll(",", ".");

        Matcher numberMatcher = NUMBER_PATTERN.matcher(amount);

        StringBuilder newAmount = new StringBuilder();

        int start, end = 0;
        while (numberMatcher.find(end)) {
            start = numberMatcher.start();

            // append non-matching
            if (end < start) { newAmount.append(amount.substring(end, start)); }

            end = numberMatcher.end();

            String numberString = amount.substring(start, end);

            if (numberString.contains("/")) {
                newAmount.append(multiplyFraction(numberString, newPersons, recipePersons));
            } else {
                newAmount.append(multiplyDecimal(numberString, newPersons, recipePersons));
            }
        }
        newAmount.append(amount.substring(end, amount.length()));
        return newAmount.toString().replaceAll("\\.", ",");
    }

    private static String multiplyDecimal(String numberString, float newFactor, float oldFactor) {
        float factor = newFactor / oldFactor;
        float number = Float.parseFloat(numberString);
        return formatNumber(factor * number);
    }

    private static String multiplyFraction(String fraction, int newFactor, int oldFactor) {
        String[] split = fraction.split("/");
        int numerator = Integer.parseInt(split[0]);
        int denominator = Integer.parseInt(split[1]);
        numerator *= newFactor;
        denominator *= oldFactor;

        return getFractionString(numerator, denominator);
    }

    private static String getFractionString(int numerator, int denominator) {
        int gcd = euclideanGCD(numerator, denominator);
        numerator /= gcd;
        denominator /= gcd;

        if (denominator == 1) { return Integer.toString(numerator); }
        return String.format("%d/%d", numerator, denominator);
    }

    private static int euclideanGCD(int a, int b) {
        if (a == 0) { return b; }

        while (b != 0) {
            if (a > b) {
                a -= b;
            } else {
                b -= a;
            }
        }
        return a;
    }

    private static String formatNumber(float number) {
        return NUMBER_FORMAT.format(number).replace('.', ',');
    }
}
