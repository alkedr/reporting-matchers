package com.github.alkedr.matchers.reporting.check.results;

import java.util.Iterator;

public interface MergeableSubValueCheckResult extends CheckResult {
    @Override boolean equals(Object other);
    @Override int hashCode();
    Iterator<? extends CheckResult> getCheckResults();
    MergeableSubValueCheckResult createNewInstanceWithDifferentCheckResults(Iterator<? extends CheckResult> newCheckResults);
}
