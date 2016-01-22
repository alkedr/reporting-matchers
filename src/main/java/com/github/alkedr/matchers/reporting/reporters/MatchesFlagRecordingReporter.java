package com.github.alkedr.matchers.reporting.reporters;

public class MatchesFlagRecordingReporter implements Reporter {
    private boolean matchesFlag = true;

    public boolean getMatchesFlag() {
        return matchesFlag;
    }

    @Override
    public void beginNode(String name, Object value) {
    }

    @Override
    public void beginMissingNode(String name) {
    }

    @Override
    public void beginBrokenNode(String name, Throwable throwable) {
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
