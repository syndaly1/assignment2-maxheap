package algorithms;

import metrics.PerformanceTracker;
import java.util.NoSuchElementException;

public class MaxHeap {
    private int[] a;
    private int size;
    private final PerformanceTracker pt;

    public MaxHeap(int capacity) { this(capacity, new PerformanceTracker()); }
    public MaxHeap(int capacity, PerformanceTracker pt) {
        if (capacity < 1) capacity = 1;
        this.a = new int[capacity];
        this.size = 0;
        this.pt = pt == null ? new PerformanceTracker() : pt;
        this.pt.addBytes(16L + 4L * capacity);
    }

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }
    public PerformanceTracker tracker() { return pt; }

    public int peek() {
        if (size == 0) throw new NoSuchElementException("heap is empty");
        pt.incRead();
        return a[0];
    }

    public void insert(int x) {
        ensure(size + 1);
        write(size, x);
        size++;
        siftUp(size - 1);
    }

    public void increaseKey(int i, int newKey) {
        if (i < 0 || i >= size) throw new IndexOutOfBoundsException();
        int old = read(i);
        pt.incComparison();
        if (newKey < old) throw new IllegalArgumentException("newKey < old");
        write(i, newKey);
        siftUp(i);
    }

    public int extractMax() {
        if (size == 0) throw new NoSuchElementException("heap is empty");
        int max = read(0);
        int last = read(size - 1);
        write(0, last);
        size--;
        siftDown(0);
        return max;
    }

    private void siftUp(int i) {
        while (i > 0) {
            int p = (i - 1) / 2;
            pt.incComparison();
            if (read(i) > read(p)) { swap(i, p); i = p; } else break;
        }
    }

    private void siftDown(int i) {
        while (true) {
            int l = 2*i + 1, r = 2*i + 2, m = i;
            if (l < size) { pt.incComparison(); if (read(l) > read(m)) m = l; }
            if (r < size) { pt.incComparison(); if (read(r) > read(m)) m = r; }
            if (m != i) { swap(i, m); i = m; } else break;
        }
    }

    private void ensure(int need) {
        if (need <= a.length) return;
        int newCap = Math.max(need, a.length * 2);
        int[] b = new int[newCap];
        pt.addBytes(16L + 4L * newCap);
        for (int i = 0; i < size; i++) { b[i] = read(i); pt.incWrite(); }
        pt.subBytes(16L + 4L * a.length);
        a = b;
    }

    private int read(int i) { pt.incRead(); return a[i]; }
    private void write(int i, int v) { a[i] = v; pt.incWrite(); }
    private void swap(int i, int j) { int t = read(i); write(i, read(j)); write(j, t); pt.incSwap(); }
}
