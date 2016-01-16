package com.github.alkedr.matchers.reporting.extraction;

import com.github.alkedr.matchers.reporting.ReportingMatcher;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.Objects;

import static com.github.alkedr.matchers.reporting.ReportingMatcher.Value.broken;
import static com.github.alkedr.matchers.reporting.ReportingMatcher.Value.missing;
import static com.github.alkedr.matchers.reporting.ReportingMatcher.Value.present;

public class FieldExtractor implements ExtractingMatcher.Extractor, ReportingMatcher.Key {
    private final Field field;

    public FieldExtractor(Field field) {
        Validate.notNull(field, "field");
        this.field = field;
    }

    @Override
    public ExtractingMatcher.KeyValue extractFrom(Object item) {
        if (item == null) {
            return new ExtractingMatcher.KeyValue(this, missing());
        }
        try {
            return new ExtractingMatcher.KeyValue(this, present(FieldUtils.readField(field, item, true)));
        } catch (IllegalArgumentException | IllegalAccessException e) {
            return new ExtractingMatcher.KeyValue(this, broken(e));  // TODO: rethrow?
        }
    }

    @Override
    public ExtractingMatcher.KeyValue extractFromMissingItem() {
        return new ExtractingMatcher.KeyValue(this, missing());
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldExtractor that = (FieldExtractor) o;
        return Objects.equals(field, that.field);
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
