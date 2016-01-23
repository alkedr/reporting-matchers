package com.github.alkedr.matchers.reporting.extractors;

import com.github.alkedr.matchers.reporting.keys.MethodKey;

import java.lang.reflect.InvocationTargetException;

import static java.lang.reflect.Modifier.isStatic;

class MethodExtractor implements Extractor {
    private final MethodKey key;

    MethodExtractor(MethodKey key) {
        this.key = key;
    }

    @Override
    public void extractFrom(Object item, ResultListener result) {
        if (item == null && !isStatic(key.getMethod().getModifiers())) {
            result.missing(key);
        } else {
            try {
                key.getMethod().setAccessible(true);
                result.present(key, key.getMethod().invoke(item, key.getArguments()));
            } catch (IllegalArgumentException | IllegalAccessException e) {
                result.broken(key, e);  // TODO: rethrow?
            } catch (InvocationTargetException e) {
                result.broken(key, e.getCause());
            }
        }
    }

    @Override
    public void extractFromMissingItem(ResultListener result) {
        extractFrom(null, result);
    }
}
