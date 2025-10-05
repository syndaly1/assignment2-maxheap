package cli;

import algorithms.MaxHeap;
import metrics.PerformanceTracker;
import util.CSVWriter;
import java.util.*;

public class BenchmarkRunner {
    public static void main(String[] args) throws Exception {
        Map<String,String> a = parse(args);
        if (!a.containsKey("--n")) {
            System.out.println("Usage: --n <size> [--runs k] [--dist random|sorted|reversed|nearly-sorted|dups] [--csv path]");
            return;
        }
        int n = Integer.parseInt(a.get("--n"));
        int runs = Integer.parseInt(a.getOrDefault("--runs","3"));
        String dist = a.getOrDefault("--dist","random");
        String csvPath = a.get("--csv");
        CSVWriter csv = csvPath == null ? null : new CSVWriter(csvPath);
        if (csv != null) csv.header("n","dist","run","millis","comparisons","swaps","reads","writes","peakBytes");
        for (int r = 1; r <= runs; r++) {
            int[] data = gen(n, dist, r);
            PerformanceTracker pt = new PerformanceTracker();
            long t0 = System.nanoTime();
            MaxHeap h = new MaxHeap(Math.max(1,n), pt);
            for (int v : data) h.insert(v);
            long s = 0;
            while (!h.isEmpty()) s += h.extractMax();
            long ms = (System.nanoTime() - t0) / 1_000_000;
            if (csv != null) { csv.writeRow(n, dist, r, ms, pt.getComparisons(), pt.getSwaps(), pt.getReads(), pt.getWrites(), pt.getPeakBytes()); csv.flush(); }
            System.out.printf(Locale.US, "run=%d n=%d dist=%s time=%dms metrics=[%s]%n", r, n, dist, ms, pt.toString());
            if (s == Long.MIN_VALUE) System.out.print("");
        }
        if (csv != null) csv.close();
    }

    private static Map<String,String> parse(String[] a) {
        Map<String,String> m = new HashMap<>();
        for (int i = 0; i < a.length; i++) if (a[i].startsWith("--")) m.put(a[i], (i+1<a.length && !a[i+1].startsWith("--")) ? a[++i] : "true");
        return m;
    }

    private static int[] gen(int n, String dist, int seed) {
        Random rnd = new Random(12345L + seed);
        int[] x = new int[n];
        for (int i = 0; i < n; i++) x[i] = rnd.nextInt(1_000_000);
        switch (dist) {
            case "sorted" -> Arrays.sort(x);
            case "reversed" -> { Arrays.sort(x); rev(x); }
            case "nearly-sorted" -> near(x, rnd, Math.max(1, n/100));
            case "dups" -> { for (int i = 0; i < n; i++) x[i] = rnd.nextInt(10); }
            default -> {}
        }
        return x;
    }

    private static void rev(int[] a){ for(int i=0,j=a.length-1;i<j;i++,j--){int t=a[i];a[i]=a[j];a[j]=t;} }
    private static void near(int[] a, Random r, int k){ int n=a.length; for(int t=0;t<k;t++){int i=r.nextInt(n),j=r.nextInt(n);int v=a[i];a[i]=a[j];a[j]=v;} }
}
