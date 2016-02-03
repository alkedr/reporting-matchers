package com.github.alkedr.matchers.reporting.sub.value.extractors;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.Method;

import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.methodByNameKey;

class MethodByNameExtractor<T, S> implements SubValuesExtractor<T, S> {
    private final String methodName;
    private final Object[] arguments;

    MethodByNameExtractor(String methodName, Object... arguments) {
        Validate.notNull(methodName, "methodName");
        Validate.notNull(arguments, "arguments");
        this.methodName = methodName;
        this.arguments = arguments.clone();
    }

    @Override
    public void run(T item, SubValuesListener<S> subValuesListener) {
        if (item == null) {
            subValuesListener.absent(methodByNameKey(methodName, arguments));
        } else {
            Method method = MethodUtils.getMatchingAccessibleMethod(item.getClass(), methodName, ClassUtils.toClass(arguments));
            if (method == null) {
                subValuesListener.broken(methodByNameKey(methodName, arguments), new NoSuchMethodException(item.getClass().getName() + "." + toString()));
            } else {
                SubValuesExtractors.<T, S>method(method, arguments).run(item, subValuesListener);
            }
        }
    }

    @Override
    public void runForAbsentItem(SubValuesListener<S> subValuesListener) {
        subValuesListener.absent(methodByNameKey(methodName, arguments));
    }
}
