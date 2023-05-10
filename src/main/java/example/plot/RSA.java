package example.plot;

import java.awt.EventQueue;
import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


/**
 * Quick and dirty implementation of the RSA algorithm
 * Read through main() for a breakdown on the RSA workings
 */
public class RSA {

    /**
     * @param args
     */
    public static void main(String[] args) throws UnsupportedEncodingException {
//        // 1. Find large primes p and q
//        BigInteger p = largePrime(899);
//        BigInteger q = largePrime(899);
//
//        // 2. Compute n from p and q
//        // n is mod for private & public keys, n bit length is key length
//        BigInteger n = 899;
//
//        // 3. Compute Phi(n) (Euler's totient function)
//        // Phi(n) = (p-1)(q-1)
//        // BigIntegers are objects and must use methods for algebraic operations
//        BigInteger phi = getPhi(p, q);
//
//        // 4. Find an int e such that 1 < e < Phi(n) 	and gcd(e,Phi) = 1
////        BigInteger e = genE(phi);
//        BigInteger e = BigInteger.valueOf(29);
//
//        // 5. Calculate d where  d ≡ e^(-1) (mod Phi(n))
//        BigInteger d = extEuclid(e, phi)[1];
//
//        // Print generated values for reference
//        System.out.println("p: " + p);
//        System.out.println("q: " + q);
//        System.out.println("n: " + n);
//        System.out.println("Phi: " + phi);
//        System.out.println("e: " + e);
//        System.out.println("d: " + d);


        BigInteger p = BigInteger.valueOf(0), q, n, d, e;

        n = BigInteger.valueOf(899);
        for (int i = 2; i < n.intValue(); i++) {
//            System.out.println(n.mod(BigInteger.valueOf(i)));
            if (n.mod(BigInteger.valueOf(i)).compareTo(BigInteger.valueOf(0)) > 0) {
                p = BigInteger.valueOf(i);
                break;
            }
        }

        q = n.divide(p);
        e = BigInteger.valueOf(29);
        d = BigInteger.valueOf(1);
        while ((e.multiply(d)).mod((p.subtract(BigInteger.valueOf(1))).multiply(q.subtract(BigInteger.valueOf(1)))).compareTo(BigInteger.valueOf(1)) < 0) {
            d.add(BigInteger.valueOf(1));
        }
        p = BigInteger.valueOf(29);
        q = BigInteger.valueOf(31);
        d = BigInteger.valueOf(29);

        System.out.println("n: " + n);
        System.out.println("p: " + p);
        System.out.println("q: " + q);
        System.out.println("d: " + d);
        System.out.println("e: " + e);
        System.out.println("(p - 1) * (q - 1): " + ((p.subtract(BigInteger.valueOf(1))).multiply (q.subtract(BigInteger.valueOf(1)))));



        // encryption / decryption example
        String message = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя яблочный сок";
        // Convert string to numbers using a cipher
        BigInteger cipherMessage = stringCipher(message);
        // Encrypt the ciphered message
        BigInteger encrypted = encrypt(cipherMessage, e, n);
        // Decrypt the encrypted message
        BigInteger decrypted = decrypt(encrypted, d, n);
        // Uncipher the decrypted message to text
        String restoredMessage = cipherToString(decrypted);

        System.out.println("Original message: " + message);
        System.out.println("Ciphered: " + cipherMessage);
        System.out.println("Encrypted: " + encrypted);
        System.out.println("Decrypted: " + decrypted);
        System.out.println("Restored: " + restoredMessage);
    }


    /**
     * Takes a string and converts each character to an ASCII decimal value
     * Returns BigInteger
     */
    public static BigInteger stringCipher(String message) {
        message = message.toUpperCase();
        String cipherString = "";
        int i = 0;
        while (i < message.length()) {
            int ch = message.charAt(i);
            cipherString = cipherString + ch;
            i++;
        }
        BigInteger cipherBig = new BigInteger(cipherString);
        return cipherBig;
    }


    /**
     * Takes a BigInteger that is ciphered and converts it back to plain text
     *	returns a String
     */
    public static String cipherToString(BigInteger message) throws UnsupportedEncodingException {
        String cipherString = message.toString();
        String output = "";
        int i = 0;
        while (i < cipherString.length()) {
            int temp = 0;
           try {
               temp = Integer.parseInt(cipherString.substring(i, i + 2));
           } catch (StringIndexOutOfBoundsException e) {
               i--;
           }
            char ch = (char) (temp + 1000);
            output = output + ch;
            i = i + 2;
        }
        output = output.replace("Ј", " ");
        output = output.replaceAll("[^А-Я\s]", "");
        return output;
    }


    /** 3. Compute Phi(n) (Euler's totient function)
     *  Phi(n) = (p-1)(q-1)
     *	BigIntegers are objects and must use methods for algebraic operations
     */
//    public static BigInteger getPhi(BigInteger p, BigInteger q) {
//        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
//        return phi;
//    }

    /**
     * Generates a random large prime number of specified bitlength
     *
     */
//    public static BigInteger largePrime(int bits) {
//        Random randomInteger = new Random();
//        BigInteger largePrime = BigInteger.probablePrime(bits, randomInteger);
//        return largePrime;
//    }


    /**
     * Recursive implementation of Euclidian algorithm to find greatest common denominator
     * Note: Uses BigInteger operations
     */
//    public static BigInteger gcd(BigInteger a, BigInteger b) {
//        if (b.equals(BigInteger.ZERO)) {
//            return a;
//        } else {
//            return gcd(b, a.mod(b));
//        }
//    }


    /** Recursive EXTENDED Euclidean algorithm, solves Bezout's identity (ax + by = gcd(a,b))
     * and finds the multiplicative inverse which is the solution to ax ≡ 1 (mod m)
     * returns [d, p, q] where d = gcd(a,b) and ap + bq = d
     * Note: Uses BigInteger operations
     */
//    public static BigInteger[] extEuclid(BigInteger a, BigInteger b) {
//        if (b.equals(BigInteger.ZERO)) return new BigInteger[] {
//                a, BigInteger.ONE, BigInteger.ZERO
//        }; // { a, 1, 0 }
//        BigInteger[] vals = extEuclid(b, a.mod(b));
//        BigInteger d = vals[0];
//        BigInteger p = vals[2];
//        BigInteger q = vals[1].subtract(a.divide(b).multiply(vals[2]));
//        return new BigInteger[] {
//                d, p, q
//        };
//    }


    /**
     * generate e by finding a Phi such that they are coprimes (gcd = 1)
     *
     */
//    public static BigInteger genE(BigInteger phi) {
//        Random rand = new Random();
//        BigInteger e = new BigInteger(1024, rand);
//        do {
//            e = new BigInteger(1024, rand);
//            while (e.min(phi).equals(phi)) { // while phi is smaller than e, look for a new e
//                e = new BigInteger(1024, rand);
//            }
//        } while (!gcd(e, phi).equals(BigInteger.ONE)); // if gcd(e,phi) isnt 1 then stay in loop
//        return e;
//    }

    public static BigInteger encrypt(BigInteger message, BigInteger e, BigInteger n) {
        return message.modPow(e, n);
    }

    public static BigInteger decrypt(BigInteger message, BigInteger d, BigInteger n) {
        return message.modPow(d, n);
    }

//    public static BigInteger n(BigInteger p, BigInteger q) {
//        return p.multiply(q);
//    }
}