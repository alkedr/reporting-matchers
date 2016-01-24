package com.github.alkedr.matchers.reporting.reporters;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MatchesFlagRecordingReporterTest {
    private final MatchesFlagRecordingReporter reporter = new MatchesFlagRecordingReporter();

    @Test
    public void initialValue() {
        assertTrue(reporter.getMatchesFlag());
    }


    @Test
    public void presentNode() {
        reporter.presentNode(null, null, null);
        assertTrue(reporter.getMatchesFlag());
    }

    @Test
    public void missingNode() {
        reporter.missingNode(null, null);
        assertTrue(reporter.getMatchesFlag());
    }

    @Test
    public void brokenNode() {
        reporter.brokenNode(null, null, null);
        assertFalse(reporter.getMatchesFlag());
    }

    @Test
    public void correctlyPresent() {
        reporter.correctlyPresent();
        assertTrue(reporter.getMatchesFlag());
    }

    @Test
    public void correctlyMissing() {
        reporter.correctlyMissing();
        assertTrue(reporter.getMatchesFlag());
    }

    @Test
    public void incorrectlyPresent() {
        reporter.incorrectlyPresent();
        assertFalse(reporter.getMatchesFlag());
    }

    @Test
    public void incorrectlyMissing() {
        reporter.incorrectlyMissing();
        assertFalse(reporter.getMatchesFlag());
    }

    @Test
    public void passedCheck() {
        reporter.passedCheck(null);
        assertTrue(reporter.getMatchesFlag());
    }

    @Test
    public void failedCheck() {
        reporter.failedCheck(null, null);
        assertFalse(reporter.getMatchesFlag());
    }

    @Test
    public void checkForMissingItem() {
        reporter.checkForMissingItem(null);
        assertFalse(reporter.getMatchesFlag());
    }

    @Test
    public void brokenCheck() {
        reporter.brokenCheck(null, null);
        assertFalse(reporter.getMatchesFlag());
    }


    // TODO: тестировать содержимое нод
}
