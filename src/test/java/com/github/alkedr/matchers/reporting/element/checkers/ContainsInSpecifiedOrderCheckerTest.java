package com.github.alkedr.matchers.reporting.element.checkers;

import com.github.alkedr.matchers.reporting.keys.Key;
import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;
import com.github.alkedr.matchers.reporting.reporters.SimpleTreeReporter;
import org.junit.Test;
import org.mockito.InOrder;

import static com.github.alkedr.matchers.reporting.ReportingMatchers.toReportingMatcher;
import static com.github.alkedr.matchers.reporting.element.checkers.IteratorMatcherElementCheckers.containsInSpecifiedOrderChecker;
import static com.github.alkedr.matchers.reporting.keys.Keys.elementKey;
import static com.github.alkedr.matchers.reporting.reporters.Reporters.simpleTreeReporterToSafeTreeReporter;
import static java.util.Collections.singleton;
import static org.hamcrest.Matchers.anything;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

public class ContainsInSpecifiedOrderCheckerTest {
    private final SimpleTreeReporter simpleTreeReporter = mock(SimpleTreeReporter.class);
    private final SafeTreeReporter safeTreeReporter = simpleTreeReporterToSafeTreeReporter(simpleTreeReporter);
    private final InOrder inOrder = inOrder(simpleTreeReporter);
    private final Key key1 = mock(Key.class);
    private final Object value1 = 1;

    @Test
    public void expectedEmpty_gotEmpty() {
        ElementChecker elementChecker = containsInSpecifiedOrderChecker();
        elementChecker.begin(safeTreeReporter);
        elementChecker.end(safeTreeReporter);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void expectedEmpty_gotOneItem() {
        ElementChecker elementChecker = containsInSpecifiedOrderChecker();

        elementChecker.begin(safeTreeReporter);
        inOrder.verifyNoMoreInteractions();

        elementChecker.element(key1, value1).accept(safeTreeReporter);
        inOrder.verify(simpleTreeReporter).incorrectlyPresent();
        inOrder.verifyNoMoreInteractions();

        elementChecker.end(safeTreeReporter);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void expectedOneItem_gotEmpty() {
        ElementChecker elementChecker = containsInSpecifiedOrderChecker(singleton(toReportingMatcher(anything("1"))));

        elementChecker.begin(safeTreeReporter);
        inOrder.verifyNoMoreInteractions();

        elementChecker.end(safeTreeReporter);
        inOrder.verify(simpleTreeReporter).beginMissingNode(elementKey(0));
        inOrder.verify(simpleTreeReporter).checkForMissingItem("1");
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void expectedOneItem_gotOneItem() {
        ElementChecker elementChecker = containsInSpecifiedOrderChecker(singleton(toReportingMatcher(anything("1"))));

        elementChecker.begin(safeTreeReporter);
        inOrder.verifyNoMoreInteractions();

        elementChecker.element(key1, value1).accept(safeTreeReporter);
        inOrder.verify(simpleTreeReporter).passedCheck("1");
        inOrder.verifyNoMoreInteractions();

        elementChecker.end(safeTreeReporter);
        inOrder.verifyNoMoreInteractions();
    }
}
