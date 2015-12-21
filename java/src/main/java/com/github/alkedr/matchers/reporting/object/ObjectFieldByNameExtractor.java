package com.github.alkedr.matchers.reporting.object;

import com.github.alkedr.matchers.reporting.ExtractingMatcher;
import org.apache.commons.lang3.reflect.FieldUtils;

import static com.github.alkedr.matchers.reporting.ExtractingMatcher.Extractor.ExtractedValue.broken;
import static com.github.alkedr.matchers.reporting.ExtractingMatcher.Extractor.ExtractedValue.normal;

public class ObjectFieldByNameExtractor<T> implements ExtractingMatcher.Extractor<T> {
    private final String fieldName;

    public ObjectFieldByNameExtractor(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public ExtractedValue<T> extractFrom(Object item) {
        try {
            return normal((T) FieldUtils.readField(item, fieldName, true));
        } catch (IllegalAccessException e) {
            return broken(e);
        }
    }
}
