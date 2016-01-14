package com.github.alkedr.matchers.reporting;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.hamcrest.Matcher;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.lang.Character.isUpperCase;
import static java.lang.reflect.Modifier.isStatic;
import static java.lang.reflect.Modifier.isTransient;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.equalTo;

// сравнивает два объекта рекурсивно с пом. рефлексии
// позволяет задавать некоторые правила проверки для отдельных классов и их полей
// (напр. учитывать ли порядок элементов в списках, поля или геттеры и пр.)
// Предполагается, что для каждого набора матчеров будет static метод, строящий такой матчер
public class ComparingReportingMatcherBuilder<T> {
    private final Map<Class<?>, Function<Object, Matcher<?>>> classToMatcherFactory = new HashMap<>();


    public ComparingReportingMatcherBuilder<T> forClass(Class<?> clazz, Function<Object, Matcher<?>> matcherFactory) {
        classToMatcherFactory.put(clazz, matcherFactory);
        return this;
    }


//    public ReportingMatcher<T> with(T expected) {
//        return toReportingMatcher((Matcher<T>) findMatcherFactoryFor(expected.getClass()).apply(expected.getClass()));
//    }


    private Function<Object, Matcher<?>> findMatcherFactoryFor(Class<?> clazz) {
        while (true) {
            if (classToMatcherFactory.containsKey(clazz)) {
                return classToMatcherFactory.get(clazz);
            }
            if (clazz == Object.class) {
                break;
            }
            clazz = clazz.getSuperclass();
        }
        throw new RuntimeException();
    }





    public static class FieldsMatcherBuilder<T> {
        private final Predicate<Field> predicate;

        public FieldsMatcherBuilder(Predicate<Field> predicate) {
            this.predicate = predicate;
        }

        public ReportingMatcher<T> areEqualToFieldsOf(T otherObject) {
            return matchFieldsOf(otherObject, (field, value) -> equalTo(value));
        }

        public ReportingMatcher<T> matchFieldsOf(T otherObject, BiFunction<Field, Object, Matcher<?>> matcherFactory) {
            return ReportingMatchers.sequence(
                    FieldUtils.getAllFieldsList(otherObject.getClass()).stream()
                            .filter(predicate)
                            .map(field -> {
                                field.setAccessible(true);
                                try {
                                    return ReportingMatchers.field(field).is(matcherFactory.apply(field, field.get(otherObject)));
                                } catch (IllegalAccessException e) {
                                    throw new RuntimeException(e);
                                }
                            })
                            .collect(toList())
            );
        }


        public static Predicate<Field> notStaticNotTransientNotSyntheticFieldsPredicate() {
            return field -> !isStatic(field.getModifiers()) && !isTransient(field.getModifiers()) && !field.isSynthetic();
        }
    }

    public static class MethodsWithoutParametersMatcherBuilder<T> {
        private final Predicate<Method> predicate;

        public MethodsWithoutParametersMatcherBuilder(Predicate<Method> predicate) {
            this.predicate = predicate;
        }

        public ReportingMatcher<T> returnValuesThatAreEqualToReturnValuesOfMethodsOf(T otherObject) {
            return returnValuesThatMatchReturnValuesOfMethodsOf(otherObject, (method, value) -> equalTo(value));
        }

        public ReportingMatcher<T> returnValuesThatMatchReturnValuesOfMethodsOf(T otherObject, BiFunction<Method, Object, Matcher<?>> matcherFactory) {
            return ReportingMatchers.sequence(
                    stream(otherObject.getClass().getMethods())
                            .filter(method -> method.getParameterCount() == 0)
                            .filter(predicate)
                            .map(method -> {
                                try {
                                    return ReportingMatchers.getter(method).is(matcherFactory.apply(method, method.invoke(otherObject)));
                                } catch (IllegalAccessException e) {
                                    throw new RuntimeException(e);
                                } catch (InvocationTargetException e) {
                                    throw new RuntimeException(e.getCause());  // TODO: обернуть в кастомное исключение
                                }
                            })
                            .collect(toList())
            );
        }


        public static Predicate<Method> gettersPredicate() {
            // TODO: разбить на методы, убрать дублирование с ExtractedValueNameUtils
            return method ->
                    !method.getName().equals("getClass") &&
                            ((method.getName().length() > 3 && method.getName().startsWith("get") && isUpperCase(method.getName().charAt(3))) ||
                                    (method.getName().length() > 2 && method.getName().startsWith("is") && isUpperCase(method.getName().charAt(2))));
        }
    }
}
