package com.github.alkedr.matchers.reporting.check.results;

import com.google.common.collect.Iterators;

import java.util.*;

// не умеет мержить кастомные реализации Check?
class MergingCheckResultsIterator implements Iterator<CheckResult> {
    private final Iterator<CheckResult> in;
    private CheckResult first = null;
    private Map<MergeableSubValueCheckResult, Collection<Iterator<? extends CheckResult>>> map = null;

    MergingCheckResultsIterator(Iterator<CheckResult> in) {
        this.in = in;
    }

    @Override
    public boolean hasNext() {
        return in.hasNext() || !firstIsEmpty() || !mapIsEmpty();
    }

    // 1. Возвращаем in.next() пока не найдём первый SequenceOfMergedSubValueCheckResults или
    //    MergeableSubValueCheckResult, сохраняем его в first
    // 2. Возвращаем in.next() пока не найдём второй SequenceOfMergedSubValueCheckResults или
    //    MergeableSubValueCheckResult, добавляем first и second в мапу
    // 3. Пока не дойдём до конца in:
    //    - SequenceOfMergedSubValueCheckResults, MergeableSubValueCheckResult добавляем в мапу
    //    - Всё остальное возвращаем без изменений
    // 4. Пока не дойдём до конца мапы, возвращаем следующий элемент мапы
    @Override
    public CheckResult next() {
        while (in.hasNext()) {
            CheckResult next = in.next();
            if (needsToBeMerged(next)) {
                rememberCheckResultThatNeedsToBeMerged(next);
            } else {
                return next;
            }
        }
        return getMergedRememberedCheckResults();
    }

    private static boolean needsToBeMerged(CheckResult next) {
        return next instanceof MergeableSubValueCheckResult || next instanceof SequenceOfMergedSubValueCheckResults;
    }

    private void rememberCheckResultThatNeedsToBeMerged(CheckResult next) {
        if (mapIsEmpty()) {
            if (firstIsEmpty()) {
                first = next;
            } else {
                map = new LinkedHashMap<>();
                addToMap(first);
                addToMap(next);
                first = null;
            }
        } else {
            addToMap(next);
        }
    }

    private CheckResult getMergedRememberedCheckResults() {
        if (!firstIsEmpty()) {
            CheckResult result = first;
            first = null;
            return result;
        }

        if (!mapIsEmpty()) {
            CheckResult result = CheckResults.sequenceOfMergedSubValues(
                    Iterators.transform(
                            Iterators.consumingIterator(map.entrySet().iterator()),
                            entry -> entry.getKey().createNewInstanceWithDifferentCheckResults(new MergingCheckResultsIterator(Iterators.concat(entry.getValue().iterator())))
                    )
            );
            map = null;
            return result;
        }

        throw new NoSuchElementException();  // FIXME
    }

    private boolean firstIsEmpty() {
        return first == null
                || (first instanceof SequenceOfMergedSubValueCheckResults
                    && !((SequenceOfMergedSubValueCheckResults) first).getSubValueChecksIterator().hasNext());
    }

    private boolean mapIsEmpty() {
        return map == null || map.isEmpty();
    }

    private void addToMap(CheckResult checkResult) {
        if (checkResult instanceof MergeableSubValueCheckResult) {
            addToMap((MergeableSubValueCheckResult) checkResult);
        }
        if (checkResult instanceof SequenceOfMergedSubValueCheckResults) {
            addToMap((SequenceOfMergedSubValueCheckResults) checkResult);
        }
    }

    private void addToMap(MergeableSubValueCheckResult checkResult) {
        map.computeIfAbsent(checkResult, k -> new ArrayList<>()).add(checkResult.getCheckResults());
    }

    private void addToMap(SequenceOfMergedSubValueCheckResults checkResults) {
        while (checkResults.getSubValueChecksIterator().hasNext()) {
            addToMap(checkResults.getSubValueChecksIterator().next());
        }
    }
}
