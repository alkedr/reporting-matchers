package com.github.alkedr.matchers.reporting.foreach.adapters;

import com.github.alkedr.matchers.reporting.keys.ExtractableKey;
import com.github.alkedr.matchers.reporting.keys.Key;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.function.BiConsumer;

import static com.github.alkedr.matchers.reporting.keys.Keys.fieldKey;

class FieldsForeachAdapter implements ForeachAdapter<Object> {
    static final FieldsForeachAdapter INSTANCE = new FieldsForeachAdapter();

    private FieldsForeachAdapter() {
    }

    @Override
    public void run(Object item, BiConsumer<Key, Object> consumer) {
        // TODO: check null
        for (Field field : FieldUtils.getAllFieldsList(item.getClass())) {
            ExtractableKey key = fieldKey(field);
            try {
                consumer.accept(key, FieldUtils.readField(field, item, true));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);  // FIXME
            }
        }
    }
}
