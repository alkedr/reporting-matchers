package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.element.checkers.ElementChecker;
import com.github.alkedr.matchers.reporting.keys.ExtractableKey;
import org.hamcrest.Matcher;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import static com.github.alkedr.matchers.reporting.element.checkers.IteratorMatcherElementCheckers.compositeElementChecker;
import static com.github.alkedr.matchers.reporting.element.checkers.IteratorMatcherElementCheckers.containsInSpecifiedOrderChecker;
import static com.github.alkedr.matchers.reporting.keys.Keys.*;
import static java.util.Arrays.asList;

// используй static import
public enum ReportingMatchers {
    ;

    public static <T> ReportingMatcher<T> noOp() {
        //noinspection unchecked
        return (ReportingMatcher<T>) NoOpMatcher.INSTANCE;
    }


    // оборачивает переданный ему матчер если он не reporting.
    public static <T> ReportingMatcher<T> toReportingMatcher(Matcher<T> matcher) {
        return matcher instanceof ReportingMatcher ? (ReportingMatcher<T>) matcher : new ReportingMatcherAdapter<>(matcher);
    }

    public static <U> Iterable<ReportingMatcher<? super U>> toReportingMatchers(Iterable<? extends Matcher<? super U>> matchers) {
        // TODO: конвертировать на лету?
        Collection<ReportingMatcher<? super U>> result = new ArrayList<>();
        for (Matcher<? super U> matcher : matchers) {
            result.add(toReportingMatcher(matcher));
        }
        return result;
    }


    @SafeVarargs
    public static <T> ReportingMatcher<T> sequence(ReportingMatcher<? super T>... matchers) {
        return sequence(asList(matchers));
    }

    public static <T> ReportingMatcher<T> sequence(Iterable<? extends ReportingMatcher<? super T>> matchers) {
        return new SequenceMatcher<>(matchers);
    }


    @SafeVarargs
    public static <T> ReportingMatcher<T> merge(ReportingMatcher<? super T>... matchers) {
        return merge(asList(matchers));
    }

    public static <T> ReportingMatcher<T> merge(Iterable<? extends ReportingMatcher<? super T>> matchers) {
        return new MergingMatcher<>(sequence(matchers));
    }


    public static <T> ReportingMatcher<T> present() {
        return new PresentMatcher<>();
    }

    public static <T> ReportingMatcher<T> missing() {
        return new MissingMatcher<>();
    }


    public static <T> ExtractingMatcherBuilder<T> value(ExtractableKey extractableKey) {
        return new ExtractingMatcher<>(extractableKey);
    }

    // TODO: что-то очень универсальное, принимающее лямбду? value("name", <lambda>)  mergeableValue("name", <lambda>)
    /*public static <T> ExtractingMatcherBuilder<T> value(String name, Function<T, Object> function) {
        return new ExtractingMatcher<>(extractableKey);
    }*/

    /*public static <T> ExtractingMatcherBuilder<T> mergeableValue(String name, Function<T, Object> function) {
        return new ExtractingMatcher<>(extractableKey);
    }*/

    // пробивает доступ к private, protected и package-private полям
    // если проверяемый объект имеет неправильный класс, то бросает исключение в matches() ?
    public static <T> ExtractingMatcherBuilder<T> field(Field field) {
        return value(fieldKey(field));
    }

    // пробивает доступ к private, protected и package-private полям
    // если поле не найдено, то бросает исключение в matches() ?
    public static <T> ExtractingMatcherBuilder<T> field(String fieldName) {
        return value(fieldByNameKey(fieldName));
    }

    // TODO: вытаскивание поля из лямбды
    /*public static <T> ExtractingMatcherBuilder<T> field(Function<T, ?> function) {
        return value(fieldByLambdaKey(function));
    }*/


    // НЕ пробивает доступ к private, protected и package-private полям TODO: пофиксить это?
    // если проверяемый объект имеет неправильный класс, то бросает исключение в matches()
    public static <T> ExtractingMatcherBuilder<T> method(Method method, Object... arguments) {
        return value(methodKey(method, arguments));
    }

    // НЕ пробивает доступ к private, protected и package-private полям TODO: пофиксить это?
    // если метод не найден, то бросает исключение в matches()
    public static <T> ExtractingMatcherBuilder<T> method(String methodName, Object... arguments) {
        return value(methodByNameKey(methodName, arguments));
    }

    // TODO: вытаскивание метода из лямбды
    /*public static <T> ExtractingMatcherBuilder<T> method(Function<T, ?> function) {
        return value(methodByLambdaKey(function));
    }*/


    // как method(), только убирает 'get' и 'is'
    public static <T> ExtractingMatcherBuilder<T> getter(Method method) {
        return value(getterKey(method));
    }

    // как method(), только убирает 'get' и 'is'
    public static <T> ExtractingMatcherBuilder<T> getter(String methodName) {
        return value(getterByNameKey(methodName));
    }

    // как method(), только убирает 'get' и 'is'
    // TODO: вытаскивание метода из лямбды
    /*public static <T> ExtractingMatcherBuilder<T> getter(Function<T, ?> function) {
        return value(getterByLambdaKey(function));
    }*/


    public static <T> ExtractingMatcherBuilder<T[]> arrayElement(int index) {
        return value(elementKey(index));
    }

    // вызывает .get(), O(N) для не-RandomAccess
    public static <T> ExtractingMatcherBuilder<List<T>> element(int index) {
        return value(elementKey(index));
    }

    // O(N)
    public static <T> ExtractingMatcherBuilder<Iterable<T>> iterableElement(int index) {
        return value(elementKey(index));
    }


    public static <K, V> ExtractingMatcherBuilder<Map<K, V>> valueForKey(K key) {
        return value(hashMapKey(key));
    }


    public static <T> ReportingMatcher<T[]> array(ElementChecker... elementCheckers) {
        return array(asList(elementCheckers));
    }

    public static <T> ReportingMatcher<T[]> array(Iterable<ElementChecker> elementCheckers) {
        return new ConvertingReportingMatcher<>(
                item -> asList((Object[]) item).iterator(),
                new IteratorMatcher<>(() -> compositeElementChecker(elementCheckers))
        );
    }


    public static <T> ReportingMatcher<Iterable<T>> iterable(ElementChecker... elementCheckers) {
        return iterable(asList(elementCheckers));
    }

    public static <T> ReportingMatcher<Iterable<T>> iterable(Iterable<ElementChecker> elementCheckers) {
        return new ConvertingReportingMatcher<>(
                item -> ((Iterable<T>) item).iterator(),
                new IteratorMatcher<>(() -> compositeElementChecker(elementCheckers))
        );
    }


    public static <T> ReportingMatcher<Iterator<T>> iterator(ElementChecker... elementCheckers) {
        return iterator(asList(elementCheckers));
    }

    public static <T> ReportingMatcher<Iterator<T>> iterator(Iterable<ElementChecker> elementCheckers) {
        return new IteratorMatcher<>(() -> compositeElementChecker(elementCheckers));
    }




    // TODO: поддерживать массивы, итераторы, итераблы ?мапы?  (это всё обёртки для IteratorMatcher, которые преобразовывают item)
    // TODO: тут нужны перегрузки для всех примитивных типов
    @Deprecated
    @SafeVarargs
    public static <T> ReportingMatcher<T[]> arrayWithElements(T... elements) {
        return new ConvertingReportingMatcher<>(
                item -> asList((Object[]) item).iterator(),
                new IteratorMatcher<>(() -> containsInSpecifiedOrderChecker(elements))
        );
    }





    // TODO: рекурсивный матчер, который работает как equalTo
    // TODO: compare().fields().getters().with(expected)
    /*public static <T> ComparingReportingMatcherBuilder<T> compare() {
        return new ComparingReportingMatcherBuilder<>();
    }*/


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


    /*public static <E> ReportingMatcher<List<E>> listWithElements(Collection<E> elements) {
        return listWithElementsMatching(
                elements.stream()
                        .map(CoreMatchers::equalTo)
                        .collect(Collectors.toList())
        );
    }
*/
    // если не использовать uncheckedIsFail() и извлекатель непроверенных элементов, то непроверенные элементы в конце
    // будут проигнорированы, даже в отчёт не попадут, в будущем поведение может измениться
    /*public static <E> ReportingMatcher<List<E>> listWithElementsMatching(Iterable<Matcher<E>> matchers) {
        Collection<ReportingMatcher<List<E>>> elementMatchers = new ArrayList<>();
        int i = 0;
        for (Matcher<E> matcher : matchers) {
            elementMatchers.add(ReportingMatchers.<E>element(i++).is(matcher));
        }
        return sequence(elementMatchers);
    }*/


}
