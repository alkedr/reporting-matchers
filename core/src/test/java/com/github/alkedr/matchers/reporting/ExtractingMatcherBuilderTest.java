package com.github.alkedr.matchers.reporting;

import org.junit.After;
import org.junit.Test;

import static com.github.alkedr.matchers.reporting.NoOpMatcher.noOp;
import static com.github.alkedr.matchers.reporting.ReportingMatcherAdapter.toReportingMatcher;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class ExtractingMatcherBuilderTest {
    private static final String NAME = "123";
    private static final String NAME2 = "234";
    private static final ExtractingMatcher.Extractor EXTRACTOR = mock(ExtractingMatcher.Extractor.class);
    private static final ExtractingMatcher.Extractor EXTRACTOR2 = mock(ExtractingMatcher.Extractor.class);
    private static final ReportingMatcher<?> MATCHER = mock(ReportingMatcher.class);

    private final ExtractingMatcherBuilder<Object> emb = new ExtractingMatcherBuilder<>(NAME, EXTRACTOR, MATCHER);

    @After
    public void tearDown() {
        check(NAME, EXTRACTOR, MATCHER, emb);
    }

    @Test
    public void displayedAs() {
        check(NAME2, EXTRACTOR, MATCHER, emb.displayedAs(NAME2));
    }

    @Test
    public void extractor() {
        check(NAME, EXTRACTOR2, MATCHER, emb.extractor(EXTRACTOR2));
    }

    @Test
    public void is_value() {
        checkEqualToOne(NAME, EXTRACTOR, emb.is(1));
    }

    @Test
    public void is_matcher() {
        checkEqualToOne(NAME, EXTRACTOR, emb.is(equalTo(1)));
    }

    @Test
    public void is_reportingMatcher() {
        checkEqualToOne(NAME, EXTRACTOR, emb.is(toReportingMatcher(equalTo(1))));
    }

    @Test
    public void extractedValue() {
        check(NAME, EXTRACTOR, noOp(), ExtractingMatcherBuilder.extractedValue(NAME, EXTRACTOR));
    }

    // TODO: тесты на is(Matcher...) и is(Iterable<Matcher>)

    private static void check(String expectedName, ExtractingMatcher.Extractor expectedExtractor,
                              ReportingMatcher<?> expectedMatcher, ExtractingMatcherBuilder<Object> emb) {
        assertSame(expectedName, emb.getName());
        assertSame(expectedExtractor, emb.getExtractor());
        assertSame(expectedMatcher, emb.getMatcher());
    }

    private static void checkEqualToOne(String expectedName, ExtractingMatcher.Extractor expectedExtractor,
                                        ExtractingMatcherBuilder<Object> emb) {
        assertSame(expectedName, emb.getName());
        assertSame(expectedExtractor, emb.getExtractor());
        assertTrue(emb.getMatcher().matches(1));
        assertFalse(emb.getMatcher().matches(2));
    }
}
