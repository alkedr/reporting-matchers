package com.github.alkedr.matchers.reporting.foreach.adapters;

import com.github.alkedr.matchers.reporting.keys.Key;

import java.util.function.BiConsumer;

import static com.github.alkedr.matchers.reporting.foreach.adapters.ForeachAdapters.iterableForeachAdapter;
import static java.util.Arrays.asList;

class ArrayForeachAdapter implements ForeachAdapter<Object[]> {
    static final ArrayForeachAdapter INSTANCE = new ArrayForeachAdapter();

    private ArrayForeachAdapter() {
    }

    @Override
    public void run(Object[] item, BiConsumer<Key, Object> consumer) {
        iterableForeachAdapter().run(asList(item), consumer);
    }
}
