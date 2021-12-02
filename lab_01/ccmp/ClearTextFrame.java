package lab_01.ccmp;

import lab_01.utils.Utils;

import java.util.List;

public class ClearTextFrame extends Frame {
    private MIC mic;

    public ClearTextFrame(FrameHeader header, Integer packageNumber, FrameData data) {
        super(header, packageNumber, data);
    }

    @Override
    public MIC calculateMic(String secret, byte[] nonce, MIC micTemp) {
        nonce = Utils.convertTo128ByteBlock(nonce);
        byte[] encryptNonce = AES.encrypt(nonce, secret);
        List<byte[]> headerBlocks = Utils.convertToByteBlocksFrameHeader(this);
        List<byte[]> dataBlocks = this.getData().getByteData();
        byte[] mic = new byte[Utils.SIZE_BYTE_ARRAY / 2];
        byte[] xor = Utils.xor(headerBlocks.get(0), encryptNonce);
        byte[] chain = AES.encrypt(xor, secret);
        for (int i = 1; i < headerBlocks.size(); i++) {
            xor = Utils.xor(headerBlocks.get(i), chain);
            chain = AES.encrypt(xor, secret);
        }
        xor = Utils.xor(dataBlocks.get(0), chain);
        chain = AES.encrypt(xor, secret);
        for (byte[] dataBlock : dataBlocks) {
            xor = Utils.xor(dataBlock, chain);
            chain = AES.encrypt(xor, secret);
        }
        System.arraycopy(chain, 0, mic, 0, Utils.SIZE_BYTE_ARRAY / 2);
        this.mic = new MIC(mic);
        return this.mic;
    }


    public static ClearTextFrame of(String line) {
        FrameHeader header = FrameHeader.of(line);
        FrameData data = FrameData.of(line);
        Integer pn = Frame.packageNumberOf(line);
        return new ClearTextFrame(header, pn, data);
    }

    public MIC getMic() {
        return mic;
    }


}
