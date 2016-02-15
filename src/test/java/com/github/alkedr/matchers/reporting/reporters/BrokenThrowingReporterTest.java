package com.github.alkedr.matchers.reporting.reporters;

import com.github.alkedr.matchers.reporting.sub.value.keys.Key;
import org.junit.Test;

import static com.github.alkedr.matchers.reporting.reporters.Reporters.brokenThrowingReporter;
import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.elementKey;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class BrokenThrowingReporterTest {
    private final SimpleTreeReporter next = mock(SimpleTreeReporter.class);

    @Test
    public void beginBrokenNode_shouldThrow() {
        Throwable throwable = new RuntimeException();
        try {
            brokenThrowingReporter(next).beginBrokenNode(elementKey(0), throwable);
            fail();
        } catch (BrokenThrowingReporter.BrokenCheckException e) {
            assertSame(throwable, e.getCause());
            assertEquals(elementKey(0).asString(), e.getMessage());
        }
    }

    @Test
    public void brokenCheck_shouldThrow() {
        Throwable throwable = new RuntimeException();
        try {
            brokenThrowingReporter(next).brokenCheck("1", throwable);
            fail();
        } catch (BrokenThrowingReporter.BrokenCheckException e) {
            assertSame(throwable, e.getCause());
            assertEquals("1", e.getMessage());
        }
    }

    @Test
    public void otherMethods_shouldDelegateToNextReporter() {
        Key key = elementKey(0);
        Object value = new Object();

        SimpleTreeReporter reporter = brokenThrowingReporter(next);

        reporter.beginPresentNode(key, value);
        verify(next).beginPresentNode(same(key), same(value));
        verifyNoMoreInteractions(next);

        reporter.beginAbsentNode(key);
        verify(next).beginAbsentNode(same(key));
        verifyNoMoreInteractions(next);

        reporter.endNode();
        verify(next).endNode();
        verifyNoMoreInteractions(next);

        reporter.correctlyPresent();
        verify(next).correctlyPresent();
        verifyNoMoreInteractions(next);

        reporter.correctlyAbsent();
        verify(next).correctlyAbsent();
        verifyNoMoreInteractions(next);

        reporter.incorrectlyPresent();
        verify(next).incorrectlyPresent();
        verifyNoMoreInteractions(next);

        reporter.incorrectlyAbsent();
        verify(next).incorrectlyAbsent();
        verifyNoMoreInteractions(next);

        reporter.passedCheck("1");
        verify(next).passedCheck("1");
        verifyNoMoreInteractions(next);

        reporter.failedCheck("2", "3");
        verify(next).failedCheck("2", "3");
        verifyNoMoreInteractions(next);

        reporter.checkForAbsentItem("4");
        verify(next).checkForAbsentItem("4");
        verifyNoMoreInteractions(next);
    }
}
