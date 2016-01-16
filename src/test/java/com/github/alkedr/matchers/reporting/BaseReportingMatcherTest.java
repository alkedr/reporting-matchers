package com.github.alkedr.matchers.reporting;

import org.apache.commons.collections4.iterators.SingletonIterator;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Description;
import org.junit.Test;

import java.util.Iterator;

import static java.util.Collections.emptyIterator;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BaseReportingMatcherTest {
    @Test
    public void matchesMethod_shouldReturnTrue_ifRunDoesNotDoAnythingThatChangesMatchesFlag() {
        assertTrue(new BaseReportingMatcherThatDoesNotAddChecks().matches(null));
    }

    @Test
    public void matchesMethod_shouldReturnFalse_ifRunDoesSomethingThatChangesMatchesFlag() {
        assertFalse(new BaseReportingMatcherThatAddsFailedCheck().matches(null));
    }


    private static class BaseReportingMatcherThatDoesNotAddChecks extends BaseReportingMatcher<Object> {
        @Override
        public Iterator<Object> run(Object item) {
            return emptyIterator();
        }

        @Override
        public Iterator<Object> runForMissingItem() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void describeTo(Description description) {
            throw new UnsupportedOperationException();
        }
    }

    private static class BaseReportingMatcherThatAddsFailedCheck extends BaseReportingMatcher<Object> {
        @Override
        public Iterator<Object> run(Object item) {
            return new SingletonIterator<>(not(CoreMatchers.anything()));
        }

        @Override
        public Iterator<Object> runForMissingItem() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void describeTo(Description description) {
            throw new UnsupportedOperationException();
        }
    }
}
