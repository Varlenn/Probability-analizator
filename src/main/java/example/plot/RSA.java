package example.plot;

import java.math.BigInteger;


class RSA {
    int num, eVal, dVal, cVal;
    BigInteger c, n, p, q, e, d, sign, el, en, de, crs, des;

    public RSA(int num, int cVal, int eVal) {
        this.num = num;
        this.cVal = cVal;
        this.eVal = eVal;
    }

    public void rsaAlgorithm() {
        c = BigInteger.valueOf(cVal);
        n = BigInteger.valueOf(num);
        int t = 0;

        for (int i = 2; i < num; i++) {
            if (num % i == 0) {
                t = i;
                break;
            }
        }
        dVal = 1;
        while ((eVal * dVal) % ((t - 1) * ((num/t) - 1)) != 1) {
            dVal++;
        }

        p = BigInteger.valueOf(t);
        q = n.divide(p);
        d = BigInteger.valueOf(dVal);
        el = (p.subtract(BigInteger.valueOf(1))).multiply(q.subtract(BigInteger.valueOf(1)));

        System.out.println("Зашифрованное сообщение: " + c);
        System.out.println("n: " + n);
        System.out.println("p: " + p);
        System.out.println("q: " + q);
        System.out.println("Значение функции Эйлера: " + el);
        System.out.println("e: " + eVal);
        System.out.println("d: " + d);


        System.out.println("Расшифрованное сообщение: " + decrypt());
        System.out.println("Еще раз зашифрованное сообщение: " + encrypt());

        createSign();
        decryptSign();
    }

    public BigInteger decrypt() {
        c = c.pow(dVal).mod(n);
        de = c;
        return c;
    }

    public BigInteger encrypt() {
        c = c.pow(eVal).mod(n);
        en = c;
        return c;
    }

    public void createSign() {
        decrypt();
        sign = c.pow(dVal).mod(n);
        crs = sign;
        System.out.println("Подпись: " + sign);
    }

    public void decryptSign() {
        sign = sign.pow(eVal).mod(n);
        des = sign;
        System.out.println("Расшифрованная подпись: " + sign);
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int geteVal() {
        return eVal;
    }

    public void seteVal(int eVal) {
        this.eVal = eVal;
    }

    public int getdVal() {
        return dVal;
    }

    public void setdVal(int dVal) {
        this.dVal = dVal;
    }

    public int getcVal() {
        return cVal;
    }

    public void setcVal(int cVal) {
        this.cVal = cVal;
    }

    public BigInteger getP() {
        return p;
    }

    public void setP(BigInteger p) {
        this.p = p;
    }

    public BigInteger getQ() {
        return q;
    }

    public void setQ(BigInteger q) {
        this.q = q;
    }

    public BigInteger getD() {
        return d;
    }

    public void setD(BigInteger d) {
        this.d = d;
    }

    public BigInteger getEl() {
        return el;
    }

    public BigInteger getEn() {
        return en;
    }

    public BigInteger getDe() {
        return de;
    }

    public BigInteger getCrs() {
        return crs;
    }

    public BigInteger getDes() {
        return des;
    }
}