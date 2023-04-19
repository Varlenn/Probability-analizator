package example.plot;

//http://habrahabr.ru/post/187606/
//http://habrahabr.ru/post/140404/

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Feistel {


    private static int rounds = 8;
    private List<Integer> list = new ArrayList<>();
    int a[];

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

    public List<Integer> getFeistel(String file) throws IOException {
        a = fileread(file);
        feist(a, keygen(3));                     //Отправляем на шифрование
//        filewrite("src/main/resources/shifr.txt", a);                   //Записываем зашифрованный файл

        for (int i = 0; i < a.length; i++) {
            list.add(a[i]);
        }
        System.out.println(list);
        return list;
    }

    public static void filewrite(String filename, int[] a) throws IOException {
        FileOutputStream file = new FileOutputStream(filename);
        DataOutputStream myfile = new DataOutputStream(file);

        for (int i = 0; i < a.length; i++) {
            myfile.write(a[i]);
        }
        myfile.close();
    }

    public static int[] keygen(int k) {                     //Генератор ключей
        int n = rounds;
        String str = "Родной куст и зайцу дорог.";
        int a[] = new int[n];
//        int t = 1;
        int t = 1;
        for (int i = 0; i < n; i++) {
            t = str.charAt(i);
            a[i] = right(k, t * 6) ^ left(k, t * 7);

//            a[i] = right(k, t * 6) ^ left(k, t * 7);
//            t++;
        }
        return a;
    }

    public static void feist(int[] a, int[] key) {
        if (a.length % 2 == 0) {                            //Проверяем четное ли количество блоков
            for (int p = 0; p < a.length - 1; p += 2) {     //Разбиваем на левый и правый блок
                int l = a[p];
                int r = a[p + 1];
                for (int i = 0; i < rounds; i++) {          //Начало шифрования
                    if (i < rounds - 1) {
                        int x = key[i] ^ r;
                        l = l ^ f(x);
                        int z = l;
                        l = r;
                        r = z;
                    } else {                                //Последний раунд без обмена
                        int x = key[i] ^ r;
                        l = l ^ f(x);
                    }
                }
                a[p] = l;
                a[p + 1] = r;
            }
        } else {
            System.out.println("Error of length");
        }
    }

//    public static void refeist(int[] a, int[] key) {
//        if (a.length % 2 == 0) {
//            for (int p = 0; p < a.length - 1; p += 2) {
//                int l = a[p];
//                int r = a[p + 1];
//                int k = key.length - 1;                     //Обратный порядок ключей
//                for (int i = 0; i < rounds; i++) {
//                    if (i < rounds - 1) {
//                        int x = key[k] ^ r;
//                        l = l ^ f(x);
//                        int z = l;
//                        l = r;
//                        r = z;
//                    } else {
//                        int x = key[k] ^ r;
//                        l = l ^ f(x);
//                    }
//                    k -= 1;
//                }
//                a[p] = l;
//                a[p + 1] = r;
//            }
//        } else System.out.println("Error of length");
//    }
//
    public static int left(int a, int i) {

        return (a << i) | (a >> 32 - i);
    }

    public static int right(int a, int i) {

        return (a >> i) | (a << 32 - i);
    }

    private static int f(int a) {
        return (((a << 17)) ^ (((a & 0x0F0F0F0F) >> 4) | (~a & 0xF0F0F0F0)));
    }
}

