package com.github.alkedr.matchers.reporting.iteration;

import com.github.alkedr.matchers.reporting.ReportingMatcher;
import com.github.alkedr.matchers.reporting.keys.ElementKey;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import static com.github.alkedr.matchers.reporting.ChecksUtils.*;
import static com.github.alkedr.matchers.reporting.ReportingMatcher.PresenceStatus.PRESENT;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IteratorMatcherTest {
    private final Object element1 = new Object();
    private final Object element2 = new Object();
    private final Iterable<Object> iterable = asList(element1, element2);
    private final Matcher<?> beginMatcher = mock(Matcher.class);
    private final Matcher<?> element1Matcher = mock(Matcher.class);
    private final Matcher<?> element2Matcher = mock(Matcher.class);
    private final Matcher<?> endMatcher = mock(Matcher.class);
    private final ReportingMatcher.Checks beginChecks = ReportingMatcher.Checks.matchers(beginMatcher);
    private final ReportingMatcher.Checks element1Checks = ReportingMatcher.Checks.matchers(element1Matcher);
    private final ReportingMatcher.Checks element2Checks = ReportingMatcher.Checks.matchers(element2Matcher);
    private final ReportingMatcher.Checks endChecks = ReportingMatcher.Checks.matchers(endMatcher);
    private final IteratorMatcher.ElementChecker elementChecker = mock(IteratorMatcher.ElementChecker.class);

    @Before
    public void setUp() {
        when(elementChecker.begin()).thenReturn(beginChecks);
        when(elementChecker.element(any(), any())).thenReturn(element1Checks).thenReturn(element2Checks);
        when(elementChecker.end()).thenReturn(endChecks);
    }

    @Test
    public void getChecks() {
        ReportingMatcher.Key element1Key = new ElementKey(0);
        ReportingMatcher.Key element2Key = new ElementKey(1);
        Matcher<ReportingMatcher.Value> value1 = value(PRESENT, element1, element1.toString(), null);
        Matcher<ReportingMatcher.Value> value2 = value(PRESENT, element2, element2.toString(), null);
        verifyChecks(
                new IteratorMatcher<>(() -> elementChecker).getChecks(iterable.iterator()),
                matcher(beginMatcher),
                kvc(equalTo(element1Key), value1, checks(matcher(element1Matcher))),
                kvc(equalTo(element2Key), value2, checks(matcher(element2Matcher))),
                matcher(endMatcher)
        );
        InOrder inOrder = inOrder(elementChecker);
        inOrder.verify(elementChecker).begin();
        inOrder.verify(elementChecker).element(eq(element1Key), argThat(value1));
        inOrder.verify(elementChecker).element(eq(element2Key), argThat(value2));
        inOrder.verify(elementChecker).end();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void getChecksForMissingItem() {
        verifyChecks(
                new IteratorMatcher<>(() -> elementChecker).getChecksForMissingItem(),
                matcher(beginMatcher),
                matcher(endMatcher)
        );
        InOrder inOrder = inOrder(elementChecker);
        inOrder.verify(elementChecker).begin();
        inOrder.verify(elementChecker).end();
        inOrder.verifyNoMoreInteractions();
    }
}
