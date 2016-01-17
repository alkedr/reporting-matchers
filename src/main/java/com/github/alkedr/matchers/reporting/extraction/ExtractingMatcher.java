package com.github.alkedr.matchers.reporting.extraction;

import com.github.alkedr.matchers.reporting.BaseReportingMatcher;
import com.github.alkedr.matchers.reporting.ReportingMatcher;
import com.github.alkedr.matchers.reporting.utility.MergingMatcher;
import org.apache.commons.collections4.iterators.SingletonIterator;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import static com.github.alkedr.matchers.reporting.ReportingMatchers.sequence;
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
public class ExtractingMatcher<T> extends BaseReportingMatcher<T> {
    static final Checks DEFAULT_CHECKS = new Checks(PresenceStatus.PRESENT, noOp());

    private final String name;
    private final Extractor extractor;
    private final Checks checks;

    public ExtractingMatcher(Extractor extractor) {
        this(null, extractor, null);
    }

    // name и checks могут быть null
    public ExtractingMatcher(String name, Extractor extractor, Checks checks) {
        this.name = name;
        this.extractor = extractor;
        this.checks = checks == null ? DEFAULT_CHECKS : checks;
    }


    @Override
    public void run(Object item, CheckListener checkListener) {
        runImpl(extractor.extractFrom(item), checkListener);
    }

    @Override
    public void runForMissingItem(CheckListener checkListener) {
        runImpl(extractor.extractFromMissingItem(), checkListener);
    }

    @Override
    public void describeTo(Description description) {
//        description.appendText(name);
        // TODO: append matcher.describeTo()
    }


    public ExtractingMatcher<T> displayedAs(String newName) {
        return new ExtractingMatcher<>(newName, extractor, checks);
    }

    public ExtractingMatcher<T> extractor(Extractor newExtractor) {
        return new ExtractingMatcher<>(name, newExtractor, checks);
    }

    public ExtractingMatcher<T> checks(Checks newChecks) {
        return new ExtractingMatcher<>(name, extractor, newChecks);
    }

    // Заменяет, а не добавляет матчеры?
    public ExtractingMatcher<T> is(Object value) {
        return is(equalTo(value));
    }

    public ExtractingMatcher<T> is(Matcher<?> matcher) {
        return is(toReportingMatcher(matcher));
    }

    public ExtractingMatcher<T> is(ReportingMatcher<?> matcher) {
        return checks(new Checks(PresenceStatus.PRESENT, matcher));
    }

    public <U> ExtractingMatcher<T> is(Matcher<? super U>... matchers) {
        return is(asList(matchers));
    }

    public <U> ExtractingMatcher<T> is(Iterable<? extends Matcher<? super U>> matchers) {
        return is(new MergingMatcher<>(sequence(toReportingMatchers(matchers))));
    }

    // TODO: are, returns
    // TODO: isPresent(), isAbsent()




    private void runImpl(KeyValue keyValue, CheckListener checkListener) {
        checkListener.keyValueChecksGroup(new SingletonIterator<>(
                new KeyValueChecks(
                        name == null ? keyValue.key : new RenamedKey(keyValue.key, name),
                        keyValue.value,
                        checks
                )
        ));
    }


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
    }

    // TODO: должен ли RenamedKey объединяться с непереименованным Key?
    static class RenamedKey implements Key {
        final Key key;
        final String name;

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
