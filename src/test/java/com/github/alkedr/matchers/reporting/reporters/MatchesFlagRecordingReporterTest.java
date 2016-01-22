package com.github.alkedr.matchers.reporting.reporters;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MatchesFlagRecordingReporterTest {
    private final MatchesFlagRecordingReporter reporter = new MatchesFlagRecordingReporter();

    @Test
    public void initialMatchesFlagValue_shouldBeTrue() {
        assertTrue(reporter.getMatchesFlag());
    }

    @Test
    public void failedCheckMethod_shouldSetMatchesFlagToFalse() {
        reporter.failedCheck(null, null);
        assertFalse(reporter.getMatchesFlag());
    }

    @Test
    public void brokenCheckMethod_shouldSetMatchesFlagToFalse() {
        reporter.brokenCheck(null, null);
        assertFalse(reporter.getMatchesFlag());
    }

    @Test
    public void otherMethods_shouldNotChangeMatchesFlag() {
        reporter.beginNode(null, null);
        assertTrue(reporter.getMatchesFlag());

        reporter.passedCheck(null);
        assertTrue(reporter.getMatchesFlag());

        reporter.endNode();
        assertTrue(reporter.getMatchesFlag());
    }
}
