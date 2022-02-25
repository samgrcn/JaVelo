package ch.epfl.javelo;

public final class Bits {
    private Bits(){};

    public int extractSigned(int value, int start, int length) {
        if (start < 0 || start + length > value) {
            throw new IllegalArgumentException();
        }
        value = value << start;
        return value >> String.valueOf(value).length() - length;
    }

    public int extractUnsigned(int value, int start, int length) {
        if (start < 0 || start + length > value || length == 32) {
            throw new IllegalArgumentException();
        }

    }
}
