package com.monch.network;

import android.support.v4.util.ArrayMap;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by monch on 2017/5/4.
 */

public final class ArrayFactory {

    private static final int DEFAULT_CAPACITY = 4;

    public static <K, V> ArrayMap<K, V> createArrayMap() {
        return new ArrayMap<>(DEFAULT_CAPACITY);
    }

    public static <K, V> ArrayMap<K, V> createArrayMap(int capacity) {
        return new ArrayMap<>(capacity);
    }

    public static <K, V> Map<K, V> createConcurrentHashMap() {
        return new ConcurrentHashMap<>();
    }

}
