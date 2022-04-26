package ch.epfl.javelo;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Implementation of a LinkedHashMap which is used as a memory cache of up to 100 entries.
 *
 * @author Quentin Chappuis (339517)
 */
public class MemoryCacheHashMap<K, V> extends LinkedHashMap<K, V> {

    private static final int MAX_ENTRIES = 100;
    private static final float LOAD_FACTOR = 0.75f;
    private static final boolean ACCESS_ORDER = true;

    public MemoryCacheHashMap() {
        super(MAX_ENTRIES, LOAD_FACTOR, ACCESS_ORDER);
    }

    /**
     * Returns true if this map should remove its eldest entry.
     *
     * @param eldest the least recently inserted entry in the map
     * @return true if the eldest entry should be removed from the map; false if it should be retained.
     */
    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return size() > MAX_ENTRIES;
    }
}
