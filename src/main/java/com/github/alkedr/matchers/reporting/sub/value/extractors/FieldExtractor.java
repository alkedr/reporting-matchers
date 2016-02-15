package com.github.alkedr.matchers.reporting.sub.value.extractors;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;

import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.fieldKey;
import static java.lang.reflect.Modifier.isStatic;

class FieldExtractor<T, S> implements SubValuesExtractor<T, S> {
    private final Field field;

    FieldExtractor(Field field) {
        Validate.notNull(field, "field");
        this.field = field;
    }

    @Override
    public void run(T item, SubValuesListener<S> subValuesListener) {
        if (item == null && !isStatic(field.getModifiers())) {
            subValuesListener.absent(fieldKey(field));
        } else {
            try {
                subValuesListener.present(fieldKey(field), (S) FieldUtils.readField(field, item, true));
            } catch (IllegalArgumentException | IllegalAccessException e) {
                // IllegalArgumentException будет если у item неправильный класс
                // IllegalAccessException быть не может, потому что мы пробиваем доступ с пом. readField(*, *, true)
                subValuesListener.broken(fieldKey(field), e);
            }
        }
    }

    @Override
    public void runForAbsentItem(SubValuesListener<S> subValuesListener) {
        run(null, subValuesListener);
    }
}
