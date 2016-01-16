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
import static com.github.alkedr.matchers.reporting.extraction.MethodNameUtils.createNameForRegularMethodInvocation;
import static java.lang.reflect.Modifier.isStatic;

public class MethodExtractor implements ExtractingMatcher.Extractor, ReportingMatcher.Key {
    protected final Method method;
    protected final Object[] arguments;

    public MethodExtractor(Method method, Object... arguments) {
        Validate.notNull(method, "method");
        Validate.notNull(arguments, "arguments");
        this.method = method;
        this.arguments = Arrays.copyOf(arguments, arguments.length);
    }

    @Override
    public ExtractingMatcher.KeyValue extractFrom(Object item) {
        if (item == null && !isStatic(method.getModifiers())) {
            return new ExtractingMatcher.KeyValue(this, missing());
        }
        try {
            method.setAccessible(true);
            return new ExtractingMatcher.KeyValue(this, present(method.invoke(item, arguments)));
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

    @Override
    public String asString() {
        return createNameForRegularMethodInvocation(method.getName(), arguments);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodExtractor that = (MethodExtractor) o;
        return Objects.equals(method, that.method) &&
                Arrays.equals(arguments, that.arguments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, arguments);
    }
}
