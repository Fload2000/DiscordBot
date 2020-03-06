package de.fload.util;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.util.Base64;

/**
 * Encryptor class
 */
public class Encryptor {
    private static String password = "KweJiBPo";

    private static void encode(byte[] bytes, OutputStream out) throws Exception {
        Cipher c = Cipher.getInstance("DES");
        SecretKey k = new SecretKeySpec(password.getBytes(), "DES");
        c.init(Cipher.ENCRYPT_MODE, k);
        c.doFinal();

        OutputStream cos = new CipherOutputStream(out, c);
        cos.write(bytes);
        cos.close();
    }

    private static byte[] decode(InputStream is) throws Exception {
        Cipher c = Cipher.getInstance("DES");
        Key k = new SecretKeySpec(password.getBytes(), "DES");
        c.init(Cipher.DECRYPT_MODE, k);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        CipherInputStream cis = new CipherInputStream(is, c);

        for (int b; (b = cis.read()) != -1; ) {
            bos.write(b);
        }

        cis.close();
        return bos.toByteArray();
    }

    public static String encrypt(String input) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        encode(input.getBytes(), out);

        return Base64.getEncoder().encodeToString(out.toByteArray());
    }

    public static String decrypt(String input) throws Exception {
        return new String(decode(new ByteArrayInputStream(Base64.getDecoder().decode(input))));
    }
}