package com.github.alkedr.matchers.reporting.iteration;

import com.github.alkedr.matchers.reporting.ReportingMatcher;
import com.github.alkedr.matchers.reporting.keys.ElementKey;
import com.google.common.collect.Iterators;

import java.util.Iterator;

import static com.github.alkedr.matchers.reporting.utility.ReportingMatchersAdapter.toReportingMatcher;
import static org.hamcrest.CoreMatchers.equalTo;

// TODO: пробовать пропускать элементы?
public class ContainsInSpecifiedOrderChecker implements IteratorMatcher.ElementChecker {
    private final Iterator<ReportingMatcher<?>> elementMatchers;
    private int index;

    public ContainsInSpecifiedOrderChecker(Iterator<ReportingMatcher<?>> elementMatchers) {
        this.elementMatchers = elementMatchers;
    }

    @Override
    public ReportingMatcher.Checks begin() {
        return ReportingMatcher.Checks.noOp();
    }

    @Override
    public ReportingMatcher.Checks element(ReportingMatcher.Key key, ReportingMatcher.Value value) {
        index++;
        return elementMatchers.hasNext()
                ? elementMatchers.next().getChecks(value.get())
                : ReportingMatcher.Checks.missing();
    }

    @Override
    public ReportingMatcher.Checks end() {
        return ReportingMatcher.Checks.sequence(
                Iterators.transform(
                        elementMatchers,
                        matcher -> ReportingMatcher.Checks.keyValueChecks(
                                new ReportingMatcher.KeyValueChecks(
                                        new ElementKey(index++),
                                        ReportingMatcher.Value.missing(),
                                        matcher.getChecksForMissingItem()
                                )
                        )
                )
        );
    }


    @SafeVarargs
    public static <T> IteratorMatcher.ElementChecker containsInSpecifiedOrderChecker(T... elements) {
        return new ContainsInSpecifiedOrderChecker(
                Iterators.transform(
                        Iterators.forArray(elements),
                        element -> toReportingMatcher(equalTo(element))
                )
        );
    }
}
