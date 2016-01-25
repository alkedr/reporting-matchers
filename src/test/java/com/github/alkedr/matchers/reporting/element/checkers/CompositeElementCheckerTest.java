package com.github.alkedr.matchers.reporting.element.checkers;

import com.github.alkedr.matchers.reporting.keys.Key;
import com.github.alkedr.matchers.reporting.reporters.FlatReporter;
import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;
import com.github.alkedr.matchers.reporting.reporters.SimpleTreeReporter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.function.Consumer;

import static com.github.alkedr.matchers.reporting.element.checkers.ElementCheckers.compositeElementChecker;
import static com.github.alkedr.matchers.reporting.reporters.Reporters.simpleTreeReporterToSafeTreeReporter;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CompositeElementCheckerTest {
    private final SimpleTreeReporter simpleTreeReporter = mock(SimpleTreeReporter.class);
    private final SafeTreeReporter safeTreeReporter = simpleTreeReporterToSafeTreeReporter(simpleTreeReporter);
    private final Key key = mock(Key.class);
    private final Object value = new Object();
    private final ElementChecker elementChecker1 = mock(ElementChecker.class);
    private final ElementChecker elementChecker2 = mock(ElementChecker.class);
    private final InOrder inOrder = inOrder(simpleTreeReporter, elementChecker1, elementChecker2);

    @Before
    public void setUp() {
        when(elementChecker1.element(key, value)).thenReturn(FlatReporter::correctlyPresent);
        when(elementChecker2.element(key, value)).thenReturn(FlatReporter::incorrectlyPresent);
    }

    @Test
    public void begin() {
        compositeElementChecker(elementChecker1, elementChecker2).begin(safeTreeReporter);
        inOrder.verify(elementChecker1).begin(same(safeTreeReporter));
        inOrder.verify(elementChecker2).begin(same(safeTreeReporter));
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void element() {
        Consumer<SafeTreeReporter> result = compositeElementChecker(elementChecker1, elementChecker2).element(key, value);
        inOrder.verify(elementChecker1).element(same(key), same(value));
        inOrder.verify(elementChecker2).element(same(key), same(value));
        inOrder.verifyNoMoreInteractions();
        result.accept(safeTreeReporter);
        inOrder.verify(simpleTreeReporter).correctlyPresent();
        inOrder.verify(simpleTreeReporter).incorrectlyPresent();
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
