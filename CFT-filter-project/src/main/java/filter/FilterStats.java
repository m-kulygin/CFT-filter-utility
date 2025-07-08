package filter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class FilterStats {

    public long integerCount;
    public long floatCount;
    public long stringCount;
    public BigInteger maxInteger;
    public BigInteger minInteger;
    public BigInteger sumInteger;
    public BigDecimal averageInteger;
    public BigDecimal maxFloat;
    public BigDecimal minFloat;
    public BigDecimal sumFloat;
    public BigDecimal averageFloat;
    public long shortestStringLength;
    public long longestStringLength;

    public FilterStats() {
        integerCount = 0;
        floatCount = 0;
        stringCount = 0;
        sumInteger = BigInteger.ZERO;
        sumFloat = BigDecimal.valueOf(0);
    }

    public String getFullStats() {
        countIntegerAverage();
        countFloatAverage();
        StringBuilder result = new StringBuilder();
        result.append("\nFILTER STATS (full)\n\nInteger count: ")
                .append(integerCount).append("\n");
        if (integerCount != 0) {
            result.append("Min integer: ").append(new BigDecimal(minInteger)).append("\n")
                    .append("Max integer: ").append(new BigDecimal(maxInteger)).append("\n")
                    .append("Sum integer: ").append(new BigDecimal(sumInteger)).append("\n")
                    .append("Integer average: ").append(averageInteger).append("\n");
        }
        result.append("\nFloat count: ").append(floatCount).append("\n");
        if (floatCount != 0) {
            result.append("Min float: ").append(minFloat).append("\n")
                    .append("Max float: ").append(maxFloat).append("\n")
                    .append("Sum float: ").append(sumFloat).append("\n")
                    .append("Float average: ").append(averageFloat).append("\n");
        }
        result.append("\nString count: ").append(stringCount).append("\n");
        if (stringCount != 0) {
            result.append("Shortest string length: ").append(shortestStringLength).append("\n")
                    .append("Longest string length: ").append(longestStringLength).append("\n");
        }
        result.append("\n");
        return result.toString();
    }

    public String getShortStats() {
        return "\nFILTER STATS (short)\n" +
                "Integer count: " + integerCount + "\n" +
                "Float count: " + floatCount + "\n" +
                "String count: " + stringCount + "\n\n";
    }

    public void updateIntegerFullStats(BigInteger integerValue) {
        if (integerCount == 0) { // если это первый интегер
            minInteger = integerValue;
            maxInteger = integerValue;
        } else {
            minInteger = minInteger.min(integerValue);
            maxInteger = maxInteger.max(integerValue);
        }
        sumInteger = sumInteger.add(integerValue);
    }

    public void countIntegerAverage() {
        if (integerCount != 0) {
            averageInteger = new BigDecimal(sumInteger)
                    .divide(new BigDecimal(integerCount),
                            String.valueOf(integerCount).length() + 2,
                            RoundingMode.HALF_UP);
        }
    }

    public void countFloatAverage() {
        if (floatCount != 0) {
            averageFloat = sumFloat
                    .divide(new BigDecimal(floatCount),
                            sumFloat.scale() + String.valueOf(floatCount).length() + 2,
                            RoundingMode.HALF_UP);

        }
    }

    public void updateFloatFullStats(BigDecimal floatValue) {
        if (floatCount == 0) { // если это первый флоат
            minFloat = floatValue;
            maxFloat = floatValue;
        } else {
            minFloat = minFloat.min(floatValue);
            maxFloat = maxFloat.max(floatValue);
        }
        sumFloat = sumFloat.add(floatValue);
    }

    public void updateStringFullStats(String stringValue) {
        if (stringCount == 0) { // если это первая строка
            shortestStringLength = stringValue.length();
            longestStringLength = stringValue.length();
        } else {
            if (stringValue.length() < shortestStringLength) {
                shortestStringLength = stringValue.length();
            }
            if (stringValue.length() > longestStringLength) {
                longestStringLength = stringValue.length();
            }
        }
    }

}
