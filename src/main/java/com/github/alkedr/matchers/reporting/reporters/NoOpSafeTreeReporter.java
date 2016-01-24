package com.github.alkedr.matchers.reporting.reporters;

import com.github.alkedr.matchers.reporting.keys.Key;

import java.util.function.Consumer;

class NoOpSafeTreeReporter implements SafeTreeReporter {
    @Override
    public void presentNode(Key key, Object value, Consumer<SafeTreeReporter> contents) {
    }

    @Override
    public void missingNode(Key key, Consumer<SafeTreeReporter> contents) {
    }

    @Override
    public void brokenNode(Key key, Throwable throwable, Consumer<SafeTreeReporter> contents) {
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