package com.github.alkedr.matchers.reporting;

import org.hamcrest.Description;
import org.junit.Test;

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


    private static class BaseReportingMatcherThatAddsFailedCheck extends BaseReportingMatcher<Object> {
        @Override
        public void describeTo(Description description) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void run(Object item, Reporter reporter) {
            reporter.addCheck(Reporter.CheckStatus.FAILED, null);
        }

        @Override
        public void runForMissingItem(Reporter reporter) {
            throw new UnsupportedOperationException();
        }
    }

    private static class BaseReportingMatcherThatDoesNotAddChecks extends BaseReportingMatcher<Object> {
        @Override
        public void describeTo(Description description) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void run(Object item, Reporter reporter) {
        }

        @Override
        public void runForMissingItem(Reporter reporter) {
            throw new UnsupportedOperationException();
        }
    }
}
