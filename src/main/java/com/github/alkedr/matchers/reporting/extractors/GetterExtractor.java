package com.github.alkedr.matchers.reporting.extractors;

import com.github.alkedr.matchers.reporting.keys.Key;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.github.alkedr.matchers.reporting.keys.Keys.getterKey;
import static java.lang.reflect.Modifier.isStatic;

class GetterExtractor implements Extractor {
    private final Method method;
    private final Key key;

    GetterExtractor(Method method) {
        this.method = method;
        this.key = getterKey(method);
    }

    @Override
    public Result extractFrom(Object item) {
        if (item == null && !isStatic(method.getModifiers())) {
            return new Result.Missing(key);
        }
        try {
            method.setAccessible(true);
            return new Result.Present(key, method.invoke(item));
        } catch (IllegalArgumentException | IllegalAccessException e) {
            return new Result.Broken(key, e);  // TODO: rethrow?
        } catch (InvocationTargetException e) {
            return new Result.Broken(key, e.getCause());
        }
    }

    @Override
    public Result extractFromMissingItem() {
        return new Result.Missing(key);
    }
}
