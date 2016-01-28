package com.github.alkedr.matchers.reporting.sub.value.keys;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.Method;
import java.util.Arrays;

import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.methodKey;

class MethodByNameKey implements ExtractableKey {
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
        return MethodNameUtils.createNameForRegularMethodInvocation(methodName, arguments);
    }

    @Override
    public void run(Object item, SubValuesListener subValuesListener) {
        if (item == null) {
            subValuesListener.missing(this);
        } else {
            Method method = MethodUtils.getMatchingAccessibleMethod(item.getClass(), methodName, ClassUtils.toClass(arguments));
            if (method == null) {
                subValuesListener.broken(this, new NoSuchMethodException(item.getClass().getName() + "." + toString()));
            } else {
                methodKey(method, arguments).run(item, subValuesListener);
            }
        }
    }

    @Override
    public void runForMissingItem(SubValuesListener subValuesListener) {
        subValuesListener.missing(this);
    }
}
