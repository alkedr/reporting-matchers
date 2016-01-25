package com.github.alkedr.matchers.reporting.element.checkers;

import com.github.alkedr.matchers.reporting.ReportingMatcher;

import java.util.Collection;
import java.util.Iterator;

import static com.github.alkedr.matchers.reporting.ReportingMatchers.toReportingMatcher;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.equalTo;

public enum ElementCheckers {
    ;

    public static ElementChecker compositeElementChecker(ElementChecker... elementCheckers) {
        return compositeElementChecker(asList(elementCheckers));
    }

    public static ElementChecker compositeElementChecker(Iterable<ElementChecker> elementCheckers) {
        return new CompositeElementChecker(elementCheckers);
    }


    public static ElementChecker containsInSpecifiedOrder(Iterator<ReportingMatcher<?>> elementMatchers) {
        return new ContainsInSpecifiedOrderChecker(elementMatchers);
    }

    public static ElementChecker containsInSpecifiedOrder(Iterable<ReportingMatcher<?>> elementMatchers) {
        return containsInSpecifiedOrder(elementMatchers.iterator());
    }

    // TODO: тут нужны перегрузки для всех примитивных типов?
    @SafeVarargs
    public static <T> ElementChecker containsInSpecifiedOrder(T... elements) {
        return containsInSpecifiedOrder(
                stream(elements)
                        .map(element -> toReportingMatcher(equalTo(element)))
                        .collect(toList())
        );
    }


    // TODO: Iterable?
    public static ElementChecker containsInAnyOrder(Collection<ReportingMatcher<?>> elementMatchers) {
        return new ContainsInAnyOrderChecker(elementMatchers);
    }

    // TODO: тут нужны перегрузки для всех примитивных типов?
    @SafeVarargs
    public static <T> ElementChecker containsInAnyOrder(T... elements) {
        return containsInAnyOrder(
                stream(elements)
                        .map(element -> toReportingMatcher(equalTo(element)))
                        .collect(toList())
        );
    }


    // TODO: everyElement(), everyArrayElement()
    // TODO: elementsThatAre(predicate/matcher).alsoAre()
    // TODO: listWithElementsInAnyOrder, listWithElementsMatchingInAnyOrder
    // TODO: возможность указать мапу ключ -> матчер?
}
