package com.github.alkedr.matchers.reporting.reporters;

import com.github.alkedr.matchers.reporting.sub.value.keys.Key;

// TODO: добавить сообщения, объясняющие кто бросил исключение: матчер или экстрактор
class BrokenThrowingReporter implements SimpleTreeReporter {
    private final SimpleTreeReporter next;

    BrokenThrowingReporter(SimpleTreeReporter next) {
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
        throw new BrokenCheckException(key.asString(), throwable);
    }

    @Override
    public void endNode() {
        next.endNode();
    }

    @Override
    public void correctlyPresent() {
        next.correctlyPresent();
    }

    @Override
    public void correctlyAbsent() {
        next.correctlyAbsent();
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
        next.passedCheck(description);
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
        throw new BrokenCheckException(description, throwable);
    }


    public static class BrokenCheckException extends RuntimeException {
        public BrokenCheckException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
