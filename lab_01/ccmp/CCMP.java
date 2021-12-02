package lab_01.ccmp;

import java.util.List;


public class CCMP {
    private List<Frame> clearTextFrame;


    public CCMP(List<Frame> clearTextFrame) {
        this.clearTextFrame = clearTextFrame;
    }


    private byte[] calculateNonce(Frame frame) {
        byte[] nonce = new byte[13];
        System.arraycopy(frame.getPackageNumberBytes(), 0, nonce, 0, frame.getPackageNumberBytes().length);
        if (frame.getHeader().getSourceIpAddressBytes().length > 12) {
            System.arraycopy(frame.getHeader().getSourceIpAddressBytes(), 0, nonce, frame.getPackageNumberBytes().length,
                    12);
        }
        return nonce;
    }

    public boolean match(String secret) {
        for (int i = 0; i < clearTextFrame.size(); i++) {
            Frame frame = this.clearTextFrame.get(i);
            Frame frameEncrypted = frame.encrypt(secret, calculateNonce(frame));
            frame.calculateMic(secret, calculateNonce(frame), frame.getMic());
            frameEncrypted.calculateMic(secret, calculateNonce(frame), frame.getMic());
            Frame toTest = frameEncrypted.decrypt(secret, calculateNonce(frame));
            MIC mic1 = frame.calculateMic(secret, calculateNonce(frame), null);
            MIC mic2 = toTest.calculateMic(secret, calculateNonce(toTest), null);
            assert (verify(mic1, mic2));
            System.out.println("Authentication successful");
            frame.print();
            assert (frame.equals(toTest));
            System.out.println("Decryption successful");
            toTest.print();
        }
        return true;

    }

    public boolean verify(MIC mic1, MIC mic2) {
        assert (mic1.equals(mic2));
        return true;
    }


}
