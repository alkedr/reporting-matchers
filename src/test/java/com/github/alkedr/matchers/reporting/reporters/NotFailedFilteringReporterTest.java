package com.github.alkedr.matchers.reporting.reporters;

import com.github.alkedr.matchers.reporting.sub.value.keys.Key;
import org.junit.Test;

import static com.github.alkedr.matchers.reporting.reporters.Reporters.notFailedFilteringReporter;
import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.elementKey;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class NotFailedFilteringReporterTest {
    private final SimpleTreeReporter next = mock(SimpleTreeReporter.class);
    private final Key key = elementKey(0);
    private final Object value = new Object();
    private final Throwable throwable = new RuntimeException();

    @Test
    public void beginPresentNode() {
        notFailedFilteringReporter(next).beginPresentNode(key, value);
        verify(next).beginPresentNode(same(key), same(value));
        verifyNoMoreInteractions(next);
    }

    @Test
    public void beginAbsentNode() {
        notFailedFilteringReporter(next).beginAbsentNode(key);
        verify(next).beginAbsentNode(same(key));
        verifyNoMoreInteractions(next);
    }

    @Test
    public void beginBrokenNode() {
        notFailedFilteringReporter(next).beginBrokenNode(key, throwable);
        verify(next).beginBrokenNode(same(key), same(throwable));
        verifyNoMoreInteractions(next);
    }

    @Test
    public void endNode() {
        notFailedFilteringReporter(next).endNode();
        verify(next).endNode();
        verifyNoMoreInteractions(next);
    }

    @Test
    public void correctlyPresent() {
        notFailedFilteringReporter(next).correctlyPresent();
        verifyNoMoreInteractions(next);
    }

    @Test
    public void correctlyAbsent() {
        notFailedFilteringReporter(next).correctlyAbsent();
        verifyNoMoreInteractions(next);
    }

    @Test
    public void incorrectlyPresent() {
        notFailedFilteringReporter(next).incorrectlyPresent();
        verify(next).incorrectlyPresent();
        verifyNoMoreInteractions(next);
    }

    @Test
    public void incorrectlyAbsent() {
        notFailedFilteringReporter(next).incorrectlyAbsent();
        verify(next).incorrectlyAbsent();
        verifyNoMoreInteractions(next);
    }

    @Test
    public void passedCheck() {
        notFailedFilteringReporter(next).passedCheck("1");
        verifyNoMoreInteractions(next);
    }

    @Test
    public void failedCheck() {
        notFailedFilteringReporter(next).failedCheck("2", "3");
        verify(next).failedCheck("2", "3");
        verifyNoMoreInteractions(next);
    }

    @Test
    public void checkForAbsentItem() {
        notFailedFilteringReporter(next).checkForAbsentItem("4");
        verify(next).checkForAbsentItem("4");
        verifyNoMoreInteractions(next);
    }

    @Test
    public void brokenCheck() {
        notFailedFilteringReporter(next).brokenCheck("5", throwable);
        verify(next).brokenCheck("5", throwable);
        verifyNoMoreInteractions(next);
    }
}
