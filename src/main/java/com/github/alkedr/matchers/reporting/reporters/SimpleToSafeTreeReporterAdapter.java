package com.github.alkedr.matchers.reporting.reporters;

import com.github.alkedr.matchers.reporting.keys.Key;

import java.util.function.Consumer;

class SimpleToSafeTreeReporterAdapter implements SafeTreeReporter {
    private final SimpleTreeReporter simpleTreeReporter;

    SimpleToSafeTreeReporterAdapter(SimpleTreeReporter simpleTreeReporter) {
        this.simpleTreeReporter = simpleTreeReporter;
    }

    @Override
    public void presentNode(Key key, Object value, Consumer<SafeTreeReporter> contents) {
        simpleTreeReporter.beginPresentNode(key, value);
        contents.accept(this);
        simpleTreeReporter.endNode();
    }

    @Override
    public void missingNode(Key key, Consumer<SafeTreeReporter> contents) {
        simpleTreeReporter.beginMissingNode(key);
        contents.accept(this);
        simpleTreeReporter.endNode();
    }

    @Override
    public void brokenNode(Key key, Throwable throwable, Consumer<SafeTreeReporter> contents) {
        simpleTreeReporter.beginBrokenNode(key, throwable);
        contents.accept(this);
        simpleTreeReporter.endNode();
    }

    @Override
    public void correctlyPresent() {
        simpleTreeReporter.correctlyPresent();
    }

    @Override
    public void correctlyMissing() {
        simpleTreeReporter.correctlyMissing();
    }

    @Override
    public void incorrectlyPresent() {
        simpleTreeReporter.incorrectlyPresent();
    }

    @Override
    public void incorrectlyMissing() {
        simpleTreeReporter.incorrectlyMissing();
    }

    @Override
    public void passedCheck(String description) {
        simpleTreeReporter.passedCheck(description);
    }

    @Override
    public void failedCheck(String expected, String actual) {
        simpleTreeReporter.failedCheck(expected, actual);
    }

    @Override
    public void checkForMissingItem(String description) {
        simpleTreeReporter.checkForMissingItem(description);
    }

    @Override
    public void brokenCheck(String description, Throwable throwable) {
        simpleTreeReporter.brokenCheck(description, throwable);
    }
}
