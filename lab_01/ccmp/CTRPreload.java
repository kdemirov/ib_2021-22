package lab_01.ccmp;

import lab_01.utils.Utils;

public class CTRPreload {
    private byte[] nonce;
    private byte[] counter;

    public CTRPreload(byte[] nonce, byte[] counter) {
        this.nonce = nonce;
        this.counter = counter;
    }

    private void incrementCounter() {
        if (counter[2] < 256) {
            ++counter[2];
        } else if (counter[2] == 256 && counter[1] < 256) {
            ++counter[1];
        } else if (counter[2] == 256 && counter[1] == 256 && counter[0] < 256) {
            ++counter[0];
        }
    }

    public byte[] toBytes(boolean increment) {
        if (increment) {
            incrementCounter();
        }
        byte[] array = new byte[Utils.SIZE_BYTE_ARRAY];
        System.arraycopy(nonce, 0, array, 0, nonce.length);
        System.arraycopy(counter, 0, array, nonce.length, counter.length);
        return array;
    }
}
