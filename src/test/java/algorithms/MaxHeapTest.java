package algorithms;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Random;

public class MaxHeapTest {

    @Test
    void basicFlow() {
        MaxHeap h = new MaxHeap(2);
        h.insert(5); h.insert(1); h.insert(9);
        Assertions.assertEquals(9, h.peek());
        Assertions.assertEquals(9, h.extractMax());
        Assertions.assertEquals(5, h.extractMax());
        Assertions.assertEquals(1, h.extractMax());
        Assertions.assertTrue(h.isEmpty());
    }

    @Test
    void throwsOnEmpty() {
        MaxHeap h = new MaxHeap(1);
        Assertions.assertThrows(NoSuchElementException.class, h::peek);
        Assertions.assertThrows(NoSuchElementException.class, h::extractMax);
    }

    @Test
    void duplicates() {
        MaxHeap h = new MaxHeap(1);
        h.insert(7); h.insert(7); h.insert(7);
        Assertions.assertEquals(7, h.extractMax());
        Assertions.assertEquals(7, h.extractMax());
        Assertions.assertEquals(7, h.extractMax());
        Assertions.assertTrue(h.isEmpty());
    }

    @Test
    void increaseKeyPromotes() {
        MaxHeap h = new MaxHeap(4);
        h.insert(5); h.insert(7); h.insert(3);
        h.increaseKey(2, 10);
        Assertions.assertEquals(10, h.peek());
    }

    @Test
    void increaseKeyRejectsDecrease() {
        MaxHeap h = new MaxHeap(3);
        h.insert(5); h.insert(7); h.insert(6);
        Assertions.assertThrows(IllegalArgumentException.class, () -> h.increaseKey(1, 4));
    }

    @Test
    void increaseKeyIndexBounds() {
        MaxHeap h = new MaxHeap(2);
        h.insert(1);
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> h.increaseKey(-1, 5));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> h.increaseKey(1, 5));
    }

    @Test
    void propertyAgainstSortDesc() {
        Random rnd = new Random(42);
        for (int t = 0; t < 10; t++) {
            int n = 50 + rnd.nextInt(70);
            int[] a = new int[n];
            for (int i = 0; i < n; i++) a[i] = rnd.nextInt(10_000);
            int[] exp = Arrays.copyOf(a, n);
            Arrays.sort(exp);
            for (int i = 0, j = n - 1; i < j; i++, j--) { int x = exp[i]; exp[i] = exp[j]; exp[j] = x; }

            MaxHeap h = new MaxHeap(n);
            for (int v : a) h.insert(v);
            int[] got = new int[n];
            for (int i = 0; i < n; i++) got[i] = h.extractMax();

            Assertions.assertArrayEquals(exp, got);
        }
    }
}
