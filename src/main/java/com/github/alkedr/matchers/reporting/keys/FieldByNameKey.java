package com.github.alkedr.matchers.reporting.keys;

import org.apache.commons.lang3.Validate;

public class FieldByNameKey implements Key {
    private final String fieldName;

    public FieldByNameKey(String fieldName) {
        Validate.notNull(fieldName, "fieldName");
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldByNameKey that = (FieldByNameKey) o;
        return fieldName.equals(that.fieldName);
    }

    @Override
    public int hashCode() {
        return fieldName.hashCode();
    }

    @Override
    public String asString() {
        return fieldName;
    }
}
