package com.github.alkedr.matchers.reporting.sub.value.keys;

import org.apache.commons.lang3.Validate;

import java.lang.reflect.Field;

class FieldKey implements Key {
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
}
