package com.github.alkedr.matchers.reporting.foreach.adapters;

import com.github.alkedr.matchers.reporting.keys.Key;

import java.util.function.BiConsumer;

import static java.util.Arrays.asList;

class ArrayForeachAdapter implements ForeachAdapter<Object[]> {
    @Override
    public void run(Object[] item, BiConsumer<Key, Object> consumer) {
        new IterableForeachAdapter().run(asList(item), consumer);
    }
}
