package de.greenrobot.common.checksum;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

@RunWith(Parameterized.class)
public class Murmur3aChecksumSpeedTest {
    public static final int ITERATIONS = 1000;

    @Parameterized.Parameter
    public int alignment;

    @Parameterized.Parameters(name = "{0}-aligned")
    public static Collection alignments() {
        return Arrays.asList(new Object[][]{{0}, {1}, {2}, {3}});
    }

    @Test
    public void measureByteArrayPerformance() {
        System.out.println("ByteArray align=" + alignment + "\t----------------------------------------------------");
        Murmur3aChecksum checksum = new Murmur3aChecksum();
        byte[] data = new byte[1024 * 1024]; // 1MB
        new Random(23).nextBytes(data);

        // Warm up a bit
        checksum.update(data);

        long hash;
        long totalTime = 0;
        for (int i = 0; i < ITERATIONS; i++) {
            prepareChecksum(checksum);
            long start = System.nanoTime();
            checksum.update(data);
            hash = checksum.getValue();
            totalTime += System.nanoTime() - start;
            if ((i + 1) % (ITERATIONS / 10) == 0) {
                printStats(i + 1, data.length, totalTime, hash);
            }
        }
    }

    @Test
    public void measureShortArrayPerformance() {
        System.out.println("ShortArray align=" + alignment + "\t----------------------------------------------------");
        Murmur3aChecksum checksum = new Murmur3aChecksum();
        short[] data = new short[512 * 1024]; // 1MB
        Random random = new Random(23);
        for (int i = 0; i < data.length; i++) {
            data[i] = (short) random.nextInt();
        }

        // Warm up a bit
        checksum.updateShort(data);

        long hash;
        long totalTime = 0;
        for (int i = 0; i < ITERATIONS; i++) {
            prepareChecksum(checksum);
            long start = System.nanoTime();
            checksum.updateShort(data);
            hash = checksum.getValue();
            totalTime += System.nanoTime() - start;
            if ((i + 1) % (ITERATIONS / 10) == 0) {
                printStats(i + 1, data.length * 2, totalTime, hash);
            }
        }
    }

    @Test
    public void measureIntArrayPerformance() {
        System.out.println("IntArray align=" + alignment + "\t----------------------------------------------------");
        Murmur3aChecksum checksum = new Murmur3aChecksum();
        int[] data = new int[256 * 1024]; // 1MB
        Random random = new Random(23);
        for (int i = 0; i < data.length; i++) {
            data[i] = random.nextInt();
        }

        // Warm up a bit
        checksum.updateInt(data);

        long hash;
        long totalTime = 0;
        for (int i = 0; i < ITERATIONS; i++) {
            prepareChecksum(checksum);
            long start = System.nanoTime();
            checksum.updateInt(data);
            hash = checksum.getValue();
            totalTime += System.nanoTime() - start;
            if ((i + 1) % (ITERATIONS / 10) == 0) {
                printStats(i + 1, data.length * 4, totalTime, hash);
            }
        }
    }

    @Test
    public void measureLongArrayPerformance() {
        System.out.println("LongArray align=" + alignment + "\t----------------------------------------------------");
        Murmur3aChecksum checksum = new Murmur3aChecksum();
        long[] data = new long[128 * 1024]; // 1MB
        Random random = new Random(23);
        for (int i = 0; i < data.length; i++) {
            data[i] = random.nextLong();
        }

        // Warm up a bit
        checksum.updateLong(data);

        long hash;
        long totalTime = 0;
        for (int i = 0; i < ITERATIONS; i++) {
            prepareChecksum(checksum);
            long start = System.nanoTime();
            checksum.updateLong(data);
            hash = checksum.getValue();
            totalTime += System.nanoTime() - start;
            if ((i + 1) % (ITERATIONS / 10) == 0) {
                printStats(i + 1, data.length * 4, totalTime, hash);
            }
        }
    }

    private void prepareChecksum(Murmur3aChecksum checksum) {
        checksum.reset();
        for (int j = 0; j < alignment; j++) {
            checksum.update(0);
        }
    }

    private void printStats(int iterations, int bytesPerIteration, long totalTime, long hash) {
        long ms = totalTime / 1000000;
        double mb = ((double) iterations) * bytesPerIteration / 1024 / 1024;
        int mbs = (int) (mb / (totalTime / 1000000000d) + 0.5f);

        System.out.println(iterations + ":\t\tms: " + ms + "\t\tMB: " + mb + "\t\tMB/s: " + mbs + "\t\thash: " + hash);
    }

}
