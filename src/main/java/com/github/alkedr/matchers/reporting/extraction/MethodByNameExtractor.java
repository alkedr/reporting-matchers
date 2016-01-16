package com.github.alkedr.matchers.reporting.extraction;

import com.github.alkedr.matchers.reporting.ReportingMatcher;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

import static com.github.alkedr.matchers.reporting.ReportingMatcher.Value.missing;
import static com.github.alkedr.matchers.reporting.extraction.MethodNameUtils.createNameForRegularMethodInvocation;

public class MethodByNameExtractor implements ExtractingMatcher.Extractor, ReportingMatcher.Key {
    protected final String methodName;
    protected final Object[] arguments;

    public MethodByNameExtractor(String methodName, Object... arguments) {
        Validate.notNull(methodName, "methodName");
        Validate.notNull(arguments, "arguments");
        this.methodName = methodName;
        this.arguments = Arrays.copyOf(arguments, arguments.length);
    }

    @Override
    public ExtractingMatcher.KeyValue extractFrom(Object item) {
        if (item == null) {
            return new ExtractingMatcher.KeyValue(this, missing());
        }
        // TODO: исключения, null
        Method method = MethodUtils.getMatchingAccessibleMethod(item.getClass(), methodName, ClassUtils.toClass(arguments));   // TODO: исключения, null
        return new MethodExtractor(method, arguments).extractFrom(item);
    }

    @Override
    public ExtractingMatcher.KeyValue extractFromMissingItem() {
        return new ExtractingMatcher.KeyValue(this, missing());
    }

    @Override
    public String asString() {
        return createNameForRegularMethodInvocation(methodName, arguments);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodByNameExtractor that = (MethodByNameExtractor) o;
        return Objects.equals(methodName, that.methodName) &&
                Arrays.equals(arguments, that.arguments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(methodName, arguments);
    }
}
