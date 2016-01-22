package com.github.alkedr.matchers.reporting.check.results;

import java.util.Iterator;

// используется для того, чтобы не мержить огромный выхлоп IteratorMatcher'ов, который заведомо не мержится
// merge() тоже помечает вещи этим интерфейсом
/*
Это не работает!
1) IteratorMatcher не может вернуть отдельно SequenceOfMergedSubValueCheckResults и свои другие проверки
2) Это не нужно, потому что можно просто не мержить. Возможность не мержить нужна в любом случае.

Можно ли избавиться от всех CheckResult'ов?
Можно мержить через репортер если передавать туда ключ
 */
public interface SequenceOfMergedSubValueCheckResults extends CheckResult {
    Iterator<? extends MergeableSubValueCheckResult> getSubValueChecksIterator();
}
