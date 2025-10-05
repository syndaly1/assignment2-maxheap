package metrics;

public class PerformanceTracker {
    private long comparisons, swaps, reads, writes, bytesAllocated, peakBytes;

    public void incComparison() { comparisons++; }
    public void incSwap() { swaps++; }
    public void incRead() { reads++; }
    public void incWrite() { writes++; }
    public void addBytes(long b) { bytesAllocated += b; if (bytesAllocated > peakBytes) peakBytes = bytesAllocated; }
    public void subBytes(long b) { bytesAllocated -= b; if (bytesAllocated < 0) bytesAllocated = 0; }

    public long getComparisons() { return comparisons; }
    public long getSwaps() { return swaps; }
    public long getReads() { return reads; }
    public long getWrites() { return writes; }
    public long getPeakBytes() { return peakBytes; }

    public void reset() { comparisons = swaps = reads = writes = bytesAllocated = peakBytes = 0; }

    @Override public String toString() {
        return "comparisons=" + comparisons + ", swaps=" + swaps + ", reads=" + reads + ", writes=" + writes + ", peakBytes=" + peakBytes;
    }
}
