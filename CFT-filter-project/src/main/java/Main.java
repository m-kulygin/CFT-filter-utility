import filter.FilterMain;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            FilterMain filter = new FilterMain(args);
            filter.doFilter();
            System.out.println(filter.getResultStatsString());
            System.out.println("Filter worked successfully");
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.err.println("Terminating utility work.");
        }
    }
}