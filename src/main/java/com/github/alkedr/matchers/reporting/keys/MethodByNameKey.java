package com.github.alkedr.matchers.reporting.keys;

import org.apache.commons.lang3.Validate;

import java.util.Arrays;

import static com.github.alkedr.matchers.reporting.keys.MethodNameUtils.createNameForRegularMethodInvocation;

// TODO: сравнивать аргументы так же, как и value в CheckResult'ах?
// TODO: возможность указывать типы аргументов отдельно для случаев, когда какие-то аргументы null и есть перегрузки?
public class MethodByNameKey implements Key {
    private final String methodName;
    private final Object[] arguments;

    public MethodByNameKey(String methodName, Object... arguments) {
        Validate.notNull(methodName, "methodName");
        Validate.notNull(arguments, "arguments");
        this.methodName = methodName;
        this.arguments = Arrays.copyOf(arguments, arguments.length);
    }

    public String getMethodName() {
        return methodName;
    }

    public Object[] getArguments() {
        return arguments;
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
