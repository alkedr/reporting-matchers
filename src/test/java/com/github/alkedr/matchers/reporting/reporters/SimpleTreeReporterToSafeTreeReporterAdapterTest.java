package com.github.alkedr.matchers.reporting.reporters;

import com.github.alkedr.matchers.reporting.keys.Key;
import org.junit.After;
import org.junit.Test;
import org.mockito.InOrder;

import static com.github.alkedr.matchers.reporting.reporters.Reporters.simpleTreeReporterToSafeTreeReporter;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

public class SimpleTreeReporterToSafeTreeReporterAdapterTest {
    private final SimpleTreeReporter simpleTreeReporter = mock(SimpleTreeReporter.class);
    private final SafeTreeReporter safeTreeReporter = simpleTreeReporterToSafeTreeReporter(simpleTreeReporter);
    private final InOrder inOrder = inOrder(simpleTreeReporter);
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
    public void presentNode() {
        safeTreeReporter.presentNode(key, value, FlatReporter::correctlyPresent);
        inOrder.verify(simpleTreeReporter).beginPresentNode(same(key), same(value));
        inOrder.verify(simpleTreeReporter).correctlyPresent();
        inOrder.verify(simpleTreeReporter).endNode();
    }

    @Test
    public void missingNode() {
        safeTreeReporter.missingNode(key, FlatReporter::correctlyPresent);
        inOrder.verify(simpleTreeReporter).beginMissingNode(same(key));
        inOrder.verify(simpleTreeReporter).correctlyPresent();
        inOrder.verify(simpleTreeReporter).endNode();
    }

    @Test
    public void brokenNode() {
        safeTreeReporter.brokenNode(key, throwable, FlatReporter::correctlyPresent);
        inOrder.verify(simpleTreeReporter).beginBrokenNode(same(key), same(throwable));
        inOrder.verify(simpleTreeReporter).correctlyPresent();
        inOrder.verify(simpleTreeReporter).endNode();
    }

    @Test
    public void correctlyPresent() {
        safeTreeReporter.correctlyPresent();
        inOrder.verify(simpleTreeReporter).correctlyPresent();
    }

    @Test
    public void correctlyMissing() {
        safeTreeReporter.correctlyMissing();
        inOrder.verify(simpleTreeReporter).correctlyMissing();
    }

    @Test
    public void incorrectlyPresent() {
        safeTreeReporter.incorrectlyPresent();
        inOrder.verify(simpleTreeReporter).incorrectlyPresent();
    }

    @Test
    public void incorrectlyMissing() {
        safeTreeReporter.incorrectlyMissing();
        inOrder.verify(simpleTreeReporter).incorrectlyMissing();
    }

    @Test
    public void passedCheck() {
        safeTreeReporter.passedCheck(S1);
        inOrder.verify(simpleTreeReporter).passedCheck(same(S1));
    }

    @Test
    public void failedCheck() {
        safeTreeReporter.failedCheck(S1, S2);
        inOrder.verify(simpleTreeReporter).failedCheck(same(S1), same(S2));
    }

    @Test
    public void checkForMissingItem() {
        safeTreeReporter.checkForMissingItem(S1);
        inOrder.verify(simpleTreeReporter).checkForMissingItem(same(S1));
    }

    @Test
    public void brokenCheck() {
        safeTreeReporter.brokenCheck(S1, throwable);
        inOrder.verify(simpleTreeReporter).brokenCheck(same(S1), same(throwable));
    }
}
