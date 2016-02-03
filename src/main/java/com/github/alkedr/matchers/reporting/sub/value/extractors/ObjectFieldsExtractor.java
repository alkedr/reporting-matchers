package com.github.alkedr.matchers.reporting.sub.value.extractors;

import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;

import static com.github.alkedr.matchers.reporting.sub.value.extractors.SubValuesExtractors.fieldExtractor;

class ObjectFieldsExtractor<T> implements SubValuesExtractor<T, Object> {
    static final ObjectFieldsExtractor INSTANCE = new ObjectFieldsExtractor<>();

    private ObjectFieldsExtractor() {
    }

    @Override
    public void run(T item, SubValuesListener<Object> subValuesListener) {
        if (item != null) {
            for (Field field : FieldUtils.getAllFieldsList(item.getClass())) {
                fieldExtractor(field).run(item, subValuesListener);
            }
        }
    }

    @Override
    public void runForAbsentItem(SubValuesListener<Object> subValuesListener) {
    }
}
