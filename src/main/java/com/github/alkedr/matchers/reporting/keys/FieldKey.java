package com.github.alkedr.matchers.reporting.keys;

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
    public void extractFrom(Object item, ResultListener result) {
        if (item == null && !isStatic(field.getModifiers())) {
            result.missing(this);
        } else {
            try {
                result.present(this, FieldUtils.readField(field, item, true));
            } catch (IllegalArgumentException | IllegalAccessException e) {
                // IllegalArgumentException быть не может, потому что мы уже проверили field на null в конструкторе FieldKey
                // IllegalAccessException быть не может, потому что мы пробиваем доступ с пом. readField(*, *, true)
                result.broken(this, e);
            }
        }
    }

    @Override
    public void extractFromMissingItem(ResultListener result) {
        extractFrom(null, result);
    }
}
