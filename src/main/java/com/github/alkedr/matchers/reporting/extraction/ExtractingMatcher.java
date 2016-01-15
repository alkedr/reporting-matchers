package com.github.alkedr.matchers.reporting.extraction;

import com.github.alkedr.matchers.reporting.base.BaseReportingMatcher;
import org.apache.commons.collections4.iterators.SingletonIterator;
import org.hamcrest.Description;

import java.util.Iterator;

import static com.github.alkedr.matchers.reporting.utility.NoOpMatcher.noOp;

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
// TODO: TypeSafeExtractingMatcher ?
// TODO: displayedAs, is, etc
public abstract class ExtractingMatcher<T> extends BaseReportingMatcher<T> {
    private final String name;
    private final Checks checks;

    protected ExtractingMatcher() {
        this(null, null);
    }

    protected ExtractingMatcher(String name, Checks checks) {
        this.name = name;
        this.checks = checks;
    }

    protected abstract KeyValue extractFrom(Object item);
    protected abstract KeyValue extractFromMissingItem();

    @Override
    public Iterator<Object> run(Object item) {
        return createRunResult(extractFrom(item));
    }

    @Override
    public Iterator<Object> runForMissingItem() {
        return createRunResult(extractFromMissingItem());
    }

    @Override
    public void describeTo(Description description) {
//        description.appendText(name);
        // TODO: append matcher.describeTo()
    }


    private Iterator<Object> createRunResult(KeyValue keyValue) {
        return new SingletonIterator<>(
                new SingletonIterator<>(
                        new KeyValueChecks(
                                name == null ? keyValue.key : new RenamedKey(keyValue.key, name),
                                keyValue.value,
                                checks == null ? new Checks(PresenceStatus.PRESENT, noOp()) : checks
                        )
                )
        );
    }


    protected static class KeyValue {
        final Key key;
        final Value value;

        public KeyValue(Key key, Value value) {
            this.key = key;
            this.value = value;
        }
    }

    private static class RenamedKey implements Key {
        private final Key key;
        private final String name;

        RenamedKey(Key key, String name) {
            this.key = key;
            this.name = name;
        }

        @Override
        public String asString() {
            return name;
        }

        @Override
        public int hashCode() {
            return key.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return key.equals(obj);
        }
    }
}
