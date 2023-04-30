package example.plot;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VigenereCipher {
    private final int bias, letters;
    String enc;
    GeneratorLFSR lfsr;
    List<Integer> list = new ArrayList<>();


    public VigenereCipher(final int bias, final int letters) {
        this.bias = bias;
        this.letters = letters;
        lfsr = new GeneratorLFSR();
    }

    public void getVigenereKeyInt(String text, Integer keyInt) throws IOException {
        list.clear();
        enc = this.encrypt(text, keyInt);
//        getList(enc);

        System.out.println(text);
        System.out.println(enc);

////        filewrite("src/main/resources/ConstVigenere.txt", enc);
//
//        return list;
    }

    public List<Integer> getVigenereKeyStr(String text, String key) throws IOException {
        list.clear();
        enc = this.encrypt(text, key);
        getList(enc);

        System.out.println(text);
        System.out.println(enc);

//        filewrite("src/main/resources/proverbVigenere.txt", enc);

        return list;
    }

    public List<Integer> getVigenereKeyList(String text, List<Integer> keyList) throws IOException {
        list.clear();
        enc = this.encrypt(text, keyList);
        getList(enc);

        System.out.println(text);
        System.out.println(enc);

//        filewrite("src/main/resources/LFSRVigenere.txt", enc);
        return list;
    }

    private void getList(String str) {
        System.out.println("\n");
        for (int i = 0; i < str.length(); i++) {
            list.add((int) str.charAt(i) - 896);
        }
    }

    public String encrypt(final String text, final Integer key) {
        String encrypt = "";
        for (int i = 0, len = text.length(); i < len; i++) {
            encrypt += (char) (text.charAt(i) + key);
        }
        return encrypt;
    }

    public String encrypt(final String text, final String key) {
        String encrypt = "";
        final int keyLen = key.length();
        for (int i = 0, len = text.length(); i < len; i++) {
            encrypt += (char) (((text.charAt(i) + key.charAt(i % keyLen) - 2 * this.bias) % this.letters) + this.bias);
        }
        return encrypt;
    }

    public String encrypt(final String text, final List<Integer> key) {
        String encrypt = "";
        final int keyLen = key.size();
        for (int i = 0, len = text.length(); i < len; i++) {
            encrypt += (char) (((text.charAt(i) + key.get(i % keyLen) - 2 * this.bias) % this.letters) + this.bias);
        }
        return encrypt;
    }

    public static void filewrite(String filename, String str) throws IOException {
        FileOutputStream file = new FileOutputStream(filename, true);
        DataOutputStream myfile = new DataOutputStream(file);

//        cp866
//        windows-1251
//        UTF-8
//        str = new String(str.getBytes("windows-1251"), "cp866");
//        myfile.writeBytes(str);

//        for (int i = 0; i < str.length(); i++) {
//            myfile.write(str.charAt(i));
//        }

        myfile.close();
    }
}
