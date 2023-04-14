package example.plot;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class GeneratorBBS implements Generator {
    private final int p, q, m;
    private int next;
    List<Integer> list = new ArrayList<>();

    public GeneratorBBS() {
        p = 65003;
        q = 65001;
        m = p * q;
    }

    @Override
    public int setRandom(int min, int max) {
        next = (next * next) % m;
        return Math.abs(min + next % (max - min));
    }

    @Override
    public List<Integer> generate(int symbols) {
        next = (int) System.currentTimeMillis();

        for (int i = 0; i < symbols; i++) {
            final int number = setRandom(0, 32);
            list.add(number);
        }
        System.out.println(list);
        return list;
    }
}
