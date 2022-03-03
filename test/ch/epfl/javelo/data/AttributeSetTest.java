package ch.epfl.javelo.data;

import org.w3c.dom.Attr;

public final class AttributeSetTest {

    static void ofWorks() {
        AttributeSet A = AttributeSet.of(Attribute.HIGHWAY_CYCLEWAY, Attribute.HIGHWAY_CYCLEWAY);
        System.out.println(A.toString());

    }

    public static void main(String[] args) {
        ofWorks();
    }

}
