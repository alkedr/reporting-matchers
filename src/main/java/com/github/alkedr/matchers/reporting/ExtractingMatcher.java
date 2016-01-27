package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.keys.ExtractableKey;
import com.github.alkedr.matchers.reporting.keys.Key;
import com.github.alkedr.matchers.reporting.keys.Keys;
import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;
import org.hamcrest.Matcher;

import static com.github.alkedr.matchers.reporting.ReportingMatchers.merge;
import static com.github.alkedr.matchers.reporting.ReportingMatchers.noOp;
import static com.github.alkedr.matchers.reporting.ReportingMatchers.toReportingMatcher;
import static com.github.alkedr.matchers.reporting.ReportingMatchers.toReportingMatchers;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;

// Можно извлекать несколько значений!
class ExtractingMatcher<T> extends BaseReportingMatcher<T> implements ExtractingMatcherBuilder<T> {
    private final ExtractableKey extractor;
    private final String name;
    private final ReportingMatcher<?> matcherForExtractedValue;

    ExtractingMatcher(ExtractableKey extractor) {
        this(extractor, null);
    }

    ExtractingMatcher(ExtractableKey extractor, String name) {
        this(extractor, name, noOp());
    }

    ExtractingMatcher(ExtractableKey extractor, String name, ReportingMatcher<?> matcherForExtractedValue) {
        this.extractor = extractor;
        this.name = name;
        this.matcherForExtractedValue = matcherForExtractedValue;
    }


    @Override
    public void run(Object item, SafeTreeReporter safeTreeReporter) {
        run(() -> extractor.extractFrom(item), safeTreeReporter);
    }

    @Override
    public void runForMissingItem(SafeTreeReporter safeTreeReporter) {
        run(extractor::extractFromMissingItem, safeTreeReporter);
    }

    private void run(Runner runner, SafeTreeReporter safeTreeReporter) {
        try {
            ExtractableKey.ExtractionResult extractionResult = runner.getExtractionResult();
            safeTreeReporter.presentNode(
                    renameKeyIfNecessary(extractionResult.getKey()),
                    extractionResult.getValue(),
                    r -> matcherForExtractedValue.run(extractionResult.getValue(), r)
            );
        } catch (ExtractableKey.MissingException e) {
            safeTreeReporter.missingNode(
                    renameKeyIfNecessary(e.getKey()),
                    matcherForExtractedValue::runForMissingItem
            );
        } catch (ExtractableKey.BrokenException e) {
            safeTreeReporter.brokenNode(
                    renameKeyIfNecessary(e.getKey()),
                    e.getCause(),
                    matcherForExtractedValue::runForMissingItem
            );
        }
    }

    @Override
    public ReportingMatcher<T> build() {
        return this;
    }

    private interface Runner {
        ExtractableKey.ExtractionResult getExtractionResult()
                throws ExtractableKey.MissingException, ExtractableKey.BrokenException;
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


    private Key renameKeyIfNecessary(Key key) {
        return name == null ? key : Keys.renamedKey(key, name);
    }
}
