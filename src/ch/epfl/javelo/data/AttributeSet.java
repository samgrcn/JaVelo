package ch.epfl.javelo.data;

import java.util.StringJoiner;

public record AttributeSet(long bits) {

    public AttributeSet {
        if (bits > Math.pow(2, Attribute.COUNT)) {
            throw new IllegalArgumentException();
        }
    }

    public static AttributeSet of(Attribute... attributes) {
        long newBits = 0L;
        for (Attribute a : attributes) {
            newBits |= (1L << (a.ordinal() - 1));
        }
        return new AttributeSet(newBits);
    }

    public boolean contains(Attribute attribute) {
        long modifiedBit = bits;
        modifiedBit >>>= attribute.ordinal();
        return (modifiedBit & 1L) == 1L;
    }

    public boolean intersects(AttributeSet that) {
        return ((that.bits & this.bits) == 0L);
    }

    @Override
    public String toString() {
        long tempBits = bits;
        StringJoiner j = new StringJoiner(",", "{", "}");
        for (int i = 0; i < (Attribute.COUNT + 1); i++) {
            if((tempBits & 1L) == 1L) {
                j.add(Integer.toString(i));
            }
            tempBits <<= 1;
        }
        return "AttributeSet{" +
                "bits=" + tempBits +
                '}';
    }
}
