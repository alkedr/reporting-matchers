package com.github.alkedr.matchers.reporting.extraction;

import com.github.alkedr.matchers.reporting.BaseReportingMatcher;
import com.github.alkedr.matchers.reporting.ReportingMatcher;
import com.github.alkedr.matchers.reporting.utility.MergingMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.Objects;

import static com.github.alkedr.matchers.reporting.utility.NoOpMatcher.noOp;
import static com.github.alkedr.matchers.reporting.utility.ReportingMatchersAdapter.toReportingMatcher;
import static com.github.alkedr.matchers.reporting.utility.ReportingMatchersAdapter.toReportingMatchers;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;

// TODO: описать зачем нужно, пример использования
// field("qwerty").displayedAs("12345").is(equalTo(1))
// is заменяет, не добавляет
// все fluent API методы возвращают новый инстанс
// TODO: найти способ сделать матчеры в is() типобезопасными в случаях, когда известен их тип
// TODO: разнести ключи и экстракторы в разные пакеты, потому что ключи нужны в IteratorMatcher'е
public class ExtractingMatcher<T> extends BaseReportingMatcher<T> {
    private final String name;
    private final Extractor extractor;
    private final ReportingMatcher<?> matcher;

    public ExtractingMatcher(Extractor extractor) {
        this(null, extractor, noOp());
    }

    // name и checks могут быть null
    public ExtractingMatcher(String name, Extractor extractor, ReportingMatcher<?> matcher) {
        this.name = name;
        this.extractor = extractor;
        this.matcher = matcher;
    }


    @Override
    public Checks getChecks(Object item) {
        KeyValue keyValue = extractor.extractFrom(item);
        return getChecksImpl(
                keyValue,
                keyValue.value.presenceStatus() == PresenceStatus.PRESENT
                        ? matcher.getChecks(keyValue.value.get())
                        : matcher.getChecksForMissingItem()
        );
    }

    @Override
    public Checks getChecksForMissingItem() {
        return getChecksImpl(extractor.extractFromMissingItem(), matcher.getChecksForMissingItem());
    }


    private Checks getChecksImpl(KeyValue keyValue, Checks checks) {
        return Checks.keyValueChecks(
                new KeyValueChecks(
                        name == null ? keyValue.key : new RenamedKey(keyValue.key, name),
                        keyValue.value,
                        checks
                )
        );
    }


    @Override
    public void describeTo(Description description) {
//        description.appendText(name);
        // TODO: append matcher.describeTo()
    }


    public ExtractingMatcher<T> displayedAs(String newName) {
        return new ExtractingMatcher<>(newName, extractor, matcher);
    }

    public ExtractingMatcher<T> extractor(Extractor newExtractor) {
        return new ExtractingMatcher<>(name, newExtractor, matcher);
    }


    // Заменяет, а не добавляет матчеры?
    public ExtractingMatcher<T> is(Object value) {
        return is(equalTo(value));
    }

    public ExtractingMatcher<T> is(Matcher<?> matcher) {
        return new ExtractingMatcher<>(name, extractor, toReportingMatcher(matcher));
    }

    @SafeVarargs
    public final <U> ExtractingMatcher<T> is(Matcher<? super U>... matchers) {
        return is(asList(matchers));
    }

    public <U> ExtractingMatcher<T> is(Iterable<? extends Matcher<? super U>> matchers) {
        return is(new MergingMatcher<>(toReportingMatchers(matchers)));
    }

    // TODO: are, returns
    // TODO: isPresent(), isAbsent()




    // TODO: TypeSafeExtractor ?
    // TODO: написать в доках интерфейсов как их реализовывать
    public interface Extractor {
        KeyValue extractFrom(Object item);
        KeyValue extractFromMissingItem();
    }

    public static class KeyValue {
        final Key key;
        final Value value;

        public KeyValue(Key key, Value value) {
            this.key = key;
            this.value = value;
        }
        // TODO: геттеры на всякий случай
    }

    // не объединяется с непереименованным Key
    static class RenamedKey implements Key {
        final Key key;
        final String name;

        RenamedKey(Key key, String name) {
            this.key = key;
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof RenamedKey)) return false;
            RenamedKey that = (RenamedKey) o;
            return Objects.equals(key, that.key) &&
                    Objects.equals(name, that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, name);
        }

        @Override
        public String asString() {
            return name;
        }
    }
}
