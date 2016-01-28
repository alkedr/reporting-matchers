package com.github.alkedr.matchers.reporting.foreach.adapters;

import com.github.alkedr.matchers.reporting.keys.Key;

import java.util.function.BiConsumer;

class IterableForeachAdapter implements ForeachAdapter<Iterable<?>> {
    @Override
    public void run(Iterable<?> item, BiConsumer<Key, Object> consumer) {
        new IteratorForeachAdapter().run(item.iterator(), consumer);
    }
}
