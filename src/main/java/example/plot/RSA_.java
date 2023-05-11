package example.plot;

import java.util.HashMap;
import java.util.Map;

public class RSA_ {
    public static void main(String[] args) {
        int p = 0, q, n, d, e;
        String str = "яблоки";

        n = 667;
        for (int i = 2; i < n; i++) {
            if (n % i == 0) {
                p = i;
                break;
            }
        }

        q = n / p;
        e = 29;
        d = 1;
        while ((e * d) % ((p - 1) * (q - 1)) != 1) {
            d++;
        }

        System.out.println("n: " + n);
        System.out.println("p: " + p);
        System.out.println("q: " + q);
        System.out.println("d: " + d);
        System.out.println("e: " + e);
        System.out.println("(p - 1) * (q - 1): " + ((p - 1) * (q - 1)));

    }
}
