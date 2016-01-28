package com.github.alkedr.matchers.reporting.foreach.adapters;

import com.github.alkedr.matchers.reporting.keys.ExtractableKey;
import com.github.alkedr.matchers.reporting.keys.Key;

import java.lang.reflect.Method;
import java.util.function.BiConsumer;

import static com.github.alkedr.matchers.reporting.keys.Keys.methodKey;

class GettersForeachAdapter implements ForeachAdapter<Object> {
    static final GettersForeachAdapter INSTANCE = new GettersForeachAdapter();

    private GettersForeachAdapter() {
    }

    @Override
    public void run(Object item, BiConsumer<Key, Object> consumer) {
        // TODO: check null
        for (Method method : item.getClass().getMethods()) {
            if (method.getParameterCount() == 0) {
                ExtractableKey key = methodKey(method);
                try {
                    consumer.accept(key, key.extractFrom(item));
                } catch (ExtractableKey.MissingException | ExtractableKey.BrokenException e) {
                    throw new RuntimeException(e);  // FIXME
                }
            }
        }
    }
}
