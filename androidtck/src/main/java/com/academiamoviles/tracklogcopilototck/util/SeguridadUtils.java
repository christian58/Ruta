package com.academiamoviles.tracklogcopilototck.util;

import android.util.Base64;
import java.security.MessageDigest;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Android on 21/02/2017.
 */

public class SeguridadUtils {

    public static String encriptar(String texto) {

        if(texto.equals(""))
            return "";

        String secretKey = "56af65d2"; //llave para encriptar datos
        String base64EncryptedString = "";

        try {

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digestOfPassword = md.digest(secretKey.getBytes("utf-8"));
            byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);

            SecretKey key = new SecretKeySpec(keyBytes, "DESede");
            Cipher cipher = Cipher.getInstance("DESede");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] plainTextBytes = texto.getBytes("utf-8");
            byte[] buf = cipher.doFinal(plainTextBytes);
            //byte[] base64Bytes = Base64.encodeBase64(buf);
            byte[] base64Bytes = Base64.encode(buf,Base64.DEFAULT);
            base64EncryptedString = new String(base64Bytes);

        } catch (Exception ex) {
        }
        return base64EncryptedString;
    }

    public static String desencriptar(String textoEncriptado) throws Exception {

        if(textoEncriptado.equals(""))
            return "";

        String secretKey = "56af65d2"; //llave para encriptar datos
        String base64EncryptedString = "";

        try {
            //byte[] message = Base64.decodeBase64(textoEncriptado.getBytes("utf-8"));
            byte[] message = Base64.decode(textoEncriptado.getBytes("utf-8"),Base64.DEFAULT);
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digestOfPassword = md.digest(secretKey.getBytes("utf-8"));
            byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
            SecretKey key = new SecretKeySpec(keyBytes, "DESede");

            Cipher decipher = Cipher.getInstance("DESede");
            decipher.init(Cipher.DECRYPT_MODE, key);

            byte[] plainText = decipher.doFinal(message);

            base64EncryptedString = new String(plainText, "UTF-8");

        } catch (Exception ex) {
        }
        return base64EncryptedString;
    }
}