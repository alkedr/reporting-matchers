package com.github.alkedr.matchers.reporting.reporters;

import com.github.alkedr.matchers.reporting.sub.value.keys.Key;
import org.junit.Test;
import org.mockito.InOrder;

import static com.github.alkedr.matchers.reporting.reporters.Reporters.simpleTreeReporterToSafeTreeReporter;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

public class UncheckedToIncorrectlyPresentConvertingReporterTest {
    private final SimpleTreeReporter simpleTreeReporter = mock(SimpleTreeReporter.class);
    private final SafeTreeReporter safeTreeReporter = simpleTreeReporterToSafeTreeReporter(simpleTreeReporter);
    private final SafeTreeReporter uncheckedToIncorrectlyPresentConvertingReporter = new UncheckedToIncorrectlyPresentConvertingReporter(safeTreeReporter);
    private final InOrder inOrder = inOrder(simpleTreeReporter);
    private final Key key = mock(Key.class);
    private final Key key2 = mock(Key.class);
    private final Object value = new Object();
    private final Object value2 = new Object();
    private final Throwable throwable = new RuntimeException();


    @Test
    public void presentNode() {
        uncheckedToIncorrectlyPresentConvertingReporter.presentNode(key, value, reporter -> {});
        inOrder.verify(simpleTreeReporter).beginPresentNode(key, value);
        inOrder.verify(simpleTreeReporter).incorrectlyPresent();
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void absentNode() {
        uncheckedToIncorrectlyPresentConvertingReporter.absentNode(key, reporter -> {});
        inOrder.verify(simpleTreeReporter).beginAbsentNode(key);
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void brokenNode() {
        uncheckedToIncorrectlyPresentConvertingReporter.brokenNode(key, throwable, reporter -> {});
        inOrder.verify(simpleTreeReporter).beginBrokenNode(key, throwable);
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void correctlyPresent() {
        uncheckedToIncorrectlyPresentConvertingReporter.correctlyPresent();
        inOrder.verify(simpleTreeReporter).correctlyPresent();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void correctlyAbsent() {
        uncheckedToIncorrectlyPresentConvertingReporter.correctlyAbsent();
        inOrder.verify(simpleTreeReporter).correctlyAbsent();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void incorrectlyPresent() {
        uncheckedToIncorrectlyPresentConvertingReporter.incorrectlyPresent();
        inOrder.verify(simpleTreeReporter).incorrectlyPresent();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void incorrectlyAbsent() {
        uncheckedToIncorrectlyPresentConvertingReporter.incorrectlyAbsent();
        inOrder.verify(simpleTreeReporter).incorrectlyAbsent();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void passedCheck() {
        uncheckedToIncorrectlyPresentConvertingReporter.passedCheck("1");
        inOrder.verify(simpleTreeReporter).passedCheck("1");
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void failedCheck() {
        uncheckedToIncorrectlyPresentConvertingReporter.failedCheck("1", "2");
        inOrder.verify(simpleTreeReporter).failedCheck("1", "2");
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void checkForAbsentItem() {
        uncheckedToIncorrectlyPresentConvertingReporter.checkForAbsentItem("1");
        inOrder.verify(simpleTreeReporter).checkForAbsentItem("1");
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void brokenCheck() {
        uncheckedToIncorrectlyPresentConvertingReporter.brokenCheck("1", throwable);
        inOrder.verify(simpleTreeReporter).brokenCheck("1", throwable);
        inOrder.verifyNoMoreInteractions();
    }



    @Test
    public void presentNode_presentNode() {
        uncheckedToIncorrectlyPresentConvertingReporter.presentNode(key, value, reporter -> reporter.presentNode(key2, value2, reporter2 -> {}));
        inOrder.verify(simpleTreeReporter).beginPresentNode(key, value);
        inOrder.verify(simpleTreeReporter).beginPresentNode(key2, value2);
        inOrder.verify(simpleTreeReporter).incorrectlyPresent();
        inOrder.verify(simpleTreeReporter, times(2)).endNode();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void presentNode_absentNode() {
        uncheckedToIncorrectlyPresentConvertingReporter.presentNode(key, value, reporter -> reporter.absentNode(key2, reporter2 -> {}));
        inOrder.verify(simpleTreeReporter).beginPresentNode(key, value);
        inOrder.verify(simpleTreeReporter).beginAbsentNode(key2);
        inOrder.verify(simpleTreeReporter, times(2)).endNode();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void presentNode_brokenNode() {
        uncheckedToIncorrectlyPresentConvertingReporter.presentNode(key, value, reporter -> reporter.brokenNode(key2, throwable, reporter2 -> {}));
        inOrder.verify(simpleTreeReporter).beginPresentNode(key, value);
        inOrder.verify(simpleTreeReporter).beginBrokenNode(key2, throwable);
        inOrder.verify(simpleTreeReporter, times(2)).endNode();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void presentNode_correctlyPresent() {
        uncheckedToIncorrectlyPresentConvertingReporter.presentNode(key, value, FlatReporter::correctlyPresent);
        inOrder.verify(simpleTreeReporter).beginPresentNode(key, value);
        inOrder.verify(simpleTreeReporter).correctlyPresent();
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void presentNode_correctlyAbsent() {
        uncheckedToIncorrectlyPresentConvertingReporter.presentNode(key, value, FlatReporter::correctlyAbsent);
        inOrder.verify(simpleTreeReporter).beginPresentNode(key, value);
        inOrder.verify(simpleTreeReporter).correctlyAbsent();
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void presentNode_incorrectlyPresent() {
        uncheckedToIncorrectlyPresentConvertingReporter.presentNode(key, value, FlatReporter::incorrectlyPresent);
        inOrder.verify(simpleTreeReporter).beginPresentNode(key, value);
        inOrder.verify(simpleTreeReporter).incorrectlyPresent();
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void presentNode_incorrectlyAbsent() {
        uncheckedToIncorrectlyPresentConvertingReporter.presentNode(key, value, FlatReporter::incorrectlyAbsent);
        inOrder.verify(simpleTreeReporter).beginPresentNode(key, value);
        inOrder.verify(simpleTreeReporter).incorrectlyAbsent();
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void presentNode_passedCheck() {
        uncheckedToIncorrectlyPresentConvertingReporter.presentNode(key, value, reporter -> reporter.passedCheck("1"));
        inOrder.verify(simpleTreeReporter).beginPresentNode(key, value);
        inOrder.verify(simpleTreeReporter).passedCheck("1");
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void presentNode_failedCheck() {
        uncheckedToIncorrectlyPresentConvertingReporter.presentNode(key, value, reporter -> reporter.failedCheck("1", "2"));
        inOrder.verify(simpleTreeReporter).beginPresentNode(key, value);
        inOrder.verify(simpleTreeReporter).failedCheck("1", "2");
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void presentNode_checkForAbsentItem() {
        uncheckedToIncorrectlyPresentConvertingReporter.presentNode(key, value, reporter -> reporter.checkForAbsentItem("1"));
        inOrder.verify(simpleTreeReporter).beginPresentNode(key, value);
        inOrder.verify(simpleTreeReporter).checkForAbsentItem("1");
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void presentNode_brokenCheck() {
        uncheckedToIncorrectlyPresentConvertingReporter.presentNode(key, value, reporter -> reporter.brokenCheck("1", throwable));
        inOrder.verify(simpleTreeReporter).beginPresentNode(key, value);
        inOrder.verify(simpleTreeReporter).brokenCheck("1", throwable);
        inOrder.verify(simpleTreeReporter).endNode();
        inOrder.verifyNoMoreInteractions();
    }



    @Test
    public void presentNode_presentNode_correctlyPresent() {
        uncheckedToIncorrectlyPresentConvertingReporter.presentNode(key, value, reporter -> reporter.presentNode(key2, value2, FlatReporter::correctlyPresent));
        inOrder.verify(simpleTreeReporter).beginPresentNode(key, value);
        inOrder.verify(simpleTreeReporter).beginPresentNode(key2, value2);
        inOrder.verify(simpleTreeReporter).correctlyPresent();
        inOrder.verify(simpleTreeReporter, times(2)).endNode();
        inOrder.verifyNoMoreInteractions();
    }
}
