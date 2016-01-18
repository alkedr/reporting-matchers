package com.github.alkedr.matchers.reporting.iteration;

import com.github.alkedr.matchers.reporting.ReportingMatcher;
import com.github.alkedr.matchers.reporting.keys.ElementKey;
import com.google.common.collect.Iterators;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;

import static com.github.alkedr.matchers.reporting.ChecksUtils.kvc;
import static com.github.alkedr.matchers.reporting.ChecksUtils.presenceStatus;
import static com.github.alkedr.matchers.reporting.ChecksUtils.value;
import static com.github.alkedr.matchers.reporting.ChecksUtils.verifyChecks;
import static com.github.alkedr.matchers.reporting.ReportingMatcher.PresenceStatus.MISSING;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ContainsInSpecifiedOrderCheckerTest {
    private final Object object1 = 1;
    private final Object object2 = 2;
    private final ReportingMatcher.Key key1 = mock(ReportingMatcher.Key.class);
    private final ReportingMatcher.Key key2 = mock(ReportingMatcher.Key.class);
    private final ReportingMatcher.Value value1 = ReportingMatcher.Value.present(object1);
    private final ReportingMatcher.Value value2 = ReportingMatcher.Value.present(object2);
    private final ReportingMatcher.Checks checks1 = ReportingMatcher.Checks.matchers(mock(Matcher.class));
    private final ReportingMatcher.Checks checks1Missing = ReportingMatcher.Checks.matchers(mock(Matcher.class));
    private final ReportingMatcher<?> matcher1 = mock(ReportingMatcher.class);

    @Before
    public void setUp() {
        when(matcher1.getChecks(object1)).thenReturn(checks1);
        when(matcher1.getChecksForMissingItem()).thenReturn(checks1Missing);
    }

    @Test
    public void expectedEmpty_gotEmpty() {
        IteratorMatcher.ElementChecker elementChecker = new ContainsInSpecifiedOrderChecker(Iterators.forArray());
        verifyChecks(elementChecker.begin());
        verifyChecks(elementChecker.end());
    }

    @Test
    public void expectedEmpty_gotOneItem() {
        IteratorMatcher.ElementChecker elementChecker = new ContainsInSpecifiedOrderChecker(Iterators.forArray());
        verifyChecks(elementChecker.begin());
        verifyChecks(elementChecker.element(key1, value1), presenceStatus(MISSING));
        verifyChecks(elementChecker.end());
    }

    @Test
    public void expectedOneItem_gotEmpty() {
        IteratorMatcher.ElementChecker elementChecker = new ContainsInSpecifiedOrderChecker(Iterators.forArray(matcher1));
        verifyChecks(elementChecker.begin());
        verifyChecks(elementChecker.end(), kvc(equalTo(new ElementKey(0)), value(MISSING, null, "(отсутствует)", null), sameInstance(checks1Missing)));
    }

    @Test
    public void expectedOneItem_gotOneItem() {
        IteratorMatcher.ElementChecker elementChecker = new ContainsInSpecifiedOrderChecker(Iterators.forArray(matcher1));
        verifyChecks(elementChecker.begin());
        verifyChecks(elementChecker.element(key1, value1), checks1);
        verifyChecks(elementChecker.end());
    }
}
