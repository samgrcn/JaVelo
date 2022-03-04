package ch.epfl.javelo.data;

public final class AttributeSetTest {

    static void ofWorks() {
        AttributeSet A = new AttributeSet((long)Math.pow(2, Attribute.COUNT));

    }

    public static void main(String[] args) {
        ofWorks();
    }

}
