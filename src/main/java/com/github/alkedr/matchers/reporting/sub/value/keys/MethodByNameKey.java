package com.github.alkedr.matchers.reporting.sub.value.keys;

import org.apache.commons.lang3.Validate;

import java.util.Arrays;

import static com.github.alkedr.matchers.reporting.sub.value.keys.MethodNameUtils.createNameForRegularMethodInvocation;

class MethodByNameKey implements Key {
    private final String methodName;
    private final Object[] arguments;

    MethodByNameKey(String methodName, Object... arguments) {
        Validate.notNull(methodName, "methodName");
        Validate.notNull(arguments, "arguments");
        this.methodName = methodName;
        this.arguments = arguments.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodByNameKey that = (MethodByNameKey) o;
        return methodName.equals(that.methodName) && Arrays.equals(arguments, that.arguments);
    }

    @Override
    public int hashCode() {
        return 31 * methodName.hashCode() + Arrays.hashCode(arguments);
    }

    @Override
    public String asString() {
        return createNameForRegularMethodInvocation(methodName, arguments);
    }
}
