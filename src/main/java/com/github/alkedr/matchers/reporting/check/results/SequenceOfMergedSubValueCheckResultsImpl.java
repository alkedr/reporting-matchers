package com.github.alkedr.matchers.reporting.check.results;

import com.github.alkedr.matchers.reporting.reporters.Reporter;

import java.util.Iterator;

class SequenceOfMergedSubValueCheckResultsImpl implements SequenceOfMergedSubValueCheckResults {
    private final Iterator<? extends MergeableSubValueCheckResult> subValueChecksIterator;

    SequenceOfMergedSubValueCheckResultsImpl(Iterator<? extends MergeableSubValueCheckResult> subValueChecksIterator) {
        this.subValueChecksIterator = subValueChecksIterator;
    }

    @Override
    public Iterator<? extends MergeableSubValueCheckResult> getSubValueChecksIterator() {
        return subValueChecksIterator;
    }

    @Override
    public void run(Reporter reporter) {
        CheckResults.runAll(subValueChecksIterator, reporter);
    }
}
