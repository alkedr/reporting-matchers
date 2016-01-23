package com.github.alkedr.matchers.reporting.reporters;

import com.github.alkedr.matchers.reporting.keys.Key;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.Map;

/*
Придётся хранить в памяти всё, потому что чтобы замёржить уровень, нужно получить все ноды этого уровня
Чтобы получить все ноды первого уровня, нужно дойти до close()
 */
public class MergingReporter implements Reporter, Closeable {
    private final Reporter reporter;
    private final Deque<Map<OneLevelMergingReporter.Node, RecordingReporter>> stack = new ArrayDeque<>();

    public MergingReporter(Reporter reporter) {
        this.reporter = reporter;
        stack.add(new LinkedHashMap<>());
    }

    @Override
    public void close() throws IOException {
//        pop();
        // TODO: check stack.isEmpty()
    }

    @Override
    public void beginNode(Key key, Object value) {
//        push(new OneLevelMergingReporter.PresentNode(key, value));
    }

    @Override
    public void beginMissingNode(Key key) {
//        push(new OneLevelMergingReporter.MissingNode(key));
    }

    @Override
    public void beginBrokenNode(Key key, Throwable throwable) {
//        push(new OneLevelMergingReporter.BrokenNode(key, throwable));
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
//        pop();
    }
}
