package com.github.alkedr.matchers.reporting.extractors;

import com.github.alkedr.matchers.reporting.keys.Key;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;

import static com.github.alkedr.matchers.reporting.keys.Keys.fieldByNameKey;

class FieldByNameExtractor implements Extractor {
    private final String fieldName;
    private final Key key;

    FieldByNameExtractor(String fieldName) {
        this.fieldName = fieldName;
        this.key = fieldByNameKey(fieldName);
    }

    @Override
    public Result extractFrom(Object item) {
        if (item == null) {
            return new Result.Missing(key);
        }
        Field field;
        try {
            field = FieldUtils.getField(item.getClass(), fieldName, true);  // TODO: проверить исключения, null
        } catch (IllegalArgumentException e) {
            return new Result.Broken(key, e);
        }
        // TODO: broken если нет такого поля?
        return field == null
                ? new Result.Missing(key)
                : new FieldExtractor(field).extractFrom(item);
    }

    @Override
    public Result extractFromMissingItem() {
        return new Result.Missing(key);
    }
}
