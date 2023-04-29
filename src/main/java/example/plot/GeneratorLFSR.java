package example.plot;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class GeneratorLFSR implements Generator {
    private final int[] TAPS = { 1, 22,  22, 32 };
    private final int M = 32;
    private final boolean[] bits  = new boolean[M + 1];
    List<Integer> list;

    public GeneratorLFSR() {
        final int seed = (int) System.currentTimeMillis();
        for (int i = 0; i < M; i++) {
            bits[i] = (((1 << i) & seed) >>> i) == 1;
        }
    }

    @Override
    public int setRandom(int min, int max) {
        int next = 0;

        for (int i = 0; i < M; i++) {
            next |= (bits[i] ? 1 : 0) << i;
        }

        if (next < 0) {
            next++;
        }

        bits[M] = false;

        for (final int tap : TAPS) {
            bits[M] ^= bits[M - tap];
        }

        for (int i = 0; i < M; i++) {
            bits[i] = bits[i + 1];
        }

        return Math.abs(min + (next % (max - min)));
    }

    @Override
    public List<Integer> generate(int symbols) {
        list = new ArrayList<>();
        for (int i = 0; i < symbols; i++) {
            final int number = setRandom(0, 32);
            list.add(number);
        }
//        System.out.println(list);
        return list;
    }
}
