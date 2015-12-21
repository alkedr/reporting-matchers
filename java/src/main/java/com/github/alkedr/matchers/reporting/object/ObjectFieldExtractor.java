package com.github.alkedr.matchers.reporting.object;

import com.github.alkedr.matchers.reporting.ExtractingMatcher;

import java.lang.reflect.Field;

import static com.github.alkedr.matchers.reporting.ExtractingMatcher.Extractor.ExtractedValue.broken;
import static com.github.alkedr.matchers.reporting.ExtractingMatcher.Extractor.ExtractedValue.normal;

public class ObjectFieldExtractor implements ExtractingMatcher.Extractor {
    private final Field field;

    public ObjectFieldExtractor(Field field) {
        this.field = field;
    }

    @Override
    public ExtractedValue extractFrom(Object item) {
        try {
            // TODO: тестировать что будет если поле имеет неправильный тип
            return normal(field.get(item));
        } catch (IllegalArgumentException e) {
            return broken(e);
        } catch (IllegalAccessException e) {
            return broken(e);
        }
    }
}
