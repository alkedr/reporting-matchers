package com.github.alkedr.matchers.reporting.element.checkers;

import com.github.alkedr.matchers.reporting.keys.Key;
import com.github.alkedr.matchers.reporting.keys.Keys;
import com.github.alkedr.matchers.reporting.reporters.Reporter;
import com.google.common.collect.Iterators;
import org.junit.Test;
import org.mockito.InOrder;

import static com.github.alkedr.matchers.reporting.ReportingMatchers.toReportingMatcher;
import static com.github.alkedr.matchers.reporting.check.results.CheckResults.runAll;
import static org.hamcrest.CoreMatchers.anything;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

public class ContainsInSpecifiedOrderCheckerTest {
    private final Reporter reporter = mock(Reporter.class);
    private final InOrder inOrder = inOrder(reporter);
    private final Key key1 = mock(Key.class);
    private final Object value1 = 1;

    @Test
    public void expectedEmpty_gotEmpty() {
        ElementChecker elementChecker = IteratorMatcherElementCheckers.containsInSpecifiedOrderChecker(Iterators.forArray());
        runAll(elementChecker.begin(), reporter);
        runAll(elementChecker.end(), reporter);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void expectedEmpty_gotOneItem() {
        ElementChecker elementChecker = IteratorMatcherElementCheckers.containsInSpecifiedOrderChecker(Iterators.forArray());

        runAll(elementChecker.begin(), reporter);
        inOrder.verifyNoMoreInteractions();

        runAll(elementChecker.element(key1, value1), reporter);
        inOrder.verify(reporter).incorrectlyPresent();
        inOrder.verifyNoMoreInteractions();

        runAll(elementChecker.end(), reporter);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void expectedOneItem_gotEmpty() {
        ElementChecker elementChecker = IteratorMatcherElementCheckers.containsInSpecifiedOrderChecker(Iterators.forArray(toReportingMatcher(anything("1"))));

        runAll(elementChecker.begin(), reporter);
        inOrder.verifyNoMoreInteractions();

        runAll(elementChecker.end(), reporter);
        inOrder.verify(reporter).beginMissingNode(Keys.elementKey(0).asString());
        inOrder.verify(reporter).checkForMissingItem("1");
        inOrder.verify(reporter).endNode();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void expectedOneItem_gotOneItem() {
        ElementChecker elementChecker = IteratorMatcherElementCheckers.containsInSpecifiedOrderChecker(Iterators.forArray(toReportingMatcher(anything("1"))));

        runAll(elementChecker.begin(), reporter);
        inOrder.verifyNoMoreInteractions();

        runAll(elementChecker.element(key1, value1), reporter);
        inOrder.verify(reporter).passedCheck("1");
        inOrder.verifyNoMoreInteractions();

        runAll(elementChecker.end(), reporter);
        inOrder.verifyNoMoreInteractions();
    }
}
