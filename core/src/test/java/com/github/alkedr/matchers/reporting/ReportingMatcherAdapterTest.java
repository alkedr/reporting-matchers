package com.github.alkedr.matchers.reporting;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;

import static com.github.alkedr.matchers.reporting.ReportingMatcherAdapter.toReportingMatcher;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

// TODO: test nulls?  toReportingMatcher(null)
public class ReportingMatcherAdapterTest {
    @Test
    public void toReportingMatcherMethod_shouldNotWrapReportingMatchers() {
        assertSame(REPORTING_MATCHER, toReportingMatcher(REPORTING_MATCHER));
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
        ReportingMatcher.Reporter reporter = mock(ReportingMatcher.Reporter.class);
        toReportingMatcher(is(1)).run(1, reporter);
        verify(reporter).addCheck(ReportingMatcher.Reporter.CheckStatus.PASSED, "is <1>");
        verifyNoMoreInteractions(reporter);
    }

    @Test
    public void run_failed() {
        ReportingMatcher.Reporter reporter = mock(ReportingMatcher.Reporter.class);
        toReportingMatcher(is(1)).run(2, reporter);
        verify(reporter).addCheck(ReportingMatcher.Reporter.CheckStatus.FAILED, "Expected: is <1>\n     but: was <2>");
        verifyNoMoreInteractions(reporter);
    }

    @Test
    public void run_broken() {
        ReportingMatcher.Reporter reporter = mock(ReportingMatcher.Reporter.class);
        Matcher<Object> throwingMatcher = mock(Matcher.class);
        doThrow(new RuntimeException("blah")).when(throwingMatcher).matches(any());
        toReportingMatcher(throwingMatcher).run(1, reporter);
        verify(reporter).addCheck(eq(ReportingMatcher.Reporter.CheckStatus.BROKEN), argThat(allOf(containsString("blah"), containsString("run_broken"))));
        verifyNoMoreInteractions(reporter);
    }


    @Test
    public void runForMissingItem() {
        ReportingMatcher.Reporter reporter = mock(ReportingMatcher.Reporter.class);
        toReportingMatcher(is(1)).runForMissingItem(reporter);
        verify(reporter).addCheck(ReportingMatcher.Reporter.CheckStatus.FAILED, "is <1>");
        verifyNoMoreInteractions(reporter);
    }


    private static final Matcher<?> REPORTING_MATCHER = new BaseReportingMatcher<Object>() {
        @Override
        public void describeTo(Description description) {
        }

        @Override
        public void run(Object item, Reporter reporter) {
        }

        @Override
        public void runForMissingItem(Reporter reporter) {
        }
    };
}
