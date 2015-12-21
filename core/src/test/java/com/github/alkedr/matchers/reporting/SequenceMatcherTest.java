package com.github.alkedr.matchers.reporting;

import org.junit.Test;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;

// TODO: test nulls? concatenateMatchers(null)  concatenateMatchers([null])
public class SequenceMatcherTest {
    private final ReportingMatcher.Reporter reporter = mock(ReportingMatcher.Reporter.class);
    private final ReportingMatcher<Object> m1 = mock(ReportingMatcher.class);
    private final ReportingMatcher<Object> m2 = mock(ReportingMatcher.class);
    private final ReportingMatcher<Object> m3 = mock(ReportingMatcher.class);

    @Test
    public void run_shouldCallRunForInnerMatchers() {
        Object item = new Object();
        new SequenceMatcher<>(asList(m1, m2, m3)).run(item, reporter);
        inOrder(m1, m2, m3).verify(m1).run(item, reporter);
        inOrder(m1, m2, m3).verify(m2).run(item, reporter);
        inOrder(m1, m2, m3).verify(m3).run(item, reporter);
        verifyNoMoreInteractions(m1, m2, m3);
    }

    @Test
    public void runForMissingItem_shouldCallRunForMissingItemForInnerMatchers() {
        new SequenceMatcher<>(asList(m1, m2, m3)).runForMissingItem(reporter);
        inOrder(m1, m2, m3).verify(m1).runForMissingItem(reporter);
        inOrder(m1, m2, m3).verify(m2).runForMissingItem(reporter);
        inOrder(m1, m2, m3).verify(m3).runForMissingItem(reporter);
        verifyNoMoreInteractions(m1, m2, m3);
    }
}
