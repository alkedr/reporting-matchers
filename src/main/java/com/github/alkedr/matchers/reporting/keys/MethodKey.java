package com.github.alkedr.matchers.reporting.keys;

import org.apache.commons.lang3.Validate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import static com.github.alkedr.matchers.reporting.keys.MethodNameUtils.createNameForRegularMethodInvocation;
import static java.lang.reflect.Modifier.isStatic;

// TODO: сравнивать аргументы так же, как и value в CheckResult'ах?
// TODO: брать метод, который выше всех в иерархии классов чтобы правильно объединять? это нужно делать не здесь.
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
        return createNameForRegularMethodInvocation(method.getName(), arguments);
    }

    @Override
    public ExtractionResult extractFrom(Object item) throws MissingException, BrokenException {
        if (item == null && !isStatic(method.getModifiers())) {
            throw new MissingException(this);
        }
        try {
            method.setAccessible(true);
            return new ExtractionResult(this, method.invoke(item, arguments.clone()));
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new BrokenException(this, e); // TODO: rethrow as is?
        } catch (InvocationTargetException e) {
            throw new BrokenException(this, e.getCause());
        }
    }

    @Override
    public ExtractionResult extractFromMissingItem() throws MissingException, BrokenException {
        return extractFrom(null);
    }
}
