package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;
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


    private static class BaseReportingMatcherThatDoesNotAddChecks extends BaseReportingMatcher<Object> {
        @Override
        public void run(Object item, SafeTreeReporter safeTreeReporter) {
        }

        @Override
        public void runForAbsentItem(SafeTreeReporter safeTreeReporter) {
        }
    }

    private static class BaseReportingMatcherThatAddsFailedCheck extends BaseReportingMatcher<Object> {
        @Override
        public void run(Object item, SafeTreeReporter safeTreeReporter) {
            safeTreeReporter.failedCheck(null, null);
        }

        @Override
        public void runForAbsentItem(SafeTreeReporter safeTreeReporter) {
        }
    }
}
