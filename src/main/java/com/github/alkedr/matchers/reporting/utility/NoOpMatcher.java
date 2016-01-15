package com.github.alkedr.matchers.reporting.utility;

import com.github.alkedr.matchers.reporting.ReportingMatcher;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.util.Iterator;

import static java.util.Collections.emptyIterator;

// matches всегда возвращает true, в отчёт никогда ничего не добавляется
// TODO: реализовать через пустой sequence?
public class NoOpMatcher<T> extends BaseMatcher<T> implements ReportingMatcher<T> {
    private static final NoOpMatcher<?> INSTANCE = new NoOpMatcher<>();

    /*private static final Checks EMPTY_CHECKS = new Checks(
            emptyIterator(),
            Collections.<KeyValueChecks>emptyList().iterator()
    );


    private NoOpMatcher() {
    }

    @Override
    public Checks getChecks(Object item) {
        return EMPTY_CHECKS;
    }

    @Override
    public Checks getChecksForMissingItem() {
        return EMPTY_CHECKS;
    }

    @Override
    public boolean matches(Object item) {
        return true;
    }

    @Override
    public void describeTo(Description description) {
        // TODO
    }*/

    // TODO: rename?
    public static <T> NoOpMatcher<T> noOp() {
        return (NoOpMatcher<T>) INSTANCE;
    }


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
}
