package com.github.alkedr.matchers.reporting.keys;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.Method;
import java.util.Arrays;

import static com.github.alkedr.matchers.reporting.keys.Keys.methodKey;
import static com.github.alkedr.matchers.reporting.keys.MethodNameUtils.createNameForRegularMethodInvocation;

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
        return createNameForRegularMethodInvocation(methodName, arguments);
    }

    @Override
    public ExtractionResult extractFrom(Object item) throws MissingException, BrokenException {
        if (item == null) {
            throw new MissingException(this);
        }
        Method method = MethodUtils.getMatchingAccessibleMethod(item.getClass(), methodName, ClassUtils.toClass(arguments));
        if (method == null) {
            throw new BrokenException(this, new NoSuchMethodException(item.getClass().getName() + "." + toString()));
        }
        return methodKey(method, arguments).extractFrom(item);
    }

    @Override
    public ExtractionResult extractFromMissingItem() throws MissingException, BrokenException {
        throw new MissingException(this);
    }
}
