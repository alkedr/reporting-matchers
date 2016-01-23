package com.github.alkedr.matchers.reporting.reporters;

import com.github.alkedr.matchers.reporting.keys.Key;

import java.util.function.Consumer;

public class MatchesFlagRecordingReporter implements Reporter {
    private boolean matchesFlag = true;

    public boolean getMatchesFlag() {
        return matchesFlag;
    }

    @Override
    public void presentNode(Key key, Object value, Consumer<Reporter> contents) {
        contents.accept(this);
    }

    @Override
    public void missingNode(Key key, Consumer<Reporter> contents) {
        contents.accept(this);
    }

    @Override
    public void brokenNode(Key key, Throwable throwable, Consumer<Reporter> contents) {
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
}
