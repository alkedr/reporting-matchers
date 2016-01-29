package com.github.alkedr.matchers.reporting.reporters;

import com.github.alkedr.matchers.reporting.sub.value.keys.Key;

class NoOpSimpleTreeReporter implements SimpleTreeReporter {
    @Override
    public void beginPresentNode(Key key, Object value) {
    }

    @Override
    public void beginAbsentNode(Key key) {
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
    public void correctlyAbsent() {
    }

    @Override
    public void incorrectlyPresent() {
    }

    @Override
    public void incorrectlyAbsent() {
    }

    @Override
    public void passedCheck(String description) {
    }

    @Override
    public void failedCheck(String expected, String actual) {
    }

    @Override
    public void checkForAbsentItem(String description) {
    }

    @Override
    public void brokenCheck(String description, Throwable throwable) {
    }
}
