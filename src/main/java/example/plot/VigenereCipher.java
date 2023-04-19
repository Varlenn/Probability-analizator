package example.plot;

import java.util.ArrayList;
import java.util.List;

public class VigenereCipher {
    private final int bias, letters;
    String enc,dec;
//    final String key;
//    final Integer keyInt;
    GeneratorLFSR lfsr;
//    List<Integer> keyList;
    List<Integer> list = new ArrayList<>();


    public VigenereCipher(final int bias, final int letters) {
        this.bias = bias;
        this.letters = letters;
        lfsr = new GeneratorLFSR();
    }

    public List<Integer> getVigenereKeyInt(String text, Integer keyInt) {
        enc = this.encrypt(text, keyInt);
//        dec = this.decrypt(enc, keyInt);
//        System.out.println(dec);
//        System.out.println(enc);
        getList(enc);

        return list;
    }

    public List<Integer> getVigenereKeyStr(String text, String key) {
        enc = this.encrypt(text, key);
//        dec = this.decrypt(enc, key);
//        System.out.println(dec);
//        System.out.println(enc);
        getList(enc);

        return list;
    }

    public List<Integer> getVigenereKeyList(String text, List<Integer> keyList) {
        enc = this.encrypt(text, keyList);
//        dec = this.decrypt(enc, keyList);
//        System.out.println(dec);
//        System.out.println(enc);
        getList(enc);
        System.out.println(list);

        return list;
    }

    private void getList(String str) {
        for (int i = 0; i < str.length(); i++) {
            list.add((int) str.charAt(i) - 912);
        }
    }


    public String encrypt(final String text, final Integer key) {
        String encrypt = "";
        for (int i = 0, len = text.length(); i < len; i++) {
            encrypt += (char) (((text.charAt(i) + key)));
        }
        return encrypt;
    }

    public String decrypt(final String cipher, Integer key) {
        String decrypt = "";
        for (int i = 0, len = cipher.length(); i < len; i++) {
            decrypt += (char) (((cipher.charAt(i) - key)));
        }
        return decrypt;
    }

    public String encrypt(final String text, final String key) {
        String encrypt = "";
        final int keyLen = key.length();
        for (int i = 0, len = text.length(); i < len; i++) {
            encrypt += (char) (((text.charAt(i) + key.charAt(i % keyLen) - 2 * this.bias) % this.letters) + this.bias);
        }
        return encrypt;
    }

    public String decrypt(final String cipher, final String key) {
        String decrypt = "";
        final int keyLen = key.length();
        for (int i = 0, len = cipher.length(); i < len; i++) {
            decrypt += (char) (((cipher.charAt(i) - key.charAt(i % keyLen) + this.letters) % this.letters) + this.bias);
        }
        return decrypt;
    }

    public String encrypt(final String text, final List<Integer> key) {
        String encrypt = "";
        final int keyLen = key.size();
        for (int i = 0, len = text.length(); i < len; i++) {
            encrypt += (char) (((text.charAt(i) + key.get(i % keyLen) - 2 * this.bias) % this.letters) + this.bias);
        }
        return encrypt;
    }

    public String decrypt(final String cipher, List<Integer> key) {
        String decrypt = "";
        final int keyLen = key.size();
        for (int i = 0, len = cipher.length(); i < len; i++) {
            decrypt += (char) (((cipher.charAt(i) - key.get(i % keyLen) + this.letters) % this.letters) + this.bias);
        }
        return decrypt;
    }
}
