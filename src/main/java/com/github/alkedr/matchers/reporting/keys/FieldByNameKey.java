package com.github.alkedr.matchers.reporting.keys;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;

import static com.github.alkedr.matchers.reporting.keys.Keys.fieldKey;

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
    public ExtractionResult extractFrom(Object item) throws MissingException, BrokenException {
        if (item == null) {
            throw new MissingException(this);
        }
        Field field;
        try {
            field = FieldUtils.getField(item.getClass(), fieldName, true);
        } catch (IllegalArgumentException e) {
            // "field name is matched at multiple places in the inheritance hierarchy"
            throw new BrokenException(this, e);
        }
        if (field == null) {
            // TODO: broken если нет такого поля?
            throw new MissingException(this);
        }
        return fieldKey(field).extractFrom(item);
    }

    @Override
    public ExtractionResult extractFromMissingItem() throws MissingException, BrokenException {
        throw new MissingException(this);
    }
}
