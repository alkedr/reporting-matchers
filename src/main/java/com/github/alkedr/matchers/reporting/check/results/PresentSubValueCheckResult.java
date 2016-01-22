package com.github.alkedr.matchers.reporting.check.results;

import com.github.alkedr.matchers.reporting.keys.Key;
import com.github.alkedr.matchers.reporting.reporters.Reporter;
import org.apache.commons.lang3.ClassUtils;

import java.util.Iterator;
import java.util.Objects;

class PresentSubValueCheckResult implements MergeableSubValueCheckResult {
    final Key key;
    final Object value;
    final Iterator<? extends CheckResult> checkResults;

    PresentSubValueCheckResult(Key key, Object value, Iterator<? extends CheckResult> checkResults) {
        this.key = key;
        this.value = value;
        this.checkResults = checkResults;
    }

    @Override
    public void run(Reporter reporter) {
        reporter.beginNode(key.asString(), value);
        CheckResults.runAll(checkResults, reporter);
        reporter.endNode();
    }

    @Override
    public Iterator<? extends CheckResult> getCheckResults() {
        return checkResults;
    }

    @Override
    public PresentSubValueCheckResult createNewInstanceWithDifferentCheckResults(Iterator<? extends CheckResult> newCheckResults) {
        return new PresentSubValueCheckResult(key, value, newCheckResults);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PresentSubValueCheckResult)) return false;
        PresentSubValueCheckResult that = (PresentSubValueCheckResult) o;
        // сравниваем value по-умному (equals для примитивных, == для остальных)
        if (value == null) {
            if (that.value != null) {
                return false;
            }
        } else {
            if (ClassUtils.isPrimitiveWrapper(value.getClass())) {
                if (!Objects.equals(value, that.value)) {
                    return false;
                }
            } else {
                if (value != that.value) {
                    return false;
                }
            }
        }
        return Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
}
