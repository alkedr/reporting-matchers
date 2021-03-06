package com.github.alkedr.matchers.reporting.sub.value.checkers;

import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;
import com.github.alkedr.matchers.reporting.reporters.SimpleTreeReporter;
import com.github.alkedr.matchers.reporting.sub.value.keys.Key;
import org.junit.Test;
import org.mockito.InOrder;

import static com.github.alkedr.matchers.reporting.ReportingMatchers.merge;
import static com.github.alkedr.matchers.reporting.ReportingMatchers.toReportingMatcher;
import static com.github.alkedr.matchers.reporting.reporters.Reporters.simpleTreeReporterToSafeTreeReporter;
import static com.github.alkedr.matchers.reporting.sub.value.checkers.SubValueCheckers.containsInSpecifiedOrder;
import static com.github.alkedr.matchers.reporting.sub.value.checkers.SubValueCheckers.containsMatchingInSpecifiedOrder;
import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.elementKey;
import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

public class ContainsInSpecifiedOrderSubValuesCheckerTest{
    private final SimpleTreeReporter simpleTreeReporter = mock(SimpleTreeReporter.class);
    private final SafeTreeReporter safeTreeReporter = simpleTreeReporterToSafeTreeReporter(simpleTreeReporter);
    private final InOrder inOrder = inOrder(simpleTreeReporter);
    private final Key key1 = mock(Key.class);
    private final Key key2 = mock(Key.class);

    @Test
    public void expectedEmpty_gotEmpty() {
        SubValuesChecker subValuesChecker = containsInSpecifiedOrder().get();
        subValuesChecker.begin(safeTreeReporter);
        subValuesChecker.end(safeTreeReporter);
        inOrder.verify(simpleTreeReporter).passedCheck("empty list");
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void expectedEmpty_gotOneItem() {
        SubValuesChecker subValuesChecker = containsInSpecifiedOrder().get();

        subValuesChecker.begin(safeTreeReporter);
        inOrder.verifyNoMoreInteractions();

        subValuesChecker.present(key1, 1).accept(safeTreeReporter);
        inOrder.verifyNoMoreInteractions();

        subValuesChecker.end(safeTreeReporter);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void expectedOneItem_gotEmpty() {
        SubValuesChecker subValuesChecker = containsMatchingInSpecifiedOrder(singleton(toReportingMatcher(anything("1")))).get();

        subValuesChecker.begin(safeTreeReporter);
        inOrder.verifyNoMoreInteractions();

        subValuesChecker.end(safeTreeReporter);
        inOrder.verify(simpleTreeReporter).beginAbsentNode(elementKey(0));
        inOrder.verify(simpleTreeReporter).checkForAbsentItem("1");
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void expectedOneItem_gotEmpty_matcherAllowsAbsent() {
        SubValuesChecker subValuesChecker = containsMatchingInSpecifiedOrder(singleton(merge())).get();

        subValuesChecker.begin(safeTreeReporter);
        inOrder.verifyNoMoreInteractions();

        subValuesChecker.end(safeTreeReporter);
        inOrder.verify(simpleTreeReporter).beginAbsentNode(elementKey(0));
        inOrder.verify(simpleTreeReporter).incorrectlyAbsent();
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void expectedOneItem_gotOneItem() {
        SubValuesChecker subValuesChecker = containsMatchingInSpecifiedOrder(singleton(toReportingMatcher(anything("1")))).get();

        subValuesChecker.begin(safeTreeReporter);
        inOrder.verifyNoMoreInteractions();

        subValuesChecker.present(key1, 1).accept(safeTreeReporter);
        inOrder.verify(simpleTreeReporter).passedCheck("1");
        inOrder.verifyNoMoreInteractions();

        subValuesChecker.end(safeTreeReporter);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void expectedTwoItems_gotTwoItemsInDifferentOrder() {
        SubValuesChecker subValuesChecker = containsMatchingInSpecifiedOrder(asList(toReportingMatcher(equalTo(1)), toReportingMatcher(equalTo(2)))).get();

        subValuesChecker.begin(safeTreeReporter);
        inOrder.verifyNoMoreInteractions();

        subValuesChecker.present(key1, 2).accept(safeTreeReporter);
        inOrder.verify(simpleTreeReporter).failedCheck("<1>", "was <2>");
        inOrder.verifyNoMoreInteractions();

        subValuesChecker.present(key2, 1).accept(safeTreeReporter);
        inOrder.verify(simpleTreeReporter).failedCheck("<2>", "was <1>");
        inOrder.verifyNoMoreInteractions();

        subValuesChecker.end(safeTreeReporter);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void expectedOneItem_gotOneAbsent() {
        SubValuesChecker subValuesChecker = containsMatchingInSpecifiedOrder(singleton(toReportingMatcher(anything("1")))).get();

        subValuesChecker.begin(safeTreeReporter);
        inOrder.verifyNoMoreInteractions();

        subValuesChecker.absent(key1).accept(safeTreeReporter);
        inOrder.verify(simpleTreeReporter).checkForAbsentItem("1");
        inOrder.verifyNoMoreInteractions();

        subValuesChecker.end(safeTreeReporter);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void expectedOneItem_gotOneBroken() {
        SubValuesChecker subValuesChecker = containsMatchingInSpecifiedOrder(singleton(toReportingMatcher(anything("1")))).get();

        subValuesChecker.begin(safeTreeReporter);
        inOrder.verifyNoMoreInteractions();

        subValuesChecker.broken(key1, new RuntimeException()).accept(safeTreeReporter);
        inOrder.verify(simpleTreeReporter).checkForAbsentItem("1");
        inOrder.verifyNoMoreInteractions();

        subValuesChecker.end(safeTreeReporter);
        inOrder.verifyNoMoreInteractions();
    }
}
