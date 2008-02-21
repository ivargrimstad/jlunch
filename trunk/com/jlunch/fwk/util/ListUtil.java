package com.jlunch.fwk.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeMap;

/**
 *
 * @author griv
 */
public final class ListUtil {
    
    public static <T> ArrayList<T> newArrayList() {
        return new ArrayList<T>();
    }
    
    public static <T> LinkedList<T> newLinkedList() {
        return new LinkedList<T>();
    }

}
