package lab_02.server.kdc;

public class KDCSendEncryptedFrameFromUser {
    private byte[] sessionKey;
    private byte[] nonce;
    private byte[] lifetime;
    private byte[] toId;

    public KDCSendEncryptedFrameFromUser(byte[] sessionKey, byte[] nonce, byte[] lifetime, byte[] toId) {
        this.sessionKey = sessionKey;
        this.nonce = nonce;
        this.lifetime = lifetime;
        this.toId = toId;
    }

    public byte[] getSessionKey() {
        return sessionKey;
    }

    public byte[] getNonce() {
        return nonce;
    }

    public byte[] getLifetime() {
        return lifetime;
    }

    public byte[] getToId() {
        return toId;
    }
}
