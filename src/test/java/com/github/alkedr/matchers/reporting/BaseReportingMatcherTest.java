package com.github.alkedr.matchers.reporting;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Description;
import org.junit.Test;

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
        public void run(Object item, CheckListener checkListener) {
        }

        @Override
        public void runForMissingItem(CheckListener checkListener) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void describeTo(Description description) {
            throw new UnsupportedOperationException();
        }
    }

    private static class BaseReportingMatcherThatAddsFailedCheck extends BaseReportingMatcher<Object> {
        @Override
        public void run(Object item, CheckListener checkListener) {
            checkListener.simpleMatcher(item, not(CoreMatchers.anything()));
        }

        @Override
        public void runForMissingItem(CheckListener checkListener) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void describeTo(Description description) {
            throw new UnsupportedOperationException();
        }
    }
}
