package com.github.alkedr.matchers.reporting.check.results;

import com.github.alkedr.matchers.reporting.keys.Key;
import com.github.alkedr.matchers.reporting.reporters.Reporter;

import java.util.Iterator;
import java.util.Objects;

class BrokenSubValueCheckResult implements MergeableSubValueCheckResult {
    final Key key;
    final Throwable throwable;
    final Iterator<? extends CheckResult> checkResults;

    BrokenSubValueCheckResult(Key key, Throwable throwable, Iterator<? extends CheckResult> checkResults) {
        this.key = key;
        this.throwable = throwable;
        this.checkResults = checkResults;
    }

    @Override
    public void run(Reporter reporter) {
        reporter.beginBrokenNode(key.asString(), throwable);
        CheckResults.runAll(checkResults, reporter);
        reporter.endNode();
    }

    @Override
    public Iterator<? extends CheckResult> getCheckResults() {
        return checkResults;
    }

    @Override
    public BrokenSubValueCheckResult createNewInstanceWithDifferentCheckResults(Iterator<? extends CheckResult> newCheckResults) {
        return new BrokenSubValueCheckResult(key, throwable, newCheckResults);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BrokenSubValueCheckResult)) return false;
        BrokenSubValueCheckResult that = (BrokenSubValueCheckResult) o;
        return Objects.equals(key, that.key) &&
                Objects.equals(throwable, that.throwable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, throwable);
    }
}
