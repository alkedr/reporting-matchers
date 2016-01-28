package com.github.alkedr.matchers.reporting.sub.value.extractors;

import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;

import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.fieldKey;

class FieldsForeachAdapter implements SubValuesExtractor<Object> {
    static final FieldsForeachAdapter INSTANCE = new FieldsForeachAdapter();

    private FieldsForeachAdapter() {
    }

    @Override
    public void run(Object item, SubValuesListener subValuesListener) {
        // TODO: check null
        for (Field field : FieldUtils.getAllFieldsList(item.getClass())) {
            fieldKey(field).run(item, subValuesListener);
        }
    }

    @Override
    public void runForMissingItem(SubValuesListener subValuesListener) {
    }
}
