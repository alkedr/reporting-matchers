package com.github.alkedr.matchers.reporting.reporters;

import com.github.alkedr.matchers.reporting.sub.value.keys.Key;
import org.junit.Test;

import static com.github.alkedr.matchers.reporting.reporters.Reporters.checksCountLimitingReporter;
import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.elementKey;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class ChecksCountLimitingReporterTest {
    private final SimpleTreeReporter next = mock(SimpleTreeReporter.class);
    private final Key key = elementKey(0);
    private final Object value = new Object();
    private final Throwable throwable = new RuntimeException();

    @Test
    public void nodeMethods_shouldAlwaysDelegateToNextReporter() {
        checksCountLimitingReporter(next, 0).beginPresentNode(key, value);
        verify(next).beginPresentNode(same(key), same(value));
        verifyNoMoreInteractions(next);

        checksCountLimitingReporter(next, 0).beginAbsentNode(key);
        verify(next).beginAbsentNode(same(key));
        verifyNoMoreInteractions(next);

        checksCountLimitingReporter(next, 0).beginBrokenNode(key, throwable);
        verify(next).beginBrokenNode(same(key), same(throwable));
        verifyNoMoreInteractions(next);

        checksCountLimitingReporter(next, 0).endNode();
        verify(next).endNode();
        verifyNoMoreInteractions(next);
    }

    @Test
    public void correctlyPresent() {
        SimpleTreeReporter reporter = checksCountLimitingReporter(next, 1);

        reporter.correctlyPresent();
        verify(next).correctlyPresent();
        verifyNoMoreInteractions(next);

        reporter.correctlyPresent();
        verifyNoMoreInteractions(next);
    }

    @Test
    public void correctlyAbsent() {
        SimpleTreeReporter reporter = checksCountLimitingReporter(next, 1);

        reporter.correctlyAbsent();
        verify(next).correctlyAbsent();
        verifyNoMoreInteractions(next);

        reporter.correctlyAbsent();
        verifyNoMoreInteractions(next);
    }

    @Test
    public void incorrectlyPresent() {
        SimpleTreeReporter reporter = checksCountLimitingReporter(next, 1);

        reporter.incorrectlyPresent();
        verify(next).incorrectlyPresent();
        verifyNoMoreInteractions(next);

        reporter.incorrectlyPresent();
        verifyNoMoreInteractions(next);
    }

    @Test
    public void incorrectlyAbsent() {
        SimpleTreeReporter reporter = checksCountLimitingReporter(next, 1);

        reporter.incorrectlyAbsent();
        verify(next).incorrectlyAbsent();
        verifyNoMoreInteractions(next);

        reporter.incorrectlyAbsent();
        verifyNoMoreInteractions(next);
    }

    @Test
    public void passedCheck() {
        SimpleTreeReporter reporter = checksCountLimitingReporter(next, 1);

        reporter.passedCheck("1");
        verify(next).passedCheck("1");
        verifyNoMoreInteractions(next);

        reporter.passedCheck("2");
        verifyNoMoreInteractions(next);
    }

    @Test
    public void failedCheck() {
        SimpleTreeReporter reporter = checksCountLimitingReporter(next, 1);

        reporter.failedCheck("1", "3");
        verify(next).failedCheck("1", "3");
        verifyNoMoreInteractions(next);

        reporter.failedCheck("2", "4");
        verifyNoMoreInteractions(next);
    }

    @Test
    public void checkForAbsentItem() {
        SimpleTreeReporter reporter = checksCountLimitingReporter(next, 1);

        reporter.checkForAbsentItem("1");
        verify(next).checkForAbsentItem("1");
        verifyNoMoreInteractions(next);

        reporter.checkForAbsentItem("2");
        verifyNoMoreInteractions(next);
    }

    @Test
    public void brokenCheck() {
        SimpleTreeReporter reporter = checksCountLimitingReporter(next, 1);

        reporter.brokenCheck("1", throwable);
        verify(next).brokenCheck("1", throwable);
        verifyNoMoreInteractions(next);

        reporter.brokenCheck("2", throwable);
        verifyNoMoreInteractions(next);
    }
}
