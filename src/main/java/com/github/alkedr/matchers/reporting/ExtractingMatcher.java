package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.keys.ExtractableKey;
import com.github.alkedr.matchers.reporting.keys.Key;
import com.github.alkedr.matchers.reporting.reporters.SafeTreeReporter;
import org.hamcrest.Matcher;

import static com.github.alkedr.matchers.reporting.ReportingMatchers.merge;
import static com.github.alkedr.matchers.reporting.ReportingMatchers.noOp;
import static com.github.alkedr.matchers.reporting.ReportingMatchers.toReportingMatcher;
import static com.github.alkedr.matchers.reporting.ReportingMatchers.toReportingMatchers;
import static com.github.alkedr.matchers.reporting.keys.Keys.renamedKey;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;

// TODO: описать зачем нужно, пример использования
// field("qwerty").displayedAs("12345").is(equalTo(1))
// все fluent API методы возвращают новый инстанс
// TODO: найти способ сделать матчеры в is() типобезопасными в случаях, когда известен их тип
// TODO: сделать конструкторы public?
public class ExtractingMatcher<T> extends BaseReportingMatcher<T> {
    private final ExtractableKey extractor;
    private final String name;
    private final ReportingMatcher<?> matcherForExtractedValue;

    ExtractingMatcher(ExtractableKey extractor) {
        this(extractor, null, noOp());
    }

    /*ExtractingMatcher(ExtractableKey extractor, String name) {
        this(extractor, name, noOp());
    }

    ExtractingMatcher(ExtractableKey extractor, ReportingMatcher<?> matcherForExtractedValue) {
        this(extractor, null, matcherForExtractedValue);
    }*/

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


    public ExtractingMatcher<T> displayedAs(String newName) {
        return new ExtractingMatcher<>(extractor, newName, matcherForExtractedValue);
    }

    public ExtractingMatcher<T> is(Object value) {
        return is(equalTo(value));
    }

    public ExtractingMatcher<T> is(Matcher<?> matcher) {
        return new ExtractingMatcher<>(extractor, name, toReportingMatcher(matcher));
    }

    @SafeVarargs
    public final <U> ExtractingMatcher<T> is(Matcher<? super U>... matchers) {
        return is(asList(matchers));
    }

    public <U> ExtractingMatcher<T> is(Iterable<? extends Matcher<? super U>> matchers) {
        return is(merge(toReportingMatchers(matchers)));
    }

    // TODO: are, returns
    // TODO: isPresent(), isAbsent()


    private interface Runner {
        ExtractableKey.ExtractionResult getExtractionResult()
                throws ExtractableKey.MissingException, ExtractableKey.BrokenException;
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

    private Key renameKeyIfNecessary(Key key) {
        return name == null ? key : renamedKey(key, name);
    }
}
