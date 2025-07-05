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
        return "\nFILTER STATS\n\n" +
                "Integer count: " + integerCount + "\n" +
                "Min integer: " + minInteger + "\n" +
                "Max integer: " + maxInteger + "\n" +
                "Sum integer: " + sumInteger + "\n" +
                "Integer average: " + averageInteger + "\n" +
                "\nFloat count: " + floatCount + "\n" +
                "Min float: " + minFloat + "\n" +
                "Max float: " + maxFloat + "\n" +
                "Sum float: " + sumFloat + "\n" +
                "Float average: " + averageFloat + "\n" +
                "\nString count: " + stringCount + "\n" +
                "Shortest string length: " + shortestStringLength + "\n" +
                "Longest string length: " + longestStringLength + "\n\n";
    }

    public String getShortStats() {
        return "\nFILTER STATS\n" +
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
                    .divide(new BigDecimal(integerCount), RoundingMode.HALF_UP);
        }
    }
    public void countFloatAverage() {
        if (floatCount != 0) {
            averageFloat = sumFloat
                    .divide(new BigDecimal(floatCount), RoundingMode.HALF_UP);
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
