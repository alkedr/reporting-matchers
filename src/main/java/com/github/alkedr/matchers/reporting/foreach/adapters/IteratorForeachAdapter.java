package com.github.alkedr.matchers.reporting.foreach.adapters;

import com.github.alkedr.matchers.reporting.keys.Key;

import java.util.Iterator;
import java.util.function.BiConsumer;

import static com.github.alkedr.matchers.reporting.keys.Keys.elementKey;

class IteratorForeachAdapter implements ForeachAdapter<Iterator<?>> {
    @Override
    public void run(Iterator<?> item, BiConsumer<Key, Object> consumer) {
        int i = 0;
        while (item.hasNext()) {
            consumer.accept(elementKey(i++), item.next());
        }
    }
}
