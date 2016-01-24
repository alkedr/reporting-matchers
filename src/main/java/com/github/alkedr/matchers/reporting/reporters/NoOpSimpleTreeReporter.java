package com.github.alkedr.matchers.reporting.reporters;

import com.github.alkedr.matchers.reporting.keys.Key;

class NoOpSimpleTreeReporter implements SimpleTreeReporter {
    @Override
    public void beginPresentNode(Key key, Object value) {
    }

    @Override
    public void beginMissingNode(Key key) {
    }

    @Override
    public void beginBrokenNode(Key key, Throwable throwable) {
    }

    @Override
    public void endNode() {
    }

    @Override
    public void correctlyPresent() {
    }

    @Override
    public void correctlyMissing() {
    }

    @Override
    public void incorrectlyPresent() {
    }

    @Override
    public void incorrectlyMissing() {
    }

    @Override
    public void passedCheck(String description) {
    }

    @Override
    public void failedCheck(String expected, String actual) {
    }

    @Override
    public void checkForMissingItem(String description) {
    }

    @Override
    public void brokenCheck(String description, Throwable throwable) {
    }
}
