package com.github.alkedr.matchers.reporting.reporters;

import org.junit.Test;

import static com.github.alkedr.matchers.reporting.reporters.Reporters.noOpSafeTreeReporter;
import static com.github.alkedr.matchers.reporting.reporters.Reporters.noOpSimpleTreeReporter;

public class NoOpReportersTest {
    @Test
    public void noOpSafeTreeReporter_methodsShouldNotUseArgumentsOrThrow() {
        SafeTreeReporter reporter = noOpSafeTreeReporter();
        reporter.presentNode(null, null, null);
        reporter.absentNode(null, null);
        reporter.brokenNode(null, null, null);
        callAllFlatReporterMethods(reporter);
    }

    @Test
    public void noOpSimpleTreeReporter_methodsShouldNotUseArgumentsOrThrow() {
        SimpleTreeReporter reporter = noOpSimpleTreeReporter();
        reporter.beginPresentNode(null, null);
        reporter.beginAbsentNode(null);
        reporter.beginBrokenNode(null, null);
        reporter.endNode();
        callAllFlatReporterMethods(reporter);
    }

    private static void callAllFlatReporterMethods(FlatReporter reporter) {
        reporter.correctlyPresent();
        reporter.correctlyAbsent();
        reporter.incorrectlyPresent();
        reporter.incorrectlyAbsent();
        reporter.passedCheck(null);
        reporter.failedCheck(null, null);
        reporter.checkForAbsentItem(null);
        reporter.brokenCheck(null, null);
    }
}
