package com.github.alkedr.matchers.reporting.element.checkers;

import com.github.alkedr.matchers.reporting.ReportingMatcher;

import java.util.Iterator;

import static com.github.alkedr.matchers.reporting.ReportingMatchers.toReportingMatcher;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.equalTo;

public enum IteratorMatcherElementCheckers {
    ;

    public static ElementChecker containsInSpecifiedOrderChecker(Iterator<ReportingMatcher<?>> elementMatchers) {
        return new ContainsInSpecifiedOrderChecker(elementMatchers);
    }

    public static ElementChecker containsInSpecifiedOrderChecker(Iterable<ReportingMatcher<?>> elementMatchers) {
        return containsInSpecifiedOrderChecker(elementMatchers.iterator());
    }

    @SafeVarargs
    public static <T> ElementChecker containsInSpecifiedOrderChecker(T... elements) {
        return containsInSpecifiedOrderChecker(
                stream(elements)
                        .map(element -> toReportingMatcher(equalTo(element)))
                        .collect(toList())
        );
    }
}
