package example.plot;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Feistel {


    private static int rounds;
    private List<Integer> list;
    int[] a;
    static int[] b;

    public static void main(String[] args) throws IOException {

//        a = fileread("src/main/resources/textChipher.txt");                     //Считываем файл, который надо зашифровать
//        feist(a, keygen(3));                     //Отправляем на шифрование
//        filewrite("src/main/resources/shifr.txt", a);                   //Записываем зашифрованный файл


//        int b[] = fileread("shifr");            //Считываем шифр
//        refeist(b, keygen(3));                   //Расшифровываем
//        filewrite("src/main/resources/output.txt", b);                  //Записываем результат
    }

    public static int[] fileread(String filename) throws IOException {
        DataInputStream myfile = new DataInputStream(new FileInputStream(filename));
        ArrayList<Integer> list = new ArrayList<Integer>();

        int p;
        p = myfile.read();
        while (p != -1) {
            list.add(p);
            p = myfile.read();
        }
        if (list.size() % 2 != 0)
            list.add(0);
        int a[] = new int[list.size()];
        for (int j = 0; j < list.size(); j++) {
            a[j] = list.get(j);
        }
        myfile.close();
        return a;
    }

    public List<Integer> getFeistel(String file, int iterations, boolean reFeist) throws IOException {
        list = new ArrayList<>();
        a = fileread(file);
        rounds = iterations;
        feist(a, keygen(rounds), reFeist);                     //Отправляем на шифрование
//        filewrite("src/main/resources/shifr.txt", a);                   //Записываем зашифрованный файл
//        feist(a, keygen(8), true);
        for (int i = 0; i < a.length; i++) {
            list.add(a[i]);
        }
        b = a;

        return list;
    }


    public static int[] keygen(int k) {                     //Генератор ключей
        int n = rounds;
        String str = "Родной куст и зайцу дорог.";
        int a[] = new int[n];
        int t = 1;
        for (int i = 0; i < n; i++) {
            t = str.charAt(i);
            a[i] = right(k, t * 6) ^ left(k, t * 7);
        }
        return a;
    }

    public static void feist(int[] a, int[] key, boolean reverse) {
        int round = reverse? rounds: 1;
        if (reverse) {
            a = b;
        }

        for (int j = 0; j < a.length; j+=2) {
            int l = a[j];
            int r = a[j+1];
            for (int i = 0; i < rounds; i++) {
                if (i < rounds - 1) // если не последний раунд
                {
                    int t = l;
                    l = r ^ f(l, round);
                    r = t;
                } else // последний раунд
                {
                    r = r ^ f(l, round);
                }
                round += reverse ? -1 : 1;
            }
            a[j] = l;
            a[j+1] = r;
        }
    }
    private static int f(int b, int k) {
        return (b + k)%256;
    }


    public static int left(int a, int i) {

        return (a << i) | (a >> 32 - i);
    }

    public static int right(int a, int i) {

        return (a >> i) | (a << 32 - i);
    }
}

