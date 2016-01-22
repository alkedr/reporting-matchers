package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.check.results.CheckResult;
import com.google.common.collect.Iterators;

import java.util.Iterator;
import java.util.function.Function;

class SequenceOrMergeMatcher<T> extends BaseReportingMatcher<T> {
    private final Iterable<? extends ReportingMatcher<? super T>> matchers;
    private final Function<Iterator<CheckResult>, Iterator<CheckResult>> mergeFunction;

    SequenceOrMergeMatcher(Iterable<? extends ReportingMatcher<? super T>> matchers, Function<Iterator<CheckResult>, Iterator<CheckResult>> mergeFunction) {
        this.matchers = matchers;
        this.mergeFunction = mergeFunction;
    }

    @Override
    public Iterator<CheckResult> getChecks(Object item) {
        return getChecksImpl(matcher -> matcher.getChecks(item));
    }

    @Override
    public Iterator<CheckResult> getChecksForMissingItem() {
        return getChecksImpl(ReportingMatcher::getChecksForMissingItem);
    }

    private Iterator<CheckResult> getChecksImpl(com.google.common.base.Function<ReportingMatcher<? super T>, Iterator<CheckResult>> checksFunction) {
        return mergeFunction.apply(
                Iterators.concat(
                        Iterators.transform(
                                matchers.iterator(),
                                checksFunction
                        )
                )
        );
    }
}
