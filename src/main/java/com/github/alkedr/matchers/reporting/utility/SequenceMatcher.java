package com.github.alkedr.matchers.reporting.utility;

import com.github.alkedr.matchers.reporting.BaseReportingMatcher;
import com.github.alkedr.matchers.reporting.ReportingMatcher;
import com.google.common.collect.Iterators;
import org.hamcrest.Description;

/**
 * "Склеивает" несколько ReportingMatcher'ов (при вызове run или matches запускает их последовательно)
 */
// TODO: поддерживать простые матчеры
public class SequenceMatcher<T> extends BaseReportingMatcher<T> {
    private final Iterable<? extends ReportingMatcher<? super T>> matchers;

    public SequenceMatcher(Iterable<? extends ReportingMatcher<? super T>> matchers) {
        this.matchers = matchers;
    }

    @Override
    public Checks getChecks(Object item) {
        return Checks.sequence(
                Iterators.transform(
                        matchers.iterator(),
                        matcher -> matcher.getChecks(item)
                )
        );
    }

    @Override
    public Checks getChecksForMissingItem() {
        return Checks.sequence(
                Iterators.transform(
                        matchers.iterator(),
                        matcher -> matcher.getChecksForMissingItem()
                )
        );
    }

    @Override
    public void describeTo(Description description) {
        // TODO
    }
}
