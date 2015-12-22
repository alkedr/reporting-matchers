package com.github.alkedr.matchers.reporting.object;

import com.github.alkedr.matchers.reporting.ExtractingMatcher;

import static com.github.alkedr.matchers.reporting.ExtractingMatcher.Extractor.ExtractedValue.broken;
import static com.github.alkedr.matchers.reporting.ExtractingMatcher.Extractor.ExtractedValue.normal;
import static org.apache.commons.lang3.reflect.FieldUtils.readField;

// пробивает доступ к private, protected и package-private полям
public class ObjectFieldByNameExtractor implements ExtractingMatcher.Extractor {
    private final String fieldName;

    public ObjectFieldByNameExtractor(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public ExtractedValue extractFrom(Object item) {
        try {
            return normal(readField(item, fieldName, true));
        } catch (IllegalAccessException e) {
            return broken(e);
        }
    }
}
