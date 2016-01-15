package com.github.alkedr.matchers.reporting.extraction;

import com.github.alkedr.matchers.reporting.ReportingMatcher;
import org.apache.commons.lang3.Validate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

import static com.github.alkedr.matchers.reporting.ReportingMatcher.Value.broken;
import static com.github.alkedr.matchers.reporting.ReportingMatcher.Value.missing;
import static com.github.alkedr.matchers.reporting.ReportingMatcher.Value.present;
import static com.github.alkedr.matchers.reporting.extraction.ExtractedValueNameUtils.createMethodValueName;
import static java.lang.reflect.Modifier.isStatic;

public class MethodExtractingMatcher<T> extends ExtractingMatcher<T> implements ReportingMatcher.Key {
    protected final Method method;
    protected final Object[] arguments;

    protected MethodExtractingMatcher(Method method, Object... arguments) {
        Validate.notNull(method, "method");
        Validate.notNull(arguments, "arguments");
        this.method = method;
        this.arguments = arguments;
    }

    @Override
    protected KeyValue extractFrom(Object item) {
        if (item == null && !isStatic(method.getModifiers())) {
            return new KeyValue(this, missing());
        }
        try {
            method.setAccessible(true);
            return new KeyValue(this, present(method.invoke(item, arguments)));
        } catch (IllegalArgumentException | IllegalAccessException e) {
            return new KeyValue(this, broken(e));  // TODO: rethrow?
        } catch (InvocationTargetException e) {
            return new KeyValue(this, broken(e.getCause()));
        }
    }

    @Override
    protected KeyValue extractFromMissingItem() {
        return new KeyValue(this, missing());
    }

    @Override
    public String asString() {
        return createMethodValueName(method.getName(), arguments);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodExtractingMatcher<?> that = (MethodExtractingMatcher<?>) o;
        return Objects.equals(method, that.method) &&
                Arrays.equals(arguments, that.arguments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, arguments);
    }
}
