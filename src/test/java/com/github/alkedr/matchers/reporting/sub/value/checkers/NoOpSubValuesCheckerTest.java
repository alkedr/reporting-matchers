package com.github.alkedr.matchers.reporting.sub.value.checkers;

import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;
import org.junit.Test;

import static com.github.alkedr.matchers.reporting.sub.value.checkers.SubValueCheckers.noOpSubValuesChecker;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class NoOpSubValuesCheckerTest {
    private final SafeTreeReporter safeTreeReporter = mock(SafeTreeReporter.class);

    @Test
    public void allMethods_shouldNotCallAnySafeTreeReporterMethods() {
        SubValuesChecker checker = noOpSubValuesChecker().get();
        checker.begin(safeTreeReporter);
        checker.present(null, null).accept(safeTreeReporter);
        checker.absent(null).accept(safeTreeReporter);
        checker.broken(null, null).accept(safeTreeReporter);
        checker.end(safeTreeReporter);
        verifyNoMoreInteractions(safeTreeReporter);
    }
}
