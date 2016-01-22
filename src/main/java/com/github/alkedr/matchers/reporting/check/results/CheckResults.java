package com.github.alkedr.matchers.reporting.check.results;

import com.github.alkedr.matchers.reporting.keys.Key;
import com.github.alkedr.matchers.reporting.reporters.Reporter;

import java.util.Iterator;

// TODO: вынести в static final всё что можно?  есть ли смысл это делать для лямбд?
public class CheckResults {

    public static CheckResult correctlyPresent() {
        return Reporter::correctlyPresent;
    }

    public static CheckResult correctlyMissing() {
        return Reporter::correctlyMissing;
    }

    public static CheckResult incorrectlyPresent() {
        return Reporter::incorrectlyPresent;
    }

    public static CheckResult incorrectlyMissing() {
        return Reporter::incorrectlyMissing;
    }


    public static CheckResult passedMatcher(String matcherDescription) {
        return reporter -> reporter.passedCheck(matcherDescription);
    }

    public static CheckResult failedMatcher(String matcherDescription, String mismatchDescription) {
        return reporter -> reporter.failedCheck(matcherDescription, mismatchDescription);
    }

    public static CheckResult brokenMatcher(String matcherDescription, Throwable throwable) {
        return reporter -> reporter.brokenCheck(matcherDescription, throwable);
    }

    public static CheckResult matcherForMissingItem(String matcherDescription) {
        return reporter -> reporter.checkForMissingItem(matcherDescription);
    }

    // TODO: passedEqualToMatcher, failedEqualToMatcher?


    public static MergeableSubValueCheckResult presentSubValue(Key key, Object value, Iterator<? extends CheckResult> checkResults) {
        return new PresentSubValueCheckResult(key, value, checkResults);
    }

    public static MergeableSubValueCheckResult missingSubValue(Key key, Iterator<? extends CheckResult> checkResults) {
        return new MissingSubValueCheckResult(key, checkResults);
    }

    public static MergeableSubValueCheckResult brokenSubValue(Key key, Throwable throwable, Iterator<? extends CheckResult> checkResults) {
        return new BrokenSubValueCheckResult(key, throwable, checkResults);
    }

    // TODO: subValueOfMissingSubValue?


    public static SequenceOfMergedSubValueCheckResults sequenceOfMergedSubValues(Iterator<? extends MergeableSubValueCheckResult> subValueChecksIterator) {
        return new SequenceOfMergedSubValueCheckResultsImpl(subValueChecksIterator);
    }


    public static Iterator<CheckResult> merge(Iterator<CheckResult> checkResultIterator) {
        return new MergingCheckResultsIterator(checkResultIterator);
    }


    public static void runAll(Iterator<? extends CheckResult> checkResultIterator, Reporter reporter) {
        while (checkResultIterator.hasNext()) {
            checkResultIterator.next().run(reporter);
        }
    }

}
