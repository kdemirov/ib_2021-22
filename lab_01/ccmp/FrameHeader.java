package lab_01.ccmp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

public class FrameHeader {
    private Double time;
    private String sourceIpAddress;
    private String destinationIpAddress;
    private String protocol;
    private Integer length;

    public FrameHeader(Double time, String sourceIpAddress, String destinationIpAddress, String protocol, Integer length) {
        this.time = time;
        this.sourceIpAddress = sourceIpAddress;
        this.destinationIpAddress = destinationIpAddress;
        this.protocol = protocol;
        this.length = length;
    }

    private byte[] getTimeBytes() {
        return new byte[]{time.byteValue()};
    }

    public byte[] getSourceIpAddressBytes() {
        return this.sourceIpAddress.getBytes();
    }

    private byte[] getDestinationIpAddressBytes() {
        return this.destinationIpAddress.getBytes();
    }

    private byte[] getProtocolBytes() {
        return this.protocol.getBytes();
    }

    private byte[] getLengthBytes() {
        return new byte[]{this.length.byteValue()};
    }

    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        buffer.write(getTimeBytes());
        buffer.write(getSourceIpAddressBytes());
        buffer.write(getDestinationIpAddressBytes());
        buffer.write(getProtocolBytes());
        buffer.write(getLengthBytes());
        return buffer.toByteArray();
    }

    public static FrameHeader of(String line) {
        String[] parts = line.replace("\"", "").split(",");
        Double time = Double.parseDouble(parts[1]);
        Integer length = Integer.parseInt(parts[5]);
        return new FrameHeader(time, parts[2], parts[3], parts[4], length);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FrameHeader that = (FrameHeader) o;
        return Objects.equals(time, that.time) &&
                Objects.equals(sourceIpAddress, that.sourceIpAddress) &&
                Objects.equals(destinationIpAddress, that.destinationIpAddress) &&
                Objects.equals(protocol, that.protocol) &&
                Objects.equals(length, that.length);
    }

    @Override
    public int hashCode() {
        return Objects.hash(time, sourceIpAddress, destinationIpAddress, protocol, length);
    }
}
