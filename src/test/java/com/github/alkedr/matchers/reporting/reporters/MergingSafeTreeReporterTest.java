package com.github.alkedr.matchers.reporting.reporters;

import com.github.alkedr.matchers.reporting.sub.value.keys.Key;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.function.Consumer;

import static com.github.alkedr.matchers.reporting.reporters.Reporters.mergingReporter;
import static com.github.alkedr.matchers.reporting.reporters.Reporters.simpleTreeReporterToSafeTreeReporter;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

// TODO: тесты на == для value, примитивные типы
public class MergingSafeTreeReporterTest {
    private final SimpleTreeReporter simpleTreeReporter = mock(SimpleTreeReporter.class);
    private final InOrder inOrder = inOrder(simpleTreeReporter);
    private final CloseableSafeTreeReporter mergingReporter = mergingReporter(simpleTreeReporterToSafeTreeReporter(simpleTreeReporter));
    private final Key key1 = mock(Key.class);
    private final Key key2 = mock(Key.class);
    private final Key key3 = mock(Key.class);
    private final Object value = new Object();
    private final Consumer<SafeTreeReporter> contents = mock(Consumer.class);
    private final Throwable throwable = new RuntimeException();

    @Test
    public void allReporterMethods_nodesFirst() {
        mergingReporter.presentNode(key1, value, contents);
        inOrder.verifyNoMoreInteractions();

        mergingReporter.missingNode(key2, contents);
        inOrder.verifyNoMoreInteractions();

        mergingReporter.brokenNode(key3, throwable, contents);
        inOrder.verifyNoMoreInteractions();

        mergingReporter.correctlyPresent();
        inOrder.verify(simpleTreeReporter).correctlyPresent();
        inOrder.verifyNoMoreInteractions();

        mergingReporter.correctlyMissing();
        inOrder.verify(simpleTreeReporter).correctlyMissing();
        inOrder.verifyNoMoreInteractions();

        mergingReporter.incorrectlyPresent();
        inOrder.verify(simpleTreeReporter).incorrectlyPresent();
        inOrder.verifyNoMoreInteractions();

        mergingReporter.incorrectlyMissing();
        inOrder.verify(simpleTreeReporter).incorrectlyMissing();
        inOrder.verifyNoMoreInteractions();

        String s1 = "1";
        mergingReporter.passedCheck(s1);
        inOrder.verify(simpleTreeReporter).passedCheck(same(s1));
        inOrder.verifyNoMoreInteractions();

        String s2 = "2";
        mergingReporter.failedCheck(s1, s2);
        inOrder.verify(simpleTreeReporter).failedCheck(same(s1), same(s2));
        inOrder.verifyNoMoreInteractions();

        mergingReporter.brokenCheck(s1, throwable);
        inOrder.verify(simpleTreeReporter).brokenCheck(same(s1), same(throwable));
        inOrder.verifyNoMoreInteractions();

        mergingReporter.checkForMissingItem(s1);
        inOrder.verify(simpleTreeReporter).checkForMissingItem(same(s1));
        inOrder.verifyNoMoreInteractions();

        mergingReporter.close();
        inOrder.verify(simpleTreeReporter).beginPresentNode(same(key1), same(value));
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verify(simpleTreeReporter).beginMissingNode(same(key2));
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verify(simpleTreeReporter).beginBrokenNode(same(key3), same(throwable));
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void unmergeable() {
        Object value2 = new Object();
        Throwable throwable2 = new RuntimeException();
        mergingReporter.presentNode(key1, value, contents);
        mergingReporter.presentNode(key1, value2, contents);
        mergingReporter.presentNode(key2, value, contents);
        mergingReporter.missingNode(key1, contents);
        mergingReporter.missingNode(key2, contents);
        mergingReporter.brokenNode(key1, throwable, contents);
        mergingReporter.brokenNode(key1, throwable2, contents);
        mergingReporter.brokenNode(key2, throwable, contents);
        mergingReporter.close();
        inOrder.verify(simpleTreeReporter).beginPresentNode(same(key1), same(value));
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verify(simpleTreeReporter).beginPresentNode(same(key1), same(value2));
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verify(simpleTreeReporter).beginPresentNode(same(key2), same(value));
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verify(simpleTreeReporter).beginMissingNode(same(key1));
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verify(simpleTreeReporter).beginMissingNode(same(key2));
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verify(simpleTreeReporter).beginBrokenNode(same(key1), same(throwable));
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verify(simpleTreeReporter).beginBrokenNode(same(key1), same(throwable2));
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verify(simpleTreeReporter).beginBrokenNode(same(key2), same(throwable));
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void recursiveMerge() {
        mergingReporter.presentNode(key1, value, r -> r.presentNode(key1, value, SafeTreeReporter::correctlyPresent));
        mergingReporter.presentNode(key1, value, r -> r.presentNode(key1, value, SafeTreeReporter::correctlyMissing));
        mergingReporter.missingNode(key1, r -> r.missingNode(key1, SafeTreeReporter::correctlyPresent));
        mergingReporter.missingNode(key1, r -> r.missingNode(key1, SafeTreeReporter::correctlyMissing));
        mergingReporter.brokenNode(key1, throwable, r -> r.brokenNode(key1, throwable, SafeTreeReporter::correctlyPresent));
        mergingReporter.brokenNode(key1, throwable, r -> r.brokenNode(key1, throwable, SafeTreeReporter::correctlyMissing));
        mergingReporter.close();
        inOrder.verify(simpleTreeReporter, times(2)).beginPresentNode(same(key1), same(value));
        inOrder.verify(simpleTreeReporter).correctlyPresent();
        inOrder.verify(simpleTreeReporter).correctlyMissing();
        inOrder.verify(simpleTreeReporter, times(2)).endNode();
        inOrder.verify(simpleTreeReporter, times(2)).beginMissingNode(same(key1));
        inOrder.verify(simpleTreeReporter).correctlyPresent();
        inOrder.verify(simpleTreeReporter).correctlyMissing();
        inOrder.verify(simpleTreeReporter, times(2)).endNode();
        inOrder.verify(simpleTreeReporter, times(2)).beginBrokenNode(same(key1), same(throwable));
        inOrder.verify(simpleTreeReporter).correctlyPresent();
        inOrder.verify(simpleTreeReporter).correctlyMissing();
        inOrder.verify(simpleTreeReporter, times(2)).endNode();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void closeTwice() {
        mergingReporter.presentNode(key1, value, contents);
        mergingReporter.close();
        mergingReporter.close();
        inOrder.verify(simpleTreeReporter).beginPresentNode(same(key1), same(value));
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verifyNoMoreInteractions();
    }
}
