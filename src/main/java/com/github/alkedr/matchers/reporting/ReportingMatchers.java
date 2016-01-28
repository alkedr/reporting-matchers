package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.sub.value.checkers.SubValueCheckers;
import com.github.alkedr.matchers.reporting.sub.value.checkers.SubValuesCheckerFactory;
import com.github.alkedr.matchers.reporting.sub.value.extractors.SubValuesExtractor;
import com.github.alkedr.matchers.reporting.sub.value.keys.ExtractableKey;
import org.hamcrest.Matcher;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import static com.github.alkedr.matchers.reporting.sub.value.checkers.SubValueCheckerFactories.compositeElementCheckerFactory;
import static com.github.alkedr.matchers.reporting.sub.value.extractors.SubValueExtractors.*;
import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.*;
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
        //noinspection unchecked
        return (ReportingMatcher<T>) PresentMatcher.INSTANCE;
    }

    // TODO: absent?
    public static <T> ReportingMatcher<T> missing() {
        //noinspection unchecked
        return (ReportingMatcher<T>) MissingMatcher.INSTANCE;
    }


    public static <T> ExtractingMatcherBuilder<T> value(ExtractableKey extractableKey) {
        return new ExtractingMatcherBuilder<>(extractableKey);
    }

    // TODO: что-то очень универсальное, принимающее лямбду? value("name", <lambda>)  mergeableValue("name", <lambda>)    customKey(), customUnmergeableKey()
    /*public static <T> ExtractingMatcherBuilder<T> value(String name, Function<T, Object> function) {
        return new ExtractingMatcher<>(extractableKey);
    }*/

    /*public static <T> ExtractingMatcherBuilder<T> mergeableValue(String name, Function<T, Object> function) {
        return new ExtractingMatcher<>(extractableKey);
    }*/

    // пробивает доступ к private, protected и package-private полям
    // если проверяемый объект имеет неправильный класс, то BROKEN ?
    public static <T> ExtractingMatcherBuilder<T> field(Field field) {
        return value(fieldKey(field));
    }

    // пробивает доступ к private, protected и package-private полям
    // если поле не найдено, то BROKEN ?
    public static <T> ExtractingMatcherBuilder<T> field(String fieldName) {
        return value(fieldByNameKey(fieldName));
    }

    // TODO: вытаскивание поля из лямбды
    /*public static <T> ExtractingMatcherBuilder<T> field(Function<T, ?> function) {
        return value(fieldByLambdaKey(function));
    }*/


    // пробивает доступ к private, protected и package-private полям
    // если проверяемый объект имеет неправильный класс, то BROKEN
    public static <T> ExtractingMatcherBuilder<T> method(Method method, Object... arguments) {
        return value(methodKey(method, arguments));
    }

    // НЕ пробивает доступ к private, protected и package-private полям, в будущем может измениться TODO: пофиксить это?
    // если метод не найден, то BROKEN
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


    public static <T> IteratingMatcherBuilder<T[], T> array() {
        return new IteratingMatcherBuilder<>(arrayForeachAdapter());
    }

    public static <T> ReportingMatcher<T[]> array(SubValuesCheckerFactory... elementCheckerFactories) {
        return array(asList(elementCheckerFactories));
    }

    public static <T> ReportingMatcher<T[]> array(Iterable<SubValuesCheckerFactory> elementCheckerFactories) {
        return iteratingMatcher(arrayForeachAdapter(), compositeElementCheckerFactory(elementCheckerFactories));
    }


    public static <T> IteratingMatcherBuilder<Iterable<T>, T> iterable() {
        return new IteratingMatcherBuilder<>(iterableForeachAdapter());
    }

    public static <T> ReportingMatcher<Iterable<T>> iterable(SubValuesCheckerFactory... elementCheckerFactories) {
        return iterable(asList(elementCheckerFactories));
    }

    public static <T> ReportingMatcher<Iterable<T>> iterable(Iterable<SubValuesCheckerFactory> elementCheckerFactories) {
        return iteratingMatcher(iterableForeachAdapter(), compositeElementCheckerFactory(elementCheckerFactories));
    }


    public static <T> IteratingMatcherBuilder<Iterator<T>, T> iterator() {
        return new IteratingMatcherBuilder<>(iteratorForeachAdapter());
    }

    public static <T> ReportingMatcher<Iterator<T>> iterator(SubValuesCheckerFactory... elementCheckerFactories) {
        return iterator(asList(elementCheckerFactories));
    }

    public static <T> ReportingMatcher<Iterator<T>> iterator(Iterable<SubValuesCheckerFactory> elementCheckerFactories) {
        return iteratingMatcher(iteratorForeachAdapter(), compositeElementCheckerFactory(elementCheckerFactories));
    }


    public static <K, V> IteratingMatcherBuilder<Map<K, V>, Map.Entry<K, V>> hashMap() {
        return new IteratingMatcherBuilder<>(hashMapForeachAdapter());
    }

    public static <K, V> ReportingMatcher<Map<K, V>> hashMap(SubValuesCheckerFactory... elementCheckerFactories) {
        return hashMap(asList(elementCheckerFactories));
    }

    public static <K, V> ReportingMatcher<Map<K, V>> hashMap(Iterable<SubValuesCheckerFactory> elementCheckerFactories) {
        return iteratingMatcher(hashMapForeachAdapter(), compositeElementCheckerFactory(elementCheckerFactories));
    }


    public static <T> ReportingMatcher<T> iteratingMatcher(SubValuesExtractor<? super T> foreachAdapter,
                                                           SubValuesCheckerFactory elementCheckerFactory) {
        return new SubValuesMatcher<>(foreachAdapter, elementCheckerFactory);
    }



    public static <T> ReportingMatcher<T> uncheckedFields() {
        return iteratingMatcher(fieldsForeachAdepter(), SubValueCheckers::noOpElementChecker);
    }

    public static <T> ReportingMatcher<T> uncheckedGetters() {
        return iteratingMatcher(gettersForeachAdepter(), SubValueCheckers::noOpElementChecker);
    }

    public static <T> ReportingMatcher<T[]> uncheckedArrayElements() {
        return ReportingMatchers.<T>array().extraElementsAreAllowed();
    }

    public static <T> ReportingMatcher<Iterable<T>> uncheckedIterableElements() {
        return ReportingMatchers.<T>iterable().extraElementsAreAllowed();
    }

    public static <T> ReportingMatcher<Iterator<T>> uncheckedIteratorElements() {
        return ReportingMatchers.<T>iterator().extraElementsAreAllowed();
    }

    public static <K, V> ReportingMatcher<Map<K, V>> uncheckedMapEntries() {
        return ReportingMatchers.<K, V>hashMap().extraElementsAreAllowed();
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
