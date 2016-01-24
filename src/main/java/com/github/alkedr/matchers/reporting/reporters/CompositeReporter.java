package com.github.alkedr.matchers.reporting.reporters;

import com.github.alkedr.matchers.reporting.keys.Key;

import java.util.function.Consumer;

import static java.util.Arrays.asList;

// TODO: вызывать contents только один раз!
// TODO: или сделать MatchesFlagRecordingReporter обёрткой? Тогда нужен базовый класс для обёрток
@Deprecated
public class CompositeReporter implements Reporter {
    private final Iterable<Reporter> reporters;

    public CompositeReporter(Reporter... reporters) {
        this(asList(reporters));
    }

    public CompositeReporter(Iterable<Reporter> reporters) {
        this.reporters = reporters;
    }

    @Override
    public void presentNode(Key key, Object value, Consumer<Reporter> contents) {
        for (Reporter reporter : reporters) {
            reporter.presentNode(key, value, contents);
        }
    }

    @Override
    public void missingNode(Key key, Consumer<Reporter> contents) {
        for (Reporter reporter : reporters) {
            reporter.missingNode(key, contents);
        }
    }

    @Override
    public void brokenNode(Key key, Throwable throwable, Consumer<Reporter> contents) {
        for (Reporter reporter : reporters) {
            reporter.brokenNode(key, throwable, contents);
        }
    }

    @Override
    public void correctlyPresent() {
        for (Reporter reporter : reporters) {
            reporter.correctlyPresent();
        }
    }

    @Override
    public void correctlyMissing() {
        for (Reporter reporter : reporters) {
            reporter.correctlyMissing();
        }
    }

    @Override
    public void incorrectlyPresent() {
        for (Reporter reporter : reporters) {
            reporter.incorrectlyPresent();
        }
    }

    @Override
    public void incorrectlyMissing() {
        for (Reporter reporter : reporters) {
            reporter.incorrectlyMissing();
        }
    }

    @Override
    public void passedCheck(String description) {
        for (Reporter reporter : reporters) {
            reporter.passedCheck(description);
        }
    }

    @Override
    public void failedCheck(String expected, String actual) {
        for (Reporter reporter : reporters) {
            reporter.failedCheck(expected, actual);
        }
    }

    @Override
    public void checkForMissingItem(String description) {
        for (Reporter reporter : reporters) {
            reporter.checkForMissingItem(description);
        }
    }

    @Override
    public void brokenCheck(String description, Throwable throwable) {
        for (Reporter reporter : reporters) {
            reporter.brokenCheck(description, throwable);
        }
    }
}
