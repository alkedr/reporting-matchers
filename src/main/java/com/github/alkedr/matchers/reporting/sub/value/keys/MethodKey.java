package com.github.alkedr.matchers.reporting.sub.value.keys;

import org.apache.commons.lang3.Validate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import static java.lang.reflect.Modifier.isStatic;

class MethodKey implements ExtractableKey {
    private final Method method;
    private final Object[] arguments;

    MethodKey(Method method, Object... arguments) {
        Validate.notNull(method, "method");
        Validate.notNull(arguments, "arguments");
        this.method = method;
        this.arguments = arguments.clone();
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
        return MethodNameUtils.createNameForRegularMethodInvocation(method.getName(), arguments);
    }

    @Override
    public void run(Object item, SubValuesListener subValuesListener) {
        if (item == null && !isStatic(method.getModifiers())) {
            subValuesListener.absent(this);
        } else {
            try {
                method.setAccessible(true);
                subValuesListener.present(this, method.invoke(item, arguments));
            } catch (IllegalArgumentException | IllegalAccessException e) {
                subValuesListener.broken(this, e);
            } catch (InvocationTargetException e) {
                subValuesListener.broken(this, e.getCause());
            }
        }
    }

    @Override
    public void runForAbsentItem(SubValuesListener subValuesListener) {
        run(null, subValuesListener);
    }
}
