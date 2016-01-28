package com.github.alkedr.matchers.reporting.sub.value.checkers;

import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;
import com.github.alkedr.matchers.reporting.reporters.SimpleTreeReporter;
import com.github.alkedr.matchers.reporting.sub.value.keys.Key;
import org.junit.Test;
import org.mockito.InOrder;

import static com.github.alkedr.matchers.reporting.ReportingMatchers.toReportingMatcher;
import static com.github.alkedr.matchers.reporting.reporters.Reporters.simpleTreeReporterToSafeTreeReporter;
import static com.github.alkedr.matchers.reporting.sub.value.checkers.SubValueCheckers.containsInSpecifiedOrder;
import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.elementKey;
import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

public class ContainsInAnyOrderSubValuesCheckerTest {
    private final SimpleTreeReporter simpleTreeReporter = mock(SimpleTreeReporter.class);
    private final SafeTreeReporter safeTreeReporter = simpleTreeReporterToSafeTreeReporter(simpleTreeReporter);
    private final InOrder inOrder = inOrder(simpleTreeReporter);
    private final Key key1 = mock(Key.class);
    private final Key key2 = mock(Key.class);

    @Test
    public void expectedEmpty_gotEmpty() {
        SubValuesChecker elementChecker = containsInSpecifiedOrder();
        elementChecker.begin(safeTreeReporter);
        elementChecker.end(safeTreeReporter);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void expectedEmpty_gotOneItem() {
        SubValuesChecker elementChecker = containsInSpecifiedOrder();

        elementChecker.begin(safeTreeReporter);
        inOrder.verifyNoMoreInteractions();

        elementChecker.present(key1, 1).accept(safeTreeReporter);
        inOrder.verify(simpleTreeReporter).incorrectlyPresent();
        inOrder.verifyNoMoreInteractions();

        elementChecker.end(safeTreeReporter);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void expectedOneItem_gotEmpty() {
        SubValuesChecker elementChecker = containsInSpecifiedOrder(singleton(toReportingMatcher(anything("1"))));

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
        SubValuesChecker elementChecker = containsInSpecifiedOrder(singleton(toReportingMatcher(anything("1"))));

        elementChecker.begin(safeTreeReporter);
        inOrder.verifyNoMoreInteractions();

        elementChecker.present(key1, 1).accept(safeTreeReporter);
        inOrder.verify(simpleTreeReporter).passedCheck("1");
        inOrder.verifyNoMoreInteractions();

        elementChecker.end(safeTreeReporter);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void expectedTwoItems_gotTwoItemsInDifferentOrder() {
        SubValuesChecker elementChecker = containsInSpecifiedOrder(asList(toReportingMatcher(equalTo(1)), toReportingMatcher(equalTo(2))));

        elementChecker.begin(safeTreeReporter);
        inOrder.verifyNoMoreInteractions();

        elementChecker.present(key1, 2).accept(safeTreeReporter);
        inOrder.verify(simpleTreeReporter).failedCheck("<1>", "was <2>");
        inOrder.verifyNoMoreInteractions();

        elementChecker.present(key2, 1).accept(safeTreeReporter);
        inOrder.verify(simpleTreeReporter).failedCheck("<2>", "was <1>");
        inOrder.verifyNoMoreInteractions();

        elementChecker.end(safeTreeReporter);
        inOrder.verifyNoMoreInteractions();
    }
}
