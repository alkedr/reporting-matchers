package com.github.alkedr.matchers.reporting.element.checkers;

import com.github.alkedr.matchers.reporting.ReportingMatcher;
import com.google.common.collect.Iterators;

import java.util.Iterator;

import static com.github.alkedr.matchers.reporting.ReportingMatchers.toReportingMatcher;
import static org.hamcrest.CoreMatchers.equalTo;

public class IteratorMatcherElementCheckers {
    public static ElementChecker containsInSpecifiedOrderChecker(Iterator<ReportingMatcher<?>> elementMatchers) {
        return new ContainsInSpecifiedOrderChecker(elementMatchers);
    }

    @SafeVarargs
    public static <T> ElementChecker containsInSpecifiedOrderChecker(T... elements) {
        return containsInSpecifiedOrderChecker(
                Iterators.transform(
                        Iterators.forArray(elements),
                        element -> toReportingMatcher(equalTo(element))
                )
        );
    }
}
