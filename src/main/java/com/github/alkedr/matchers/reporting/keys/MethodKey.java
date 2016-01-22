package com.github.alkedr.matchers.reporting.keys;

import org.apache.commons.lang3.Validate;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

class MethodKey implements Key {
    private final Method method;
    private final Object[] arguments;

    MethodKey(Method method, Object... arguments) {
        Validate.notNull(method, "method");
        Validate.notNull(arguments, "arguments");
        // TODO: брать метод, который выше всех в иерархии классов чтобы правильно объединять?
        this.method = method;
        this.arguments = Arrays.copyOf(arguments, arguments.length);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MethodKey)) return false;
        MethodKey methodKey = (MethodKey) o;
        return Objects.equals(method, methodKey.method) &&
                Arrays.equals(arguments, methodKey.arguments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, arguments);
    }

    @Override
    public String asString() {
        return Keys.createNameForRegularMethodInvocation(method.getName(), arguments);
    }
}
