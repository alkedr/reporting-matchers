package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.sub.value.checkers.SubValueCheckers;
import com.github.alkedr.matchers.reporting.sub.value.checkers.SubValuesCheckerFactory;
import com.github.alkedr.matchers.reporting.sub.value.extractors.SubValuesExtractor;
import com.github.alkedr.matchers.reporting.sub.value.extractors.SubValuesExtractors;
import org.hamcrest.Matcher;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import static com.github.alkedr.matchers.reporting.sub.value.checkers.SubValueCheckerFactories.compositeSubValuesCheckerFactory;
import static com.github.alkedr.matchers.reporting.sub.value.checkers.SubValueCheckers.matcherSubValuesChecker;
import static com.github.alkedr.matchers.reporting.sub.value.extractors.SubValuesExtractors.*;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;

// используй static import
public enum ReportingMatchers {
    ;

    // TODO: unchecked() ?
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



    public static <T, S> ReportingMatcher<T> value(SubValuesExtractor<T, S> subValuesExtractor, S value) {
        return value(subValuesExtractor, equalTo(value));
    }

    public static <T, S> ReportingMatcher<T> value(SubValuesExtractor<T, S> subValuesExtractor, Matcher<? super S> matcher) {
        return value(subValuesExtractor, toReportingMatcher(matcher));
    }

    public static <T, S> ReportingMatcher<T> value(SubValuesExtractor<T, S> subValuesExtractor, ReportingMatcher<? super S> reportingMatcher) {
        return subValuesMatcher(subValuesExtractor, () -> matcherSubValuesChecker(reportingMatcher));
    }

    @SafeVarargs   // TODO: требовать наличия хотя бы одного матчера!
    public static <T, S> ReportingMatcher<T> value(SubValuesExtractor<T, S> subValuesExtractor, Matcher<? super S>... matchers) {
        return value(subValuesExtractor, asList(matchers));
    }

    public static <T, S> ReportingMatcher<T> value(SubValuesExtractor<T, S> subValuesExtractor, Iterable<? extends Matcher<? super S>> matchers) {
        return value(subValuesExtractor, merge(toReportingMatchers(matchers)));
    }


    public static <T, S> ReportingMatcher<T> value(String nameForReport, SubValuesExtractor<T, S> subValuesExtractor, S value) {
        return value(renamedExtractor(subValuesExtractor, nameForReport), value);
    }

    public static <T, S> ReportingMatcher<T> value(String nameForReport, SubValuesExtractor<T, S> subValuesExtractor, Matcher<? super S> matcher) {
        return value(renamedExtractor(subValuesExtractor, nameForReport), matcher);
    }

    public static <T, S> ReportingMatcher<T> value(String nameForReport, SubValuesExtractor<T, S> subValuesExtractor, ReportingMatcher<? super S> reportingMatcher) {
        return value(renamedExtractor(subValuesExtractor, nameForReport), reportingMatcher);
    }

    @SafeVarargs
    public static <T, S> ReportingMatcher<T> value(String nameForReport, SubValuesExtractor<T, S> subValuesExtractor, Matcher<? super S>... matchers) {
        return value(renamedExtractor(subValuesExtractor, nameForReport), matchers);
    }

    public static <T, S> ReportingMatcher<T> value(String nameForReport, SubValuesExtractor<T, S> subValuesExtractor, Iterable<? extends Matcher<? super S>> matchers) {
        return value(renamedExtractor(subValuesExtractor, nameForReport), matchers);
    }



    public static <T, S> ReportingMatcher<T> field(Field field, S value) {
        return value(SubValuesExtractors.<T, S>fieldExtractor(field), value);
    }

    public static <T, S> ReportingMatcher<T> field(Field field, Matcher<? super S> matcher) {
        return value(SubValuesExtractors.<T, S>fieldExtractor(field), matcher);
    }

    public static <T, S> ReportingMatcher<T> field(Field field, ReportingMatcher<? super S> reportingMatcher) {
        return value(SubValuesExtractors.<T, S>fieldExtractor(field), reportingMatcher);
    }

    @SafeVarargs
    public static <T, S> ReportingMatcher<T> field(Field field, Matcher<? super S>... matchers) {
        return value(SubValuesExtractors.<T, S>fieldExtractor(field), matchers);
    }

    public static <T, S> ReportingMatcher<T> field(Field field, Iterable<? extends Matcher<? super S>> matchers) {
        return value(SubValuesExtractors.<T, S>fieldExtractor(field), matchers);
    }


    public static <T, S> ReportingMatcher<T> field(String fieldName, S value) {
        return value(SubValuesExtractors.<T, S>fieldByNameExtractor(fieldName), value);
    }

    public static <T, S> ReportingMatcher<T> field(String fieldName, Matcher<? super S> matcher) {
        return value(SubValuesExtractors.<T, S>fieldByNameExtractor(fieldName), matcher);
    }

    public static <T, S> ReportingMatcher<T> field(String fieldName, ReportingMatcher<? super S> reportingMatcher) {
        return value(SubValuesExtractors.<T, S>fieldByNameExtractor(fieldName), reportingMatcher);
    }

    @SafeVarargs
    public static <T, S> ReportingMatcher<T> field(String fieldName, Matcher<? super S>... matchers) {
        return value(SubValuesExtractors.<T, S>fieldByNameExtractor(fieldName), matchers);
    }

    public static <T, S> ReportingMatcher<T> field(String fieldName, Iterable<? extends Matcher<? super S>> matchers) {
        return value(SubValuesExtractors.<T, S>fieldByNameExtractor(fieldName), matchers);
    }


    public static <T, S> ReportingMatcher<T> field(String nameForReport, Field field, S value) {
        return value(nameForReport, SubValuesExtractors.<T, S>fieldExtractor(field), value);
    }

    public static <T, S> ReportingMatcher<T> field(String nameForReport, Field field, Matcher<? super S> matcher) {
        return value(nameForReport, SubValuesExtractors.<T, S>fieldExtractor(field), matcher);
    }

    public static <T, S> ReportingMatcher<T> field(String nameForReport, Field field, ReportingMatcher<? super S> reportingMatcher) {
        return value(nameForReport, SubValuesExtractors.<T, S>fieldExtractor(field), reportingMatcher);
    }

    @SafeVarargs
    public static <T, S> ReportingMatcher<T> field(String nameForReport, Field field, Matcher<? super S>... matchers) {
        return value(nameForReport, SubValuesExtractors.<T, S>fieldExtractor(field), matchers);
    }

    public static <T, S> ReportingMatcher<T> field(String nameForReport, Field field, Iterable<? extends Matcher<? super S>> matchers) {
        return value(nameForReport, SubValuesExtractors.<T, S>fieldExtractor(field), matchers);
    }


    public static <T, S> ReportingMatcher<T> field(String nameForReport, String fieldName, S value) {
        return value(nameForReport, SubValuesExtractors.<T, S>fieldByNameExtractor(fieldName), value);
    }

    public static <T, S> ReportingMatcher<T> field(String nameForReport, String fieldName, Matcher<? super S> matcher) {
        return value(nameForReport, SubValuesExtractors.<T, S>fieldByNameExtractor(fieldName), matcher);
    }

    public static <T, S> ReportingMatcher<T> field(String nameForReport, String fieldName, ReportingMatcher<? super S> reportingMatcher) {
        return value(nameForReport, SubValuesExtractors.<T, S>fieldByNameExtractor(fieldName), reportingMatcher);
    }

    @SafeVarargs
    public static <T, S> ReportingMatcher<T> field(String nameForReport, String fieldName, Matcher<? super S>... matchers) {
        return value(nameForReport, SubValuesExtractors.<T, S>fieldByNameExtractor(fieldName), matchers);
    }

    public static <T, S> ReportingMatcher<T> field(String nameForReport, String fieldName, Iterable<? extends Matcher<? super S>> matchers) {
        return value(nameForReport, SubValuesExtractors.<T, S>fieldByNameExtractor(fieldName), matchers);
    }



    public static <T, S> ReportingMatcher<T> method(MethodInvocation invocation, S value) {
        return value(SubValuesExtractors.<T, S>methodExtractor(invocation.method, invocation.arguments), value);
    }

    public static <T, S> ReportingMatcher<T> method(MethodInvocation invocation, Matcher<? super S> matcher) {
        return value(SubValuesExtractors.<T, S>methodExtractor(invocation.method, invocation.arguments), matcher);
    }

    public static <T, S> ReportingMatcher<T> method(MethodInvocation invocation, ReportingMatcher<? super S> reportingMatcher) {
        return value(SubValuesExtractors.<T, S>methodExtractor(invocation.method, invocation.arguments), reportingMatcher);
    }

    @SafeVarargs
    public static <T, S> ReportingMatcher<T> method(MethodInvocation invocation, Matcher<? super S>... matchers) {
        return value(SubValuesExtractors.<T, S>methodExtractor(invocation.method, invocation.arguments), matchers);
    }

    public static <T, S> ReportingMatcher<T> method(MethodInvocation invocation, Iterable<? extends Matcher<? super S>> matchers) {
        return value(SubValuesExtractors.<T, S>methodExtractor(invocation.method, invocation.arguments), matchers);
    }


    public static <T, S> ReportingMatcher<T> method(MethodByNameInvocation invocation, S value) {
        return value(SubValuesExtractors.<T, S>methodByNameExtractor(invocation.methodName, invocation.arguments), value);
    }

    public static <T, S> ReportingMatcher<T> method(MethodByNameInvocation invocation, Matcher<? super S> matcher) {
        return value(SubValuesExtractors.<T, S>methodByNameExtractor(invocation.methodName, invocation.arguments), matcher);
    }

    public static <T, S> ReportingMatcher<T> method(MethodByNameInvocation invocation, ReportingMatcher<? super S> reportingMatcher) {
        return value(SubValuesExtractors.<T, S>methodByNameExtractor(invocation.methodName, invocation.arguments), reportingMatcher);
    }

    @SafeVarargs
    public static <T, S> ReportingMatcher<T> method(MethodByNameInvocation invocation, Matcher<? super S>... matchers) {
        return value(SubValuesExtractors.<T, S>methodByNameExtractor(invocation.methodName, invocation.arguments), matchers);
    }

    public static <T, S> ReportingMatcher<T> method(MethodByNameInvocation invocation, Iterable<? extends Matcher<? super S>> matchers) {
        return value(SubValuesExtractors.<T, S>methodByNameExtractor(invocation.methodName, invocation.arguments), matchers);
    }


    public static <T, S> ReportingMatcher<T> method(String nameForReport, MethodInvocation invocation, S value) {
        return value(nameForReport, SubValuesExtractors.<T, S>methodExtractor(invocation.method, invocation.arguments), value);
    }

    public static <T, S> ReportingMatcher<T> method(String nameForReport, MethodInvocation invocation, Matcher<? super S> matcher) {
        return value(nameForReport, SubValuesExtractors.<T, S>methodExtractor(invocation.method, invocation.arguments), matcher);
    }

    public static <T, S> ReportingMatcher<T> method(String nameForReport, MethodInvocation invocation, ReportingMatcher<? super S> reportingMatcher) {
        return value(nameForReport, SubValuesExtractors.<T, S>methodExtractor(invocation.method, invocation.arguments), reportingMatcher);
    }

    @SafeVarargs
    public static <T, S> ReportingMatcher<T> method(String nameForReport, MethodInvocation invocation, Matcher<? super S>... matchers) {
        return value(nameForReport, SubValuesExtractors.<T, S>methodExtractor(invocation.method, invocation.arguments), matchers);
    }

    public static <T, S> ReportingMatcher<T> method(String nameForReport, MethodInvocation invocation, Iterable<? extends Matcher<? super S>> matchers) {
        return value(nameForReport, SubValuesExtractors.<T, S>methodExtractor(invocation.method, invocation.arguments), matchers);
    }


    public static <T, S> ReportingMatcher<T> method(String nameForReport, MethodByNameInvocation invocation, S value) {
        return value(nameForReport, SubValuesExtractors.<T, S>methodByNameExtractor(invocation.methodName, invocation.arguments), value);
    }

    public static <T, S> ReportingMatcher<T> method(String nameForReport, MethodByNameInvocation invocation, Matcher<? super S> matcher) {
        return value(nameForReport, SubValuesExtractors.<T, S>methodByNameExtractor(invocation.methodName, invocation.arguments), matcher);
    }

    public static <T, S> ReportingMatcher<T> method(String nameForReport, MethodByNameInvocation invocation, ReportingMatcher<? super S> reportingMatcher) {
        return value(nameForReport, SubValuesExtractors.<T, S>methodByNameExtractor(invocation.methodName, invocation.arguments), reportingMatcher);
    }

    @SafeVarargs
    public static <T, S> ReportingMatcher<T> method(String nameForReport, MethodByNameInvocation invocation, Matcher<? super S>... matchers) {
        return value(nameForReport, SubValuesExtractors.<T, S>methodByNameExtractor(invocation.methodName, invocation.arguments), matchers);
    }

    public static <T, S> ReportingMatcher<T> method(String nameForReport, MethodByNameInvocation invocation, Iterable<? extends Matcher<? super S>> matchers) {
        return value(nameForReport, SubValuesExtractors.<T, S>methodByNameExtractor(invocation.methodName, invocation.arguments), matchers);
    }


    public static MethodInvocation invocation(Method method, Object... arguments) {
        return new MethodInvocation(method, arguments);
    }

    public static MethodByNameInvocation invocation(String methodName, Object... arguments) {
        return new MethodByNameInvocation(methodName, arguments);
    }



    public static <T, S> ReportingMatcher<T> getter(Method method, S value) {
        return value(SubValuesExtractors.<T, S>getterExtractor(method), value);
    }

    public static <T, S> ReportingMatcher<T> getter(Method method, Matcher<? super S> matcher) {
        return value(SubValuesExtractors.<T, S>getterExtractor(method), matcher);
    }

    public static <T, S> ReportingMatcher<T> getter(Method method, ReportingMatcher<? super S> reportingMatcher) {
        return value(SubValuesExtractors.<T, S>getterExtractor(method), reportingMatcher);
    }

    @SafeVarargs
    public static <T, S> ReportingMatcher<T> getter(Method method, Matcher<? super S>... matchers) {
        return value(SubValuesExtractors.<T, S>getterExtractor(method), matchers);
    }

    public static <T, S> ReportingMatcher<T> getter(Method method, Iterable<? extends Matcher<? super S>> matchers) {
        return value(SubValuesExtractors.<T, S>getterExtractor(method), matchers);
    }


    public static <T, S> ReportingMatcher<T> getter(String methodName, S value) {
        return value(SubValuesExtractors.<T, S>getterByNameExtractor(methodName), value);
    }

    public static <T, S> ReportingMatcher<T> getter(String methodName, Matcher<? super S> matcher) {
        return value(SubValuesExtractors.<T, S>getterByNameExtractor(methodName), matcher);
    }

    public static <T, S> ReportingMatcher<T> getter(String methodName, ReportingMatcher<? super S> reportingMatcher) {
        return value(SubValuesExtractors.<T, S>getterByNameExtractor(methodName), reportingMatcher);
    }

    @SafeVarargs
    public static <T, S> ReportingMatcher<T> getter(String methodName, Matcher<? super S>... matchers) {
        return value(SubValuesExtractors.<T, S>getterByNameExtractor(methodName), matchers);
    }

    public static <T, S> ReportingMatcher<T> getter(String methodName, Iterable<? extends Matcher<? super S>> matchers) {
        return value(SubValuesExtractors.<T, S>getterByNameExtractor(methodName), matchers);
    }



    public static <T> ReportingMatcher<T[]> arrayElement(int index, T value) {
        return value(SubValuesExtractors.<T>arrayElementExtractor(index), value);
    }

    public static <T> ReportingMatcher<T[]> arrayElement(int index, Matcher<? super T> matcher) {
        return value(SubValuesExtractors.<T>arrayElementExtractor(index), matcher);
    }

    public static <T> ReportingMatcher<T[]> arrayElement(int index, ReportingMatcher<? super T> reportingMatcher) {
        return value(SubValuesExtractors.<T>arrayElementExtractor(index), reportingMatcher);
    }

    @SafeVarargs
    public static <T> ReportingMatcher<T[]> arrayElement(int index, Matcher<? super T>... matchers) {
        return value(SubValuesExtractors.<T>arrayElementExtractor(index), matchers);
    }

    public static <T> ReportingMatcher<T[]> arrayElement(int index, Iterable<? extends Matcher<? super T>> matchers) {
        return value(SubValuesExtractors.<T>arrayElementExtractor(index), matchers);
    }


    public static <T> ReportingMatcher<Iterable<T>> iterableElement(int index, T value) {
        return value(SubValuesExtractors.<T>iterableElementExtractor(index), value);
    }

    public static <T> ReportingMatcher<Iterable<T>> iterableElement(int index, Matcher<? super T> matcher) {
        return value(SubValuesExtractors.<T>iterableElementExtractor(index), matcher);
    }

    public static <T> ReportingMatcher<Iterable<T>> iterableElement(int index, ReportingMatcher<? super T> reportingMatcher) {
        return value(SubValuesExtractors.<T>iterableElementExtractor(index), reportingMatcher);
    }

    @SafeVarargs
    public static <T> ReportingMatcher<Iterable<T>> iterableElement(int index, Matcher<? super T>... matchers) {
        return value(SubValuesExtractors.<T>iterableElementExtractor(index), matchers);
    }

    public static <T> ReportingMatcher<Iterable<T>> iterableElement(int index, Iterable<? extends Matcher<? super T>> matchers) {
        return value(SubValuesExtractors.<T>iterableElementExtractor(index), matchers);
    }


    public static <T> ReportingMatcher<List<T>> listElement(int index, T value) {
        return value(SubValuesExtractors.<T>listElementExtractor(index), value);
    }

    public static <T> ReportingMatcher<List<T>> listElement(int index, Matcher<? super T> matcher) {
        return value(SubValuesExtractors.<T>listElementExtractor(index), matcher);
    }

    public static <T> ReportingMatcher<List<T>> listElement(int index, ReportingMatcher<? super T> reportingMatcher) {
        return value(SubValuesExtractors.<T>listElementExtractor(index), reportingMatcher);
    }

    @SafeVarargs
    public static <T> ReportingMatcher<List<T>> listElement(int index, Matcher<? super T>... matchers) {
        return value(SubValuesExtractors.<T>listElementExtractor(index), matchers);
    }

    public static <T> ReportingMatcher<List<T>> listElement(int index, Iterable<? extends Matcher<? super T>> matchers) {
        return value(SubValuesExtractors.<T>listElementExtractor(index), matchers);
    }



    public static <K, V> ReportingMatcher<Map<K, V>> entry(K key, V value) {
        return value(SubValuesExtractors.<K, V>hashMapExtractor(key), value);
    }

    public static <K, V> ReportingMatcher<Map<K, V>> entry(K key, Matcher<? super V> matcher) {
        return value(SubValuesExtractors.<K, V>hashMapExtractor(key), matcher);
    }

    public static <K, V> ReportingMatcher<Map<K, V>> entry(K key, ReportingMatcher<? super V> reportingMatcher) {
        return value(SubValuesExtractors.<K, V>hashMapExtractor(key), reportingMatcher);
    }

    @SafeVarargs
    public static <K, V> ReportingMatcher<Map<K, V>> entry(K key, Matcher<? super V>... matchers) {
        return value(SubValuesExtractors.<K, V>hashMapExtractor(key), matchers);
    }

    public static <K, V> ReportingMatcher<Map<K, V>> entry(K key, Iterable<? extends Matcher<? super V>> matchers) {
        return value(SubValuesExtractors.<K, V>hashMapExtractor(key), matchers);
    }



    @SafeVarargs
    public static <T> ReportingMatcher<Iterable<T>> iterable(SubValuesCheckerFactory<T>... subValuesCheckerFactories) {
        return iterable(asList(subValuesCheckerFactories));
    }

    public static <T> ReportingMatcher<Iterable<T>> iterable(Iterable<SubValuesCheckerFactory<T>> subValuesCheckerFactories) {
        return subValuesMatcher(iterableElementsExtractor(), subValuesCheckerFactories);
    }

    @SafeVarargs
    public static <T> ReportingMatcher<Iterator<T>> iterator(SubValuesCheckerFactory<T>... subValuesCheckerFactories) {
        return iterator(asList(subValuesCheckerFactories));
    }

    public static <T> ReportingMatcher<Iterator<T>> iterator(Iterable<SubValuesCheckerFactory<T>> subValuesCheckerFactories) {
        return subValuesMatcher(iteratorElementsExtractor(), subValuesCheckerFactories);
    }

    @SafeVarargs
    public static <T> ReportingMatcher<T[]> array(SubValuesCheckerFactory<T>... subValuesCheckerFactories) {
        return array(asList(subValuesCheckerFactories));
    }

    public static <T> ReportingMatcher<T[]> array(Iterable<SubValuesCheckerFactory<T>> subValuesCheckerFactories) {
        return subValuesMatcher(arrayElementsExtractor(), subValuesCheckerFactories);
    }

    @SafeVarargs
    public static <K, V> ReportingMatcher<Map<K, V>> hashMap(SubValuesCheckerFactory<V>... subValuesCheckerFactories) {
        return hashMap(asList(subValuesCheckerFactories));
    }

    public static <K, V> ReportingMatcher<Map<K, V>> hashMap(Iterable<SubValuesCheckerFactory<V>> subValuesCheckerFactories) {
        return subValuesMatcher(hashMapEntriesExtractor(), subValuesCheckerFactories);
    }


    @SafeVarargs
    public static <T, S> ReportingMatcher<T> subValuesMatcher(SubValuesExtractor<T, S> subValuesExtractor, SubValuesCheckerFactory<S>... subValuesCheckerFactories) {
        return subValuesMatcher(subValuesExtractor, asList(subValuesCheckerFactories));
    }

    public static <T, S> ReportingMatcher<T> subValuesMatcher(SubValuesExtractor<T, S> subValuesExtractor, Iterable<SubValuesCheckerFactory<S>> subValuesCheckerFactories) {
        return new SubValuesMatcher<>(subValuesExtractor, compositeSubValuesCheckerFactory(subValuesCheckerFactories));
    }



    public static <T> ReportingMatcher<T> displayAll(SubValuesExtractor<T, ?> subValuesExtractor) {
        return new SubValuesMatcher<>(subValuesExtractor, SubValueCheckers::noOpSubValuesChecker);
    }



    public static class MethodInvocation {
        final Method method;
        final Object[] arguments;

        MethodInvocation(Method method, Object... arguments) {
            this.method = method;
            this.arguments = arguments;
        }
    }

    public static class MethodByNameInvocation {
        final String methodName;
        final Object[] arguments;

        MethodByNameInvocation(String methodName, Object... arguments) {
            this.methodName = methodName;
            this.arguments = arguments;
        }
    }
}
