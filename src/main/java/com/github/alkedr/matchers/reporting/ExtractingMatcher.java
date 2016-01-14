package com.github.alkedr.matchers.reporting;

import org.apache.commons.collections4.iterators.SingletonIterator;
import org.hamcrest.Description;

import java.util.Iterator;

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
    private final Extractor extractor;
    private final Checks checks;

    public ExtractingMatcher(Extractor extractor, Checks checks) {
        this.extractor = extractor;
        this.checks = checks;
    }

    @Override
    public Iterator<Object> run(Object item) {
        Extractor.KeyValue keyValue = extractor.extractFrom(item);
        return new SingletonIterator<>(new SingletonIterator<>(new KeyValueChecks(keyValue.key, keyValue.value, checks)));
    }

    @Override
    public Iterator<Object> runForMissingItem() {
        Extractor.KeyValue keyValue = extractor.extractFromMissingItem();
        return new SingletonIterator<>(new SingletonIterator<>(new KeyValueChecks(keyValue.key, keyValue.value, checks)));
    }

    @Override
    public void describeTo(Description description) {
//        description.appendText(name);
        // TODO: append matcher.describeTo()
    }

    public Extractor getExtractor() {
        return extractor;
    }

    public Checks getChecks() {
        return checks;
    }


    // TODO: TypeSafeExtractor ?
    // TODO: объединить Extractor, ExtractingMatcher и ExtractingMatcherBuilder?
    public interface Extractor {
        KeyValue extractFrom(Object item);
        KeyValue extractFromMissingItem();

        class KeyValue {
            final Key key;
            final Value value;

            public KeyValue(Key key, Value value) {
                this.key = key;
                this.value = value;
            }
        }
    }
}
