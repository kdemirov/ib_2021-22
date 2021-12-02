package lab_02.utils;



import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Utils {
    public static String fromEncryptedBytesToString(byte[] encBytes,SecretKey secret){
        byte [] bytes = AES.decrypt(encBytes,secret);
        return new String(bytes, StandardCharsets.UTF_8);
    }
    public static SecretKey generateRandomSecretKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        return keyGen.generateKey();
    }
    public static byte[] longToByteArray(long i) throws IOException {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bos);
            dos.writeLong(i);
            dos.flush();
            return bos.toByteArray();

    }

    public static long convertByteArrayToLong(byte[] longBytes){
        ByteBuffer byteBuffer = ByteBuffer.allocate(Long.BYTES);
        byteBuffer.put(longBytes);
        byteBuffer.flip();
        return byteBuffer.getLong();
    }
    public static long generateRandomLifetime(){
        return new SecureRandom().nextInt(100);
    }
}
