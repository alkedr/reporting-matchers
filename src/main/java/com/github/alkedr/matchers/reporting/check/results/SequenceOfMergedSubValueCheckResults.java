package com.github.alkedr.matchers.reporting.check.results;

import java.util.Iterator;

// используется для того, чтобы не мержить огромный выхлоп IteratorMatcher'ов, который заведомо не мержится
// merge() тоже помечает вещи этим интерфейсом
public interface SequenceOfMergedSubValueCheckResults extends CheckResult {
    Iterator<? extends MergeableSubValueCheckResult> getSubValueChecksIterator();
}
