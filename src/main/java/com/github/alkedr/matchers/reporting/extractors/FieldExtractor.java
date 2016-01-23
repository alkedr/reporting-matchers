package com.github.alkedr.matchers.reporting.extractors;

import com.github.alkedr.matchers.reporting.keys.FieldKey;
import org.apache.commons.lang3.reflect.FieldUtils;

import static java.lang.reflect.Modifier.isStatic;

class FieldExtractor implements Extractor {
    private final FieldKey key;

    FieldExtractor(FieldKey key) {
        this.key = key;
    }

    @Override
    public void extractFrom(Object item, ResultListener result) {
        if (item == null && !isStatic(key.getField().getModifiers())) {
            result.missing(key);
        } else {
            try {
                result.present(key, FieldUtils.readField(key.getField(), item, true));
            } catch (IllegalArgumentException | IllegalAccessException e) {
                // IllegalArgumentException быть не может, потому что мы уже проверили field на null в конструкторе FieldKey
                // IllegalAccessException быть не может, потому что мы пробиваем доступ с пом. readField(*, *, true)
                result.broken(key, e);
            }
        }
    }

    @Override
    public void extractFromMissingItem(ResultListener result) {
        extractFrom(null, result);
    }
}
