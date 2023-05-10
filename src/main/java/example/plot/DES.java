package example.plot;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;

public class DES {

    public String Left_Circular_Shift(String key, int number){
        return key.substring(number) + key.substring(0, number);
    }

    public ArrayList<String> Subkey_Generation(String key) throws UnsupportedEncodingException {

        ArrayList<String> keys = new ArrayList<String>();
//        byte[] decodedBytes = Base64.getUrlDecoder().decode("Родной куст и зайцу дорог.");
//        key = new String(decodedBytes);
        key = "Родной куст и зайцу дорог.";
        key = new String(key.getBytes(StandardCharsets.UTF_8), "UTF-8");
        if(key.length() == 96){
            for(int i= 0; i < 10 ; i ++){
                String new_shifted = Left_Circular_Shift(key, i+1);
                String key_i_th = "";
                if(i % 2 == 0){
                    for (int j = 0; j < 96 ; j = j + 2) {
                        key_i_th += new_shifted.charAt(j);
                    }
                }
                else{
                    for (int j = 1; j < 96 ; j = j + 2) {
                        key_i_th += new_shifted.charAt(j);
                    }
                }

                keys.add(key_i_th);
            }
        }
        return keys;
    }

    public String XOR(String round_i, String key_i){
        String result = "";
        for (int i = 0; i < round_i.length(); i++) {
            result += round_i.charAt(i)^key_i.charAt(i);
        }
        return result;
    }

    private int find_index(String[] row, String element) {
        int index = -1;

        int i = 0;
        while(i < row.length) {
            if(row[i].equals(element)) {
                index = i;
            }
            i++;
        }
        return index;
    }

    public String DES_boxes(String divided){
        String outer = "", middle = "";
        outer += divided.charAt(0);
        outer += divided.charAt(5);
        middle = divided.substring(1,5);

        String [] row = {"00", "01", "10", "11"};
        String [] column = {"0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111", "1000", "1001", "1010", "1011", "1100", "1101", "1110", "1111"};
        String[][] arr = {{"0010", "1100", "0100", "0001", "0111", "1010", "1011", "0110", "1000", "0101", "0011", "1111", "1101", "0000", "1110", "1001"},
                {"1110", "1011", "0010", "1100", "0100", "0111", "1101", "0001", "0101", "0000", "1111", "1010", "0011", "1001", "1000", "0110"},
                {"0100", "0010", "0001", "1011", "1010", "1101", "0111", "1000", "1111", "1001", "1100", "0101", "0110", "0011", "0000", "1110"},
                {"1011", "1000", "1100", "0111", "0001", "1110", "0010", "1101", "0110", "1111", "0000", "1001", "1010", "0100", "0101", "0011"}};
        int row_index = find_index(row, outer);
        int column_index = find_index(column, middle);

        return arr[row_index][column_index];
    }

    public String scramble_function(String round_i, String key_i){
        String result = "";
        String new_plaintext = XOR(round_i, key_i);
        new_plaintext += XOR(new_plaintext.substring(0,6), new_plaintext.substring(6,12));
        new_plaintext += XOR(new_plaintext.substring(12,18), new_plaintext.substring(18,24));
        new_plaintext += XOR(new_plaintext.substring(24,30), new_plaintext.substring(30,36));
        new_plaintext += XOR(new_plaintext.substring(36,42), new_plaintext.substring(42));
        for(int i = 0; i < 72; i = i + 6){
            result += DES_boxes(new_plaintext.substring(i, i+ 6));
        }
        String dummy = "";
        for(int i=0; i <= result.length()-2; i=i+2){
            dummy += result.charAt(i+1);
            dummy += result.charAt(i);
        }
        result = dummy;
        return result;
    }

    public ArrayList<String> input_generator(String input,boolean isEnc){

        while(input.length()%96!=0){
            input+="0";
        }
        ArrayList<String> inputs = new ArrayList<>();
        for(int i=0;i<input.length();i+=96){
            String dummy = input.substring(i,i+96);
            inputs.add(dummy);
        }
        return inputs;
    }

    public String feistel_EBC(String input,String key,boolean isEnc) throws UnsupportedEncodingException {

        String output="";
        ArrayList<String> keys = Subkey_Generation(key);
        ArrayList<String> inputs = input_generator(input,isEnc);
        ArrayList<String> func_output = new ArrayList<String>();
        //Encription
        if(isEnc){
            for (String plaintext:inputs) {
                String l ="";
                String r ="";
                String l0=plaintext.substring(0,(plaintext.length()/2));
                String r0=plaintext.substring((plaintext.length()/2));
                for(int i=1;i<=10;i++){
                    l = r0;
                    r = XOR(l0,scramble_function(r0,keys.get(i-1)));
                    r0 = r;
                    l0 = l;
                }
                func_output.add((l+r));
            }
        }

        else{
            for (String ciphertext:inputs){
                String l = "";
                String r = "";
                String l0 = ciphertext.substring(0,(ciphertext.length()/2));
                String r0 = ciphertext.substring((ciphertext.length()/2));
                for(int i=10;i>=1;i--){
                    l = XOR(r0,scramble_function(l0,keys.get(i-1)));
                    r = l0;
                    r0 = r;
                    l0 = l;
                }
                func_output.add((l+r));
            }

        }

        for (String out:func_output) {
            output += out;
        }
        return output;
    }

    public String feistel_CBC(String input,String key,boolean isEnc) throws UnsupportedEncodingException {
        String output="";
        ArrayList<String> keys = Subkey_Generation(key);
        ArrayList<String> inputs = input_generator(input,isEnc);
        ArrayList<String> func_output = new ArrayList<String>();
        String iv="";
        for (int i=0;i<96;i++) iv+="1";
        func_output.add(iv);

        if(isEnc){
            int counter=0;
            for (String plaintext:inputs) {
                plaintext = XOR(func_output.get(counter),plaintext);
                String l ="";
                String r ="";
                String l0=plaintext.substring(0,(plaintext.length()/2));
                String r0=plaintext.substring((plaintext.length()/2));
                counter++;
                for(int i=1;i<=10;i++){
                    l = r0;
                    r = XOR(l0,scramble_function(r0,keys.get(i-1)));
                    r0 = r;
                    l0 = l;
                }
                func_output.add((l+r));
            }
        }
        //Decription
        else{
            ArrayList<String> ciphers = new ArrayList<String>();
            ciphers.add(iv);
            int counter=0;
            for (String ciphertext:inputs){
                String l = "";
                String r = "";
                String l0 = ciphertext.substring(0,(ciphertext.length()/2));
                String r0 = ciphertext.substring((ciphertext.length()/2));
                ciphers.add(ciphertext);
                for(int i=10;i>=1;i--){
                    l = XOR(r0,scramble_function(l0,keys.get(i-1)));
                    r = l0;
                    r0 = r;
                    l0 = l;
                }
                func_output.add(XOR(ciphers.get(counter),(l+r)));
                counter++;
            }
        }
        func_output.remove(0);
        for (String out:func_output) {
            output+=out;
        }
        return output;
    }


    public String feistel_OFB(String input,String key,boolean isEnc) throws UnsupportedEncodingException {
        String output="";
        ArrayList<String> keys = Subkey_Generation(key);
        ArrayList<String> inputs = input_generator(input,isEnc);
        ArrayList<String> func_output = new ArrayList<String>();
        ArrayList<String> func_input = new ArrayList<String>();
        String iv="";
        for (int i=0;i<96;i++) iv+="1";
        func_input.add(iv);

        if(isEnc){
            int counter=0;
            for (String plaintext:inputs) {
                input = func_input.get(counter);
                String l ="";
                String r ="";
                String l0=input.substring(0,(input.length()/2));
                String r0=input.substring((input.length()/2));
                counter++;
                for(int i=1;i<=10;i++){
                    l = r0;
                    r = XOR(l0,scramble_function(r0,keys.get(i-1)));
                    r0 = r;
                    l0 = l;
                }
                String feistel_output = l+r;
                func_input.add(feistel_output);
                func_output.add(XOR(feistel_output,plaintext));
            }
        }
        //Decription
        else{
            int counter=0;
            for (String plaintext:inputs) {
                input = func_input.get(counter);
                String l ="";
                String r ="";
                String l0=input.substring(0,(input.length()/2));
                String r0=input.substring((input.length()/2));
                counter++;
                for(int i=1;i<=10;i++){
                    l = r0;
                    r = XOR(l0,scramble_function(r0,keys.get(i-1)));
                    r0 = r;
                    l0 = l;
                }
                String feistel_output = l+r;
                func_input.add(feistel_output);
                func_output.add(XOR(feistel_output,plaintext));
            }
        }
        for (String out:func_output) {
            output+=out;
        }
        return output;
    }

    public static void main(String[] args) throws IOException {

        DES feistel = new DES();
        boolean isEnc = true; // true for encryption false for decryption
        String keyPath="src/main/resources/key.txt";
        String inputPath="src/main/resources/test.txt";
        String outputPath="src/main/resources/testOutp.txt";
        String mode="OFB";
        for (int i = 1; i < args.length; i++) {
            switch (args[i]) {
                case "-K":
                    keyPath = args[i + 1];
                    break;
                case "-I":
                    inputPath = args[i + 1];
                    break;
                case "-O":
                    outputPath = args[i + 1];
                    break;
                case "-M":
                    mode = args[i + 1];
                    break;
            }
        }

        System.setOut(new PrintStream(outputPath));                                                          // creates output file
        FileWriter writer = new FileWriter(outputPath);

        File inputfile = new File(inputPath);
        File keyfile = new File(keyPath);
        BufferedReader key_br = new BufferedReader(new FileReader(keyfile));
        BufferedReader input_br = new BufferedReader(new FileReader(inputfile));
        String input, key;

        try {
            input = input_br.readLine();
            key = key_br.readLine();

            switch (mode) {
                case "ECB":
                    System.out.print(feistel.feistel_EBC(input, key, isEnc));
                    break;
                case "CBC":
                    System.out.print(feistel.feistel_CBC(input, key, isEnc));
                    break;
                case "OFB":
                    System.out.print(feistel.feistel_OFB(input, key, isEnc));
                    break;
            }

        }


        catch (IOException ex) {
            // Handle exception
            System.out.println("error!");
        }
    }
}



















//
//import java.io.UnsupportedEncodingException;
//import java.math.BigInteger;
//
//public class DES {
//
//    public static int KEY_LENGTH = 64;
//
//    private static int[] PC1 =
//            {
//                    57, 49, 41, 33, 25, 17,  9,
//                    1, 58, 50, 42, 34, 26, 18,
//                    10,  2, 59, 51, 43, 35, 27,
//                    19, 11,  3, 60, 52, 44, 36,
//                    63, 55, 47, 39, 31, 23, 15,
//                    7, 62, 54, 46, 38, 30, 22,
//                    14,  6, 61, 53, 45, 37, 29,
//                    21, 13,  5, 28, 20, 12,  4
//            };
//
//    // First index is garbage value, loops operating on this should start with index = 1
//    private static int[] KEY_SHIFTS =
//            {
//                    0,  1,  1,  2,  2,  2,  2,  2,  2,  1,  2,  2,  2,  2,  2,  2,  1
//            };
//
//    private static int[] PC2 =
//            {
//                    14, 17, 11, 24,  1,  5,
//                    3, 28, 15,  6, 21, 10,
//                    23, 19, 12,  4, 26,  8,
//                    16,  7, 27, 20, 13,  2,
//                    41, 52, 31, 37, 47, 55,
//                    30, 40, 51, 45, 33, 48,
//                    44, 49, 39, 56, 34, 53,
//                    46, 42, 50, 36, 29, 32
//            };
//
//
//    private static int[][] s1 = {
//            {14, 4, 13,  1,  2, 15, 11,  8,  3, 10,  6, 12,  5,  9,  0,  7},
//            {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11,  9,  5,  3,  8},
//            {4, 1, 14,  8, 13,  6, 2, 11, 15, 12,  9,  7,  3, 10,  5,  0},
//            {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}
//    };
//
//    private static int[][] s2 = {
//            {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
//            {3, 13,  4, 7, 15,  2,  8, 14, 12,  0, 1, 10,  6,  9, 11,  5},
//            {0, 14, 7, 11, 10,  4, 13,  1,  5,  8, 12,  6,  9,  3,  2, 15},
//            {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14,  9}
//    };
//
//    private static int[][] s3 = {
//            {10, 0, 9, 14, 6, 3, 15, 5,  1, 13, 12, 7, 11, 4, 2,  8},
//            {13, 7, 0, 9, 3,  4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
//            {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14,  7},
//            {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}
//    };
//
//    private static int[][] s4 = {
//            {7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
//            {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14,  9},
//            {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
//            {3, 15, 0, 6, 10, 1, 13, 8, 9,  4, 5, 11, 12, 7, 2, 14}
//    };
//
//    private static int[][] s5 = {
//            {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
//            {14, 11, 2, 12,  4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
//            {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
//            {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}
//    };
//
//    private static int[][] s6 = {
//            {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
//            {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
//            {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
//            {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}
//    };
//
//    private static int[][] s7 = {
//            {4, 11, 2, 14, 15,  0, 8, 13 , 3, 12, 9 , 7,  5, 10, 6, 1},
//            {13 , 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
//            {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
//            {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}
//    };
//
//    private static int[][] s8 = {
//            {13, 2, 8,  4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
//            {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6 ,11, 0, 14, 9, 2},
//            {7, 11, 4, 1, 9, 12, 14, 2,  0, 6, 10 ,13, 15, 3, 5, 8},
//            {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6 ,11}
//    };
//
//    private static int[][][] s = {s1, s2, s3, s4, s5, s6, s7, s8};
//
//    private static int[] g =
//            {
//                    32,  1,  2,  3,  4,  5,
//                    4,  5,  6,  7,  8,  9,
//                    8,  9, 10, 11, 12, 13,
//                    12, 13, 14, 15, 16, 17,
//                    16, 17, 18, 19, 20, 21,
//                    20, 21, 22, 23, 24, 25,
//                    24, 25, 26, 27, 28, 29,
//                    28, 29, 30, 31, 32,  1
//            };
//
//
//    static int[] p =
//            {
//                    16,  7, 20, 21,
//                    29, 12, 28, 17,
//                    1, 15, 23, 26,
//                    5, 18, 31, 10,
//                    2,  8, 24, 14,
//                    32, 27,  3,  9,
//                    19, 13, 30,  6,
//                    22, 11,  4, 25
//            };
//
//    static int[] IP =
//            {
//                    58, 50, 42, 34, 26, 18, 10 , 2,
//                    60, 52, 44, 36, 28, 20, 12, 4,
//                    62, 54, 46, 38, 30, 22, 14, 6,
//                    64, 56, 48, 40, 32, 24, 16, 8,
//                    57, 49, 41, 33, 25, 17, 9, 1,
//                    59, 51, 43, 35, 27, 19, 11, 3,
//                    61, 53, 45, 37, 29, 21, 13, 5,
//                    63, 55, 47, 39, 31, 23, 15, 7
//            };
//
//    static int[] IPi =
//            {
//                    40, 8, 48, 16, 56, 24, 64, 32,
//                    39, 7, 47, 15, 55, 23, 63, 31,
//                    38, 6, 46, 14, 54, 22, 62, 30,
//                    37, 5, 45, 13, 53, 21, 61, 29,
//                    36, 4, 44, 12, 52, 20, 60, 28,
//                    35, 3, 43 ,11, 51, 19, 59, 27,
//                    34, 2, 42, 10, 50, 18, 58, 26,
//                    33, 1, 41, 9, 49, 17, 57, 25
//            };
//
//    private long[] K;
//
//    public DES() {
//
//        // First index is garbage value, loops operating on this should start with index = 1
//        K = new long[17];
//
//    }
//
//    private static String binToHex(String bin) {
//
//        BigInteger b = new BigInteger(bin, 2);
//        String ciphertext = b.toString(16);
//
//        return ciphertext;
//    }
//
//    private static String hexToBin(String hex) {
//
//        BigInteger b = new BigInteger(hex, 16);
//        String bin = b.toString(2);
//
//        return bin;
//    }
//
//    private static String binToUTF(String bin) {
//
//        // Convert back to String
//        byte[] ciphertextBytes = new byte[bin.length()/8];
//        String ciphertext = null;
//        for(int j = 0; j < ciphertextBytes.length; j++) {
//            String temp = bin.substring(0, 8);
//            byte b = (byte) Integer.parseInt(temp, 2);
//            ciphertextBytes[j] = b;
//            bin = bin.substring(8);
//        }
//
//        try {
//            ciphertext = new String(ciphertextBytes, "utf-8");
//        } catch (UnsupportedEncodingException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        return ciphertext.trim();
//    }
//
//    private static String utfToBin(String utf) {
//
//        // Convert to binary
//        byte[] bytes = null;
//        try {
//            bytes = utf.getBytes("utf-8");
//        } catch (UnsupportedEncodingException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        String bin = "";
//        for (int i = 0; i < bytes.length; i++) {
//            int value = bytes[i];
//            for (int j = 0; j < 8; j++)
//            {
//                bin += ((value & 128) == 0 ? 0 : 1);
//                value <<= 1;
//            }
//        }
//        return bin;
//    }
//
//    /**
//     * Encrypt a string message with the DES block cipher
//     * @param key
//     * @param plaintext
//     * @return
//     */
//    public String encrypt(String key, String plaintext) {
//
//        // Build the key schedule
//        buildKeySchedule(hash(key));
//
//        String binPlaintext = plaintext;
//
//        // Add padding if necessary
//        int remainder = binPlaintext.length() % 64;
//        if (remainder != 0) {
//            for (int i = 0; i < (64 - remainder); i++)
//                binPlaintext = "0" + binPlaintext;
//        }
//
//        // Separate binary plaintext into blocks
//        String[] binPlaintextBlocks = new String[binPlaintext.length()/64];
//        int offset = 0;
//        for (int i = 0; i < binPlaintextBlocks.length; i++) {
//            binPlaintextBlocks[i] = binPlaintext.substring(offset, offset+64);
//            offset += 64;
//        }
//
//        String[] binCiphertextBlocks = new String[binPlaintext.length()/64];
//
//        // Encrypt the blocks
//        for (int i = 0; i < binCiphertextBlocks.length; i++)
//            try {
//                binCiphertextBlocks[i] = encryptBlock(binPlaintextBlocks[i]);
//            } catch (Exception e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//
//        // Build the ciphertext binary string from the blocks
//        String binCiphertext = "";
//        for (int i = 0; i < binCiphertextBlocks.length; i++)
//            binCiphertext += binCiphertextBlocks[i];
//
//        // Destroy key schedule
//        for (int i=0;i<K.length;i++)
//            K[i] = 0;
//
//
//        return binCiphertext;
//    }
//
//    /**
//     * Decrypt a string message with the DES block cipher
//     * @param key : String - the key to decrypt with
//     * @param plaintext : String - Hex string to decrypt
//     * @return Plaintext message string
//     */
//    public String decrypt(String key, String plaintext) {
//
//        // Build the key schedule
//        buildKeySchedule(hash(key));
//
//        String binPlaintext = null;
//
//        binPlaintext = plaintext;
//
//        // Add padding if necessary
//        int remainder = binPlaintext.length() % 64;
//        if (remainder != 0) {
//            for (int i = 0; i < (64 - remainder); i++)
//                binPlaintext = "0" + binPlaintext;
//        }
//
//        // Separate binary plaintext into blocks
//        String[] binPlaintextBlocks = new String[binPlaintext.length()/64];
//        int offset = 0;
//        for (int i = 0; i < binPlaintextBlocks.length; i++) {
//            binPlaintextBlocks[i] = binPlaintext.substring(offset, offset+64);
//            offset += 64;
//        }
//
//        String[] binCiphertextBlocks = new String[binPlaintext.length()/64];
//
//        // Encrypt the blocks
//        for (int i = 0; i < binCiphertextBlocks.length; i++) {
//            try {
//                binCiphertextBlocks[i] = decryptBlock(binPlaintextBlocks[i]);
//            } catch (Exception e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//
//        // Build the ciphertext binary string from the blocks
//        String binCiphertext = "";
//        for (int i = 0; i < binCiphertextBlocks.length; i++)
//            binCiphertext += binCiphertextBlocks[i];
//
//        // Destroy key schedule
//        for (int i=0;i<K.length;i++)
//            K[i] = 0;
//
//        return binCiphertext;
//    }
//
//    public String encryptBlock(String plaintextBlock) throws Exception {
//        int length = plaintextBlock.length();
//        if (length != 64)
//            throw new RuntimeException("Input block length is not 64 bits!");
//
//        //Initial permutation
//        String out = "";
//        for (int i = 0; i < IP.length; i++) {
//            out = out + plaintextBlock.charAt(IP[i] - 1);
//        }
//
//        String mL = out.substring(0, 32);
//        String mR = out.substring(32);
//
//        for (int i = 0; i < 16; i++) {
//
//            // 48-bit current key
//            String curKey = Long.toBinaryString(K[i+1]);
//            while(curKey.length() < 48)
//                curKey = "0" + curKey;
//
//            // Get 32-bit result from f with m1 and ki
//            String fResult = f(mR, curKey);
//
//            // XOR m0 and f
//            long f = Long.parseLong(fResult, 2);
//            long cmL = Long.parseLong(mL, 2);
//
//            long m2 = cmL ^ f;
//            String m2String = Long.toBinaryString(m2);
//
//            while(m2String.length() < 32)
//                m2String = "0" + m2String;
//
//            mL = mR;
//            mR = m2String;
//        }
//
//        String in = mR + mL;
//        String output = "";
//        for (int i = 0; i < IPi.length; i++) {
//            output = output + in.charAt(IPi[i] - 1);
//        }
//
//        return output;
//    }
//
//    public String decryptBlock(String plaintextBlock) throws RuntimeException {
//        int length = plaintextBlock.length();
//        if (length != 64)
//            throw new RuntimeException("Input block length is not 64 bits!");
//
//        //Initial permutation
//        String out = "";
//        for (int i = 0; i < IP.length; i++) {
//            out = out + plaintextBlock.charAt(IP[i] - 1);
//        }
//
//        String mL = out.substring(0, 32);
//        String mR = out.substring(32);
//
//        for (int i = 16; i > 0; i--) {
//
//            // 48-bit current key
//            String curKey = Long.toBinaryString(K[i]);
//            while(curKey.length() < 48)
//                curKey = "0" + curKey;
//
//            // Get 32-bit result from f with m1 and ki
//            String fResult = f(mR, curKey);
//
//            // XOR m0 and f
//            long f = Long.parseLong(fResult, 2);
//            long cmL = Long.parseLong(mL, 2);
//
//            long m2 = cmL ^ f;
//            String m2String = Long.toBinaryString(m2);
//
//            while(m2String.length() < 32)
//                m2String = "0" + m2String;
//
//            mL = mR;
//            mR = m2String;
//        }
//
//        String in = mR + mL;
//        String output = "";
//        for (int i = 0; i < IPi.length; i++) {
//            output = output + in.charAt(IPi[i] - 1);
//        }
//
//        return output;
//    }
//
//    /**
//     * Hash Function from user <b>sfussenegger</b> on stackoverflow
//     *
//     * @param string : String to hash
//     * @return 64-bit long hash value
//     * @source http://stackoverflow.com/questions/1660501/what-is-a-good-64bit-hash-function-in-java-for-textual-strings
//     */
//
//    // adapted from String.hashCode()
//    public static long hash(String string) {
//        long h = 1125899906842597L; // prime
//        int len = string.length();
//
//        for (int i = 0; i < len; i++) {
//            h = 31*h + string.charAt(i);
//        }
//        return h;
//    }
//
//    public void buildKeySchedule(long key) {
//
//        // Convert long value to 64bit binary string
//        String binKey = Long.toBinaryString(key);
//
//        // Add leading zeros if not at key length for ease of computations
//        while (binKey.length() < 64)
//            binKey = "0" + binKey;
//
//        // For the 56-bit permuted key
//        String binKey_PC1 = "";
//
//        // Apply Permuted Choice 1 (64 -> 56 bit)
//        for (int i = 0; i < PC1.length; i++)
//            binKey_PC1 = binKey_PC1 + binKey.charAt(PC1[i]-1);
//
//        String sL, sR;
//        int iL, iR;
//
//        // Split permuted string in half | 56/2 = 28
//        sL = binKey_PC1.substring(0, 28);
//        sR = binKey_PC1.substring(28);
//
//        // Parse binary strings into integers for shifting
//        iL = Integer.parseInt(sL, 2);
//        iR = Integer.parseInt(sR, 2);
//
//        // Build the keys (Start at index 1)
//        for (int i = 1; i < K.length; i++) {
//
//            // Perform left shifts according to key shift array
//            iL = Integer.rotateLeft(iL, KEY_SHIFTS[i]);
//            iR = Integer.rotateLeft(iR, KEY_SHIFTS[i]);
//
//            // Merge the two halves
//            long merged = ((long)iL << 28) + iR;
//
//            // 56-bit merged
//            String sMerged = Long.toBinaryString(merged);
//
//            // Fix length if leading zeros absent
//            while (sMerged.length() < 56)
//                sMerged = "0" + sMerged;
//
//            // For the 56-bit permuted key
//            String binKey_PC2 = "";
//
//            // Apply Permuted Choice 2 (56 -> 48 bit)
//            for (int j = 0; j < PC2.length; j++)
//                binKey_PC2 = binKey_PC2 + sMerged.charAt(PC2[j]-1);
//
//            // Set the 48-bit key
//            K[i] = Long.parseLong(binKey_PC2, 2);
//        }
//    }
//
//
//    /**
//     * Feistel function in DES algorithm specified in FIPS Pub 46
//     * @param mi : String - 32-bit message binary string
//     * @param key : String - 48-bit key binary string
//     * @return 32-bit output string
//     */
//    public static String f(String mi, String key) {
//
//        // Expansion function g (named E in fips pub 46)
//        String gMi = "";
//        for (int i = 0; i < g.length; i++) {
//            gMi = gMi + mi.charAt(g[i] - 1);
//        }
//
//        long m =  Long.parseLong(gMi, 2);
//        long k = Long.parseLong(key, 2);
//
//        // XOR expanded message block and key block (48 bits)
//        Long result = m ^ k;
//
//        String bin = Long.toBinaryString(result);
//        // Making sure the string is 48 bits
//        while (bin.length() < 48) {
//            bin = "0" + bin;
//        }
//
//        // Split into eight 6-bit strings
//        String[] sin = new String[8];
//        for (int i = 0; i < 8; i++) {
//            sin[i] = bin.substring(0, 6);
//            bin = bin.substring(6);
//        }
//
//
//        // Do S-Box calculations
//        String[] sout = new String[8];
//        for (int i = 0 ; i < 8; i++) {
//            int[][] curS = s[i];
//            String cur = sin[i];
//
//            // Get binary values
//            int row = Integer.parseInt(cur.charAt(0) + "" + cur.charAt(5), 2);
//            int col = Integer.parseInt(cur.substring(1, 5), 2);
//
//            // Do S-Box table lookup
//            sout[i] = Integer.toBinaryString(curS[row][col]);
//
//            // Make sure the string is 4 bits
//            while(sout[i].length() < 4)
//                sout[i] = "0" + sout[i];
//
//        }
//
//        // Merge S-Box outputs into one 32-bit string
//        String merged = "";
//        for (int i = 0; i < 8; i++) {
//            merged = merged + sout[i];
//        }
//
//        // Apply Permutation P
//        String mergedP = "";
//        for (int i = 0; i < p.length; i++) {
//            mergedP = mergedP + merged.charAt(p[i] - 1);
//        }
//
//        return mergedP;
//    }
//
//    public static void main(String[] args) {
//
//        DES des = new DES();
//
//        boolean enc = true;
//        String key1 = "Родной куст и зайцу дорог.", key2 = null, key3 = null, message = "маленький текст!", result = null;
//
//        for (int i = 0; i < args.length; i++) {
//            if (args[i].equals("-k1"))
//                key1 = args[++i];
//            else if (args[i].equals("-k2"))
//                key2 = args[++i];
//            else if (args[i].equals("-k3"))
//                key3 = args[++i];
//            else if (args[i].equals("-m"))
//                message = args[++i];
//            else if (args[i].equals("-d"))
//                enc = false;
//        }
//
//        if (enc) {
//            if (message == null) {
//                System.out.println("No message given to encrypt. Exiting..");
//                System.exit(0);
//            } else if (key1 == null) {
//                System.out.println("Improper use of key arguments. Exiting..");
//                System.exit(0);
//            }
//
//            if (key2 == null) {
//                if (key3 != null) {
//                    System.out.println("Improper use of key arguments. Exiting..");
//                    System.exit(0);
//                }
//                result = des.encrypt(key1, utfToBin(message));
//                System.out.println(binToHex(result));
//            } else {
//                if (key3 == null) {
//                    System.out.println("Improper use of key arguments. Exiting..");
//                    System.exit(0);
//                }
//                result = des.encrypt(key3, des.decrypt(key2, des.encrypt(key1, utfToBin(message))));
//                System.out.println(binToHex(result));
//            }
//        } else {
//            if (message == null) {
//                System.out.println("No data given to decrypt. Exiting..");
//                System.exit(0);
//            } else if (key1 == null) {
//                System.out.println("Improper use of key arguments. Exiting..");
//                System.exit(0);
//            }
//
//            if (key2 == null) {
//                if (key3 != null) {
//                    System.out.println("Improper use of key arguments. Exiting..");
//                    System.exit(0);
//                }
//                result = des.decrypt(key1, hexToBin(message));
//                System.out.println(binToUTF(result));
//            } else {
//                if (key3 == null) {
//                    System.out.println("Improper use of key arguments. Exiting..");
//                    System.exit(0);
//                }
//                result = des.decrypt(key1, des.encrypt(key2, des.decrypt(key3, hexToBin(message))));
//                System.out.println(binToUTF(result));
//            }
//        }
//
//    }
//
//}