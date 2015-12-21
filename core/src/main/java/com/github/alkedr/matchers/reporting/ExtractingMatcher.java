package com.github.alkedr.matchers.reporting;

import org.hamcrest.Description;

import java.io.PrintWriter;
import java.io.StringWriter;

/*
Объединение:
  - N ExtractingMatcher'ов -> ExtractingMatcher  (нужно сделать мапу extractor -> matchers)
  - N IteratorMatcher'ов -> IteratorMatcher/ReportingMatcher  (если итерируются по одному и тому же, то объединяем, иначе склеиваем)
  - ExtractingMatcher + IteratorMatcher -> IteratorMatcher (нужно добавить мапу extractor -> matchers в IteratorMatcher?)
  - N ReportingMatcher'ов -> ReportingMatcher  (склеивание)


У ExtractingMatcher и IteratorMatcher общее то, что они извлекают из item'а список KeyValuePair'ов
Объединять сами списки KeyValuePair'ов не хочется, потому что для этого нужно их держать в памяти в мапе
Значит нужно объединять матчеры, которые их генерируют (экстракторы экстракторов), и получать один матчер, который сгенерирует один список KeyValuePair'ов
Если объединяем без удерживания всего в памяти, то придётся запускать матчеры не в том порядке, в котором они были добавлены


public class MergeableMatcher {
    private final Map<ExtractingMatcher.Extractor, Iterable<Matcher<?>>> extractorToMatchersMap;
    private final KeyValuePairIteratorFactory keyValuePairIteratorFactory;


    interface KeyValuePairIteratorFactory {
        Iterator<KeyValuePair> createIterator(Object item);
        Iterator<KeyValuePair> createIteratorForMissingItem();

        class KeyValuePair {
            private final String keyAsString;
            private final ReportingMatcher.Reporter.ValueStatus status;
            private final String valueAsString;
            private final Object value;
            private final Iterable<Matcher<?>> matchers;
        }
    }
}



Ещё можно не объединять, а делать несколько разделов в отчёте

проверка 1:
    мой объект:
        моё поле:
            мои проверки 1
проверка 2:
    мой объект:
        моё поле:
            мои проверки 2

вместо

мой объект:
    моё поле:
        мои проверки 1 и 2

Но тогда не получится по-нормальному сделать извлечение непроверенных значений, потому что чтобы его сделать нужно
объединять матчер, извлекающий непроверенные значения с остальными матчерами

 */
public class ExtractingMatcher<T> extends BaseReportingMatcher<T> {
    private final String name;
    private final Extractor extractor;
    private final ReportingMatcher<?> matcher;

    public ExtractingMatcher(String name, Extractor extractor, ReportingMatcher<?> matcher) {
        this.name = name;
        this.extractor = extractor;
        this.matcher = matcher;
    }

    @Override
    public void run(Object item, Reporter reporter) {
        run(extractor.extractFrom(item), reporter);
    }

    @Override
    public void runForMissingItem(Reporter reporter) {
        run(Extractor.ExtractedValue.missing(), reporter);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(name);
        // TODO: append matcher.describeTo()
    }

    public String getName() {
        return name;
    }

    public Extractor getExtractor() {
        return extractor;
    }

    public ReportingMatcher<?> getMatcher() {
        return matcher;
    }

    private void run(Extractor.ExtractedValue extractedValue, Reporter reporter) {
        reporter.beginKeyValuePair(name, extractedValue.getStatus(), extractedValue.getValueAsString());
        if (extractedValue.getStatus() == Reporter.ValueStatus.NORMAL) {
            matcher.run(extractedValue.getValue(), reporter);
        } else {
            matcher.runForMissingItem(reporter);
        }
        reporter.endKeyValuePair();
    }


    // Если придётся добавлять методы для объединения, то нужно будет написать для них реализации по умолчанию
    // TODO: сделать этот интерфейс абстрактным классом?
    public interface Extractor {
        ExtractedValue extractFrom(Object item);

        class ExtractedValue {
            private final Reporter.ValueStatus status;
            private final String valueAsString;
            private final Object value;

            private ExtractedValue(Reporter.ValueStatus status, String valueAsString, Object value) {
                this.status = status;
                this.valueAsString = valueAsString;
                this.value = value;
            }

            public Reporter.ValueStatus getStatus() {
                return status;
            }

            public String getValueAsString() {
                return valueAsString;
            }

            public Object getValue() {
                return value;
            }

            public static <U> ExtractedValue normal(U value) {
                return normal(String.valueOf(value), value);  // TODO: String.valueOf только для примитивных типов
            }

            public static <U> ExtractedValue normal(String valueAsString, U value) {
                return new ExtractedValue(Reporter.ValueStatus.NORMAL, valueAsString, value);
            }

            public static <U> ExtractedValue missing() {  // TODO: (missing), (broken)?, исключение там же, где и матчеры
                return new ExtractedValue(Reporter.ValueStatus.MISSING, "", null);   // TODO: static instance
            }

            public static <U> ExtractedValue broken(String errorMessage) {
                return new ExtractedValue(Reporter.ValueStatus.BROKEN, errorMessage, null);
            }

            // здесь Throwable, но реализации Extractor'а должны ловить только Exception
            public static <U> ExtractedValue broken(Throwable throwable) {
                // TODO: убрать дублирование с ReportingMatcherAdapter.run
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw, true);
                throwable.printStackTrace(pw);
                return broken(sw.getBuffer().toString());
            }
        }
    }
}
