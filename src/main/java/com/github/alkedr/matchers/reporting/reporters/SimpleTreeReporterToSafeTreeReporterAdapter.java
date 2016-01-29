package com.github.alkedr.matchers.reporting.reporters;

import com.github.alkedr.matchers.reporting.sub.value.keys.Key;

import java.util.function.Consumer;

class SimpleTreeReporterToSafeTreeReporterAdapter implements SafeTreeReporter {
    private final SimpleTreeReporter simpleTreeReporter;

    SimpleTreeReporterToSafeTreeReporterAdapter(SimpleTreeReporter simpleTreeReporter) {
        this.simpleTreeReporter = simpleTreeReporter;
    }

    @Override
    public void presentNode(Key key, Object value, Consumer<SafeTreeReporter> contents) {
        simpleTreeReporter.beginPresentNode(key, value);
        contents.accept(this);
        simpleTreeReporter.endNode();
    }

    @Override
    public void absentNode(Key key, Consumer<SafeTreeReporter> contents) {
        simpleTreeReporter.beginAbsentNode(key);
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
    public void correctlyAbsent() {
        simpleTreeReporter.correctlyAbsent();
    }

    @Override
    public void incorrectlyPresent() {
        simpleTreeReporter.incorrectlyPresent();
    }

    @Override
    public void incorrectlyAbsent() {
        simpleTreeReporter.incorrectlyAbsent();
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
    public void checkForAbsentItem(String description) {
        simpleTreeReporter.checkForAbsentItem(description);
    }

    @Override
    public void brokenCheck(String description, Throwable throwable) {
        simpleTreeReporter.brokenCheck(description, throwable);
    }
}
