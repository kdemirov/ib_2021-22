package lab_02.model;

import lab_02.server.kdc.KDCSendEncryptedFrameFromUser;
import lab_02.utils.AES;
import lab_02.server.kdc.KDCSendEncryptedFrame;
import lab_02.server.kdc.KDCSendEncryptedFrameToUser;
import lab_02.utils.Utils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class User {
    private String id;
    private String name;
    private String surname;
    private String nonce;
    private SecretKey mySecretKey;
    private Map<String,SecretKey> sessionKeys;

    public User(String name,String surname) throws NoSuchAlgorithmException {
        this.id = UUID.randomUUID().toString();
        this.name=name;
        this.surname=surname;
        this.sessionKeys= new HashMap<>();
        this.mySecretKey= Utils.generateRandomSecretKey();

    }

    public SecretKey getMySecretKey() {
        return mySecretKey;
    }

    public String getId() {
        return id;
    }



    public String generateNonce(){
        this.nonce=UUID.randomUUID().toString();
        return nonce;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    public boolean verifyFrameToKDC(KDCSendEncryptedFrameToUser frame,byte[] tsEnc){
        return verifyLifetime(frame.getLifetime())&&
                verifyTimestamp(tsEnc,frame.getLifetime());

    }

    public Map<String, SecretKey> getSessionKeys() {
        return sessionKeys;
    }

    private boolean verifyTimestamp(byte[] tsEnc, byte[] ltEnc){
        byte[] ts = AES.decrypt(tsEnc,mySecretKey);
        long timestamp = Utils.convertByteArrayToLong(ts);
        ZonedDateTime time = ZonedDateTime.ofInstant(Instant.ofEpochSecond(timestamp),
                ZoneId.systemDefault());
        return time.plusNanos(getLifetime(ltEnc)).isBefore(ZonedDateTime.now(ZoneId.systemDefault()));
    }
    private long getLifetime(byte[] lifetimeEnc){
        byte[] lifetimeBytes = AES.decrypt(lifetimeEnc,mySecretKey);
        return Utils.convertByteArrayToLong(lifetimeBytes);
     }
    public boolean verifyFrameFromKDC(KDCSendEncryptedFrame frame){
        return verifyNonce(frame.getFrameFromUser().getNonce())&&
                verifyLifetime(frame.getFrameFromUser().getLifetime());

    }
    public void setSessionKeyTo(KDCSendEncryptedFrameToUser frame){
        byte[] secretBytes = AES.decrypt(frame.getSessionKey(),mySecretKey);
        this.sessionKeys.putIfAbsent(Utils.fromEncryptedBytesToString(frame.getFromId(),mySecretKey),new SecretKeySpec(secretBytes,0,secretBytes.length,"AES"));
    }
    public void setSessionKeyFrom(KDCSendEncryptedFrameFromUser frame){
        byte[] secretBytes = AES.decrypt(frame.getSessionKey(),mySecretKey);
        this.sessionKeys.putIfAbsent(Utils.fromEncryptedBytesToString(frame.getToId(),mySecretKey),new SecretKeySpec(secretBytes,0,secretBytes.length,"AES"));
    }
    private boolean verifyLifetime(byte[] lifetimeEncrypted){
        byte[] lifetimeBytes = AES.decrypt(lifetimeEncrypted,mySecretKey);
        int lifetime = lifetimeBytes[0];
        return lifetime<=100;
    }
    private boolean verifyNonce(byte[] nonceEncrypted){
        byte [] nonceBytes  = AES.decrypt(nonceEncrypted,mySecretKey);
        return new String(nonceBytes).equals(this.nonce);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return name + surname;
    }
}
