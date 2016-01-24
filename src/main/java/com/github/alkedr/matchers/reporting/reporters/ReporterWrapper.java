package com.github.alkedr.matchers.reporting.reporters;

import com.github.alkedr.matchers.reporting.keys.Key;

import java.util.function.Consumer;

public class ReporterWrapper implements Reporter {
    protected final Reporter wrappedReporter;

    public ReporterWrapper() {
        this(new NoOpReporter());
    }

    public ReporterWrapper(Reporter wrappedReporter) {
        this.wrappedReporter = wrappedReporter;
    }

    @Override
    public void presentNode(Key key, Object value, Consumer<Reporter> contents) {
        wrappedReporter.presentNode(key, value, contents);
    }

    @Override
    public void missingNode(Key key, Consumer<Reporter> contents) {
        wrappedReporter.missingNode(key, contents);
    }

    @Override
    public void brokenNode(Key key, Throwable throwable, Consumer<Reporter> contents) {
        wrappedReporter.brokenNode(key, throwable, contents);
    }

    @Override
    public void correctlyPresent() {
        wrappedReporter.correctlyPresent();
    }

    @Override
    public void correctlyMissing() {
        wrappedReporter.correctlyMissing();
    }

    @Override
    public void incorrectlyPresent() {
        wrappedReporter.incorrectlyPresent();
    }

    @Override
    public void incorrectlyMissing() {
        wrappedReporter.incorrectlyMissing();
    }

    @Override
    public void passedCheck(String description) {
        wrappedReporter.passedCheck(description);
    }

    @Override
    public void failedCheck(String expected, String actual) {
        wrappedReporter.failedCheck(expected, actual);
    }

    @Override
    public void checkForMissingItem(String description) {
        wrappedReporter.checkForMissingItem(description);
    }

    @Override
    public void brokenCheck(String description, Throwable throwable) {
        wrappedReporter.brokenCheck(description, throwable);
    }
}
