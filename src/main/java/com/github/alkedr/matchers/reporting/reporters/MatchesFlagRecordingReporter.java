package com.github.alkedr.matchers.reporting.reporters;

import com.github.alkedr.matchers.reporting.keys.Key;

import java.util.function.Consumer;

public class MatchesFlagRecordingReporter extends ReporterWrapper {
    private boolean matchesFlag = true;

    public MatchesFlagRecordingReporter() {
    }

    public MatchesFlagRecordingReporter(Reporter wrappedReporter) {
        super(wrappedReporter);
    }

    public boolean getMatchesFlag() {
        return matchesFlag;
    }

    @Override
    public void presentNode(Key key, Object value, Consumer<Reporter> contents) {
        super.presentNode(key, value, createReportConsumerWrapper(contents));
    }

    @Override
    public void missingNode(Key key, Consumer<Reporter> contents) {
        super.missingNode(key, createReportConsumerWrapper(contents));
    }

    @Override
    public void brokenNode(Key key, Throwable throwable, Consumer<Reporter> contents) {
        matchesFlag = false;
        super.brokenNode(key, throwable, contents);
    }

    @Override
    public void incorrectlyPresent() {
        matchesFlag = false;
        super.incorrectlyPresent();
    }

    @Override
    public void incorrectlyMissing() {
        matchesFlag = false;
        super.incorrectlyMissing();
    }

    @Override
    public void failedCheck(String expected, String actual) {
        matchesFlag = false;
        super.failedCheck(expected, actual);
    }

    @Override
    public void checkForMissingItem(String description) {
        matchesFlag = false;
        super.checkForMissingItem(description);
    }

    @Override
    public void brokenCheck(String description, Throwable throwable) {
        matchesFlag = false;
        super.brokenCheck(description, throwable);
    }


    private Consumer<Reporter> createReportConsumerWrapper(Consumer<Reporter> contents) {
        return r -> {
            MatchesFlagRecordingReporter matchesFlagRecordingReporter = new MatchesFlagRecordingReporter(r);
            contents.accept(matchesFlagRecordingReporter);
            matchesFlag &= matchesFlagRecordingReporter.matchesFlag;
        };
    }
}
