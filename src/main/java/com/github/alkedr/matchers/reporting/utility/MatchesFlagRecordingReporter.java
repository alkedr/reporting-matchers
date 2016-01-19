package com.github.alkedr.matchers.reporting.utility;

import com.github.alkedr.matchers.reporting.Reporter;

public class MatchesFlagRecordingReporter implements Reporter {
    private boolean matchesFlag = true;

    public boolean getMatchesFlag() {
        return matchesFlag;
    }

    @Override
    public void beginNode(String name, String value) {
    }

    @Override
    public void passedCheck(String description) {
    }

    @Override
    public void failedCheck(String expected, String actual) {
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
