package com.github.alkedr.matchers.reporting.keys;

import org.apache.commons.lang3.Validate;

import java.util.Arrays;

import static com.github.alkedr.matchers.reporting.keys.MethodNameUtils.createNameForRegularMethodInvocation;

// TODO: в keys оставить только FieldKey и MethodKey, *ByNameKey перенести в экстракторы
// Это нужно чтобы потенциально позволить произвольные критерии выбора полей и методов, а не только название/аргументы

// TODO: позволять указывать типы аргументов отдельно на случай нуллов и перегрузок
// TODO: сравнивать аргументы так же, как и value в CheckResult'ах?
// TODO: возможность указывать типы аргументов отдельно для случаев, когда какие-то аргументы null и есть перегрузки?
@Deprecated
public class MethodByNameKey implements Key {
    private final String methodName;
//    private final Class<?>[] argumentClasses;
    private final Object[] arguments;

    @Deprecated
    public MethodByNameKey(String methodName, Object... arguments) {
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

    public String getMethodName() {
        return methodName;
    }

    /*public Class<?>[] getArgumentClasses() {
        return argumentClasses.clone();
    }*/

    public Object[] getArguments() {
        return arguments.clone();
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
