package com.github.alkedr.matchers.reporting.sub.value.checkers;

import com.github.alkedr.matchers.reporting.ReportingMatcher;
import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;
import com.github.alkedr.matchers.reporting.sub.value.keys.Key;
import org.junit.Test;

import java.util.function.Consumer;

import static com.github.alkedr.matchers.reporting.sub.value.checkers.SubValueCheckers.matcherSubValuesChecker;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class MatcherSubValuesCheckerTest {
    @Test
    public void test() {
        Key key = mock(Key.class);
        Object value = new Object();
        Throwable throwable = new RuntimeException();
        SafeTreeReporter safeTreeReporter = mock(SafeTreeReporter.class);
        ReportingMatcher<?> reportingMatcher = mock(ReportingMatcher.class);
        SubValuesChecker matcherSubValuesChecker = matcherSubValuesChecker(reportingMatcher);

        matcherSubValuesChecker.begin(safeTreeReporter);
        Consumer<SafeTreeReporter> consumer1 = matcherSubValuesChecker.present(key, value);
        Consumer<SafeTreeReporter> consumer2 = matcherSubValuesChecker.absent(key);
        Consumer<SafeTreeReporter> consumer3 = matcherSubValuesChecker.broken(key, throwable);
        matcherSubValuesChecker.end(safeTreeReporter);
        verifyNoMoreInteractions(reportingMatcher);

        consumer1.accept(safeTreeReporter);
        verify(reportingMatcher).run(value, safeTreeReporter);
        verifyNoMoreInteractions(reportingMatcher);

        consumer2.accept(safeTreeReporter);
        verify(reportingMatcher).runForAbsentItem(safeTreeReporter);
        verifyNoMoreInteractions(reportingMatcher);

        consumer3.accept(safeTreeReporter);
        verify(reportingMatcher, times(2)).runForAbsentItem(safeTreeReporter);
        verifyNoMoreInteractions(reportingMatcher);
    }
}
