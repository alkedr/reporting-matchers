package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;
import org.hamcrest.CustomMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;

import static com.github.alkedr.matchers.reporting.ReportingMatchers.toReportingMatcher;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class ReportingMatcherAdapterTest {
    private final SafeTreeReporter reporter = mock(SafeTreeReporter.class);


    @Test
    public void toReportingMatcherMethod_shouldNotWrapReportingMatchers() {
        Matcher<?> reportingMatcher = mock(ReportingMatcher.class);
        assertSame(reportingMatcher, toReportingMatcher(reportingMatcher));
    }


    @Test
    public void matchesMethod() {
        ReportingMatcher<Integer> matcher = toReportingMatcher(is(1));
        assertTrue(matcher.matches(1));
        assertFalse(matcher.matches(2));
    }

    @Test
    public void describeToMethod() {
        assertEquals(StringDescription.asString(is(1)), StringDescription.asString(toReportingMatcher(is(1))));
    }

    @Test
    public void describeMismatchMethod() {
        StringDescription expected = new StringDescription();
        StringDescription actual = new StringDescription();
        is(1).describeMismatch(2, expected);
        toReportingMatcher(is(1)).describeMismatch(2, actual);
        assertEquals(expected.toString(), actual.toString());
    }


    @Test
    public void run_passed() {
        toReportingMatcher(is(1)).run(1, reporter);
        verify(reporter).passedCheck("is <1>");
        verifyNoMoreInteractions(reporter);
    }

    @Test
    public void run_failed() {
        toReportingMatcher(is(1)).run(2, reporter);
        verify(reporter).failedCheck("is <1>", "was <2>");
        verifyNoMoreInteractions(reporter);
    }

    @Test
    public void run_broken() {
        RuntimeException exception = new RuntimeException("blah");
        toReportingMatcher(new ThrowingMatcher("123", exception)).run(1, reporter);
        verify(reporter).brokenCheck(eq("123"), same(exception));
        verifyNoMoreInteractions(reporter);
    }


    @Test
    public void runForMissingItem() {
        toReportingMatcher(is(1)).runForMissingItem(reporter);
        verify(reporter).checkForMissingItem("is <1>");
        verifyNoMoreInteractions(reporter);
    }


    private static class ThrowingMatcher extends CustomMatcher<Object> {
        private final RuntimeException exception;

        ThrowingMatcher(String description, RuntimeException exception) {
            super(description);
            this.exception = exception;
        }

        @Override
        public boolean matches(Object item) {
            throw exception;
        }
    }
}
