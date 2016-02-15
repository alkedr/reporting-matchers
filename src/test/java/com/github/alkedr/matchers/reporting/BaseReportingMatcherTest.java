package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;
import org.hamcrest.StringDescription;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
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

    @Test
    public void describeTo() {
        assertEquals("is correct", StringDescription.asString(new BaseReportingMatcherThatDoesNotAddChecks()));
    }

    @Test
    public void describeMismatch() {
        StringDescription stringDescription = new StringDescription();
        new BaseReportingMatcherThatAddsFailedCheck().describeMismatch(null, stringDescription);
        assertEquals("[fail] - Expected: 1\n              but: 2\n", stringDescription.toString());
    }

    // TODO: интеграционные тесты createReporterForDescribeMismatch


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
            safeTreeReporter.failedCheck("1", "2");
        }

        @Override
        public void runForAbsentItem(SafeTreeReporter safeTreeReporter) {
        }
    }
}
