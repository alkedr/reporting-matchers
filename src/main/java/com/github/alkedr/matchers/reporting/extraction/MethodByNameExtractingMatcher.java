package com.github.alkedr.matchers.reporting.extraction;

import com.github.alkedr.matchers.reporting.ReportingMatcher;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

import static com.github.alkedr.matchers.reporting.ReportingMatcher.Value.missing;

public abstract class MethodByNameExtractingMatcher<T> extends ExtractingMatcher<T> implements ReportingMatcher.Key {
    protected final String methodName;
    protected final Object[] arguments;

    protected MethodByNameExtractingMatcher(String methodName, Object... arguments) {
        Validate.notNull(methodName, "methodName");
        Validate.notNull(arguments, "arguments");
        this.methodName = methodName;
        this.arguments = arguments;
    }

    @Override
    protected KeyValue extractFrom(Object item) {
        if (item == null) {
            return new KeyValue(this, missing());
        }
        Method method = MethodUtils.getMatchingAccessibleMethod(item.getClass(), methodName, ClassUtils.toClass(arguments));   // TODO: исключения, null
        return new MethodExtractingMatcher<>(method, arguments).extractFrom(item);
    }

    @Override
    protected KeyValue extractFromMissingItem() {
        return new KeyValue(this, missing());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodByNameExtractingMatcher<?> that = (MethodByNameExtractingMatcher<?>) o;
        return Objects.equals(methodName, that.methodName) &&
                Arrays.equals(arguments, that.arguments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(methodName, arguments);
    }
}
