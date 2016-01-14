package com.github.alkedr.matchers.reporting;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.github.alkedr.matchers.reporting.ExtractingMatcherBuilder.extractedValue;
import static com.github.alkedr.matchers.reporting.Extractors.*;
import static java.util.Arrays.asList;

// используй static import
public class ReportingMatchers {

    @SafeVarargs
    public static <T> ReportingMatcher<T> sequence(ReportingMatcher<? super T>... matchers) {
        return sequence(asList(matchers));
    }

    public static <T> ReportingMatcher<T> sequence(Iterable<? extends ReportingMatcher<? super T>> matchers) {
        return new SequenceMatcher<>(matchers);
    }


    // TODO: noOp() ?
    // TODO: что-то очень универсальное, принимающее лямбду? value("name", <lambda>)  mergeableValue("name", <lambda>)




    /*public static <T> ExtractingMatcherBuilder<T> field(Field field, Iterable<Matcher<Object>> matchers) {
        return new ReportingMatcher<T>() {
            @Override
            public Checks<T> getChecks(Object item) {
                try {
                    return checks(normal(field, field.getName(), field.get(item), ""));
                } catch (IllegalAccessException e) {
                    return checks(broken(field, field.getName(), e));
                }
            }

            @Override
            public Checks<T> getChecksForMissingItem() {
                return checks(missing());
            }
        };


        return extractedValue(field.getName(), new FieldExtractor(field));
    }


    static <T> ReportingMatcher.Checks<T> checks(ReportingMatcher.ExtractedValueChecksPair.ExtractedValue checks, Iterable<Matcher<Object>> matchers) {
        return new ReportingMatcher.Checks<T>(
                Collections.<Matcher<T>>emptyList().iterator(),
                singletonList(new ReportingMatcher.ExtractedValueChecksPair(checks, checks(matchers))).iterator()
        );
    }

    static ReportingMatcher.ExtractedValueChecksPair.Checks checks(Iterable<Matcher<Object>> matchers) {
        return new ReportingMatcher.ExtractedValueChecksPair.Checks(ReportingMatcher.PresenceStatus.PRESENT, matchers.iterator());
    }

    static ReportingMatcher.ExtractedValueChecksPair.ExtractedValue normal(Object key, String keyAsString, Object value, String valueAsString) {
        return new ReportingMatcher.ExtractedValueChecksPair.ExtractedValue(key, keyAsString, ReportingMatcher.PresenceStatus.PRESENT, value, null);
    }

    static ReportingMatcher.ExtractedValueChecksPair.ExtractedValue broken(Object key, String keyAsString, Throwable throwable) {
        return new ReportingMatcher.ExtractedValueChecksPair.ExtractedValue(key, keyAsString, null, null, throwable);
    }

    static ReportingMatcher.ExtractedValueChecksPair.ExtractedValue missing(Object key, String keyAsString) {
        return new ReportingMatcher.ExtractedValueChecksPair.ExtractedValue(key, keyAsString, ReportingMatcher.PresenceStatus.MISSING, null, null);
    }

*/


    // пробивает доступ к private, protected и package-private полям
    // если проверяемый объект имеет неправильный класс, то бросает исключение в matches() ?
    public static <T> ExtractingMatcherBuilder<T> field(Field field) {
        /*return new ReportingMatcher<T>() {
            @Override
            public Checks<T> getChecks(Object item) {
                try {
                    Object value = field.get(item);
                    return new Checks<T>(
                            Collections.<Matcher<T>>emptyList().iterator(),
                            singletonList(new ExtractedValueChecksPair(
                                    new ExtractedValueChecksPair.ExtractedValue(
                                            field,
                                            field.getName(),
                                            PresenceStatus.PRESENT,
                                            value,
                                            null
                                    ),
                                    new ExtractedValueChecksPair.Checks(
                                            PresenceStatus.PRESENT,
                                            emptyIterator()    // TODO: матчеры
                                    )
                            )).iterator()
                    );
                } catch (IllegalAccessException e) {
                    return new Checks<T>(
                            Collections.<Matcher<T>>emptyList().iterator(),
                            singletonList(new ExtractedValueChecksPair(
                                    new ExtractedValueChecksPair.ExtractedValue(
                                            field,
                                            field.getName(),
                                            null,
                                            null,
                                            e
                                    ),
                                    new ExtractedValueChecksPair.Checks(
                                            PresenceStatus.PRESENT,
                                            emptyIterator()    // TODO: матчеры
                                    )
                            )).iterator()
                    );
                }
            }

            @Override
            public Checks<T> getChecksForMissingItem() {
                return new Checks<T>(
                        Collections.<Matcher<T>>emptyList().iterator(),
                        singletonList(new ExtractedValueChecksPair(
                                new ExtractedValueChecksPair.ExtractedValue(
                                        field,
                                        field.getName(),
                                        PresenceStatus.MISSING,
                                        null,
                                        null
                                ),
                                new ExtractedValueChecksPair.Checks(
                                        PresenceStatus.PRESENT,
                                        emptyIterator()    // TODO: матчеры
                                )
                        )).iterator()
                );
            }
        };*/


        return extractedValue(new FieldExtractor(field));
    }

    // пробивает доступ к private, protected и package-private полям
    // если поле не найдено, то бросает исключение в matches() ?
    public static <T> ExtractingMatcherBuilder<T> field(String fieldName) {
        return extractedValue(new FieldByNameExtractor(fieldName));
    }


    // НЕ пробивает доступ к private, protected и package-private полям TODO: пофиксить это?
    // если проверяемый объект имеет неправильный класс, то бросает исключение в matches()
    public static <T> ExtractingMatcherBuilder<T> method(Method method, Object... arguments) {
        return extractedValue(new MethodExtractor(method, arguments));
    }

    // НЕ пробивает доступ к private, protected и package-private полям TODO: пофиксить это?
    // если метод не найден, то бросает исключение в matches()
    public static <T> ExtractingMatcherBuilder<T> method(String methodName, Object... arguments) {
        return extractedValue(new MethodByNameExtractor(methodName, arguments));
    }


    // как method(), только убирает 'get' и 'is'
    public static <T> ExtractingMatcherBuilder<T> getter(Method method) {
        return extractedValue(new MethodExtractor(method))
//                .displayedAs(getterNameToPropertyName(method.getName()))
                ;
    }

    // как method(), только убирает 'get' и 'is'
    public static <T> ExtractingMatcherBuilder<T> getter(String methodName) {
        return extractedValue(new MethodByNameExtractor(methodName))
//                .displayedAs(getterNameToPropertyName(methodName))
                ;
    }


    public static <T> ExtractingMatcherBuilder<T[]> arrayElement(int index) {
        return extractedValue(new ArrayElementExtractor(index));
    }

    public static <T> ExtractingMatcherBuilder<List<T>> element(int index) {
        return extractedValue(new ListElementExtractor(index));
    }


    public static <K, V> ExtractingMatcherBuilder<Map<K, V>> valueForKey(K key) {
        return extractedValue(new ValueForKeyExtractor(key));
    }




    // TODO: compare().fields().getters().with(expected)

    public static <T> ComparingReportingMatcherBuilder<T> compare() {
        return new ComparingReportingMatcherBuilder<>();
    }



//    public static <T> ComparingReportingMatcherBuilder.FieldsMatcherBuilder<T> fields() {
//        return fields(notStaticNotTransientNotSyntheticFieldsPredicate());
//    }
//
//    public static <T> ComparingReportingMatcherBuilder.FieldsMatcherBuilder<T> fields(Predicate<Field> predicate) {
//        return new ComparingReportingMatcherBuilder.FieldsMatcherBuilder<>(predicate);
//    }
//
//    public static <T> ComparingReportingMatcherBuilder.MethodsWithoutParametersMatcherBuilder<T> methodsWithoutParameters() {
//        return methodsWithoutParameters(method -> true);
//    }
//
//    public static <T> ComparingReportingMatcherBuilder.MethodsWithoutParametersMatcherBuilder<T> methodsWithoutParameters(Predicate<Method> predicate) {
//        return new ComparingReportingMatcherBuilder.MethodsWithoutParametersMatcherBuilder<>(predicate);
//    }
//
//    public static <T> ComparingReportingMatcherBuilder.MethodsWithoutParametersMatcherBuilder<T> getters() {
//        return getters(method -> true);
//    }
//
//    public static <T> ComparingReportingMatcherBuilder.MethodsWithoutParametersMatcherBuilder<T> getters(Predicate<Method> predicate) {
//        return methodsWithoutParameters(gettersPredicate().and(predicate));
//    }



    // TODO: метод, который принимает мапу и лямбду, которая преобразовывает значение в матчер, то же для массивов и списков?

    public static <E> ReportingMatcher<List<E>> listWithElements(Collection<E> elements) {
        return listWithElementsMatching(
                elements.stream()
                        .map(CoreMatchers::equalTo)
                        .collect(Collectors.toList())
        );
    }

    // TODO: пробовать пропускать элементы чтобы лишний элемент в начале списка не сделал весь список красным?
    // если не использовать uncheckedIsFail() и извлекатель непроверенных элементов, то непроверенные элементы в конце
    // будут проигнорированы, даже в отчёт не попадут, в будущем поведение может измениться
    public static <E> ReportingMatcher<List<E>> listWithElementsMatching(Iterable<Matcher<E>> matchers) {
        Collection<ReportingMatcher<List<E>>> elementMatchers = new ArrayList<>();
        int i = 0;
        for (Matcher<E> matcher : matchers) {
            elementMatchers.add(ReportingMatchers.<E>element(i++).is(matcher));
        }
        return sequence(elementMatchers);
    }

    // TODO: listWithElementsInAnyOrder, listWithElementsMatchingInAnyOrder

    // TODO: рекурсивный матчер, который работает как equalTo
}
