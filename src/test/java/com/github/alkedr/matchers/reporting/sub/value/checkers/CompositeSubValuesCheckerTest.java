package com.github.alkedr.matchers.reporting.sub.value.checkers;

import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;
import com.github.alkedr.matchers.reporting.reporters.SimpleTreeReporter;
import com.github.alkedr.matchers.reporting.sub.value.keys.Key;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.function.Consumer;

import static com.github.alkedr.matchers.reporting.reporters.Reporters.simpleTreeReporterToSafeTreeReporter;
import static com.github.alkedr.matchers.reporting.sub.value.checkers.SubValueCheckers.compositeSubValuesChecker;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CompositeSubValuesCheckerTest {
    private final SimpleTreeReporter simpleTreeReporter = mock(SimpleTreeReporter.class);
    private final SafeTreeReporter safeTreeReporter = simpleTreeReporterToSafeTreeReporter(simpleTreeReporter);
    private final Key key = mock(Key.class);
    private final Object value = new Object();
    private final Throwable throwable = new RuntimeException();
    private final SubValuesChecker subValuesChecker1 = mock(SubValuesChecker.class);
    private final SubValuesChecker subValuesChecker2 = mock(SubValuesChecker.class);
    private final InOrder inOrder = inOrder(simpleTreeReporter, subValuesChecker1, subValuesChecker2);

    @Before
    public void setUp() {
        when(subValuesChecker1.present(key, value)).thenReturn(reporter -> reporter.passedCheck("11"));
        when(subValuesChecker2.present(key, value)).thenReturn(reporter -> reporter.passedCheck("12"));
        when(subValuesChecker1.absent(key)).thenReturn(reporter -> reporter.passedCheck("21"));
        when(subValuesChecker2.absent(key)).thenReturn(reporter -> reporter.passedCheck("22"));
        when(subValuesChecker1.broken(key, throwable)).thenReturn(reporter -> reporter.passedCheck("31"));
        when(subValuesChecker2.broken(key, throwable)).thenReturn(reporter -> reporter.passedCheck("32"));
    }

    @Test
    public void begin() {
        compositeSubValuesChecker(subValuesChecker1, subValuesChecker2).begin(safeTreeReporter);
        inOrder.verify(subValuesChecker1).begin(same(safeTreeReporter));
        inOrder.verify(subValuesChecker2).begin(same(safeTreeReporter));
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void present() {
        Consumer<SafeTreeReporter> result = compositeSubValuesChecker(subValuesChecker1, subValuesChecker2).present(key, value);
        inOrder.verify(subValuesChecker1).present(same(key), same(value));
        inOrder.verify(subValuesChecker2).present(same(key), same(value));
        inOrder.verifyNoMoreInteractions();
        result.accept(safeTreeReporter);
        inOrder.verify(simpleTreeReporter).passedCheck("11");
        inOrder.verify(simpleTreeReporter).passedCheck("12");
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void absent() {
        Consumer<SafeTreeReporter> result = compositeSubValuesChecker(subValuesChecker1, subValuesChecker2).absent(key);
        inOrder.verify(subValuesChecker1).absent(same(key));
        inOrder.verify(subValuesChecker2).absent(same(key));
        inOrder.verifyNoMoreInteractions();
        result.accept(safeTreeReporter);
        inOrder.verify(simpleTreeReporter).passedCheck("21");
        inOrder.verify(simpleTreeReporter).passedCheck("22");
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void broken() {
        Consumer<SafeTreeReporter> result = compositeSubValuesChecker(subValuesChecker1, subValuesChecker2).broken(key, throwable);
        inOrder.verify(subValuesChecker1).broken(same(key), same(throwable));
        inOrder.verify(subValuesChecker2).broken(same(key), same(throwable));
        inOrder.verifyNoMoreInteractions();
        result.accept(safeTreeReporter);
        inOrder.verify(simpleTreeReporter).passedCheck("31");
        inOrder.verify(simpleTreeReporter).passedCheck("32");
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void end() {
        compositeSubValuesChecker(subValuesChecker1, subValuesChecker2).end(safeTreeReporter);
        inOrder.verify(subValuesChecker1).end(same(safeTreeReporter));
        inOrder.verify(subValuesChecker2).end(same(safeTreeReporter));
        inOrder.verifyNoMoreInteractions();
    }
}
