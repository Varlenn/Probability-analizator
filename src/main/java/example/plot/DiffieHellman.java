package example.plot;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.Math.pow;

public class DiffieHellman {

    static Scanner in = new Scanner(System.in);
    static int mem, num, deg, modul, res, len;
    static int[] mas;
    static boolean stackFull = false;
    static List<Integer> list = new ArrayList<>();
    static long x, y, kx, ky;
    static BigInteger a, b;


    public static void main(String[] args) {
        System.out.println("Введите P: ");
        modul = in.nextInt();
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

        System.out.println("Введите секретный ключ участника A: ");
        x = in.nextInt();
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
}