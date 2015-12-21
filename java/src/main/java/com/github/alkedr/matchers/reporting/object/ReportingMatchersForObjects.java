package com.github.alkedr.matchers.reporting.object;

import com.github.alkedr.matchers.reporting.ExtractingMatcherBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static com.github.alkedr.matchers.reporting.ExtractingMatcherBuilder.extractedValue;

// используй static import
// TODO: убрать <T> чтобы клиентам не надо было писать ReportingMatchersForObjects.<T>
// TODO: плевать на type-safety при построении матчера, главное чтобы в результате получился ReportingMatcher<T>
public class ReportingMatchersForObjects {

    public static <T> ExtractingMatcherBuilder<T> field(Field field) {
        return extractedValue(field.getName(), new ObjectFieldExtractor(field));
    }

    // если поле не найдено, то бросает исключение в matches()
    public static <T> ExtractingMatcherBuilder<T> field(String fieldName) {
        return extractedValue(fieldName, new ObjectFieldByNameExtractor(fieldName));
    }


    // TODO: fields() с матчером/предикатом для имени когда будет инфраструктура для итерирования?
//    public static <T> ExtractingMatcherBuilder<T, Iterator<U>> fields(String fieldName) {
//        return createBuilder(fieldName, new FieldByNameExtractor<>(fieldName));
//    }


    public static <T> ExtractingMatcherBuilder<T> method(Method method, Object... arguments) {
        return extractedValue(method.getName(), new ObjectMethodExtractor<>(method, arguments));
    }

    public static <T> ExtractingMatcherBuilder<T> method(String methodName, Object... arguments) {
        return extractedValue(methodName, new ObjectMethodByNameExtractor(methodName, arguments));
    }

    public static <T> ExtractingMatcherBuilder<T> method(MethodReference<T> methodReference) {
        MethodInvocation invocation = getMethodInvocation(methodReference);
        return method(invocation.method, invocation.arguments);
    }


    public static <T> ExtractingMatcherBuilder<T> getter(Method method) {
        return fixPropertyName(method(method));
    }

    public static <T> ExtractingMatcherBuilder<T> getter(String methodName) {
        return fixPropertyName(method(methodName));
    }

    public static <T> ExtractingMatcherBuilder<T> getter(MethodReference<T> methodReference) {
        return fixPropertyName(method(methodReference));
    }


    public interface MethodReference<T> {
        T get();
    }


    private static <T> ExtractingMatcherBuilder<T> fixPropertyName(ExtractingMatcherBuilder<T> builder) {
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
