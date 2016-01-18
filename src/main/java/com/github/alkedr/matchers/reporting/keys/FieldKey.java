package com.github.alkedr.matchers.reporting.keys;

import com.github.alkedr.matchers.reporting.ReportingMatcher;
import org.apache.commons.lang3.Validate;

import java.lang.reflect.Field;
import java.util.Objects;

public class FieldKey implements ReportingMatcher.Key {
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
        if (!(o instanceof FieldKey)) return false;
        FieldKey fieldKey = (FieldKey) o;
        return Objects.equals(field, fieldKey.field);
    }

    @Override
    public int hashCode() {
        return Objects.hash(field);
    }

    @Override
    public String asString() {
        return field.getName();
    }
}
