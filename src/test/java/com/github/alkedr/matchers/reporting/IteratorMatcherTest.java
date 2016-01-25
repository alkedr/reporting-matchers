package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.element.checkers.ElementChecker;
import com.github.alkedr.matchers.reporting.keys.Key;
import com.github.alkedr.matchers.reporting.reporters.FlatReporter;
import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;
import com.github.alkedr.matchers.reporting.reporters.SimpleTreeReporter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import static com.github.alkedr.matchers.reporting.keys.Keys.elementKey;
import static com.github.alkedr.matchers.reporting.reporters.Reporters.simpleTreeReporterToSafeTreeReporter;
import static java.util.Arrays.asList;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IteratorMatcherTest {
    private final SimpleTreeReporter simpleTreeReporter = mock(SimpleTreeReporter.class);
    private final SafeTreeReporter safeTreeReporter = simpleTreeReporterToSafeTreeReporter(simpleTreeReporter);
    private final Object element1 = new Object();
    private final Object element2 = new Object();
    private final Iterable<Object> iterable = asList(element1, element2);
    private final ElementChecker elementChecker = mock(ElementChecker.class);
    private final Key key1 = elementKey(0);
    private final Key key2 = elementKey(1);

    @Before
    public void setUp() {
        doAnswer(invocation -> {
            ((FlatReporter) invocation.getArguments()[0]).correctlyPresent();
            return null;
        }).when(elementChecker).begin(safeTreeReporter);

        when(elementChecker.element(any(), any()))
                .thenReturn(FlatReporter::incorrectlyPresent)
                .thenReturn(FlatReporter::incorrectlyMissing)
                .thenReturn(null);

        doAnswer(invocation -> {
            ((FlatReporter) invocation.getArguments()[0]).correctlyMissing();
            return null;
        }).when(elementChecker).end(safeTreeReporter);
    }


    // TODO: item == null, item.getClass() != Iterator.class

    @Test
    public void run() {
        new IteratorMatcher<>(() -> elementChecker).run(iterable.iterator(), safeTreeReporter);
        InOrder inOrder = inOrder(simpleTreeReporter);
        inOrder.verify(simpleTreeReporter).correctlyPresent();
        inOrder.verify(simpleTreeReporter).beginPresentNode(eq(key1), same(element1));
        inOrder.verify(simpleTreeReporter).incorrectlyPresent();
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verify(simpleTreeReporter).beginPresentNode(eq(key2), same(element2));
        inOrder.verify(simpleTreeReporter).incorrectlyMissing();
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verify(simpleTreeReporter).correctlyMissing();
        inOrder.verifyNoMoreInteractions();
    }


    @Test
    public void runForMissingItem() {
        new IteratorMatcher<>(() -> elementChecker).runForMissingItem(safeTreeReporter);
        InOrder inOrder = inOrder(simpleTreeReporter);
        inOrder.verify(simpleTreeReporter).correctlyPresent();
        inOrder.verify(simpleTreeReporter).correctlyMissing();
        inOrder.verifyNoMoreInteractions();
    }



    @Test
    public void run_elementCheckerMethodsCallOrder() {
        new IteratorMatcher<>(() -> elementChecker).run(iterable.iterator(), safeTreeReporter);
        InOrder inOrder = inOrder(elementChecker);
        inOrder.verify(elementChecker).begin(same(safeTreeReporter));
        inOrder.verify(elementChecker).element(eq(key1), same(element1));
        inOrder.verify(elementChecker).element(eq(key2), same(element2));
        inOrder.verify(elementChecker).end(same(safeTreeReporter));
        inOrder.verifyNoMoreInteractions();
    }


    @Test
    public void runForMissingItem_elementCheckerMethodsCallOrder() {
        new IteratorMatcher<>(() -> elementChecker).runForMissingItem(safeTreeReporter);
        InOrder inOrder = inOrder(elementChecker);
        inOrder.verify(elementChecker).begin(same(safeTreeReporter));
        inOrder.verify(elementChecker).end(same(safeTreeReporter));
        inOrder.verifyNoMoreInteractions();
    }
}
