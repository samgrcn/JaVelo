package ch.epfl.javelo.data;

import java.util.StringJoiner;

public record AttributeSet(long bits) {

    public AttributeSet {
        if (bits > Math.pow(2, Attribute.COUNT)) {
            throw new IllegalArgumentException();
        }
    }

    public static AttributeSet of(Attribute... attributes) {
        long modifiedBits = 0L;
        for (Attribute a : attributes) {
            modifiedBits |= (1L << (a.ordinal()));
        }
        return new AttributeSet(modifiedBits);
    }

    public boolean contains(Attribute attribute) {
        long modifiedBits = this.bits;
        modifiedBits >>>= attribute.ordinal();
        return (modifiedBits & 1L) == 1L;
    }

    public boolean intersects(AttributeSet that) {
        return ((that.bits & this.bits) == 0L);
    }

    @Override
    public String toString() {
        long modifiedBits = this.bits;
        StringJoiner j = new StringJoiner(", ", "{", "}");
        for (int i = 0; i < (Attribute.COUNT + 1); i++) {
            if ((modifiedBits & 1L) == 1L) {
                j.add(Attribute.ALL.get(i).toString());
            }
            modifiedBits >>= 1;
        }
        return j.toString();
    }
}
