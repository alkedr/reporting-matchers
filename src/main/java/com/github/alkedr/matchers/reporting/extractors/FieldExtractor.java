package com.github.alkedr.matchers.reporting.extractors;

import com.github.alkedr.matchers.reporting.keys.Key;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;

import static com.github.alkedr.matchers.reporting.keys.Keys.fieldKey;
import static java.lang.reflect.Modifier.isStatic;

class FieldExtractor implements Extractor {
    private final Field field;
    private final Key key;

    FieldExtractor(Field field) {
        this.field = field;
        this.key = fieldKey(field);
    }

    @Override
    public Result extractFrom(Object item) {
        if (item == null && !isStatic(field.getModifiers())) {
            return new Result.Missing(key);
        }
        try {
            return new Result.Present(key, FieldUtils.readField(field, item, true));
        } catch (IllegalArgumentException | IllegalAccessException e) {
            return new Result.Broken(key, e);  // TODO: rethrow?
        }
    }

    @Override
    public Result extractFromMissingItem() {
        return new Result.Missing(key);
    }
}
