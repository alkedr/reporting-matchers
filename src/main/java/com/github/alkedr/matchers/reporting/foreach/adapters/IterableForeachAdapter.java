package com.github.alkedr.matchers.reporting.foreach.adapters;

import com.github.alkedr.matchers.reporting.keys.Key;

import java.util.function.BiConsumer;

import static com.github.alkedr.matchers.reporting.foreach.adapters.ForeachAdapters.iteratorForeachAdapter;

class IterableForeachAdapter implements ForeachAdapter<Iterable<?>> {
    static final IterableForeachAdapter INSTANCE = new IterableForeachAdapter();

    private IterableForeachAdapter() {
    }

    @Override
    public void run(Iterable<?> item, BiConsumer<Key, Object> consumer) {
        iteratorForeachAdapter().run(item.iterator(), consumer);
    }
}
