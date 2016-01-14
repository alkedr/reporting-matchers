package com.github.alkedr.matchers.reporting;

import static org.mockito.Mockito.mock;

public class ExtractingMatcherTest {
    private static final String NAME = "NAME";
    private static final String BROKEN_ERROR_MESSAGE = "BROKEN_ERROR_MESSAGE";
    private final ExtractingMatcher.Extractor normalExtractor = mock(ExtractingMatcher.Extractor.class);
    private final ExtractingMatcher.Extractor missingExtractor = mock(ExtractingMatcher.Extractor.class);
    private final ExtractingMatcher.Extractor brokenExtractor = mock(ExtractingMatcher.Extractor.class);

    private final Object item = new Object();
    private final Object extractedItem = new Object();
    private static final String EXTRACTED_ITEM_AS_STRING = "EXTRACTED_ITEM_AS_STRING";

    private final ReportingMatcher<Object> matcher = mock(ReportingMatcher.class);
    private final ReportingMatcher.Reporter reporter = mock(ReportingMatcher.Reporter.class);

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
