package com.github.alkedr.matchers.reporting.reporters;

import org.junit.Test;

public class NoOpSafeTreeReporterTest {
    @Test
    public void methodsShouldNotUseArgumentsOrThrow() {
        SafeTreeReporter reporter = Reporters.noOpSafeTreeReporter();
        reporter.presentNode(null, null, null);
        reporter.missingNode(null, null);
        reporter.brokenNode(null, null, null);
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
