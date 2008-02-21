package com.jlunch.fwk.util;

import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 *
 * @author griv
 */
public final class SetUtil {
    
    public static <T> HashSet<T> newHashSet() {
        return new HashSet<T>();
    }
    
    public static <T> SortedSet<T> newSortedSet() {
        return new TreeSet<T>();
    }
}
