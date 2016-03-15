package com.github.alkedr.matchers.reporting.reporters;

import com.github.alkedr.matchers.reporting.sub.value.keys.Key;

import java.util.function.Consumer;

public class UncheckedToIncorrectlyPresentConvertingReporter implements SafeTreeReporter {
    private final SafeTreeReporter next;

    public UncheckedToIncorrectlyPresentConvertingReporter(SafeTreeReporter next) {
        this.next = next;
    }

    @Override
    public void presentNode(Key key, Object value, Consumer<SafeTreeReporter> contents) {
        next.presentNode(
                key,
                value,
                safeTreeReporter -> {
                    CheckedFlagRecordingReporter checkedFlagRecordingReporter = new CheckedFlagRecordingReporter(
                            new UncheckedToIncorrectlyPresentConvertingReporter(safeTreeReporter)
                    );
                    contents.accept(checkedFlagRecordingReporter);
                    if (!checkedFlagRecordingReporter.getFlag()) {
                        safeTreeReporter.incorrectlyPresent();
                    }
                }
        );
    }

    @Override
    public void absentNode(Key key, Consumer<SafeTreeReporter> contents) {
        next.absentNode(key, contents);
    }

    @Override
    public void brokenNode(Key key, Throwable throwable, Consumer<SafeTreeReporter> contents) {
        next.brokenNode(key, throwable, contents);
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
        next.brokenCheck(description, throwable);
    }


    private static class CheckedFlagRecordingReporter implements SafeTreeReporter {
        private final SafeTreeReporter next;
        private boolean flag;

        CheckedFlagRecordingReporter(SafeTreeReporter next) {
            this.next = next;
        }

        public boolean getFlag() {
            return flag;
        }

        @Override
        public void presentNode(Key key, Object value, Consumer<SafeTreeReporter> contents) {
            next.presentNode(key, value, safeTreeReporter -> {
                CheckedFlagRecordingReporter checkedFlagRecordingReporter = new CheckedFlagRecordingReporter(safeTreeReporter);
                contents.accept(checkedFlagRecordingReporter);
                flag |= checkedFlagRecordingReporter.flag;
            });
        }

        @Override
        public void absentNode(Key key, Consumer<SafeTreeReporter> contents) {
            flag = true;
            next.absentNode(key, contents);
        }

        @Override
        public void brokenNode(Key key, Throwable throwable, Consumer<SafeTreeReporter> contents) {
            flag = true;
            next.brokenNode(key, throwable, contents);
        }

        @Override
        public void correctlyPresent() {
            flag = true;
            next.correctlyPresent();
        }

        @Override
        public void correctlyAbsent() {
            flag = true;
            next.correctlyAbsent();
        }

        @Override
        public void incorrectlyPresent() {
            flag = true;
            next.incorrectlyPresent();
        }

        @Override
        public void incorrectlyAbsent() {
            flag = true;
            next.incorrectlyAbsent();
        }

        @Override
        public void passedCheck(String description) {
            flag = true;
            next.passedCheck(description);
        }

        @Override
        public void failedCheck(String expected, String actual) {
            flag = true;
            next.failedCheck(expected, actual);
        }

        @Override
        public void checkForAbsentItem(String description) {
            flag = true;
            next.checkForAbsentItem(description);
        }

        @Override
        public void brokenCheck(String description, Throwable throwable) {
            flag = true;
            next.brokenCheck(description, throwable);
        }
    }
}
