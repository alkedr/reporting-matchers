package com.github.alkedr.matchers.reporting.keys;

import com.github.alkedr.matchers.reporting.ReportingMatcher;
import com.github.alkedr.matchers.reporting.extraction.MethodKind;
import org.apache.commons.lang3.Validate;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

public class MethodKey implements ReportingMatcher.Key {
    private final MethodKind methodKind;
    private final Method method;
    private final Object[] arguments;

    public MethodKey(MethodKind methodKind, Method method, Object... arguments) {
        Validate.notNull(methodKind, "methodKind");
        Validate.notNull(method, "method");
        Validate.notNull(arguments, "arguments");
        // TODO: брать метод, который выше всех в иерархии классов чтобы правильно объединять?
        this.methodKind = methodKind;
        this.method = method;
        this.arguments = Arrays.copyOf(arguments, arguments.length);
    }

    public MethodKind getMethodKind() {
        return methodKind;
    }

    public Method getMethod() {
        return method;
    }

    public Object[] getArguments() {
        return Arrays.copyOf(arguments, arguments.length);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MethodKey)) return false;
        MethodKey methodKey = (MethodKey) o;
        return methodKind == methodKey.methodKind &&
                Objects.equals(method, methodKey.method) &&
                Arrays.equals(arguments, methodKey.arguments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(methodKind, method, arguments);
    }

    @Override
    public String asString() {
        return methodKind.invocationToString(method.getName(), arguments);
    }
}
