package com.github.alkedr.matchers.reporting.keys;

import org.apache.commons.lang3.Validate;

import java.lang.reflect.Method;
import java.util.Arrays;

import static com.github.alkedr.matchers.reporting.keys.Keys.createNameForRegularMethodInvocation;

// TODO: сравнивать аргументы так же, как и value в CheckResult'ах?
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
        if (o == null || getClass() != o.getClass()) return false;
        MethodKey methodKey = (MethodKey) o;
        return method.equals(methodKey.method) && Arrays.equals(arguments, methodKey.arguments);
    }

    @Override
    public int hashCode() {
        return 31 * method.hashCode() + Arrays.hashCode(arguments);
    }

    @Override
    public String asString() {
        return createNameForRegularMethodInvocation(method.getName(), arguments);
    }
}
