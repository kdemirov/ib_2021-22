package lab_02.server.kdc;

public class KDCSendEncryptedFrameToUser {
    private byte[] sessionKey;
    private byte[] fromId;
    private byte[] lifetime;

    public KDCSendEncryptedFrameToUser(byte[] sessionKey, byte[] fromId, byte[] lifetime) {
        this.sessionKey = sessionKey;
        this.fromId = fromId;
        this.lifetime = lifetime;
    }

    public byte[] getSessionKey() {
        return sessionKey;
    }

    public byte[] getFromId() {
        return fromId;
    }

    public byte[] getLifetime() {
        return lifetime;
    }
}
