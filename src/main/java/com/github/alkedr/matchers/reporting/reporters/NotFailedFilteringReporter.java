package com.github.alkedr.matchers.reporting.reporters;

import com.github.alkedr.matchers.reporting.sub.value.keys.Key;

// не удаляет пустые ноды
class NotFailedFilteringReporter implements SimpleTreeReporter {
    private final SimpleTreeReporter next;

    NotFailedFilteringReporter(SimpleTreeReporter next) {
        this.next = next;
    }

    @Override
    public void beginPresentNode(Key key, Object value) {
        next.beginPresentNode(key, value);
    }

    @Override
    public void beginAbsentNode(Key key) {
        next.beginAbsentNode(key);
    }

    @Override
    public void beginBrokenNode(Key key, Throwable throwable) {
        next.beginBrokenNode(key, throwable);
    }

    @Override
    public void endNode() {
        next.endNode();
    }

    @Override
    public void correctlyPresent() {
    }

    @Override
    public void correctlyAbsent() {
    }

    @Override
    public void incorrectlyPresent() {
        next.incorrectlyPresent();
    }

    @Override
    public void incorrectlyAbsent() {
        next.incorrectlyAbsent();
    }

    @Override
    public void passedCheck(String description) {
    }

    @Override
    public void failedCheck(String expected, String actual) {
        next.failedCheck(expected, actual);
    }

    @Override
    public void checkForAbsentItem(String description) {
        next.checkForAbsentItem(description);
    }

    @Override
    public void brokenCheck(String description, Throwable throwable) {
        next.brokenCheck(description, throwable);
    }
}
