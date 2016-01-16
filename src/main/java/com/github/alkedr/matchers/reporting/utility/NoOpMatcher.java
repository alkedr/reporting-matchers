package com.github.alkedr.matchers.reporting.utility;

import com.github.alkedr.matchers.reporting.ReportingMatcher;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.util.Iterator;

import static java.util.Collections.emptyIterator;

// matches всегда возвращает true, в отчёт никогда ничего не добавляется
public class NoOpMatcher<T> extends BaseMatcher<T> implements ReportingMatcher<T> {
    private static final NoOpMatcher<?> INSTANCE = new NoOpMatcher<>();


    @Override
    public boolean matches(Object item) {
        return false;
    }

    @Override
    public void describeTo(Description description) {
    }

    @Override
    public Iterator<Object> run(Object item) {
        return emptyIterator();
    }

    @Override
    public Iterator<Object> runForMissingItem() {
        return emptyIterator();
    }


    // TODO: rename?
    public static <T> NoOpMatcher<T> noOp() {
        return (NoOpMatcher<T>) INSTANCE;
    }
}
