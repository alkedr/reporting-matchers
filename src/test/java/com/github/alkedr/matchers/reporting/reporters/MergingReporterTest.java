package com.github.alkedr.matchers.reporting.reporters;

import com.github.alkedr.matchers.reporting.keys.Key;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.function.Consumer;

import static com.github.alkedr.matchers.reporting.ReporterNodeContentsMatchers.contentsThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

public class MergingReporterTest {
    private final Reporter reporter = mock(Reporter.class);
    private final InOrder inOrder = inOrder(reporter);
    private final MergingReporter mergingReporter = new MergingReporter(reporter);
    private final Key key1 = mock(Key.class);
    private final Key key2 = mock(Key.class);
    private final Key key3 = mock(Key.class);
    private final Object value = new Object();
    private final Consumer<Reporter> contents = mock(Consumer.class);
    private final Throwable throwable = new RuntimeException();
    private final String s1 = "1";
    private final String s2 = "1";

    @Test
    public void allReporterMethods_nodesFirst() {
        mergingReporter.presentNode(key1, value, contents);
        inOrder.verifyNoMoreInteractions();

        mergingReporter.missingNode(key2, contents);
        inOrder.verifyNoMoreInteractions();

        mergingReporter.brokenNode(key3, throwable, contents);
        inOrder.verifyNoMoreInteractions();

        mergingReporter.correctlyPresent();
        inOrder.verify(reporter).correctlyPresent();
        inOrder.verifyNoMoreInteractions();

        mergingReporter.correctlyMissing();
        inOrder.verify(reporter).correctlyMissing();
        inOrder.verifyNoMoreInteractions();

        mergingReporter.incorrectlyPresent();
        inOrder.verify(reporter).incorrectlyPresent();
        inOrder.verifyNoMoreInteractions();

        mergingReporter.incorrectlyMissing();
        inOrder.verify(reporter).incorrectlyMissing();
        inOrder.verifyNoMoreInteractions();

        mergingReporter.passedCheck(s1);
        inOrder.verify(reporter).passedCheck(same(s1));
        inOrder.verifyNoMoreInteractions();

        mergingReporter.failedCheck(s1, s2);
        inOrder.verify(reporter).failedCheck(same(s1), same(s2));
        inOrder.verifyNoMoreInteractions();

        mergingReporter.brokenCheck(s1, throwable);
        inOrder.verify(reporter).brokenCheck(same(s1), same(throwable));
        inOrder.verifyNoMoreInteractions();

        mergingReporter.checkForMissingItem(s1);
        inOrder.verify(reporter).checkForMissingItem(same(s1));
        inOrder.verifyNoMoreInteractions();

        mergingReporter.close();
        inOrder.verify(reporter).presentNode(same(key1), same(value), any());
        inOrder.verify(reporter).missingNode(same(key2), any());
        inOrder.verify(reporter).brokenNode(same(key3), same(throwable), any());
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
        inOrder.verify(reporter).presentNode(same(key1), same(value), any());
        inOrder.verify(reporter).presentNode(same(key1), same(value2), any());
        inOrder.verify(reporter).presentNode(same(key2), same(value), any());
        inOrder.verify(reporter).missingNode(same(key1), any());
        inOrder.verify(reporter).missingNode(same(key2), any());
        inOrder.verify(reporter).brokenNode(same(key1), same(throwable), any());
        inOrder.verify(reporter).brokenNode(same(key1), same(throwable2), any());
        inOrder.verify(reporter).brokenNode(same(key2), same(throwable), any());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void recursiveMerge() {
        mergingReporter.presentNode(key1, value, r -> r.presentNode(key1, value, Reporter::correctlyPresent));
        mergingReporter.presentNode(key1, value, r -> r.presentNode(key1, value, Reporter::correctlyMissing));
        mergingReporter.missingNode(key1, r -> r.missingNode(key1, Reporter::correctlyPresent));
        mergingReporter.missingNode(key1, r -> r.missingNode(key1, Reporter::correctlyMissing));
        mergingReporter.brokenNode(key1, throwable, r -> r.brokenNode(key1, throwable, Reporter::correctlyPresent));
        mergingReporter.brokenNode(key1, throwable, r -> r.brokenNode(key1, throwable, Reporter::correctlyMissing));
        mergingReporter.close();
        inOrder.verify(reporter).presentNode(same(key1), same(value), contentsThat((inOrder2, reporter2) -> {
            inOrder2.verify(reporter2).presentNode(same(key1), same(value), contentsThat((inOrder3, reporter3) -> {
                inOrder3.verify(reporter3).correctlyPresent();
                inOrder3.verify(reporter3).correctlyMissing();
            }));
        }));
        inOrder.verify(reporter).missingNode(same(key1), contentsThat((inOrder2, reporter2) -> {
            inOrder2.verify(reporter2).missingNode(same(key1), contentsThat((inOrder3, reporter3) -> {
                inOrder3.verify(reporter3).correctlyPresent();
                inOrder3.verify(reporter3).correctlyMissing();
            }));
        }));
        inOrder.verify(reporter).brokenNode(same(key1), same(throwable), contentsThat((inOrder2, reporter2) -> {
            inOrder2.verify(reporter2).brokenNode(same(key1), same(throwable), contentsThat((inOrder3, reporter3) -> {
                inOrder3.verify(reporter3).correctlyPresent();
                inOrder3.verify(reporter3).correctlyMissing();
            }));
        }));
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void closeTwice() {
        mergingReporter.presentNode(key1, value, contents);
        mergingReporter.close();
        mergingReporter.close();
        inOrder.verify(reporter).presentNode(same(key1), same(value), any());
        inOrder.verifyNoMoreInteractions();
    }

    // TODO: тесты на == для value, примитивные типы
}
