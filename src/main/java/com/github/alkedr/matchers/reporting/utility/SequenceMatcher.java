package com.github.alkedr.matchers.reporting.utility;

import com.github.alkedr.matchers.reporting.ReportingMatcher;
import com.github.alkedr.matchers.reporting.base.BaseReportingMatcher;
import com.google.common.collect.Iterators;
import org.hamcrest.Description;

import java.util.Iterator;

/**
 * "Склеивает" несколько ReportingMatcher'ов (при вызове run или matches запускает их последовательно)
 */
public class SequenceMatcher<T> extends BaseReportingMatcher<T> {
    private final Iterable<? extends ReportingMatcher<? super T>> matchers;

    public SequenceMatcher(Iterable<? extends ReportingMatcher<? super T>> matchers) {
        this.matchers = matchers;
    }

    @Override
    public Iterator<Object> run(Object item) {
        return Iterators.concat(Iterators.transform(matchers.iterator(), reportingMatcher -> reportingMatcher.run(item)));
    }

    @Override
    public Iterator<Object> runForMissingItem() {
        return Iterators.concat(Iterators.transform(matchers.iterator(), ReportingMatcher::runForMissingItem));
    }

    @Override
    public void describeTo(Description description) {
        // TODO
    }
}
