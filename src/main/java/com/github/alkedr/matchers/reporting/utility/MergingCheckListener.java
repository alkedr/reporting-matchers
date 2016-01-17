package com.github.alkedr.matchers.reporting.utility;

import com.github.alkedr.matchers.reporting.ReportingMatcher;
import org.hamcrest.Matcher;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Collections.emptyIterator;

/*
Объединяет KeyValueChecks с объединяемыми ключами
Переносит простые матчеры в начало
Сохраняет порядок простых матчеров друг относительно друга
Сохраняет порядок reporting матчеров друг относительно друга

Алгоритм:
  - выполняем простые матчеры (из in) пока не найдём сложный
  - если нашли сложный, то запоминаем его (first) и продолжаем выполнять простые
  - если нашли второй сложный, то начинаем объединять эти два и все последующие сложные матчеры
    (map - промежуточный результат, merged - конечный результат)
  - если дошли до конца и оказалось, что там только один сложный, запускаем его без объединения
    (он будет лежать в first)

Используем LinkedHashMap чтобы сохранять порядок следования матчеров и извлечённых значений

TODO: всегда игнорировать пустые итераторы
 */
public class MergingCheckListener implements ReportingMatcher.CheckListener {
    private final ReportingMatcher.CheckListener next;
    private Iterator<ReportingMatcher.KeyValueChecks> first = emptyIterator();
    private Map<ReportingMatcher.Key, ReportingMatcher.KeyValueChecks> map = null;

    public MergingCheckListener(ReportingMatcher.CheckListener next) {
        this.next = next;
    }

    @Override
    public void simpleMatcher(Object item, Matcher<?> matcher) {
        next.simpleMatcher(item, matcher);
    }

    @Override
    public void simpleMatcherForMissingItem(Matcher<?> matcher) {
        next.simpleMatcherForMissingItem(matcher);
    }

    @Override
    public void keyValueChecksGroup(Iterator<ReportingMatcher.KeyValueChecks> keyValueChecksGroup) {
        if (map == null) {
            if (first.hasNext()) {
                map = new LinkedHashMap<>();
                addToMap(map, first);
                addToMap(map, keyValueChecksGroup);
            } else {
                first = keyValueChecksGroup;
            }
        } else {
            addToMap(map, keyValueChecksGroup);
        }
    }

    public void flush() {
        if (first.hasNext()) {
            next.keyValueChecksGroup(first);
        }
        if (map != null && !map.isEmpty()) {
            next.keyValueChecksGroup(map.values().iterator());
        }
    }

    private static void addToMap(Map<ReportingMatcher.Key, ReportingMatcher.KeyValueChecks> map,
                                 Iterator<ReportingMatcher.KeyValueChecks> keyValueChecksIterator) {
        keyValueChecksIterator.forEachRemaining(kvc -> map.merge(
                kvc.key(),
                kvc,
                (kvc1, kvc2) -> {
                    throw new RuntimeException("merge");
                    // TODO: merge
//                        return null;
                }
        ));
    }
}
