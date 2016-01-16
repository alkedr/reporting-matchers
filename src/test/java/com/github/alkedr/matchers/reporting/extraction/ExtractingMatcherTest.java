package com.github.alkedr.matchers.reporting.extraction;

import com.github.alkedr.matchers.reporting.ReportingMatcher;
import org.apache.commons.collections4.IteratorUtils;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.collection.IsIterableContainingInOrder;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.alkedr.matchers.reporting.ReportingMatcher.Value.present;
import static com.github.alkedr.matchers.reporting.extraction.ExtractingMatcher.DEFAULT_CHECKS;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExtractingMatcherTest {
    private final Object item = new Object();
    private final Object extractedItem = new Object();
    private final String extractedItemAsString = "123";
    private final ExtractingMatcher.Extractor extractor = mock(ExtractingMatcher.Extractor.class);
    private final ReportingMatcher.Key key = mock(ReportingMatcher.Key.class);
    private final ReportingMatcher.Value value = present(extractedItem, extractedItemAsString);
    private final ExtractingMatcher.KeyValue keyValue = new ExtractingMatcher.KeyValue(key, value);

    @Before
    public void setUp() {
        when(extractor.extractFrom(item)).thenReturn(keyValue);
    }

    @Test
    public void run() {
        checkRunResults(
                new ExtractingMatcher<>(null, extractor, null).run(item),
                contains(keyValueChecks(key, value, DEFAULT_CHECKS))
        );
    }

    private static void checkRunResults(Iterator<Object> runResults, Matcher<?>... matchers) {
        List<Object> list = IteratorUtils.toList(runResults).stream()
                .map(o -> o instanceof Iterator ? IteratorUtils.toList((Iterator<?>) o) : o)
                .collect(Collectors.toList());
        assertThat(list, new IsIterableContainingInOrder<>((List<Matcher<? super Object>>)(Object) asList(matchers)));
    }

    private static Matcher<ReportingMatcher.KeyValueChecks> keyValueChecks(ReportingMatcher.Key key,
                                                                           ReportingMatcher.Value value,
                                                                           ReportingMatcher.Checks checks) {
        return allOf(
                new FeatureMatcher<ReportingMatcher.KeyValueChecks, ReportingMatcher.Key>(sameInstance(key), "key", "key") {
                    @Override
                    protected ReportingMatcher.Key featureValueOf(ReportingMatcher.KeyValueChecks actual) {
                        return actual.key();
                    }
                },

                new FeatureMatcher<ReportingMatcher.KeyValueChecks, ReportingMatcher.Value>(sameInstance(value), "value", "value") {
                    @Override
                    protected ReportingMatcher.Value featureValueOf(ReportingMatcher.KeyValueChecks actual) {
                        return actual.value();
                    }
                },

                new FeatureMatcher<ReportingMatcher.KeyValueChecks, ReportingMatcher.Checks>(sameInstance(checks), "checks", "checks") {
                    @Override
                    protected ReportingMatcher.Checks featureValueOf(ReportingMatcher.KeyValueChecks actual) {
                        return actual.checks();
                    }
                }
        );
    }





    /*private static final String NAME = "NAME";
    private static final String BROKEN_ERROR_MESSAGE = "BROKEN_ERROR_MESSAGE";
    private final ExtractingMatcher.Extractor normalExtractor = mock(ExtractingMatcher.Extractor.class);
    private final ExtractingMatcher.Extractor missingExtractor = mock(ExtractingMatcher.Extractor.class);
    private final ExtractingMatcher.Extractor brokenExtractor = mock(ExtractingMatcher.Extractor.class);

    private final Object item = new Object();
    private final Object extractedItem = new Object();
    private static final String EXTRACTED_ITEM_AS_STRING = "EXTRACTED_ITEM_AS_STRING";

    private final ReportingMatcher<Object> matcher = mock(ReportingMatcher.class);
    private final Reporter reporter = mock(Reporter.class);*/

    /*@Before
    public void setUp() {
        when(normalExtractor.extractFrom(item)).thenReturn(normal(EXTRACTED_ITEM_AS_STRING, extractedItem));
        when(missingExtractor.extractFrom(item)).thenReturn(missing());
        when(brokenExtractor.extractFrom(item)).thenReturn(broken(BROKEN_ERROR_MESSAGE));
    }

    @Test
    public void getters() {
        String name = "123";
        ExtractingMatcher.Extractor extractor = mock(ExtractingMatcher.Extractor.class);
        ReportingMatcher<?> matcher = mock(ReportingMatcher.class);
        ExtractingMatcher<Object> extractingMatcher = new ExtractingMatcher<>(name, extractor, matcher);
        assertSame(name, extractingMatcher.getName());
        assertSame(extractor, extractingMatcher.getExtractor());
        assertSame(matcher, extractingMatcher.getMatcher());
    }

    @Test
    public void run_normal() {
        new ExtractingMatcher<>(NAME, normalExtractor, matcher).run(item, reporter);
        verifyNormal();
    }

    @Test
    public void run_missing() {
        new ExtractingMatcher<>(NAME, missingExtractor, matcher).run(item, reporter);
        verifyMissing();
    }

    @Test
    public void run_broken() {
        new ExtractingMatcher<>(NAME, brokenExtractor, matcher).run(item, reporter);
        verifyBroken();
    }

    @Test
    public void runForMissingItem_normal() {
        new ExtractingMatcher<>(NAME, normalExtractor, matcher).runForMissingItem(reporter);
        verifyMissing();
    }

    @Test
    public void runForMissingItem_missing() {
        new ExtractingMatcher<>(NAME, missingExtractor, matcher).runForMissingItem(reporter);
        verifyMissing();
    }

    @Test
    public void runForMissingItem_broken() {
        new ExtractingMatcher<>(NAME, brokenExtractor, matcher).runForMissingItem(reporter);
        verifyMissing();
    }


    private void verifyNormal() {
        inOrder(matcher, reporter).verify(reporter).beginKeyValuePair(NAME, NORMAL, EXTRACTED_ITEM_AS_STRING);
        inOrder(matcher, reporter).verify(matcher).run(extractedItem, reporter);
        inOrder(matcher, reporter).verify(reporter).endKeyValuePair();
        verifyNoMoreInteractions(matcher, reporter);
    }

    private void verifyMissing() {
        inOrder(matcher, reporter).verify(reporter).beginKeyValuePair(NAME, MISSING, "");
        inOrder(matcher, reporter).verify(matcher).runForMissingItem(reporter);
        inOrder(matcher, reporter).verify(reporter).endKeyValuePair();
        verifyNoMoreInteractions(matcher, reporter);
    }

    private void verifyBroken() {
        inOrder(matcher, reporter).verify(reporter).beginKeyValuePair(NAME, BROKEN, BROKEN_ERROR_MESSAGE);
        inOrder(matcher, reporter).verify(matcher).runForMissingItem(reporter);
        inOrder(matcher, reporter).verify(reporter).endKeyValuePair();
        verifyNoMoreInteractions(matcher, reporter);
    }*/
}
