package com.github.alkedr.matchers.reporting.reporters;

import org.junit.Test;
import org.mockito.InOrder;

import java.util.function.Consumer;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@Deprecated
public class UncheckedToIncorrectlyPresentConvertingReporterTest {
    private final SimpleTreeReporter simpleTreeReporter = mock(SimpleTreeReporter.class);
    private final InOrder inOrder = inOrder(simpleTreeReporter);
    private final Throwable throwable = new RuntimeException();

    @Test
    public void flat() {
        testFlat(FlatReporter::correctlyPresent);
        testFlat(FlatReporter::correctlyAbsent);
        testFlat(FlatReporter::incorrectlyPresent);
        testFlat(FlatReporter::incorrectlyAbsent);
        testFlat(flatReporter -> flatReporter.passedCheck("1"));
        testFlat(flatReporter -> flatReporter.failedCheck("1", "2"));
        testFlat(flatReporter -> flatReporter.checkForAbsentItem("1"));
        testFlat(flatReporter -> flatReporter.brokenCheck("1", throwable));
    }

    @Test
    public void emptyNodes() {
//        testEmptyNode()

        testFlat(FlatReporter::correctlyPresent);
        testFlat(FlatReporter::correctlyAbsent);
        testFlat(FlatReporter::incorrectlyPresent);
        testFlat(FlatReporter::incorrectlyAbsent);
        testFlat(flatReporter -> flatReporter.passedCheck("1"));
        testFlat(flatReporter -> flatReporter.failedCheck("1", "2"));
        testFlat(flatReporter -> flatReporter.checkForAbsentItem("1"));
        testFlat(flatReporter -> flatReporter.brokenCheck("1", throwable));
    }

    private void testFlat(Consumer<FlatReporter> method) {
        method.accept(new UncheckedToIncorrectlyPresentConvertingReporter(simpleTreeReporter));
        method.accept(verify(simpleTreeReporter));
        verifyNoMoreInteractions(simpleTreeReporter);
    }
}
