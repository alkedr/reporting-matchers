package com.github.alkedr.matchers.reporting.sub.value.checkers;

import static java.util.Arrays.asList;

public class SubValueCheckerFactories {
    /*public static ElementCheckerFactory noOpElementCheckerFactory() {
        return ElementCheckers::noOpElementChecker;
    }*/


    @SafeVarargs
    public static <T> SubValuesCheckerFactory<T> compositeSubValuesCheckerFactory(SubValuesCheckerFactory<T>... subValuesCheckerFactories) {
        return compositeSubValuesCheckerFactory(asList(subValuesCheckerFactories));
    }

    public static <T> SubValuesCheckerFactory<T> compositeSubValuesCheckerFactory(Iterable<SubValuesCheckerFactory<T>> subValuesCheckerFactories) {
        return new CompositeSubValuesCheckerFactory(subValuesCheckerFactories);
    }


    /*public static ElementCheckerFactory containsInSpecifiedOrderFactory(Iterator<ReportingMatcher<?>> elementMatchers) {
        return () -> containsInSpecifiedOrder(elementMatchers);
    }

    public static ElementCheckerFactory containsInSpecifiedOrderFactory(Iterable<ReportingMatcher<?>> elementMatchers) {
        return () -> containsInSpecifiedOrder(elementMatchers);
    }

    // TODO: тут нужны перегрузки для всех примитивных типов?
    @SafeVarargs
    public static <T> ElementCheckerFactory containsInSpecifiedOrderFactory(T... elements) {
        return () -> containsInSpecifiedOrder(elements);
    }


    public static ElementCheckerFactory containsInSpecifiedOrderWithExtraElementsAllowedFactory(Collection<ReportingMatcher<?>> elementMatchers) {
        return () -> containsInSpecifiedOrderWithExtraElementsAllowed(elementMatchers);
    }

    // TODO: тут нужны перегрузки для всех примитивных типов?
    @SafeVarargs
    public static <T> ElementCheckerFactory containsInSpecifiedOrderWithExtraElementsAllowedFactory(T... elements) {
        return containsInSpecifiedOrderWithExtraElementsAllowed(
                stream(elements)
                        .map(element -> toReportingMatcher(equalTo(element)))
                        .collect(toList())
        );
    }


    // TODO: Iterable?
    public static ElementCheckerFactory containsInAnyOrderFactory(Collection<ReportingMatcher<?>> elementMatchers) {
        return new ContainsInAnyOrderChecker(elementMatchers, false);
    }

    // TODO: тут нужны перегрузки для всех примитивных типов?
    @SafeVarargs
    public static <T> ElementCheckerFactory containsInAnyOrderFactory(T... elements) {
        return containsInAnyOrder(
                stream(elements)
                        .map(element -> toReportingMatcher(equalTo(element)))
                        .collect(toList())
        );
    }


    // TODO: Iterable?
    public static ElementCheckerFactory containsInAnyOrderWithExtraElementsAllowedFactory(Collection<ReportingMatcher<?>> elementMatchers) {
        return new ContainsInAnyOrderChecker(elementMatchers, true);
    }

    // TODO: тут нужны перегрузки для всех примитивных типов?
    @SafeVarargs
    public static <T> ElementCheckerFactory containsInAnyOrderWithExtraElementsAllowedFactory(T... elements) {
        return containsInAnyOrderWithExtraElementsAllowed(
                stream(elements)
                        .map(element -> toReportingMatcher(equalTo(element)))
                        .collect(toList())
        );
    }*/
}
