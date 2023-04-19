package example.plot;

import java.util.ArrayList;
import java.util.List;

public class GeneratorBBS implements Generator {
    private final int p, q, m;
    private int x;
    List<Integer> list = new ArrayList<>();

    public GeneratorBBS() {
        p = 65003;
        q = 65011;
        m = p * q;
    }

    @Override
    public int setRandom(int min, int max) {
        x = (x * x) % m;
        return Math.abs(x % (max - min));
    }

    @Override
    public List<Integer> generate(int symbols) {
        x = (int) System.currentTimeMillis();

        for (int i = 0; i < symbols; i++) {
            final int number = setRandom(0, 32);
            list.add(number);
        }
//        System.out.println(list);
        return list;
    }
}
