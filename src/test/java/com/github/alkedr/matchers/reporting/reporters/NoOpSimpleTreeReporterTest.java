package com.github.alkedr.matchers.reporting.reporters;

import org.junit.Test;

public class NoOpSimpleTreeReporterTest {
    @Test
    public void methodsShouldNotUseArgumentsOrThrow() {
        SimpleTreeReporter reporter = Reporters.noOpSimpleTreeReporter();
        reporter.beginPresentNode(null, null);
        reporter.beginMissingNode(null);
        reporter.beginBrokenNode(null, null);
        reporter.endNode();
        reporter.correctlyPresent();
        reporter.correctlyMissing();
        reporter.incorrectlyPresent();
        reporter.incorrectlyMissing();
        reporter.passedCheck(null);
        reporter.failedCheck(null, null);
        reporter.checkForMissingItem(null);
        reporter.brokenCheck(null, null);
    }
}
