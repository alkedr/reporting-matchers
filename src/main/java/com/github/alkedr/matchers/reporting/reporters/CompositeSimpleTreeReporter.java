package com.github.alkedr.matchers.reporting.reporters;

import com.github.alkedr.matchers.reporting.sub.value.keys.Key;

class CompositeSimpleTreeReporter implements SimpleTreeReporter {
    private final Iterable<SimpleTreeReporter> reporters;

    CompositeSimpleTreeReporter(Iterable<SimpleTreeReporter> reporters) {
        this.reporters = reporters;
    }

    @Override
    public void correctlyPresent() {
        for (SimpleTreeReporter safeTreeReporter : reporters) {
            safeTreeReporter.correctlyPresent();
        }
    }

    @Override
    public void correctlyMissing() {
        for (SimpleTreeReporter safeTreeReporter : reporters) {
            safeTreeReporter.correctlyMissing();
        }
    }

    @Override
    public void incorrectlyPresent() {
        for (SimpleTreeReporter safeTreeReporter : reporters) {
            safeTreeReporter.incorrectlyPresent();
        }
    }

    @Override
    public void incorrectlyMissing() {
        for (SimpleTreeReporter safeTreeReporter : reporters) {
            safeTreeReporter.incorrectlyMissing();
        }
    }

    @Override
    public void passedCheck(String description) {
        for (SimpleTreeReporter safeTreeReporter : reporters) {
            safeTreeReporter.passedCheck(description);
        }
    }

    @Override
    public void failedCheck(String expected, String actual) {
        for (SimpleTreeReporter safeTreeReporter : reporters) {
            safeTreeReporter.failedCheck(expected, actual);
        }
    }

    @Override
    public void checkForMissingItem(String description) {
        for (SimpleTreeReporter safeTreeReporter : reporters) {
            safeTreeReporter.checkForMissingItem(description);
        }
    }

    @Override
    public void brokenCheck(String description, Throwable throwable) {
        for (SimpleTreeReporter safeTreeReporter : reporters) {
            safeTreeReporter.brokenCheck(description, throwable);
        }
    }

    @Override
    public void beginPresentNode(Key key, Object value) {
        for (SimpleTreeReporter safeTreeReporter : reporters) {
            safeTreeReporter.beginPresentNode(key, value);
        }
    }

    @Override
    public void beginMissingNode(Key key) {
        for (SimpleTreeReporter safeTreeReporter : reporters) {
            safeTreeReporter.beginMissingNode(key);
        }
    }

    @Override
    public void beginBrokenNode(Key key, Throwable throwable) {
        for (SimpleTreeReporter safeTreeReporter : reporters) {
            safeTreeReporter.beginBrokenNode(key, throwable);
        }
    }

    @Override
    public void endNode() {
        for (SimpleTreeReporter safeTreeReporter : reporters) {
            safeTreeReporter.endNode();
        }
    }
}
