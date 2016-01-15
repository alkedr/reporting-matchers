package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.comparison.ComparingReportingMatcherBuilder;
import com.github.alkedr.matchers.reporting.extraction.*;
import com.github.alkedr.matchers.reporting.utility.SequenceMatcher;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.github.alkedr.matchers.reporting.extraction.ExtractingMatcherBuilder.extractedValue;
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


    // пробивает доступ к private, protected и package-private полям
    // если проверяемый объект имеет неправильный класс, то бросает исключение в matches() ?
    public static <T> ExtractingMatcher<T> field(Field field) {
        return new FieldExtractingMatcher<>(field);
    }

    // пробивает доступ к private, protected и package-private полям
    // если поле не найдено, то бросает исключение в matches() ?
    public static <T> ExtractingMatcher<T> field(String fieldName) {
        return new FieldByNameExtractingMatcher<>(fieldName);
    }


    // НЕ пробивает доступ к private, protected и package-private полям TODO: пофиксить это?
    // если проверяемый объект имеет неправильный класс, то бросает исключение в matches()
    public static <T> ExtractingMatcher<T> method(Method method, Object... arguments) {
        return new SimpleMethodExtractingMatcher<>(method, arguments);
    }

    // НЕ пробивает доступ к private, protected и package-private полям TODO: пофиксить это?
    // если метод не найден, то бросает исключение в matches()
    public static <T> ExtractingMatcher<T> method(String methodName, Object... arguments) {
        return new SimpleMethodByNameExtractingMatcher<>(methodName, arguments);
    }


    // как method(), только убирает 'get' и 'is'
    public static <T> ExtractingMatcher<T> getter(Method method) {
        return new GetterMethodExtractingMatcher<>(method);
    }

    // как method(), только убирает 'get' и 'is'
    public static <T> ExtractingMatcher<T> getter(String methodName) {
        return new GetterMethodByNameExtractingMatcher<>(methodName);
    }


    public static <T> ExtractingMatcher<T[]> arrayElement(int index) {
        return extractedValue(new ArrayElementExtractor(index));
    }

    public static <T> ExtractingMatcher<List<T>> element(int index) {
        return extractedValue(new ElementExtractingMatcher(index));
    }


    public static <K, V> ExtractingMatcher<Map<K, V>> valueForKey(K key) {
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
