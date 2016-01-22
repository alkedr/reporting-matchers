package com.github.alkedr.matchers.reporting.keys;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.Method;
import java.util.Arrays;

import static com.github.alkedr.matchers.reporting.keys.MethodNameUtils.createNameForRegularMethodInvocation;

// TODO: сравнивать аргументы так же, как и value в CheckResult'ах?
class MethodByNameKey implements ExtractableKey {
    private final String methodName;
    private final Object[] arguments;

    MethodByNameKey(String methodName, Object... arguments) {
        Validate.notNull(methodName, "methodName");
        Validate.notNull(arguments, "arguments");
        this.methodName = methodName;
        this.arguments = Arrays.copyOf(arguments, arguments.length);
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
    public ExtractableKey.Result extractFrom(Object item) {
        if (item == null) {
            return new ExtractableKey.Result.Missing(this);
        }
        // TODO: брать метод, который выше всех в иерархии классов чтобы правильно объединять?
        Method method = MethodUtils.getMatchingAccessibleMethod(
                item.getClass(),
                methodName,
                ClassUtils.toClass(arguments)
        );   // TODO: исключения, null
        // TODO: broken если нет такого поля?
        return method == null
                ? new ExtractableKey.Result.Broken(this, new NoSuchMethodException(item.getClass().getName() + "." + toString()))
                : new MethodKey(method, arguments).extractFrom(item);
    }

    @Override
    public ExtractableKey.Result extractFromMissingItem() {
        return new ExtractableKey.Result.Missing(this);
    }
}
