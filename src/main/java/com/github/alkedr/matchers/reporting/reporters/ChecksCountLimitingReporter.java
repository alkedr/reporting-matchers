package com.github.alkedr.matchers.reporting.reporters;

import com.github.alkedr.matchers.reporting.sub.value.keys.Key;

// не удаляет пустые ноды
// не учитывает ноды при подсчёте кол-ва проверок
// TODO: для каждой ноды подсчитывать кол-во пропущенных проверок и добавлять в отчёт если > 0
class ChecksCountLimitingReporter implements SimpleTreeReporter {
    private final SimpleTreeReporter next;
    private final int maxChecksCount;
    private int checksCount;

    ChecksCountLimitingReporter(SimpleTreeReporter next, int maxChecksCount) {
        this.next = next;
        this.maxChecksCount = maxChecksCount;
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
        if (checksCount++ < maxChecksCount) {
            next.correctlyPresent();
        }
    }

    @Override
    public void correctlyAbsent() {
        if (checksCount++ < maxChecksCount) {
            next.correctlyAbsent();
        }
    }

    @Override
    public void incorrectlyPresent() {
        if (checksCount++ < maxChecksCount) {
            next.incorrectlyPresent();
        }
    }

    @Override
    public void incorrectlyAbsent() {
        if (checksCount++ < maxChecksCount) {
            next.incorrectlyAbsent();
        }
    }

    @Override
    public void passedCheck(String description) {
        if (checksCount++ < maxChecksCount) {
            next.passedCheck(description);
        }
    }

    @Override
    public void failedCheck(String expected, String actual) {
        if (checksCount++ < maxChecksCount) {
            next.failedCheck(expected, actual);
        }
    }

    @Override
    public void checkForAbsentItem(String description) {
        if (checksCount++ < maxChecksCount) {
            next.checkForAbsentItem(description);
        }
    }

    @Override
    public void brokenCheck(String description, Throwable throwable) {
        if (checksCount++ < maxChecksCount) {
            next.brokenCheck(description, throwable);
        }
    }
}
