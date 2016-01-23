package com.github.alkedr.matchers.reporting.reporters;

import com.github.alkedr.matchers.reporting.keys.Key;

public class MatchesFlagRecordingReporter implements Reporter {
    private boolean matchesFlag = true;

    public boolean getMatchesFlag() {
        return matchesFlag;
    }

    @Override
    public void beginNode(Key key, Object value) {
    }

    @Override
    public void beginMissingNode(Key key) {
    }

    @Override
    public void beginBrokenNode(Key key, Throwable throwable) {
        matchesFlag = false;
    }

    @Override
    public void correctlyPresent() {
    }

    @Override
    public void correctlyMissing() {
    }

    @Override
    public void incorrectlyPresent() {
        matchesFlag = false;
    }

    @Override
    public void incorrectlyMissing() {
        matchesFlag = false;
    }

    @Override
    public void passedCheck(String description) {
    }

    @Override
    public void failedCheck(String expected, String actual) {
        matchesFlag = false;
    }

    @Override
    public void checkForMissingItem(String description) {
        matchesFlag = false;
    }

    @Override
    public void brokenCheck(String description, Throwable throwable) {
        matchesFlag = false;
    }

    @Override
    public void endNode() {
    }
}
