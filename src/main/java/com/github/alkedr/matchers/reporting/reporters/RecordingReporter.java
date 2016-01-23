package com.github.alkedr.matchers.reporting.reporters;

import com.github.alkedr.matchers.reporting.keys.Key;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

public class RecordingReporter implements Reporter {
    private final Collection<Consumer<Reporter>> invocations = new ArrayList<>();


    public void playback(Reporter reporter) {
        for (Consumer<Reporter> invocation : invocations) {
            invocation.accept(reporter);
        }
    }


    @Override
    public void beginNode(Key key, Object value) {
        invocations.add(reporter -> reporter.beginNode(key, value));
    }

    @Override
    public void beginMissingNode(Key key) {
        invocations.add(reporter -> reporter.beginMissingNode(key));
    }

    @Override
    public void beginBrokenNode(Key key, Throwable throwable) {
        invocations.add(reporter -> reporter.beginBrokenNode(key, throwable));
    }

    @Override
    public void correctlyPresent() {
        invocations.add(Reporter::correctlyPresent);
    }

    @Override
    public void correctlyMissing() {
        invocations.add(Reporter::correctlyMissing);
    }

    @Override
    public void incorrectlyPresent() {
        invocations.add(Reporter::incorrectlyPresent);
    }

    @Override
    public void incorrectlyMissing() {
        invocations.add(Reporter::incorrectlyMissing);
    }

    @Override
    public void passedCheck(String description) {
        invocations.add(reporter -> reporter.passedCheck(description));
    }

    @Override
    public void failedCheck(String expected, String actual) {
        invocations.add(reporter -> reporter.failedCheck(expected, actual));
    }

    @Override
    public void checkForMissingItem(String description) {
        invocations.add(reporter -> reporter.checkForMissingItem(description));
    }

    @Override
    public void brokenCheck(String description, Throwable throwable) {
        invocations.add(reporter -> reporter.brokenCheck(description, throwable));
    }

    @Override
    public void endNode() {
        invocations.add(Reporter::endNode);
    }
}
