package com.github.alkedr.matchers.reporting.iteration;

import com.github.alkedr.matchers.reporting.ReportingMatcher;
import com.google.common.collect.Iterators;

public class CompositeElementChecker implements IteratorMatcher.ElementChecker {
    private final Iterable<IteratorMatcher.ElementChecker> elementCheckers;

    public CompositeElementChecker(Iterable<IteratorMatcher.ElementChecker> elementCheckers) {
        this.elementCheckers = elementCheckers;
    }

    @Override
    public ReportingMatcher.Checks begin() {
        return ReportingMatcher.Checks.merge(
                Iterators.transform(
                        elementCheckers.iterator(),
                        IteratorMatcher.ElementChecker::begin
                )
        );
    }

    @Override
    public ReportingMatcher.Checks element(ReportingMatcher.Key key, ReportingMatcher.Value value) {
        return ReportingMatcher.Checks.merge(
                Iterators.transform(
                        elementCheckers.iterator(),
                        elementChecker -> elementChecker.element(key, value)
                )
        );
    }

    @Override
    public ReportingMatcher.Checks end() {
        return ReportingMatcher.Checks.merge(
                Iterators.transform(
                        elementCheckers.iterator(),
                        IteratorMatcher.ElementChecker::end
                )
        );
    }
}
