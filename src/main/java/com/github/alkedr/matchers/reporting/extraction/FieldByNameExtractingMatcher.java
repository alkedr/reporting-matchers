package com.github.alkedr.matchers.reporting.extraction;

import com.github.alkedr.matchers.reporting.ReportingMatcher;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.Objects;

import static com.github.alkedr.matchers.reporting.ReportingMatcher.Value.missing;

public class FieldByNameExtractingMatcher<T> extends ExtractingMatcher<T> implements ReportingMatcher.Key {
    private final String fieldName;

    public FieldByNameExtractingMatcher(String fieldName) {
        Validate.notNull(fieldName, "fieldName");
        this.fieldName = fieldName;
    }

    @Override
    protected KeyValue extractFrom(Object item) {
        if (item == null) {
            return new KeyValue(this, missing());
        }
        Field field = FieldUtils.getField(item.getClass(), fieldName, true);  // TODO: проверить исключения, null
        return new FieldExtractingMatcher<>(field).extractFrom(item);
    }

    @Override
    protected KeyValue extractFromMissingItem() {
        return new KeyValue(this, missing());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldByNameExtractingMatcher<?> that = (FieldByNameExtractingMatcher<?>) o;
        return Objects.equals(fieldName, that.fieldName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fieldName);
    }

    @Override
    public String asString() {
        return fieldName;
    }
}
