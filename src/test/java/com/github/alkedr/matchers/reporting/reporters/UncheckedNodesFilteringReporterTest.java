package com.github.alkedr.matchers.reporting.reporters;

import com.github.alkedr.matchers.reporting.sub.value.keys.Key;
import org.junit.Test;
import org.mockito.InOrder;

import static com.github.alkedr.matchers.reporting.reporters.Reporters.uncheckedNodesFilteringReporter;
import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.elementKey;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

public class UncheckedNodesFilteringReporterTest {
    private final SimpleTreeReporter next = mock(SimpleTreeReporter.class);
    private final InOrder inOrder = inOrder(next);
    private final Key key = elementKey(0);
    private final Object value = new Object();
    private final Throwable throwable = new RuntimeException();
    private final SimpleTreeReporter reporter = uncheckedNodesFilteringReporter(next);

    @Test
    public void emptyPresentNode() {
        reporter.beginPresentNode(key, value);
        reporter.endNode();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void notEmptyPresentNode() {
        reporter.beginPresentNode(key, value);
        reporter.correctlyPresent();
        reporter.endNode();
        inOrder.verify(next).beginPresentNode(same(key), same(value));
        inOrder.verify(next).correctlyPresent();
        inOrder.verify(next).endNode();
        inOrder.verifyNoMoreInteractions();
    }


    @Test
    public void emptyAbsentNode() {
        reporter.beginAbsentNode(key);
        reporter.endNode();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void notEmptyAbsentNode() {
        reporter.beginAbsentNode(key);
        reporter.correctlyAbsent();
        reporter.endNode();
        inOrder.verify(next).beginAbsentNode(same(key));
        inOrder.verify(next).correctlyAbsent();
        inOrder.verify(next).endNode();
        inOrder.verifyNoMoreInteractions();
    }


    @Test
    public void emptyBrokenNode() {
        reporter.beginBrokenNode(key, throwable);
        reporter.endNode();
        inOrder.verify(next).beginBrokenNode(same(key), same(throwable));
        inOrder.verify(next).endNode();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void notEmptyBrokenNode() {
        reporter.beginBrokenNode(key, throwable);
        reporter.correctlyPresent();
        reporter.endNode();
        inOrder.verify(next).beginBrokenNode(same(key), same(throwable));
        inOrder.verify(next).correctlyPresent();
        inOrder.verify(next).endNode();
        inOrder.verifyNoMoreInteractions();
    }


    @Test
    public void threeLevelsOfUnchecked() {
        reporter.beginPresentNode(key, value);
        reporter.beginPresentNode(key, value);
        reporter.beginPresentNode(key, value);
        reporter.endNode();
        reporter.endNode();
        reporter.endNode();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void threeLevelsOfChecked() {
        Object value1 = new Object();
        Object value2 = new Object();
        Object value3 = new Object();
        reporter.beginPresentNode(key, value1);
        reporter.beginPresentNode(key, value2);
        reporter.beginPresentNode(key, value3);
        reporter.correctlyPresent();
        reporter.endNode();
        reporter.endNode();
        reporter.endNode();
        inOrder.verify(next).beginPresentNode(same(key), same(value1));
        inOrder.verify(next).beginPresentNode(same(key), same(value2));
        inOrder.verify(next).beginPresentNode(same(key), same(value3));
        inOrder.verify(next).correctlyPresent();
        inOrder.verify(next, times(3)).endNode();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void twoConsecutiveCheckedPresentNodes() {
        Object value1 = new Object();
        Object value2 = new Object();
        reporter.beginPresentNode(key, value1);
        reporter.correctlyPresent();
        reporter.endNode();
        reporter.beginPresentNode(key, value2);
        reporter.correctlyPresent();
        reporter.endNode();
        inOrder.verify(next).beginPresentNode(same(key), same(value1));
        inOrder.verify(next).correctlyPresent();
        inOrder.verify(next).endNode();
        inOrder.verify(next).beginPresentNode(same(key), same(value2));
        inOrder.verify(next).correctlyPresent();
        inOrder.verify(next).endNode();
        inOrder.verifyNoMoreInteractions();
    }


    @Test
    public void presentNode_correctlyAbsent() {
        reporter.beginPresentNode(key, value);
        reporter.correctlyAbsent();
        reporter.endNode();
        inOrder.verify(next).beginPresentNode(same(key), same(value));
        inOrder.verify(next).correctlyAbsent();
        inOrder.verify(next).endNode();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void presentNode_incorrectlyPresent() {
        reporter.beginPresentNode(key, value);
        reporter.incorrectlyPresent();
        reporter.endNode();
        inOrder.verify(next).beginPresentNode(same(key), same(value));
        inOrder.verify(next).incorrectlyPresent();
        inOrder.verify(next).endNode();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void presentNode_incorrectlyAbsent() {
        reporter.beginPresentNode(key, value);
        reporter.incorrectlyAbsent();
        reporter.endNode();
        inOrder.verify(next).beginPresentNode(same(key), same(value));
        inOrder.verify(next).incorrectlyAbsent();
        inOrder.verify(next).endNode();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void presentNode_passedCheck() {
        reporter.beginPresentNode(key, value);
        reporter.passedCheck("1");
        reporter.endNode();
        inOrder.verify(next).beginPresentNode(same(key), same(value));
        inOrder.verify(next).passedCheck("1");
        inOrder.verify(next).endNode();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void presentNode_failedCheck() {
        reporter.beginPresentNode(key, value);
        reporter.failedCheck("1", "2");
        reporter.endNode();
        inOrder.verify(next).beginPresentNode(same(key), same(value));
        inOrder.verify(next).failedCheck("1", "2");
        inOrder.verify(next).endNode();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void presentNode_checkForAbsentItem() {
        reporter.beginPresentNode(key, value);
        reporter.checkForAbsentItem("1");
        reporter.endNode();
        inOrder.verify(next).beginPresentNode(same(key), same(value));
        inOrder.verify(next).checkForAbsentItem("1");
        inOrder.verify(next).endNode();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void presentNode_brokenCheck() {
        reporter.beginPresentNode(key, value);
        reporter.brokenCheck("1", throwable);
        reporter.endNode();
        inOrder.verify(next).beginPresentNode(same(key), same(value));
        inOrder.verify(next).brokenCheck("1", throwable);
        inOrder.verify(next).endNode();
        inOrder.verifyNoMoreInteractions();
    }
}
