package com.github.alkedr.matchers.reporting.object;

import com.github.alkedr.matchers.reporting.ExtractingMatcher;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.github.alkedr.matchers.reporting.ExtractingMatcher.Extractor.ExtractedValue.broken;
import static com.github.alkedr.matchers.reporting.ExtractingMatcher.Extractor.ExtractedValue.normal;

public class ObjectMethodExtractor<T> implements ExtractingMatcher.Extractor {
    private final Method method;
    private final Object[] arguments;

    public ObjectMethodExtractor(Method method, Object... arguments) {
        this.method = method;
        this.arguments = arguments;
    }

    @Override
    public ExtractedValue extractFrom(Object item) {
        try {
            return normal(method.invoke(item, arguments));
        } catch (IllegalArgumentException e) {
            return broken(e);
        } catch (InvocationTargetException e) {
            return broken(e.getCause());
        } catch (IllegalAccessException e) {
            return broken(e);
        }
    }
}
