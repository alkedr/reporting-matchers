package com.github.alkedr.matchers.reporting;

import org.hamcrest.Description;

import java.util.Iterator;
import java.util.List;

public class IteratingMatcher<T> extends BaseReportingMatcher<Iterable<T>> {
    public final Type type;
    public final List<ReportingMatcher<T>> matchers;

    public IteratingMatcher(Type type, List<ReportingMatcher<T>> matchers) {
        this.type = type;
        this.matchers = matchers;
    }

//    @Override
//    public void run(Object item, Reporter reporter) {
//        Iterable<Object> iterable = (Iterable<Object>) item;
//
//    }
//
//    @Override
//    public void runForMissingItem(Reporter reporter) {
//
//    }

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

//    @Override
//    public Checks getChecks(Object item) {
//        return null;
//    }
//
//    @Override
//    public Checks getChecksForMissingItem() {
//        return null;
//    }


    public enum Type {
        IN_ORDER,
        IN_ANY_ORDER,
    }
}
