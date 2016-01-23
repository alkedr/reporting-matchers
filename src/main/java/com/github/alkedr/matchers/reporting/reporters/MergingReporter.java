package com.github.alkedr.matchers.reporting.reporters;

import com.github.alkedr.matchers.reporting.keys.Key;

import java.io.Closeable;

public class MergingReporter implements Reporter, Closeable {
    private final Reporter reporter;

    public MergingReporter(Reporter reporter) {
        this.reporter = reporter;
    }

    @Override
    public void beginNode(Key key, Object value) {
        // TODO: мержить только самый верхний уровень!
        reporter.beginNode(key, value);
    }

    @Override
    public void beginMissingNode(Key key) {
        // TODO: мержить только самый верхний уровень!
        reporter.beginMissingNode(key);
    }

    @Override
    public void beginBrokenNode(Key key, Throwable throwable) {
        // TODO: мержить только самый верхний уровень!
        reporter.beginBrokenNode(key, throwable);
    }

    @Override
    public void correctlyPresent() {
        reporter.correctlyPresent();
    }

    @Override
    public void correctlyMissing() {
        reporter.correctlyMissing();
    }

    @Override
    public void incorrectlyPresent() {
        reporter.incorrectlyPresent();
    }

    @Override
    public void incorrectlyMissing() {
        reporter.incorrectlyMissing();
    }

    @Override
    public void passedCheck(String description) {
        reporter.passedCheck(description);
    }

    @Override
    public void failedCheck(String expected, String actual) {
        reporter.failedCheck(expected, actual);
    }

    @Override
    public void checkForMissingItem(String description) {
        reporter.checkForMissingItem(description);
    }

    @Override
    public void brokenCheck(String description, Throwable throwable) {
        reporter.brokenCheck(description, throwable);
    }

    @Override
    public void endNode() {
        reporter.endNode();
    }

    @Override
    public void close() {
        // TODO:
    }
}
