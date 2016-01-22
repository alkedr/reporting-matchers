package com.github.alkedr.matchers.reporting.keys;

import org.apache.commons.lang3.Validate;

import java.util.Arrays;
import java.util.Objects;

class MethodByNameKey implements Key {
    private final String methodName;
    private final Object[] arguments;

    MethodByNameKey(String methodName, Object... arguments) {
        Validate.notNull(methodName, "methodName");
        Validate.notNull(arguments, "arguments");
        this.methodName = methodName;
        this.arguments = Arrays.copyOf(arguments, arguments.length);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MethodByNameKey)) return false;
        MethodByNameKey that = (MethodByNameKey) o;
        return Objects.equals(methodName, that.methodName) &&
                Arrays.equals(arguments, that.arguments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(methodName, arguments);
    }

    @Override
    public String asString() {
        return Keys.createNameForRegularMethodInvocation(methodName, arguments);
    }
}
