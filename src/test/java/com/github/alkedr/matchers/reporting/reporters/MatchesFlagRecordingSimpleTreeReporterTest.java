package com.github.alkedr.matchers.reporting.reporters;

import org.junit.Test;

import static com.github.alkedr.matchers.reporting.reporters.Reporters.matchesFlagRecordingReporter;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MatchesFlagRecordingSimpleTreeReporterTest {
    private final MatchesFlagRecordingSimpleTreeReporter reporter = matchesFlagRecordingReporter();

    @Test
    public void initialValue() {
        assertTrue(reporter.getMatchesFlag());
    }

    @Test
    public void beginPresentNode() {
        reporter.beginPresentNode(null, null);
        assertTrue(reporter.getMatchesFlag());
    }

    @Test
    public void beginMissingNode() {
        reporter.beginMissingNode(null);
        assertTrue(reporter.getMatchesFlag());
    }

    @Test
    public void beginBrokenNode() {
        reporter.beginBrokenNode(null, null);
        assertFalse(reporter.getMatchesFlag());
    }

    @Test
    public void endNode() {
        reporter.endNode();
        assertTrue(reporter.getMatchesFlag());
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
}
