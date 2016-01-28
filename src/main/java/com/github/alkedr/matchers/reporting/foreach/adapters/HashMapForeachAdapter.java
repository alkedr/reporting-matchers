package com.github.alkedr.matchers.reporting.foreach.adapters;

import com.github.alkedr.matchers.reporting.keys.Key;

import java.util.Map;
import java.util.function.BiConsumer;

import static com.github.alkedr.matchers.reporting.keys.Keys.hashMapKey;

class HashMapForeachAdapter implements ForeachAdapter<Map<?, ?>> {
    static final HashMapForeachAdapter INSTANCE = new HashMapForeachAdapter();

    private HashMapForeachAdapter() {
    }

    @Override
    public void run(Map<?, ?> item, BiConsumer<Key, Object> consumer) {
        for (Map.Entry<?, ?> entry : item.entrySet()) {
            consumer.accept(hashMapKey(entry.getKey()), entry.getValue());
        }
    }
}
