package com.github.alkedr.matchers.reporting.sub.value.keys;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;

import static java.lang.reflect.Modifier.isStatic;

class FieldKey implements ExtractableKey {
    private final Field field;

    FieldKey(Field field) {
        Validate.notNull(field, "field");
        this.field = field;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldKey fieldKey = (FieldKey) o;
        return field.equals(fieldKey.field);
    }

    @Override
    public int hashCode() {
        return field.hashCode();
    }

    @Override
    public String asString() {
        return field.getName();
    }

    @Override
    public void run(Object item, SubValuesListener subValuesListener) {
        if (item == null && !isStatic(field.getModifiers())) {
            subValuesListener.absent(this);
        } else {
            try {
                subValuesListener.present(this, FieldUtils.readField(field, item, true));
            } catch (IllegalArgumentException | IllegalAccessException e) {
                // IllegalArgumentException будет если у item неправильный класс
                // IllegalAccessException быть не может, потому что мы пробиваем доступ с пом. readField(*, *, true)
                subValuesListener.broken(this, e);
            }
        }
    }

    @Override
    public void runForAbsentItem(SubValuesListener subValuesListener) {
        run(null, subValuesListener);
    }
}
