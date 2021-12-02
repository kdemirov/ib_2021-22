package lab_01.ccmp;


import lab_01.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public abstract class Frame {
    private FrameHeader header;
    private Integer packageNumber;
    private FrameData data;


    public Frame(FrameHeader header, Integer packageNumber, FrameData data) {
        this.header = header;
        this.packageNumber = packageNumber;
        this.data = data;

    }

    public abstract MIC calculateMic(String secret, byte[] nonce, MIC mic);

    public FrameHeader getHeader() {
        return header;
    }

    public Integer getPackageNumber() {
        return packageNumber;
    }

    public EncryptedFrame encrypt(String secret, byte[] nonce) {
        byte[] counter = new byte[]{0, 0, 0};
        CTRPreload preload = new CTRPreload(nonce, counter);
        List<byte[]> dataBlocks = this.getData().getByteData();
        List<byte[]> chainEncryptedData = new ArrayList<>();
        byte[] chain = AES.encrypt(preload.toBytes(true), secret);
        byte[] xor = Utils.xor(chain, dataBlocks.get(0));
        chainEncryptedData.add(xor);
        for (int i = 1; i < dataBlocks.size(); i++) {
            chain = AES.encrypt(preload.toBytes(true), secret);
            xor = Utils.xor(dataBlocks.get(i), chain);
            chainEncryptedData.add(xor);
        }

        return new EncryptedFrame(this.header, this.packageNumber, new FrameData(chainEncryptedData));
    }

    public ClearTextFrame decrypt(String secret, byte[] nonce) {
        byte[] counter = new byte[]{0, 0, 0};
        CTRPreload preload = new CTRPreload(nonce, counter);
        List<byte[]> dataBlocks = this.getData().getByteData();
        byte[] chain = AES.encrypt(preload.toBytes(true), secret);
        byte[] xor = Utils.xor(dataBlocks.get(0), chain);
        List<byte[]> clearTextFrameBlocks = new ArrayList<>();
        clearTextFrameBlocks.add(xor);
        for (int i = 1; i < dataBlocks.size(); i++) {
            chain = AES.encrypt(preload.toBytes(true), secret);
            clearTextFrameBlocks.add(Utils.xor(dataBlocks.get(i), chain));
        }
        return new ClearTextFrame(getHeader(), getPackageNumber(), new FrameData(clearTextFrameBlocks));
    }

    public FrameData getData() {
        return data;
    }

    public abstract MIC getMic();

    public byte[] getPackageNumberBytes() {
        return new byte[]{packageNumber.byteValue()};
    }

    public static Integer packageNumberOf(String line) {
        String[] parts = line.replace("\"", "").split(",");
        return Integer.parseInt(parts[0]);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Frame frame = (Frame) o;
        return this.getData().getByteData().equals(frame.getData().getByteData()) &&
                this.getHeader().equals(frame.header) &&
                this.packageNumber.equals(frame.getPackageNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }

    public void print() {

        StringBuilder es = new StringBuilder();
        for (byte[] array : this.getData().getByteData()) {
            for (byte b : array) {
                es.append(String.format("%02X ", b));
            }
        }
        byte[] newStringByte = new byte[this.getData().getByteData().size() * 16];
        int i = 0;
        for (byte[] array : this.getData().getByteData()) {
            System.arraycopy(array, 0, newStringByte, i * array.length, array.length);
            i++;
        }
        System.out.println(es.toString());
        System.out.println(new String(newStringByte));


    }

}
