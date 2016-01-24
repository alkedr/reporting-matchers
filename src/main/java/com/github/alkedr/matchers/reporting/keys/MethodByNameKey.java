package com.github.alkedr.matchers.reporting.keys;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.Method;
import java.util.Arrays;

import static com.github.alkedr.matchers.reporting.keys.MethodNameUtils.createNameForRegularMethodInvocation;

// TODO: позволять указывать типы аргументов отдельно на случай нуллов и перегрузок
// TODO: сравнивать аргументы так же, как и value в CheckResult'ах?
// TODO: возможность указывать типы аргументов отдельно для случаев, когда какие-то аргументы null и есть перегрузки?
class MethodByNameKey implements ExtractableKey {
    private final String methodName;
//    private final Class<?>[] argumentClasses;
    private final Object[] arguments;

    MethodByNameKey(String methodName, Object... arguments) {
        Validate.notNull(methodName, "methodName");
        Validate.notNull(arguments, "arguments");
        this.methodName = methodName;
        // FIXME: если аргумент null, то и argumentClass будет null
//        this.argumentClasses = ClassUtils.toClass(arguments);
        this.arguments = arguments.clone();
    }

    /*public MethodByNameKey(String methodName, Class<?>[] argumentClasses, Object... arguments) {
        Validate.notNull(methodName, "method");
        Validate.notNull(argumentClasses, "argumentClasses");
        Validate.notNull(arguments, "arguments");
        Validate.isTrue(argumentClasses.length == arguments.length,
                "argumentClasses.length must be equal to arguments.length");
        this.methodName = methodName;
        this.argumentClasses = argumentClasses.clone();
        this.arguments = arguments.clone();
    }*/

    /*public Class<?>[] getArgumentClasses() {
        return argumentClasses.clone();
    }*/

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
    public void extractFrom(Object item, ResultListener result) {
        if (item == null) {
            result.missing(this);
        } else {
            // TODO: брать метод, который выше всех в иерархии классов чтобы правильно объединять?
            Method method = MethodUtils.getMatchingAccessibleMethod(
                    item.getClass(),
                    methodName,
                    ClassUtils.toClass(arguments.clone())
            );
            if (method == null) {
                result.broken(this, new NoSuchMethodException(item.getClass().getName() + "." + toString()));
            } else {
                Keys.methodKey(method, arguments.clone()).extractFrom(item, result);
            }
        }
    }

    @Override
    public void extractFromMissingItem(ResultListener result) {
        extractFrom(null, result);
    }
}
