package example.plot;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.Math.pow;

public class DiffieHellman {

    Scanner in = new Scanner(System.in);
    int mem, num, deg, modul, res, len;
    int[] mas;
    boolean stackFull = false;
    List<Integer> list = new ArrayList<>();
    long x, y, kx, ky;
    BigInteger a, b;

    public DiffieHellman(int modul, long x) {
        this.modul = modul;
        this.x = x;
    }

    public void dh() {
        System.out.println("Секретный ключ участника A: " + x);
        y = 35 - x;
        System.out.println("Секретный ключ участника В: " + y);

        kx = (long) (pow(list.get(0), x) % modul);
        System.out.println("Открытый ключ участника А: " + kx);
        ky = (long) (pow(list.get(0), y) % modul);
        System.out.println("Открытый ключ участника В: " + ky);


        a = BigInteger.valueOf(ky).pow((int) x).mod(BigInteger.valueOf(modul));
        System.out.println("Обменный ключ участника А: " + a);
        b = BigInteger.valueOf(kx).pow((int) y).mod(BigInteger.valueOf(modul));
        System.out.println("Обменный ключ участника В: " + b);
    }

    public void dhAlgorithm() {
        deg = 0;
        mem = 2;
        mas = new int[modul + 1];
        len = modul - 1;

        while (mem < len) {
            num = mem;
            deg = 0;
            while (deg != len) {
                res = 1;
                deg = 0;
                for (int i = 0; i < len; i++) {
                    mas[i] = 0;
                }
                do {
                    res = res * num;
                    res = res % modul;
                    deg++;
                    if (deg > modul) {
                        res = 1;
                        stackFull = true;
                        continue;
                    }
                    mas[deg] = res;
                }
                while (res != 1);
                num++;
            }

//            System.out.println(num - 1);
            list.add(num - 1);

            for (int i = 0; i < len; i++) {
                for (int j = 0; j < len; j++) {
                    if ((i != j) && (mas[j] == mas[i])) {
                        System.out.println("Неправильно");
                    }
                }
                mem = num;
            }
        }

        if (stackFull) {
            list.remove(list.size() - 1);
        }

        System.out.println(list);
    }

    public int getModul() {
        return modul;
    }

    public void setModul(int modul) {
        this.modul = modul;
    }

    public long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }

    public long getY() {
        return y;
    }

    public long getKx() {
        return kx;
    }

    public long getKy() {
        return ky;
    }

    public BigInteger getA() {
        return a;
    }

    public BigInteger getB() {
        return b;
    }
}