package com.github.alkedr.matchers.reporting.foreach.adapters;

import com.github.alkedr.matchers.reporting.keys.Key;

import java.util.function.BiConsumer;

public interface ForeachAdapter<T> {
    void run(T item, BiConsumer<Key, Object> consumer);
}
