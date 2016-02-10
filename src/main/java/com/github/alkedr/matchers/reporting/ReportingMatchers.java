package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.sub.value.checkers.SubValuesChecker;
import com.github.alkedr.matchers.reporting.sub.value.extractors.SubValuesExtractor;
import com.github.alkedr.matchers.reporting.sub.value.extractors.SubValuesExtractors;
import org.hamcrest.Matcher;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Supplier;

import static com.github.alkedr.matchers.reporting.sub.value.checkers.SubValueCheckers.compositeSubValuesCheckerSupplier;
import static com.github.alkedr.matchers.reporting.sub.value.checkers.SubValueCheckers.matcherSubValuesChecker;
import static com.github.alkedr.matchers.reporting.sub.value.checkers.SubValueCheckers.noOpSubValuesChecker;
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
        return subValuesMatcher(subValuesExtractor, matcherSubValuesChecker(reportingMatcher));
    }

    @SafeVarargs   // TODO: требовать наличия хотя бы одного матчера!
    public static <T, S> ReportingMatcher<T> value(SubValuesExtractor<T, S> subValuesExtractor, Matcher<? super S>... matchers) {
        return value(subValuesExtractor, asList(matchers));
    }

    public static <T, S> ReportingMatcher<T> value(SubValuesExtractor<T, S> subValuesExtractor, Iterable<? extends Matcher<? super S>> matchers) {
        return value(subValuesExtractor, merge(toReportingMatchers(matchers)));
    }


    public static <T, S> ReportingMatcher<T> value(String nameForReport, SubValuesExtractor<T, S> subValuesExtractor, S value) {
        return value(renamed(subValuesExtractor, nameForReport), value);
    }

    public static <T, S> ReportingMatcher<T> value(String nameForReport, SubValuesExtractor<T, S> subValuesExtractor, Matcher<? super S> matcher) {
        return value(renamed(subValuesExtractor, nameForReport), matcher);
    }

    public static <T, S> ReportingMatcher<T> value(String nameForReport, SubValuesExtractor<T, S> subValuesExtractor, ReportingMatcher<? super S> reportingMatcher) {
        return value(renamed(subValuesExtractor, nameForReport), reportingMatcher);
    }

    @SafeVarargs
    public static <T, S> ReportingMatcher<T> value(String nameForReport, SubValuesExtractor<T, S> subValuesExtractor, Matcher<? super S>... matchers) {
        return value(renamed(subValuesExtractor, nameForReport), matchers);
    }

    public static <T, S> ReportingMatcher<T> value(String nameForReport, SubValuesExtractor<T, S> subValuesExtractor, Iterable<? extends Matcher<? super S>> matchers) {
        return value(renamed(subValuesExtractor, nameForReport), matchers);
    }



    public static <T, S> ReportingMatcher<T> field(Field field, S value) {
        return value(SubValuesExtractors.<T, S>field(field), value);
    }

    public static <T, S> ReportingMatcher<T> field(Field field, Matcher<? super S> matcher) {
        return value(SubValuesExtractors.<T, S>field(field), matcher);
    }

    public static <T, S> ReportingMatcher<T> field(Field field, ReportingMatcher<? super S> reportingMatcher) {
        return value(SubValuesExtractors.<T, S>field(field), reportingMatcher);
    }

    @SafeVarargs
    public static <T, S> ReportingMatcher<T> field(Field field, Matcher<? super S>... matchers) {
        return value(SubValuesExtractors.<T, S>field(field), matchers);
    }

    public static <T, S> ReportingMatcher<T> field(Field field, Iterable<? extends Matcher<? super S>> matchers) {
        return value(SubValuesExtractors.<T, S>field(field), matchers);
    }


    public static <T, S> ReportingMatcher<T> field(String fieldName, S value) {
        return value(SubValuesExtractors.<T, S>fieldByName(fieldName), value);
    }

    public static <T, S> ReportingMatcher<T> field(String fieldName, Matcher<? super S> matcher) {
        return value(SubValuesExtractors.<T, S>fieldByName(fieldName), matcher);
    }

    public static <T, S> ReportingMatcher<T> field(String fieldName, ReportingMatcher<? super S> reportingMatcher) {
        return value(SubValuesExtractors.<T, S>fieldByName(fieldName), reportingMatcher);
    }

    @SafeVarargs
    public static <T, S> ReportingMatcher<T> field(String fieldName, Matcher<? super S>... matchers) {
        return value(SubValuesExtractors.<T, S>fieldByName(fieldName), matchers);
    }

    public static <T, S> ReportingMatcher<T> field(String fieldName, Iterable<? extends Matcher<? super S>> matchers) {
        return value(SubValuesExtractors.<T, S>fieldByName(fieldName), matchers);
    }


    public static <T, S> ReportingMatcher<T> field(String nameForReport, Field field, S value) {
        return value(nameForReport, SubValuesExtractors.<T, S>field(field), value);
    }

    public static <T, S> ReportingMatcher<T> field(String nameForReport, Field field, Matcher<? super S> matcher) {
        return value(nameForReport, SubValuesExtractors.<T, S>field(field), matcher);
    }

    public static <T, S> ReportingMatcher<T> field(String nameForReport, Field field, ReportingMatcher<? super S> reportingMatcher) {
        return value(nameForReport, SubValuesExtractors.<T, S>field(field), reportingMatcher);
    }

    @SafeVarargs
    public static <T, S> ReportingMatcher<T> field(String nameForReport, Field field, Matcher<? super S>... matchers) {
        return value(nameForReport, SubValuesExtractors.<T, S>field(field), matchers);
    }

    public static <T, S> ReportingMatcher<T> field(String nameForReport, Field field, Iterable<? extends Matcher<? super S>> matchers) {
        return value(nameForReport, SubValuesExtractors.<T, S>field(field), matchers);
    }


    public static <T, S> ReportingMatcher<T> field(String nameForReport, String fieldName, S value) {
        return value(nameForReport, SubValuesExtractors.<T, S>fieldByName(fieldName), value);
    }

    public static <T, S> ReportingMatcher<T> field(String nameForReport, String fieldName, Matcher<? super S> matcher) {
        return value(nameForReport, SubValuesExtractors.<T, S>fieldByName(fieldName), matcher);
    }

    public static <T, S> ReportingMatcher<T> field(String nameForReport, String fieldName, ReportingMatcher<? super S> reportingMatcher) {
        return value(nameForReport, SubValuesExtractors.<T, S>fieldByName(fieldName), reportingMatcher);
    }

    @SafeVarargs
    public static <T, S> ReportingMatcher<T> field(String nameForReport, String fieldName, Matcher<? super S>... matchers) {
        return value(nameForReport, SubValuesExtractors.<T, S>fieldByName(fieldName), matchers);
    }

    public static <T, S> ReportingMatcher<T> field(String nameForReport, String fieldName, Iterable<? extends Matcher<? super S>> matchers) {
        return value(nameForReport, SubValuesExtractors.<T, S>fieldByName(fieldName), matchers);
    }



    public static <T, S> ReportingMatcher<T> method(MethodInvocation invocation, S value) {
        return value(SubValuesExtractors.<T, S>method(invocation.method, invocation.arguments), value);
    }

    public static <T, S> ReportingMatcher<T> method(MethodInvocation invocation, Matcher<? super S> matcher) {
        return value(SubValuesExtractors.<T, S>method(invocation.method, invocation.arguments), matcher);
    }

    public static <T, S> ReportingMatcher<T> method(MethodInvocation invocation, ReportingMatcher<? super S> reportingMatcher) {
        return value(SubValuesExtractors.<T, S>method(invocation.method, invocation.arguments), reportingMatcher);
    }

    @SafeVarargs
    public static <T, S> ReportingMatcher<T> method(MethodInvocation invocation, Matcher<? super S>... matchers) {
        return value(SubValuesExtractors.<T, S>method(invocation.method, invocation.arguments), matchers);
    }

    public static <T, S> ReportingMatcher<T> method(MethodInvocation invocation, Iterable<? extends Matcher<? super S>> matchers) {
        return value(SubValuesExtractors.<T, S>method(invocation.method, invocation.arguments), matchers);
    }


    public static <T, S> ReportingMatcher<T> method(MethodByNameInvocation invocation, S value) {
        return value(SubValuesExtractors.<T, S>methodByName(invocation.methodName, invocation.arguments), value);
    }

    public static <T, S> ReportingMatcher<T> method(MethodByNameInvocation invocation, Matcher<? super S> matcher) {
        return value(SubValuesExtractors.<T, S>methodByName(invocation.methodName, invocation.arguments), matcher);
    }

    public static <T, S> ReportingMatcher<T> method(MethodByNameInvocation invocation, ReportingMatcher<? super S> reportingMatcher) {
        return value(SubValuesExtractors.<T, S>methodByName(invocation.methodName, invocation.arguments), reportingMatcher);
    }

    @SafeVarargs
    public static <T, S> ReportingMatcher<T> method(MethodByNameInvocation invocation, Matcher<? super S>... matchers) {
        return value(SubValuesExtractors.<T, S>methodByName(invocation.methodName, invocation.arguments), matchers);
    }

    public static <T, S> ReportingMatcher<T> method(MethodByNameInvocation invocation, Iterable<? extends Matcher<? super S>> matchers) {
        return value(SubValuesExtractors.<T, S>methodByName(invocation.methodName, invocation.arguments), matchers);
    }


    public static <T, S> ReportingMatcher<T> method(String nameForReport, MethodInvocation invocation, S value) {
        return value(nameForReport, SubValuesExtractors.<T, S>method(invocation.method, invocation.arguments), value);
    }

    public static <T, S> ReportingMatcher<T> method(String nameForReport, MethodInvocation invocation, Matcher<? super S> matcher) {
        return value(nameForReport, SubValuesExtractors.<T, S>method(invocation.method, invocation.arguments), matcher);
    }

    public static <T, S> ReportingMatcher<T> method(String nameForReport, MethodInvocation invocation, ReportingMatcher<? super S> reportingMatcher) {
        return value(nameForReport, SubValuesExtractors.<T, S>method(invocation.method, invocation.arguments), reportingMatcher);
    }

    @SafeVarargs
    public static <T, S> ReportingMatcher<T> method(String nameForReport, MethodInvocation invocation, Matcher<? super S>... matchers) {
        return value(nameForReport, SubValuesExtractors.<T, S>method(invocation.method, invocation.arguments), matchers);
    }

    public static <T, S> ReportingMatcher<T> method(String nameForReport, MethodInvocation invocation, Iterable<? extends Matcher<? super S>> matchers) {
        return value(nameForReport, SubValuesExtractors.<T, S>method(invocation.method, invocation.arguments), matchers);
    }


    public static <T, S> ReportingMatcher<T> method(String nameForReport, MethodByNameInvocation invocation, S value) {
        return value(nameForReport, SubValuesExtractors.<T, S>methodByName(invocation.methodName, invocation.arguments), value);
    }

    public static <T, S> ReportingMatcher<T> method(String nameForReport, MethodByNameInvocation invocation, Matcher<? super S> matcher) {
        return value(nameForReport, SubValuesExtractors.<T, S>methodByName(invocation.methodName, invocation.arguments), matcher);
    }

    public static <T, S> ReportingMatcher<T> method(String nameForReport, MethodByNameInvocation invocation, ReportingMatcher<? super S> reportingMatcher) {
        return value(nameForReport, SubValuesExtractors.<T, S>methodByName(invocation.methodName, invocation.arguments), reportingMatcher);
    }

    @SafeVarargs
    public static <T, S> ReportingMatcher<T> method(String nameForReport, MethodByNameInvocation invocation, Matcher<? super S>... matchers) {
        return value(nameForReport, SubValuesExtractors.<T, S>methodByName(invocation.methodName, invocation.arguments), matchers);
    }

    public static <T, S> ReportingMatcher<T> method(String nameForReport, MethodByNameInvocation invocation, Iterable<? extends Matcher<? super S>> matchers) {
        return value(nameForReport, SubValuesExtractors.<T, S>methodByName(invocation.methodName, invocation.arguments), matchers);
    }


    public static MethodInvocation invocation(Method method, Object... arguments) {
        return new MethodInvocation(method, arguments);
    }

    public static MethodByNameInvocation invocation(String methodName, Object... arguments) {
        return new MethodByNameInvocation(methodName, arguments);
    }



    public static <T, S> ReportingMatcher<T> getter(Method method, S value) {
        return value(SubValuesExtractors.<T, S>getter(method), value);
    }

    public static <T, S> ReportingMatcher<T> getter(Method method, Matcher<? super S> matcher) {
        return value(SubValuesExtractors.<T, S>getter(method), matcher);
    }

    public static <T, S> ReportingMatcher<T> getter(Method method, ReportingMatcher<? super S> reportingMatcher) {
        return value(SubValuesExtractors.<T, S>getter(method), reportingMatcher);
    }

    @SafeVarargs
    public static <T, S> ReportingMatcher<T> getter(Method method, Matcher<? super S>... matchers) {
        return value(SubValuesExtractors.<T, S>getter(method), matchers);
    }

    public static <T, S> ReportingMatcher<T> getter(Method method, Iterable<? extends Matcher<? super S>> matchers) {
        return value(SubValuesExtractors.<T, S>getter(method), matchers);
    }


    public static <T, S> ReportingMatcher<T> getter(String methodName, S value) {
        return value(SubValuesExtractors.<T, S>getterByName(methodName), value);
    }

    public static <T, S> ReportingMatcher<T> getter(String methodName, Matcher<? super S> matcher) {
        return value(SubValuesExtractors.<T, S>getterByName(methodName), matcher);
    }

    public static <T, S> ReportingMatcher<T> getter(String methodName, ReportingMatcher<? super S> reportingMatcher) {
        return value(SubValuesExtractors.<T, S>getterByName(methodName), reportingMatcher);
    }

    @SafeVarargs
    public static <T, S> ReportingMatcher<T> getter(String methodName, Matcher<? super S>... matchers) {
        return value(SubValuesExtractors.<T, S>getterByName(methodName), matchers);
    }

    public static <T, S> ReportingMatcher<T> getter(String methodName, Iterable<? extends Matcher<? super S>> matchers) {
        return value(SubValuesExtractors.<T, S>getterByName(methodName), matchers);
    }



    public static <T> ReportingMatcher<T[]> arrayElement(int index, T value) {
        return value(SubValuesExtractors.<T>arrayElement(index), value);
    }

    public static <T> ReportingMatcher<T[]> arrayElement(int index, Matcher<? super T> matcher) {
        return value(SubValuesExtractors.<T>arrayElement(index), matcher);
    }

    public static <T> ReportingMatcher<T[]> arrayElement(int index, ReportingMatcher<? super T> reportingMatcher) {
        return value(SubValuesExtractors.<T>arrayElement(index), reportingMatcher);
    }

    @SafeVarargs
    public static <T> ReportingMatcher<T[]> arrayElement(int index, Matcher<? super T>... matchers) {
        return value(SubValuesExtractors.<T>arrayElement(index), matchers);
    }

    public static <T> ReportingMatcher<T[]> arrayElement(int index, Iterable<? extends Matcher<? super T>> matchers) {
        return value(SubValuesExtractors.<T>arrayElement(index), matchers);
    }


    public static <T> ReportingMatcher<Iterable<T>> iterableElement(int index, T value) {
        return value(SubValuesExtractors.<T>iterableElement(index), value);
    }

    public static <T> ReportingMatcher<Iterable<T>> iterableElement(int index, Matcher<? super T> matcher) {
        return value(SubValuesExtractors.<T>iterableElement(index), matcher);
    }

    public static <T> ReportingMatcher<Iterable<T>> iterableElement(int index, ReportingMatcher<? super T> reportingMatcher) {
        return value(SubValuesExtractors.<T>iterableElement(index), reportingMatcher);
    }

    @SafeVarargs
    public static <T> ReportingMatcher<Iterable<T>> iterableElement(int index, Matcher<? super T>... matchers) {
        return value(SubValuesExtractors.<T>iterableElement(index), matchers);
    }

    public static <T> ReportingMatcher<Iterable<T>> iterableElement(int index, Iterable<? extends Matcher<? super T>> matchers) {
        return value(SubValuesExtractors.<T>iterableElement(index), matchers);
    }


    public static <T> ReportingMatcher<List<T>> listElement(int index, T value) {
        return value(SubValuesExtractors.<T>listElement(index), value);
    }

    public static <T> ReportingMatcher<List<T>> listElement(int index, Matcher<? super T> matcher) {
        return value(SubValuesExtractors.<T>listElement(index), matcher);
    }

    public static <T> ReportingMatcher<List<T>> listElement(int index, ReportingMatcher<? super T> reportingMatcher) {
        return value(SubValuesExtractors.<T>listElement(index), reportingMatcher);
    }

    @SafeVarargs
    public static <T> ReportingMatcher<List<T>> listElement(int index, Matcher<? super T>... matchers) {
        return value(SubValuesExtractors.<T>listElement(index), matchers);
    }

    public static <T> ReportingMatcher<List<T>> listElement(int index, Iterable<? extends Matcher<? super T>> matchers) {
        return value(SubValuesExtractors.<T>listElement(index), matchers);
    }



    public static <K, V> ReportingMatcher<Map<K, V>> entry(K key, V value) {
        return value(SubValuesExtractors.<K, V>hashMapValueForKey(key), value);
    }

    public static <K, V> ReportingMatcher<Map<K, V>> entry(K key, Matcher<? super V> matcher) {
        return value(SubValuesExtractors.<K, V>hashMapValueForKey(key), matcher);
    }

    public static <K, V> ReportingMatcher<Map<K, V>> entry(K key, ReportingMatcher<? super V> reportingMatcher) {
        return value(SubValuesExtractors.<K, V>hashMapValueForKey(key), reportingMatcher);
    }

    @SafeVarargs
    public static <K, V> ReportingMatcher<Map<K, V>> entry(K key, Matcher<? super V>... matchers) {
        return value(SubValuesExtractors.<K, V>hashMapValueForKey(key), matchers);
    }

    public static <K, V> ReportingMatcher<Map<K, V>> entry(K key, Iterable<? extends Matcher<? super V>> matchers) {
        return value(SubValuesExtractors.<K, V>hashMapValueForKey(key), matchers);
    }



    @SafeVarargs
    public static <T> ReportingMatcher<Iterable<T>> iterable(Supplier<SubValuesChecker>... subValuesCheckerSuppliers) {
        return iterable(asList(subValuesCheckerSuppliers));
    }

    public static <T> ReportingMatcher<Iterable<T>> iterable(Iterable<Supplier<SubValuesChecker>> subValuesCheckerSuppliers) {
        return subValuesMatcher(iterableElements(), subValuesCheckerSuppliers);
    }

    @SafeVarargs
    public static <T> ReportingMatcher<Iterator<T>> iterator(Supplier<SubValuesChecker>... subValuesCheckerSuppliers) {
        return iterator(asList(subValuesCheckerSuppliers));
    }

    public static <T> ReportingMatcher<Iterator<T>> iterator(Iterable<Supplier<SubValuesChecker>> subValuesCheckerSuppliers) {
        return subValuesMatcher(iteratorElements(), subValuesCheckerSuppliers);
    }

    @SafeVarargs
    public static <T> ReportingMatcher<T[]> array(Supplier<SubValuesChecker>... subValuesCheckerSuppliers) {
        return array(asList(subValuesCheckerSuppliers));
    }

    public static <T> ReportingMatcher<T[]> array(Iterable<Supplier<SubValuesChecker>> subValuesCheckerSuppliers) {
        return subValuesMatcher(arrayElements(), subValuesCheckerSuppliers);
    }

    @SafeVarargs
    public static <K, V> ReportingMatcher<Map<K, V>> hashMap(Supplier<SubValuesChecker>... subValuesCheckerSuppliers) {
        return hashMap(asList(subValuesCheckerSuppliers));
    }

    public static <K, V> ReportingMatcher<Map<K, V>> hashMap(Iterable<Supplier<SubValuesChecker>> subValuesCheckerSuppliers) {
        return subValuesMatcher(hashMapEntries(), subValuesCheckerSuppliers);
    }


    @SafeVarargs
    public static <T, S> ReportingMatcher<T> subValuesMatcher(SubValuesExtractor<T, S> subValuesExtractor, Supplier<SubValuesChecker>... subValuesCheckerSuppliers) {
        return subValuesMatcher(subValuesExtractor, asList(subValuesCheckerSuppliers));
    }

    public static <T, S> ReportingMatcher<T> subValuesMatcher(SubValuesExtractor<T, S> subValuesExtractor, Iterable<Supplier<SubValuesChecker>> subValuesCheckerSuppliers) {
        return new SubValuesMatcher<>(subValuesExtractor, compositeSubValuesCheckerSupplier(subValuesCheckerSuppliers));
    }



    public static <T> ReportingMatcher<T> displayAll(SubValuesExtractor<T, ?> subValuesExtractor) {
        return new SubValuesMatcher<>(subValuesExtractor, noOpSubValuesChecker());
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
