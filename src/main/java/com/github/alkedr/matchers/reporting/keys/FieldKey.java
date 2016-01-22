package com.github.alkedr.matchers.reporting.keys;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;

import static com.github.alkedr.matchers.reporting.keys.Keys.fieldKey;
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
    public Result extractFrom(Object item) {
        if (item == null && !isStatic(field.getModifiers())) {
            return new ExtractableKey.Result.Missing(fieldKey(field));
        }
        try {
            return new ExtractableKey.Result.Present(fieldKey(field), FieldUtils.readField(field, item, true));
        } catch (IllegalArgumentException | IllegalAccessException e) {
            return new ExtractableKey.Result.Broken(fieldKey(field), e);  // TODO: rethrow?
        }
    }

    @Override
    public Result extractFromMissingItem() {
        return new Result.Missing(fieldKey(field));
    }
}
