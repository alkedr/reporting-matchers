package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.sub.value.checkers.SubValuesCheckerFactory;
import com.github.alkedr.matchers.reporting.sub.value.extractors.SubValuesExtractor;
import com.github.alkedr.matchers.reporting.sub.value.keys.ExtractableKey;
import org.hamcrest.Matcher;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import static com.github.alkedr.matchers.reporting.sub.value.checkers.SubValueCheckerFactories.compositeSubValuesCheckerFactory;
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

    public static <T> ReportingMatcher<T> absent() {
        //noinspection unchecked
        return (ReportingMatcher<T>) AbsentMatcher.INSTANCE;
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
        return iteratingMatcherBuilder(arrayElementsExtractor());
    }

    public static <T> IteratingMatcherBuilder<T[], T> array(Class<T> elementClass) {
        return array();
    }

    public static <T> ReportingMatcher<T[]> array(SubValuesCheckerFactory subValuesCheckerFactory) {
        return subValuesMatcher(arrayElementsExtractor(), subValuesCheckerFactory);
    }

    public static <T> ReportingMatcher<T[]> array(SubValuesCheckerFactory... subValuesCheckerFactories) {
        return subValuesMatcher(arrayElementsExtractor(), subValuesCheckerFactories);
    }

    public static <T> ReportingMatcher<T[]> array(Iterable<SubValuesCheckerFactory> subValuesCheckerFactories) {
        return subValuesMatcher(arrayElementsExtractor(), subValuesCheckerFactories);
    }


    public static <T> IteratingMatcherBuilder<Iterable<T>, T> iterable() {
        return iteratingMatcherBuilder(iterableElementsExtractor());
    }

    public static <T> IteratingMatcherBuilder<Iterable<T>, T> iterable(Class<T> elementClass) {
        return iterable();
    }

    public static <T> ReportingMatcher<Iterable<T>> iterable(SubValuesCheckerFactory subValuesCheckerFactory) {
        return subValuesMatcher(iterableElementsExtractor(), subValuesCheckerFactory);
    }

    public static <T> ReportingMatcher<Iterable<T>> iterable(SubValuesCheckerFactory... subValuesCheckerFactories) {
        return subValuesMatcher(iterableElementsExtractor(), subValuesCheckerFactories);
    }

    public static <T> ReportingMatcher<Iterable<T>> iterable(Iterable<SubValuesCheckerFactory> subValuesCheckerFactories) {
        return subValuesMatcher(iterableElementsExtractor(), subValuesCheckerFactories);
    }


    public static <T> IteratingMatcherBuilder<Iterator<T>, T> iterator() {
        return iteratingMatcherBuilder(iteratorElementsExtractor());
    }

    public static <T> IteratingMatcherBuilder<Iterator<T>, T> iterator(Class<T> elementClass) {
        return iterator();
    }

    public static <T> ReportingMatcher<Iterator<T>> iterator(SubValuesCheckerFactory subValuesCheckerFactory) {
        return subValuesMatcher(iteratorElementsExtractor(), subValuesCheckerFactory);
    }

    public static <T> ReportingMatcher<Iterator<T>> iterator(SubValuesCheckerFactory... subValuesCheckerFactories) {
        return subValuesMatcher(iteratorElementsExtractor(), subValuesCheckerFactories);
    }

    public static <T> ReportingMatcher<Iterator<T>> iterator(Iterable<SubValuesCheckerFactory> subValuesCheckerFactories) {
        return subValuesMatcher(iteratorElementsExtractor(), subValuesCheckerFactories);
    }


    public static <K, V> IteratingMatcherBuilder<Map<K, V>, Map.Entry<K, V>> hashMap() {
        return iteratingMatcherBuilder(hashMapEntriesExtractor());
    }

    public static <K, V> IteratingMatcherBuilder<Map<K, V>, Map.Entry<K, V>> hashMap(Class<K> keyClass, Class<V> valueClass) {
        return hashMap();
    }

    public static <K, V> ReportingMatcher<Map<K, V>> hashMap(SubValuesCheckerFactory subValuesCheckerFactory) {
        return subValuesMatcher(hashMapEntriesExtractor(), subValuesCheckerFactory);
    }

    public static <K, V> ReportingMatcher<Map<K, V>> hashMap(SubValuesCheckerFactory... subValuesCheckerFactories) {
        return subValuesMatcher(hashMapEntriesExtractor(), subValuesCheckerFactories);
    }

    public static <K, V> ReportingMatcher<Map<K, V>> hashMap(Iterable<SubValuesCheckerFactory> subValuesCheckerFactories) {
        return subValuesMatcher(hashMapEntriesExtractor(), subValuesCheckerFactories);
    }


    public static <T> ReportingMatcher<T> objectWithFields() {
        return iteratingMatcherBuilder(objectFieldsExtractor());
    }

    public static <T> ReportingMatcher<T> objectWithFields(SubValuesCheckerFactory subValuesCheckerFactory) {
        return subValuesMatcher(objectFieldsExtractor(), subValuesCheckerFactory);
    }

    public static <T> ReportingMatcher<T> objectWithFields(SubValuesCheckerFactory... subValuesCheckerFactories) {
        return subValuesMatcher(objectFieldsExtractor(), subValuesCheckerFactories);
    }

    public static <T> ReportingMatcher<T> objectWithFields(Iterable<SubValuesCheckerFactory> subValuesCheckerFactories) {
        return subValuesMatcher(objectFieldsExtractor(), subValuesCheckerFactories);
    }


    public static <T> ReportingMatcher<T> objectWithGetters() {
        return iteratingMatcherBuilder(objectGettersExtractor());
    }

    public static <T> ReportingMatcher<T> objectWithGetters(SubValuesCheckerFactory subValuesCheckerFactory) {
        return subValuesMatcher(objectGettersExtractor(), subValuesCheckerFactory);
    }

    public static <T> ReportingMatcher<T> objectWithGetters(SubValuesCheckerFactory... subValuesCheckerFactories) {
        return subValuesMatcher(objectGettersExtractor(), subValuesCheckerFactories);
    }

    public static <T> ReportingMatcher<T> objectWithGetters(Iterable<SubValuesCheckerFactory> subValuesCheckerFactories) {
        return subValuesMatcher(objectGettersExtractor(), subValuesCheckerFactories);
    }


    public static <T, U> IteratingMatcherBuilder<T, U> iteratingMatcherBuilder(SubValuesExtractor<? super T> subValuesExtractor) {
        return new IteratingMatcherBuilder<>(subValuesExtractor);
    }

    public static <T> ReportingMatcher<T> subValuesMatcher(SubValuesExtractor<? super T> subValuesExtractor,
                                                           SubValuesCheckerFactory subValuesCheckerFactory) {
        return new SubValuesMatcher<>(subValuesExtractor, subValuesCheckerFactory);
    }

    public static <T> ReportingMatcher<T> subValuesMatcher(SubValuesExtractor<? super T> subValuesExtractor,
                                                           SubValuesCheckerFactory... subValuesCheckerFactories) {
        return subValuesMatcher(subValuesExtractor, compositeSubValuesCheckerFactory(subValuesCheckerFactories));
    }

    public static <T> ReportingMatcher<T> subValuesMatcher(SubValuesExtractor<? super T> subValuesExtractor,
                                                           Iterable<SubValuesCheckerFactory> subValuesCheckerFactories) {
        return subValuesMatcher(subValuesExtractor, compositeSubValuesCheckerFactory(subValuesCheckerFactories));
    }
}
