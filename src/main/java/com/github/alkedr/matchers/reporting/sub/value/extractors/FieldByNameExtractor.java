package com.github.alkedr.matchers.reporting.sub.value.extractors;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;

import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.fieldByNameKey;

class FieldByNameExtractor<T, S> implements SubValuesExtractor<T, S> {
    private final String fieldName;

    FieldByNameExtractor(String fieldName) {
        Validate.notNull(fieldName, "fieldName");
        this.fieldName = fieldName;
    }

    @Override
    public void run(T item, SubValuesListener<S> subValuesListener) {
        if (item == null) {
            subValuesListener.absent(fieldByNameKey(fieldName));
        } else {
            Field field;
            try {
                field = FieldUtils.getField(item.getClass(), fieldName, true);
            } catch (IllegalArgumentException e) {
                // "field name is matched at multiple places in the inheritance hierarchy"
                subValuesListener.broken(fieldByNameKey(fieldName), e);
                return;
            }
            if (field == null) {
                // TODO: broken если нет такого поля?
                subValuesListener.absent(fieldByNameKey(fieldName));
            } else {
                SubValuesExtractors.<T, S>fieldExtractor(field).run(item, subValuesListener);
            }
        }
    }

    @Override
    public void runForAbsentItem(SubValuesListener<S> subValuesListener) {
        subValuesListener.absent(fieldByNameKey(fieldName));
    }
}
