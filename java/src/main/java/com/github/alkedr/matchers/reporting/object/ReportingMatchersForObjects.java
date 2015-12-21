package com.github.alkedr.matchers.reporting.object;

import com.github.alkedr.matchers.reporting.ExtractingMatcherBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static com.github.alkedr.matchers.reporting.ExtractingMatcherBuilder.extractedValue;

// используй static import
// TODO: убрать <T, U> чтобы клиентам не надо было писать ReportingMatchersForObjects.<T, U>
// TODO: плевать на type-safety при построении матчера, главное чтобы в результате получился ReportingMatcher<T>
public class ReportingMatchersForObjects {

    public static <T, U> ExtractingMatcherBuilder<T, ?> field(Field field) {
        return extractedValue(field.getName(), new ObjectFieldExtractor<>(field));
    }

    // если поле не найдено, то бросает исключение в matches()
    public static <T, U> ExtractingMatcherBuilder<T, ?> field(String fieldName) {
        return extractedValue(fieldName, new ObjectFieldByNameExtractor<>(fieldName));
    }


    // TODO: fields() с матчером/предикатом для имени когда будет инфраструктура для итерирования?
//    public static <T, U> ExtractingMatcherBuilder<T, Iterator<U>> fields(String fieldName) {
//        return createBuilder(fieldName, new FieldByNameExtractor<>(fieldName));
//    }


    public static <T, U> ExtractingMatcherBuilder<T, U> method(Method method, Object... arguments) {
        return extractedValue(method.getName(), new ObjectMethodExtractor<>(method, arguments));
    }

    public static <T, U> ExtractingMatcherBuilder<T, U> method(String methodName, Object... arguments) {
        return extractedValue(methodName, new ObjectMethodByNameExtractor<>(methodName, arguments));
    }

    public static <T, U> ExtractingMatcherBuilder<T, U> method(MethodReference<T> methodReference) {
        MethodInvocation invocation = getMethodInvocation(methodReference);
        return method(invocation.method, invocation.arguments);
    }


    public static <T, U> ExtractingMatcherBuilder<T, U> getter(Method method) {
        return fixPropertyName(method(method));
    }

    public static <T, U> ExtractingMatcherBuilder<T, U> getter(String methodName) {
        return fixPropertyName(method(methodName));
    }

    public static <T, U> ExtractingMatcherBuilder<T, U> getter(MethodReference<T> methodReference) {
        return fixPropertyName(method(methodReference));
    }


    public interface MethodReference<T> {
        T get();
    }


    private static <T, U> ExtractingMatcherBuilder<T, U> fixPropertyName(ExtractingMatcherBuilder<T, U> builder) {
        return builder.displayedAs(getterNameToPropertyName(builder.getName()));
    }

    private static String getterNameToPropertyName(String name) {
        // TODO: x(), getX(), isX() -> x
        return null;
    }


    private static <T> MethodInvocation getMethodInvocation(MethodReference<T> methodReference) {
        return null;
    }

    private static class MethodInvocation {
        private final Method method;
        private final Object[] arguments;

        private MethodInvocation(Method method, Object... arguments) {
            this.method = method;
            this.arguments = arguments;
        }
    }
}
