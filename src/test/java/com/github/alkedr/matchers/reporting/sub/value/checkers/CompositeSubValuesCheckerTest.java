package com.github.alkedr.matchers.reporting.sub.value.checkers;

import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;
import com.github.alkedr.matchers.reporting.reporters.SimpleTreeReporter;
import com.github.alkedr.matchers.reporting.sub.value.keys.Key;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.function.Consumer;

import static com.github.alkedr.matchers.reporting.reporters.Reporters.simpleTreeReporterToSafeTreeReporter;
import static com.github.alkedr.matchers.reporting.sub.value.checkers.SubValueCheckers.compositeElementChecker;
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
    private final SubValuesChecker elementChecker1 = mock(SubValuesChecker.class);
    private final SubValuesChecker elementChecker2 = mock(SubValuesChecker.class);
    private final InOrder inOrder = inOrder(simpleTreeReporter, elementChecker1, elementChecker2);

    @Before
    public void setUp() {
        when(elementChecker1.present(key, value)).thenReturn(reporter -> reporter.passedCheck("11"));
        when(elementChecker2.present(key, value)).thenReturn(reporter -> reporter.passedCheck("12"));
        when(elementChecker1.absent(key)).thenReturn(reporter -> reporter.passedCheck("21"));
        when(elementChecker2.absent(key)).thenReturn(reporter -> reporter.passedCheck("22"));
        when(elementChecker1.broken(key, throwable)).thenReturn(reporter -> reporter.passedCheck("31"));
        when(elementChecker2.broken(key, throwable)).thenReturn(reporter -> reporter.passedCheck("32"));
    }

    @Test
    public void begin() {
        compositeElementChecker(elementChecker1, elementChecker2).begin(safeTreeReporter);
        inOrder.verify(elementChecker1).begin(same(safeTreeReporter));
        inOrder.verify(elementChecker2).begin(same(safeTreeReporter));
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void present() {
        Consumer<SafeTreeReporter> result = compositeElementChecker(elementChecker1, elementChecker2).present(key, value);
        inOrder.verify(elementChecker1).present(same(key), same(value));
        inOrder.verify(elementChecker2).present(same(key), same(value));
        inOrder.verifyNoMoreInteractions();
        result.accept(safeTreeReporter);
        inOrder.verify(simpleTreeReporter).passedCheck("11");
        inOrder.verify(simpleTreeReporter).passedCheck("12");
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void absent() {
        Consumer<SafeTreeReporter> result = compositeElementChecker(elementChecker1, elementChecker2).absent(key);
        inOrder.verify(elementChecker1).absent(same(key));
        inOrder.verify(elementChecker2).absent(same(key));
        inOrder.verifyNoMoreInteractions();
        result.accept(safeTreeReporter);
        inOrder.verify(simpleTreeReporter).passedCheck("21");
        inOrder.verify(simpleTreeReporter).passedCheck("22");
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void broken() {
        Consumer<SafeTreeReporter> result = compositeElementChecker(elementChecker1, elementChecker2).broken(key, throwable);
        inOrder.verify(elementChecker1).broken(same(key), same(throwable));
        inOrder.verify(elementChecker2).broken(same(key), same(throwable));
        inOrder.verifyNoMoreInteractions();
        result.accept(safeTreeReporter);
        inOrder.verify(simpleTreeReporter).passedCheck("31");
        inOrder.verify(simpleTreeReporter).passedCheck("32");
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void end() {
        compositeElementChecker(elementChecker1, elementChecker2).end(safeTreeReporter);
        inOrder.verify(elementChecker1).end(same(safeTreeReporter));
        inOrder.verify(elementChecker2).end(same(safeTreeReporter));
        inOrder.verifyNoMoreInteractions();
    }
}
