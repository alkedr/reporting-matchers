package com.github.alkedr.matchers.reporting.sub.value.extractors;

import org.apache.commons.lang3.Validate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.methodKey;
import static java.lang.reflect.Modifier.isStatic;

class MethodExtractor<T, S> implements SubValuesExtractor<T, S> {
    private final Method method;
    private final Object[] arguments;

    MethodExtractor(Method method, Object... arguments) {
        Validate.notNull(method, "method");
        Validate.notNull(arguments, "arguments");
        this.method = method;
        this.arguments = arguments.clone();
    }

    @Override
    public void run(T item, SubValuesListener<S> subValuesListener) {
        if (item == null && !isStatic(method.getModifiers())) {
            subValuesListener.absent(methodKey(method, arguments));
        } else {
            try {
                method.setAccessible(true);
                subValuesListener.present(methodKey(method, arguments), (S) method.invoke(item, arguments));
            } catch (IllegalArgumentException | IllegalAccessException e) {   // TODO: ClassCastException
                subValuesListener.broken(methodKey(method, arguments), e);
            } catch (InvocationTargetException e) {
                subValuesListener.broken(methodKey(method, arguments), e.getCause());
            }
        }
    }

    @Override
    public void runForAbsentItem(SubValuesListener<S> subValuesListener) {
        run(null, subValuesListener);
    }
}
