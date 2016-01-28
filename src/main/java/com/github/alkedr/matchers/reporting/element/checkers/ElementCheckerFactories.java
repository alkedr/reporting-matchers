package com.github.alkedr.matchers.reporting.element.checkers;

import static java.util.Arrays.asList;

public class ElementCheckerFactories {
    /*public static ElementCheckerFactory noOpElementCheckerFactory() {
        return ElementCheckers::noOpElementChecker;
    }*/


    public static ElementCheckerFactory compositeElementCheckerFactory(ElementCheckerFactory... elementCheckers) {
        return compositeElementCheckerFactory(asList(elementCheckers));
    }

    public static ElementCheckerFactory compositeElementCheckerFactory(Iterable<ElementCheckerFactory> elementCheckers) {
        return new CompositeElementCheckerFactory(elementCheckers);
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