package com.github.alkedr.matchers.reporting.extraction;

import com.github.alkedr.matchers.reporting.keys.MethodKey;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.github.alkedr.matchers.reporting.ReportingMatcher.Value.broken;
import static com.github.alkedr.matchers.reporting.ReportingMatcher.Value.missing;
import static com.github.alkedr.matchers.reporting.ReportingMatcher.Value.present;
import static java.lang.reflect.Modifier.isStatic;

// TODO: должны ли объединяться method() и getter() ?
public class MethodExtractor extends MethodKey implements ExtractingMatcher.Extractor {
    public MethodExtractor(Method method, Object... arguments) {
        super(method, arguments);
    }

    @Override
    public ExtractingMatcher.KeyValue extractFrom(Object item) {
        if (item == null && !isStatic(getMethod().getModifiers())) {
            return new ExtractingMatcher.KeyValue(this, missing());
        }
        try {
            getMethod().setAccessible(true);
            return new ExtractingMatcher.KeyValue(this, present(getMethod().invoke(item, getArguments())));
        } catch (IllegalArgumentException | IllegalAccessException e) {
            return new ExtractingMatcher.KeyValue(this, broken(e));  // TODO: rethrow?
        } catch (InvocationTargetException e) {
            return new ExtractingMatcher.KeyValue(this, broken(e.getCause()));
        }
    }

    @Override
    public ExtractingMatcher.KeyValue extractFromMissingItem() {
        return new ExtractingMatcher.KeyValue(this, missing());
    }
}
