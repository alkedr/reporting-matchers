package com.github.alkedr.matchers.reporting;

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
    public void beginMethod_withValueStatusNull_shouldLeaveMatchesFlagEqualToTrue() {
        reporter.beginKeyValuePair(null, null, null);
        assertFalse(reporter.getMatchesFlag());
    }

    @Test
    public void beginMethod_withValueStatusNormal_shouldLeaveMatchesFlagEqualToTrue() {
        reporter.beginKeyValuePair(null, Reporter.ValueStatus.NORMAL, null);
        assertTrue(reporter.getMatchesFlag());
    }

    @Test
    public void beginMethod_withValueStatusMissing_shouldSetMatchesFlagToFalse() {
        reporter.beginKeyValuePair(null, Reporter.ValueStatus.MISSING, null);
        assertFalse(reporter.getMatchesFlag());
    }

    @Test
    public void beginMethod_withValueStatusBroken_shouldSetMatchesFlagToFalse() {
        reporter.beginKeyValuePair(null, Reporter.ValueStatus.BROKEN, null);
        assertFalse(reporter.getMatchesFlag());
    }


    @Test
    public void checkMethod_withCheckStatusNull_shouldLeaveMatchesFlagEqualToTrue() {
        reporter.addCheck(null, null);
        assertFalse(reporter.getMatchesFlag());
    }

    @Test
    public void checkMethod_withCheckStatusPassed_shouldLeaveMatchesFlagEqualToTrue() {
        reporter.addCheck(Reporter.CheckStatus.PASSED, null);
        assertTrue(reporter.getMatchesFlag());
    }

    @Test
    public void checkMethod_withCheckStatusFailed_shouldSetMatchesFlagToFalse() {
        reporter.addCheck(Reporter.CheckStatus.FAILED, null);
        assertFalse(reporter.getMatchesFlag());
    }

    @Test
    public void checkMethod_withCheckStatusBroken_shouldSetMatchesFlagToFalse() {
        reporter.addCheck(Reporter.CheckStatus.BROKEN, null);
        assertFalse(reporter.getMatchesFlag());
    }


    @Test
    public void otherMethods_shouldNotChangeMatchesFlag() {
        reporter.beginReport();
        assertTrue(reporter.getMatchesFlag());
        reporter.endKeyValuePair();
        assertTrue(reporter.getMatchesFlag());
        reporter.endReport();
        assertTrue(reporter.getMatchesFlag());
    }
}
