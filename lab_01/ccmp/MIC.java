package lab_01.ccmp;

import java.util.Arrays;

public class MIC {
    private byte[] mic;

    public MIC(byte [] mic){
        this.mic=mic;
    }

    public byte[] getMic() {
        return mic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MIC mic1 = (MIC) o;
        return Arrays.equals(mic, mic1.mic);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(mic);
    }


    public void print() {
        StringBuilder es = new StringBuilder();
        for (byte b : this.mic) {
            es.append(String.format("%02X ", b));
        }
        System.out.println(es.toString());
        System.out.println(new String(this.mic));
    }
}
