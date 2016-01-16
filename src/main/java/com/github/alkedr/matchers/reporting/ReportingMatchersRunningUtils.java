package com.github.alkedr.matchers.reporting;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Collections.emptyIterator;

public class ReportingMatchersRunningUtils {


    public static void runReportingMatcher(Reporter reporter, Object item, ReportingMatcher<?> reportingMatcher) {
        runReportingMatcherChecks(reporter, item, reportingMatcher.run(item));
    }

    public static void runReportingMatcherChecks(Reporter reporter, Object item, Iterator<Object> iterator) {
        while (iterator.hasNext()) {
            Object next = iterator.next();
            if (next instanceof Matcher) {
                runSimpleMatcher(reporter, item, (Matcher<?>) next);
            } else if (next instanceof Iterator) {
                runKeyValueCheckses(reporter, item, (Iterator<?>) next);
            } else {
                throw new RuntimeException("БАГА: " + next.getClass().getName());  // FIXME
            }
        }
    }


    // TODO: отдельная обёртка для Matcher'ов (?и ReportingMatcher'ов?), которая ловит исключения и делает BROKEN
    // TODO: (сделать ловлю исключений опциональной/настраиваемой)
    // TODO: м. б. в Iterator<Object> сделать не Matcher, а объект, который умеет добавляться в отчёт?
    private static void runSimpleMatcher(Reporter reporter, Object item, Matcher<?> matcher) {
        boolean matches;
        try {
            matches = matcher.matches(item);
        } catch (RuntimeException e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw, true);
            e.printStackTrace(pw);
            reporter.addCheck(Reporter.CheckStatus.BROKEN, sw.getBuffer().toString());
            return;
        }
        if (matches) {
            // TODO: подсовывать свой Description, который отлавливает equalTo и is
            reporter.addCheck(Reporter.CheckStatus.PASSED, StringDescription.toString(matcher));
        } else {
            Description stringDescription = new StringDescription()
                    .appendText("Expected: ")
                    .appendDescriptionOf(matcher)
                    .appendText("\n     but: ");
            matcher.describeMismatch(item, stringDescription);
            reporter.addCheck(Reporter.CheckStatus.FAILED, stringDescription.toString());
        }
    }

    private static void runKeyValueCheckses(Reporter reporter, Object item, Iterator<?> iterator) {
        while (iterator.hasNext()) {
            Object next = iterator.next();
            if (next instanceof ReportingMatcher.KeyValueChecks) {
                runKeyValueChecks(reporter, item, (ReportingMatcher.KeyValueChecks) next);
            } else {
                throw new RuntimeException();  // FIXME
            }
        }
    }

    private static void runKeyValueChecks(Reporter reporter, Object item, ReportingMatcher.KeyValueChecks kvc) {
        reporter.beginKeyValuePair(kvc.key().asString(), null, kvc.value().asString());
        runReportingMatcher(reporter, kvc.value().get(), kvc.checks().getMatcher());   // TODO: missing, broken etc
        reporter.endKeyValuePair();
    }




    // TODO: описать что такое Iterator<Object> (ссылка на ReportingMatcher)
    // TODO: описать изменение порядка элементов
    public static Iterator<Object> mergeReportingMatcherChecks(Iterator<Object> in) {
        return new MergingChecksIterator(in);
    }


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
    private static class MergingChecksIterator implements Iterator<Object> {
        private final Iterator<Object> in;
        private Iterator<ReportingMatcher.KeyValueChecks> first = emptyIterator();
        private Map<ReportingMatcher.Key, ReportingMatcher.KeyValueChecks> map = null;

        MergingChecksIterator(Iterator<Object> in) {
            this.in = in;
        }

        @Override
        public boolean hasNext() {
            return in.hasNext() || first.hasNext() || (map != null && !map.isEmpty());
        }

        @Override
        public Object next() {
            // Пока in не пуст, нужно читать in
            while (in.hasNext()) {
                Object result = in.next();
                if (result instanceof Matcher) {
                    // Если это не итератор, а простой матчер, возвращаем его сразу
                    return result;
                }
                if (!first.hasNext()) {
                    // Если это первый итератор, то запоминаем его и читаем in дальше.
                    // Это нужно чтобы можно было ничего не мёржить через мапу если в in только один итератор.
                    first = (Iterator<ReportingMatcher.KeyValueChecks>) result;
                } else {
                    // Если это не первый итератор, добавляем его и первый итератор в мапу
                    // Первый итератор будет добавляться много раз, но это не страшно, потому что второй и последующие
                    // разы он будет пустым
                    map = new LinkedHashMap<>();
                    addToMap(map, first);
                    addToMap(map, (Iterator<ReportingMatcher.KeyValueChecks>) result);
                }
                // TODO: что если не Matcher и не Iterator?
            }
            // Если in пуст, то это значит, что все простые уже матчеры были обработаны, остались только итераторы
            // Если в in был только один итератор, то он лежит в first, map в таком случае непроинициализирована
            // Если в in было несколько итераторов, то мы их объединили, и результат объединения лежит в map
            Iterator<ReportingMatcher.KeyValueChecks> result = map == null ? first : map.values().iterator();
            map = null;  // Зануляем мапу чтобы следующий hasNext вернул false
            return result;
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
}
