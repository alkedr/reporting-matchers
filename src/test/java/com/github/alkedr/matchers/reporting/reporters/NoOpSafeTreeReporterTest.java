package com.github.alkedr.matchers.reporting.reporters;

import org.junit.Test;

public class NoOpSafeTreeReporterTest {
    @Test
    public void noOpReporterMethodsShouldNotUseArgumentsOrThrow() {
        SafeTreeReporter safeTreeReporter = Reporters.noOpSafeTreeReporter();
        safeTreeReporter.presentNode(null, null, null);
        safeTreeReporter.missingNode(null, null);
        safeTreeReporter.brokenNode(null, null, null);
        safeTreeReporter.correctlyPresent();
        safeTreeReporter.correctlyMissing();
        safeTreeReporter.incorrectlyPresent();
        safeTreeReporter.incorrectlyMissing();
        safeTreeReporter.passedCheck(null);
        safeTreeReporter.failedCheck(null, null);
        safeTreeReporter.checkForMissingItem(null);
        safeTreeReporter.brokenCheck(null, null);
    }
}
