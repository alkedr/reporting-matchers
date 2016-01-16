package com.github.alkedr.matchers.reporting;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;

import static org.mockito.Mockito.mock;

public class ExtractingMatcherTest {

    static class X {
        X(int a){}

        int getX() {
            return 5;
        }
    }

    static  {
        mock(X.class);
    }

    @Test
    public void lambda() throws Exception {
        f(X::getX);
    }

    @Test
    public void inlineClass() throws Exception {
        f(new Function<X, Object>() {
            @Override
            public Object apply(X x) {
                return x.getX();
            }
        });
    }

    <T> void f(Function<T, ?> function) throws InvocationTargetException, IllegalAccessException {
        System.out.println(function.getClass().getMethods()[0].getGenericParameterTypes()[0]);
//        function.getClass().getMethods()[0].invoke(function, mock());
        function.getClass().getMethods()[0].invoke(function, mock(function.getClass().getMethods()[0].getParameterTypes()[0]));
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
