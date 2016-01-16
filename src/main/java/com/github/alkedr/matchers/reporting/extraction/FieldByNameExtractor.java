package com.github.alkedr.matchers.reporting.extraction;

import com.github.alkedr.matchers.reporting.ReportingMatcher;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.Objects;

import static com.github.alkedr.matchers.reporting.ReportingMatcher.Value.missing;

public class FieldByNameExtractor implements ExtractingMatcher.Extractor, ReportingMatcher.Key {
    private final String fieldName;

    public FieldByNameExtractor(String fieldName) {
        Validate.notNull(fieldName, "fieldName");
        this.fieldName = fieldName;
    }

    @Override
    public ExtractingMatcher.KeyValue extractFrom(Object item) {
        if (item == null) {
            return new ExtractingMatcher.KeyValue(this, missing());
        }
        Field field = FieldUtils.getField(item.getClass(), fieldName, true);  // TODO: проверить исключения, null
        return new FieldExtractor(field).extractFrom(item);
    }

    @Override
    public ExtractingMatcher.KeyValue extractFromMissingItem() {
        return new ExtractingMatcher.KeyValue(this, missing());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldByNameExtractor that = (FieldByNameExtractor) o;
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
