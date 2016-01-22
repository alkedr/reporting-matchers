package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.keys.ExtractableKey;
import com.github.alkedr.matchers.reporting.keys.Key;
import com.github.alkedr.matchers.reporting.reporters.Reporter;
import org.junit.After;
import org.junit.Test;
import org.mockito.InOrder;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

public class ExtractingMatcherTest {
    private final Reporter reporter = mock(Reporter.class);
    private final InOrder inOrder = inOrder(reporter);
    private final Object item = new Object();
    private final ExtractableKey extractableKey = mock(ExtractableKey.class);
    private final Key key = mock(Key.class);
    private final ReportingMatcher<?> matcher = mock(ReportingMatcher.class);

    @After
    public void tearDown() {
        inOrder.verifyNoMoreInteractions();
    }

    // TODO: item == null, item.getClass() != Iterator.class

    @Test
    public void getChecks() {
    }

    /*@Test
    public void getChecks_withMatcher_present() {
        Object extractedItem = new Object();
        when(extractor.extractFrom(item)).thenReturn(new ExtractingMatcher.PresentExtractionResult(key, extractedItem));
        when(matcher.getChecks(extractedItem)).thenReturn(emptyIterator());  // TODO: add CheckResult
        Iterator<CheckResult> actual = new ExtractingMatcher<>(extractor).is(matcher).getChecks(item);
        verifyCheckResult(actual, isPresentSubValue(key, extractedItem));
    }

    @Test
    public void getChecks_withMatcher_missing() {
        when(extractor.extractFrom(item)).thenReturn(new ExtractingMatcher.MissingExtractionResult(key));
        when(matcher.getChecksForMissingItem()).thenReturn(emptyIterator());
        Iterator<CheckResult> actual = new ExtractingMatcher<>(extractor).is(matcher).getChecks(item);
        verifyCheckResult(actual, isMissingSubValue(key));
    }

    @Test
    public void getChecksForMissingItem() {
        when(extractor.extractFromMissingItem()).thenReturn(new ExtractingMatcher.MissingExtractionResult(key));
        when(matcher.getChecksForMissingItem()).thenReturn(emptyIterator());
        Iterator<CheckResult> actual = new ExtractingMatcher<>(extractor).is(matcher).getChecksForMissingItem();
        verifyCheckResult(actual, isMissingSubValue(key));
    }*/


    // TODO: тесты на fluent API, в т. ч. преобразование matcher -> reportingMatcher и merge()
    // TODO: тесты на RenamedKey



    /*@Test
    public void nameInConstructor() {
        String customName = "12345";
        new ExtractingMatcher<>(customName, extractor, null).runForMissingItem(checkListener);
        CheckListenerUtils.verifyKvcGroup(checkListener, CheckListenerUtils.kvc(renamedKey(customName, key), sameInstance(missingValue), sameInstance(DEFAULT_CHECKS)));
        verifyNoMoreInteractions(checkListener);
    }

    @Test
    public void checksInConstructor() {
        ReportingMatcher.Checks customChecks = new ReportingMatcher.Checks(MISSING, noOp());
        new ExtractingMatcher<>(null, extractor, customChecks).runForMissingItem(checkListener);
        CheckListenerUtils.verifyKvcGroup(checkListener, CheckListenerUtils.kvc(key, missingValue, customChecks));
        verifyNoMoreInteractions(checkListener);
    }*/



   /* public static Matcher<ReportingMatcher.Key> renamedKey(String customName, ReportingMatcher.Key key) {
        return new CustomMatcher<ReportingMatcher.Key>("RenamedKey{" + customName + ", " + key + "}") {
            @Override
            public boolean matches(Object item) {
                return item instanceof ExtractingMatcher.RenamedKey
                        && ((ExtractingMatcher.RenamedKey) item).name == customName
                        && ((ExtractingMatcher.RenamedKey) item).key == key
                        ;
            }
        };
    }*/
}
