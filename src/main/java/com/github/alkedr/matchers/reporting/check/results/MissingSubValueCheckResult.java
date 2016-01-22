package com.github.alkedr.matchers.reporting.check.results;

import com.github.alkedr.matchers.reporting.keys.Key;
import com.github.alkedr.matchers.reporting.reporters.Reporter;

import java.util.Iterator;
import java.util.Objects;

class MissingSubValueCheckResult implements MergeableSubValueCheckResult {
    final Key key;
    final Iterator<? extends CheckResult> checkResults;

    MissingSubValueCheckResult(Key key, Iterator<? extends CheckResult> checkResults) {
        this.key = key;
        this.checkResults = checkResults;
    }

    @Override
    public void run(Reporter reporter) {
        reporter.beginMissingNode(key.asString());
        CheckResults.runAll(checkResults, reporter);
        reporter.endNode();
    }

    @Override
    public Iterator<? extends CheckResult> getCheckResults() {
        return checkResults;
    }

    @Override
    public MissingSubValueCheckResult createNewInstanceWithDifferentCheckResults(Iterator<? extends CheckResult> newCheckResults) {
        return new MissingSubValueCheckResult(key, newCheckResults);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MissingSubValueCheckResult)) return false;
        MissingSubValueCheckResult that = (MissingSubValueCheckResult) o;
        return Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
}
