package lab_01.ccmp;

import lab_01.utils.Utils;


public class EncryptedFrame extends Frame {
    private MIC mic;

    public EncryptedFrame(FrameHeader header, Integer packageNumber, FrameData data) {
        super(header, packageNumber, data);
    }

    @Override
    public MIC calculateMic(String secret, byte[] nonce, MIC mic) {
        byte[] counter = new byte[]{0, 0, 0};
        CTRPreload preload = new CTRPreload(nonce, counter);
        byte[] aes = AES.encrypt(preload.toBytes(false), secret);
        this.mic = new MIC(Utils.xor(Utils.convertTo128ByteBlock(mic.getMic()), aes));
        return this.mic;
    }


    public MIC getMic() {
        return mic;
    }


}
