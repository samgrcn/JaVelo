package ch.epfl.javelo.data;

import ch.epfl.javelo.Preconditions;

import java.util.StringJoiner;

/**
 * Allows the representation of a list of attributes by a long bits
 *
 * @author Samuel Garcin (345633)
 */

public record AttributeSet(long bits) {

    /**
     * @throws IllegalArgumentException if there's elements in the bit than in the list of attribute (Attribute)
     */

    public AttributeSet {
        Preconditions.checkArgument((bits < Math.pow(2, Attribute.COUNT)));
    }

    /**
     * Method used to return a bit with 1 for the attributes entered in parameter in the same position as theirs
     * in the list.
     *
     * @param attributes some attributes of the list of all attributes (Attribute)
     * @return a bit with 1 in the position from right to left of the position of the parameter attribute in the
     * list ALL (in Attribute). This bit can contain at most one of each attribute in the list.
     */

    public static AttributeSet of(Attribute... attributes) {
        long modifiedBits = 0L;
        for (Attribute a : attributes) {
            modifiedBits |= (1L << (a.ordinal()));
        }
        return new AttributeSet(modifiedBits);
    }

    /**
     *  Determines whether the set contains the given attribute.
     *
     * @param attribute the given attribute
     * @return true if the attribute is in the set, false otherwise
     */

    public boolean contains(Attribute attribute) {
        long modifiedBits = this.bits;
        modifiedBits >>>= attribute.ordinal();
        return (modifiedBits & 1L) == 1L;
    }

    /**
     * Determines whether a bit has attributes in common with this
     * @param that the bits we want to test
     * @return true if there's at least one attribute in common
     */

    public boolean intersects(AttributeSet that) {
        return ((that.bits & this.bits) != 0L);
    }

    /**
     * Used to show the list of attributes in the current bits
     *
     * @return the list of attributes marked as 1 in the bits
     */

    @Override
    public String toString() {
        long modifiedBits = this.bits;
        StringJoiner j = new StringJoiner(", ", "{", "}");
        for (int i = 0; i < (Attribute.COUNT + 1); i++) {
            if ((modifiedBits & 1L) == 1L) {
                j.add(Attribute.ALL.get(i).keyValue());
            }
            modifiedBits >>= 1;
        }
        return j.toString();
    }
}
