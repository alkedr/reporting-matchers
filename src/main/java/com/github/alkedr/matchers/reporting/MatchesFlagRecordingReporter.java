package com.github.alkedr.matchers.reporting;

public class MatchesFlagRecordingReporter implements Reporter {
    private boolean matchesFlag = true;

    @Override
    public void beginReport() {
    }

    @Override
    public void beginKeyValuePair(String keyAsString, ValueStatus valueStatus, String valueAsString) {
        if (valueStatus != ValueStatus.NORMAL) {
            matchesFlag = false;
        }
    }

    @Override
    public void addCheck(CheckStatus status, String description) {
        if (status != CheckStatus.PASSED) {
            matchesFlag = false;
        }
    }

    @Override
    public void endKeyValuePair() {
    }

    @Override
    public void endReport() {
    }

    public boolean getMatchesFlag() {
        return matchesFlag;
    }
}
