package com.github.alkedr.matchers.reporting.keys;

import com.github.alkedr.matchers.reporting.ReportingMatcher;
import com.github.alkedr.matchers.reporting.extraction.MethodKind;
import org.apache.commons.lang3.Validate;

import java.util.Arrays;
import java.util.Objects;

public class MethodByNameKey implements ReportingMatcher.Key {
    private final MethodKind methodKind;
    private final String methodName;
    private final Object[] arguments;

    public MethodByNameKey(MethodKind methodKind, String methodName, Object... arguments) {
        Validate.notNull(methodKind, "methodKind");
        Validate.notNull(methodName, "methodName");
        Validate.notNull(arguments, "arguments");
        this.methodKind = methodKind;
        this.methodName = methodName;
        this.arguments = Arrays.copyOf(arguments, arguments.length);
    }

    public MethodKind getMethodKind() {
        return methodKind;
    }

    public String getMethodName() {
        return methodName;
    }

    public Object[] getArguments() {
        return Arrays.copyOf(arguments, arguments.length);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MethodByNameKey)) return false;
        MethodByNameKey that = (MethodByNameKey) o;
        return methodKind == that.methodKind &&
                Objects.equals(methodName, that.methodName) &&
                Arrays.equals(arguments, that.arguments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(methodKind, methodName, arguments);
    }

    @Override
    public String asString() {
        return methodKind.invocationToString(methodName, arguments);
    }
}
