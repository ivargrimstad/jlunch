package com.jlunch.fwk.util;

import java.util.HashMap;
import java.util.TreeMap;

/**
 *
 * @author ivar.grimstad
 */
public final class MapUtil {
    
    public static <K, V> HashMap<K, V> newHashMap() {
        return new HashMap<K, V>();
    }
    
    public static <K, V> TreeMap<K, V> newTreeMap() {
        return new TreeMap<K, V>();
    }
}