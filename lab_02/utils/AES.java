package lab_02.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class AES {

        private static SecretKeySpec secretKey;
        private static byte[] key;

        public static void setKey(String myKey) {
            MessageDigest sha = null;
            try {
                key = myKey.getBytes("UTF-8");
                sha = MessageDigest.getInstance("SHA-256");
                key = sha.digest(key);
                key = Arrays.copyOf(key, 16);
                secretKey = new SecretKeySpec(key, "AES");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        public static byte[] encrypt(byte[] strToEncrypt, SecretKey secret) {
            try {
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, secret,new IvParameterSpec(new byte[16]));
                return cipher.doFinal(strToEncrypt);
            } catch (Exception e) {
                System.out.println("Error while encrypting: " + e.toString());
            }
            return null;
        }
        public static byte[] decrypt(byte[] strToDecrypt, SecretKey secret)
        {
            try
            {

                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, secret,new IvParameterSpec(new byte[16]));
                return cipher.doFinal(strToDecrypt);
            }
            catch (Exception e)
            {
                System.out.println("Error while decrypting: " + e.toString());
            }
            return null;
        }


}
