package com.github.alkedr.matchers.reporting.extractors;

import com.github.alkedr.matchers.reporting.keys.FieldByNameKey;
import com.github.alkedr.matchers.reporting.keys.FieldKey;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;

import static com.github.alkedr.matchers.reporting.extractors.Extractors.fieldExtractor;

class FieldByNameExtractor implements Extractor {
    private final FieldByNameKey key;

    FieldByNameExtractor(FieldByNameKey key) {
        this.key = key;
    }

    @Override
    public void extractFrom(Object item, ResultListener result) {
        if (item == null) {
            result.missing(key);
        } else {
            Field field;
            try {
                field = FieldUtils.getField(item.getClass(), key.getFieldName(), true);
            } catch (IllegalArgumentException e) {
                // "field name is matched at multiple places in the inheritance hierarchy"
                result.broken(key, e);
                return;
            }
            if (field == null) {
                // TODO: broken если нет такого поля?
                result.missing(key);
            } else {
                fieldExtractor(new FieldKey(field)).extractFrom(item, result);
            }
        }
    }

    @Override
    public void extractFromMissingItem(ResultListener result) {
        extractFrom(null, result);
    }
}
