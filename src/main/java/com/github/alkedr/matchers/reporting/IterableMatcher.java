package com.github.alkedr.matchers.reporting;

import org.hamcrest.Description;

import java.util.Iterator;

public class IterableMatcher<T> extends BaseReportingMatcher<Iterable<T>> {
    private final Checker checker;

    public IterableMatcher(Checker checker) {
        this.checker = checker;
    }


    interface Checker {
        Iterator<Object> begin();
        Checks element(Key key, Value value);
        Iterator<Object> end();
    }


    // Iterator<Object> – Matcher и Checks
    interface Checker2 extends Iterator<Object> {
        void element(Object element);
    }


    @Override
    public Iterator<Object> run(Object item) {
        return null;
    }

    @Override
    public Iterator<Object> runForMissingItem() {
        return null;
    }

    @Override
    public void describeTo(Description description) {

    }
}
