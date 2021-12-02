package lab_01.ccmp;

import lab_01.utils.Utils;

import java.util.List;
import java.util.Objects;

public class FrameData {
    private String data;
    private List<byte[]> byteData;

    public FrameData(String data) {
        this.data = data;
    }

    public FrameData(List<byte[]> data) {
        this.byteData = data;
    }

    public List<byte[]> getByteData() {
        return byteData;
    }

    public byte[] toBytes() {
        return data.getBytes();
    }

    public static FrameData of(String line) {
        String[] parts = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        return new FrameData(Utils.convertToByteBlockFrameData(new FrameData(parts[parts.length - 1])));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FrameData frameData = (FrameData) o;
        return Objects.equals(data, frameData.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }
}
