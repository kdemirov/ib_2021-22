package lab_01.utils;

import lab_01.ccmp.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {
    public static int SIZE_BYTE_ARRAY = 16;

    public static byte[] convertTo128ByteBlock(byte[] array) {
        byte[] newArray = new byte[SIZE_BYTE_ARRAY];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        return newArray;
    }

    public static List<byte[]> convertToByteBlocksFrameHeader(Frame frame) {
        List<byte[]> blocksOf128Bytes = new ArrayList<>();
        try {
            int n = frame.getHeader().toBytes().length % SIZE_BYTE_ARRAY;
            int numberOfBlocks = frame.getHeader().toBytes().length / SIZE_BYTE_ARRAY;
            if (n == 0) {
                for (int i = 0; i < numberOfBlocks; i++) {
                    byte[] array = new byte[SIZE_BYTE_ARRAY];
                    System.arraycopy(frame.getHeader().toBytes(), i * SIZE_BYTE_ARRAY, array, 0, SIZE_BYTE_ARRAY);
                    blocksOf128Bytes.add(array);
                }
            } else {
                int i;
                for (i = 0; i < numberOfBlocks; i++) {
                    byte[] array = new byte[SIZE_BYTE_ARRAY];
                    System.arraycopy(frame.getHeader().toBytes(), i * SIZE_BYTE_ARRAY, array, 0, SIZE_BYTE_ARRAY);
                    blocksOf128Bytes.add(array);
                }
                int size = frame.getHeader().toBytes().length - i * SIZE_BYTE_ARRAY;
                byte[] last = new byte[size];
                System.arraycopy(frame.getHeader().toBytes(), i * SIZE_BYTE_ARRAY, last, 0, size);
                last = convertTo128ByteBlock(last);
                blocksOf128Bytes.add(last);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return blocksOf128Bytes;
    }

    public static List<byte[]> convertToByteBlockFrameData(FrameData frame) {
        List<byte[]> blocksOf128Bytes = new ArrayList<>();
        int n = frame.toBytes().length % SIZE_BYTE_ARRAY;
        int numberOfBlocks = frame.toBytes().length / SIZE_BYTE_ARRAY;
        if (n == 0) {
            for (int i = 0; i < numberOfBlocks; i++) {
                byte[] array = new byte[SIZE_BYTE_ARRAY];
                System.arraycopy(frame.toBytes(), i * SIZE_BYTE_ARRAY, array, 0, SIZE_BYTE_ARRAY);
                blocksOf128Bytes.add(array);
            }
        } else {
            int i;
            for (i = 0; i < numberOfBlocks; i++) {
                byte[] array = new byte[SIZE_BYTE_ARRAY];
                System.arraycopy(frame.toBytes(), i * SIZE_BYTE_ARRAY, array, 0, SIZE_BYTE_ARRAY);
                blocksOf128Bytes.add(array);
            }
            int size = frame.toBytes().length - i * SIZE_BYTE_ARRAY;
            byte[] last = new byte[size];
            System.arraycopy(frame.toBytes(), i * SIZE_BYTE_ARRAY, last, 0, size);
            last = convertTo128ByteBlock(last);
            blocksOf128Bytes.add(last);
        }
        return blocksOf128Bytes;
    }

    public static List<Frame> ofFile() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("src/lab_01/utils/packets.csv"));
            return bufferedReader.lines().skip(1)
                    .map(ClearTextFrame::of)
                    .collect(Collectors.toList());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static byte[] xor(byte[] array1, byte[] array2) {
        byte[] newArray = new byte[SIZE_BYTE_ARRAY];
        for (int i = 0; i < SIZE_BYTE_ARRAY; i++) {
            newArray[i] = (byte) (array1[i] ^ array2[i]);
        }
        return newArray;
    }
}
