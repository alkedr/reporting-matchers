package com.github.alkedr.matchers.reporting.reporters;

import com.github.alkedr.matchers.reporting.keys.Key;
import org.junit.After;
import org.junit.Test;
import org.mockito.InOrder;

import static com.github.alkedr.matchers.reporting.reporters.Reporters.compositeSimpleTreeReporter;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

public class CompositeSimpleTreeReporterTest {
    private final SimpleTreeReporter reporter1 = mock(SimpleTreeReporter.class);
    private final SimpleTreeReporter reporter2 = mock(SimpleTreeReporter.class);
    private final InOrder inOrder = inOrder(reporter1, reporter2);
    private final SimpleTreeReporter compositeReporter = compositeSimpleTreeReporter(reporter1, reporter2);
    private final Key key = mock(Key.class);
    private final Object value = new Object();
    private final Throwable throwable = new RuntimeException();
    private static final String S1 = "1";
    private static final String S2 = "2";

    @After
    public void tearDown() {
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void beginPresentNode() {
        compositeReporter.beginPresentNode(key, value);
        inOrder.verify(reporter1).beginPresentNode(same(key), same(value));
        inOrder.verify(reporter2).beginPresentNode(same(key), same(value));
    }

    @Test
    public void beginMissingNode() {
        compositeReporter.beginMissingNode(key);
        inOrder.verify(reporter1).beginMissingNode(same(key));
        inOrder.verify(reporter2).beginMissingNode(same(key));
    }

    @Test
    public void beginBrokenNode() {
        compositeReporter.beginBrokenNode(key, throwable);
        inOrder.verify(reporter1).beginBrokenNode(same(key), same(throwable));
        inOrder.verify(reporter2).beginBrokenNode(same(key), same(throwable));
    }

    @Test
    public void endNode() {
        compositeReporter.endNode();
        inOrder.verify(reporter1).endNode();
        inOrder.verify(reporter2).endNode();
    }

    @Test
    public void correctlyPresent() {
        compositeReporter.correctlyPresent();
        inOrder.verify(reporter1).correctlyPresent();
        inOrder.verify(reporter2).correctlyPresent();
    }

    @Test
    public void correctlyMissing() {
        compositeReporter.correctlyMissing();
        inOrder.verify(reporter1).correctlyMissing();
        inOrder.verify(reporter2).correctlyMissing();
    }

    @Test
    public void incorrectlyPresent() {
        compositeReporter.incorrectlyPresent();
        inOrder.verify(reporter1).incorrectlyPresent();
        inOrder.verify(reporter2).incorrectlyPresent();
    }

    @Test
    public void incorrectlyMissing() {
        compositeReporter.incorrectlyMissing();
        inOrder.verify(reporter1).incorrectlyMissing();
        inOrder.verify(reporter2).incorrectlyMissing();
    }

    @Test
    public void passedCheck() {
        compositeReporter.passedCheck(S1);
        inOrder.verify(reporter1).passedCheck(same(S1));
        inOrder.verify(reporter2).passedCheck(same(S1));
    }

    @Test
    public void failedCheck() {
        compositeReporter.failedCheck(S1, S2);
        inOrder.verify(reporter1).failedCheck(same(S1), same(S2));
        inOrder.verify(reporter2).failedCheck(same(S1), same(S2));
    }

    @Test
    public void checkForMissingItem() {
        compositeReporter.checkForMissingItem(S1);
        inOrder.verify(reporter1).checkForMissingItem(same(S1));
        inOrder.verify(reporter2).checkForMissingItem(same(S1));
    }

    @Test
    public void brokenCheck() {
        compositeReporter.brokenCheck(S1, throwable);
        inOrder.verify(reporter1).brokenCheck(same(S1), same(throwable));
        inOrder.verify(reporter2).brokenCheck(same(S1), same(throwable));
    }
}
