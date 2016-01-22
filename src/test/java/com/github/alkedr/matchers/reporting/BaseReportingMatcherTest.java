package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.check.results.CheckResult;
import org.apache.commons.collections4.iterators.SingletonIterator;
import org.hamcrest.Description;
import org.junit.Test;

import java.util.Iterator;

import static com.github.alkedr.matchers.reporting.check.results.CheckResults.failedMatcher;
import static java.util.Collections.emptyIterator;
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
        public Iterator<CheckResult> getChecks(Object item) {
            return emptyIterator();
        }

        @Override
        public Iterator<CheckResult> getChecksForMissingItem() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void describeTo(Description description) {
            throw new UnsupportedOperationException();
        }
    }

    private static class BaseReportingMatcherThatAddsFailedCheck extends BaseReportingMatcher<Object> {
        @Override
        public Iterator<CheckResult> getChecks(Object item) {
            return new SingletonIterator<>(failedMatcher(null, null));
        }

        @Override
        public Iterator<CheckResult> getChecksForMissingItem() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void describeTo(Description description) {
            throw new UnsupportedOperationException();
        }
    }
}
