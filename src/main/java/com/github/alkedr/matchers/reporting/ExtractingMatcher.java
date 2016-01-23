package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.extractors.Extractor;
import com.github.alkedr.matchers.reporting.keys.Key;
import com.github.alkedr.matchers.reporting.keys.RenamedKey;
import com.github.alkedr.matchers.reporting.reporters.Reporter;
import org.hamcrest.Matcher;

import static com.github.alkedr.matchers.reporting.ReportingMatchers.merge;
import static com.github.alkedr.matchers.reporting.ReportingMatchers.noOp;
import static com.github.alkedr.matchers.reporting.ReportingMatchers.toReportingMatcher;
import static com.github.alkedr.matchers.reporting.ReportingMatchers.toReportingMatchers;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;

// Можно извлекать несколько значений!
class ExtractingMatcher<T> extends BaseReportingMatcher<T> implements ExtractingMatcherBuilder<T> {
    private final Extractor extractor;
    private final String name;
    private final ReportingMatcher<?> matcherForExtractedValue;

    ExtractingMatcher(Extractor extractor) {
        this(extractor, null);
    }

    ExtractingMatcher(Extractor extractor, String name) {
        this(extractor, name, noOp());
    }

    ExtractingMatcher(Extractor extractor, String name, ReportingMatcher<?> matcherForExtractedValue) {
        this.extractor = extractor;
        this.name = name;
        this.matcherForExtractedValue = matcherForExtractedValue;
    }


    @Override
    public void run(Object item, Reporter reporter) {
        extractor.extractFrom(item, new ExtractionResultListenerImpl(name, matcherForExtractedValue, reporter));
    }

    @Override
    public void runForMissingItem(Reporter reporter) {
        extractor.extractFromMissingItem(new ExtractionResultListenerImpl(name, matcherForExtractedValue, reporter));
    }


    @Override
    public ExtractingMatcher<T> displayedAs(String newName) {
        return new ExtractingMatcher<>(extractor, newName, matcherForExtractedValue);
    }

    @Override
    public ExtractingMatcher<T> is(Object value) {
        return is(equalTo(value));
    }

    @Override
    public ExtractingMatcher<T> is(Matcher<?> matcher) {
        return new ExtractingMatcher<>(extractor, name, toReportingMatcher(matcher));
    }

    @Override
    @SafeVarargs
    public final <U> ExtractingMatcher<T> is(Matcher<? super U>... matchers) {
        return is(asList(matchers));
    }

    @Override
    public <U> ExtractingMatcher<T> is(Iterable<? extends Matcher<? super U>> matchers) {
        return is(merge(toReportingMatchers(matchers)));
    }


    private static class ExtractionResultListenerImpl implements Extractor.ResultListener {
        private final String name;
        private final ReportingMatcher<?> matcherForExtractedValue;
        private final Reporter reporter;

        private ExtractionResultListenerImpl(String name, ReportingMatcher<?> matcherForExtractedValue, Reporter reporter) {
            this.name = name;
            this.matcherForExtractedValue = matcherForExtractedValue;
            this.reporter = reporter;
        }

        @Override
        public void present(Key key, Object value) {
            reporter.presentNode(renameKeyIfNecessary(key), value, r -> matcherForExtractedValue.run(value, r));
        }

        @Override
        public void missing(Key key) {
            reporter.missingNode(renameKeyIfNecessary(key), matcherForExtractedValue::runForMissingItem);
        }

        @Override
        public void broken(Key key, Throwable throwable) {
            reporter.brokenNode(renameKeyIfNecessary(key), throwable, matcherForExtractedValue::runForMissingItem);
        }

        private Key renameKeyIfNecessary(Key key) {
            return name == null ? key : new RenamedKey<>(key, name);
        }
    }
}
