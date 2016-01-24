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
    public void extractFrom(Object item, ResultListener result) {
        if (item == null) {
            result.missing(this);
        } else {
            Field field;
            try {
                field = FieldUtils.getField(item.getClass(), fieldName, true);
            } catch (IllegalArgumentException e) {
                // "field name is matched at multiple places in the inheritance hierarchy"
                result.broken(this, e);
                return;
            }
            if (field == null) {
                // TODO: broken если нет такого поля?
                result.missing(this);
            } else {
                Keys.fieldKey(field).extractFrom(item, result);
            }
        }
    }

    @Override
    public void extractFromMissingItem(ResultListener result) {
        extractFrom(null, result);
    }
}
