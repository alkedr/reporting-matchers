package com.github.alkedr.matchers.reporting.keys;

import org.apache.commons.lang3.Validate;

import java.lang.reflect.Field;

public class FieldKey implements Key {
    private final Field field;

    public FieldKey(Field field) {
        Validate.notNull(field, "field");
        this.field = field;
    }

    public Field getField() {
        return field;
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
