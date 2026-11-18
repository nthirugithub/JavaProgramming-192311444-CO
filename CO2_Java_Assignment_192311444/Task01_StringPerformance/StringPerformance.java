import java.util.Locale;

public class StringPerformance {
    private static volatile int blackHole;

    interface Builder { String build(int fields); }

    static String withString(int fields) {
        String result = "";
        for (int i = 0; i < fields; i++) result = result + "FIELD-" + i + "|";
        return result;
    }
    static String withStringBuilder(int fields) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < fields; i++) result.append("FIELD-").append(i).append('|');
        return result.toString();
    }
    static String withStringBuffer(int fields) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < fields; i++) result.append("FIELD-").append(i).append('|');
        return result.toString();
    }
    static long measure(Builder builder, int fields, int tickets, int repetitions) {
        for (int w = 0; w < 2; w++) run(builder, fields, tickets);
        long total = 0;
        for (int r = 0; r < repetitions; r++) {
            long start = System.nanoTime();
            run(builder, fields, tickets);
            total += System.nanoTime() - start;
        }
        return total / repetitions;
    }
    static void run(Builder builder, int fields, int tickets) {
        int checksum = 0;
        for (int t = 0; t < tickets; t++) checksum += builder.build(fields).length();
        blackHole = checksum;
    }
    public static void main(String[] args) {
        int[][] workloads = {{6, 1}, {50, 10_000}, {200, 2_000}};
        int repetitions = 5;
        System.out.println("workload,fields,tickets,String_ns,StringBuilder_ns,StringBuffer_ns");
        for (int w = 0; w < workloads.length; w++) {
            int fields = workloads[w][0], tickets = workloads[w][1];
            long a = measure(StringPerformance::withString, fields, tickets, repetitions);
            long b = measure(StringPerformance::withStringBuilder, fields, tickets, repetitions);
            long c = measure(StringPerformance::withStringBuffer, fields, tickets, repetitions);
            System.out.printf(Locale.ROOT, "%d,%d,%d,%d,%d,%d%n", w+1, fields, tickets, a, b, c);
        }
    }
}
