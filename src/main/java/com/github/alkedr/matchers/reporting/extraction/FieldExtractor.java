package com.github.alkedr.matchers.reporting.extraction;

import com.github.alkedr.matchers.reporting.keys.FieldKey;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;

import static com.github.alkedr.matchers.reporting.ReportingMatcher.Value.broken;
import static com.github.alkedr.matchers.reporting.ReportingMatcher.Value.missing;
import static com.github.alkedr.matchers.reporting.ReportingMatcher.Value.present;
import static java.lang.reflect.Modifier.isStatic;

public class FieldExtractor extends FieldKey implements ExtractingMatcher.Extractor {
    public FieldExtractor(Field field) {
        super(field);
    }

    @Override
    public ExtractingMatcher.KeyValue extractFrom(Object item) {
        if (item == null && !isStatic(getField().getModifiers())) {
            return new ExtractingMatcher.KeyValue(this, missing());
        }
        try {
            return new ExtractingMatcher.KeyValue(this, present(FieldUtils.readField(getField(), item, true)));
        } catch (IllegalArgumentException | IllegalAccessException e) {
            return new ExtractingMatcher.KeyValue(this, broken(e));  // TODO: rethrow?
        }
    }

    @Override
    public ExtractingMatcher.KeyValue extractFromMissingItem() {
        return new ExtractingMatcher.KeyValue(this, missing());
    }
}
