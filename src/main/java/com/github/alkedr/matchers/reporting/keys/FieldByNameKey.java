package com.github.alkedr.matchers.reporting.keys;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;

class FieldByNameKey implements ExtractableKey {
    private final String fieldName;

    FieldByNameKey(String fieldName) {
        Validate.notNull(fieldName, "fieldName");
        this.fieldName = fieldName;
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

    @Override
    public Result extractFrom(Object item) {
        if (item == null) {
            return new Result.Missing(this);
        }
        Field field;
        try {
            field = FieldUtils.getField(item.getClass(), fieldName, true);  // TODO: проверить исключения, null
        } catch (IllegalArgumentException e) {
            return new Result.Broken(this, e);
        }
        // TODO: broken если нет такого поля?
        return field == null
                ? new Result.Missing(this)
                : new FieldKey(field).extractFrom(item);
    }

    @Override
    public Result extractFromMissingItem() {
        return new Result.Missing(this);
    }
}
