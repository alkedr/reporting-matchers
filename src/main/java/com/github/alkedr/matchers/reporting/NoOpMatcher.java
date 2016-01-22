package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.check.results.CheckResult;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.util.Iterator;

import static java.util.Collections.emptyIterator;

// matches всегда возвращает true, в отчёт никогда ничего не добавляется
class NoOpMatcher<T> extends BaseMatcher<T> implements ReportingMatcher<T> {
    static final NoOpMatcher<?> INSTANCE = new NoOpMatcher<>();

    @Override
    public Iterator<CheckResult> getChecks(Object item) {
        return emptyIterator();
    }

    @Override
    public Iterator<CheckResult> getChecksForMissingItem() {
        return emptyIterator();
    }


    @Override
    public boolean matches(Object item) {
        return true;
    }

    @Override
    public void describeTo(Description description) {
    }
}
