import java.math.BigDecimal;
import java.math.BigInteger;

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

}
