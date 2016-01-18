package com.github.alkedr.matchers.reporting.extraction;

import com.github.alkedr.matchers.reporting.ReportingMatcher;
import org.junit.Test;

import static com.github.alkedr.matchers.reporting.ChecksUtils.kvc;
import static com.github.alkedr.matchers.reporting.ChecksUtils.verifyChecks;
import static com.github.alkedr.matchers.reporting.ReportingMatcher.Value.missing;
import static com.github.alkedr.matchers.reporting.ReportingMatcher.Value.present;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExtractingMatcherTest {
    private final Object item = new Object();
    private final Object extractedItem = new Object();
    private final ExtractingMatcher.Extractor extractor = mock(ExtractingMatcher.Extractor.class);
    private final ReportingMatcher.Key key = mock(ReportingMatcher.Key.class);
    private final ReportingMatcher.Value presentValue = present(extractedItem, "1");
    private final ReportingMatcher.Value missingValue = missing();
    private final ReportingMatcher<?> matcher = mock(ReportingMatcher.class);
    private final ReportingMatcher.Checks checksForExtractedValue = ReportingMatcher.Checks.noOp();


    @Test
    public void getChecks_withMatcher_present() {
        when(extractor.extractFrom(item)).thenReturn(new ExtractingMatcher.KeyValue(key, presentValue));
        when(matcher.getChecks(extractedItem)).thenReturn(checksForExtractedValue);
        ReportingMatcher.Checks actual = new ExtractingMatcher<>(extractor).is(matcher).getChecks(item);
        verifyChecks(actual, kvc(key, presentValue, checksForExtractedValue));
    }

    @Test
    public void getChecks_withMatcher_missing() {
        when(extractor.extractFrom(item)).thenReturn(new ExtractingMatcher.KeyValue(key, missingValue));
        when(matcher.getChecksForMissingItem()).thenReturn(checksForExtractedValue);
        ReportingMatcher.Checks actual = new ExtractingMatcher<>(extractor).is(matcher).getChecks(item);
        verifyChecks(actual, kvc(key, missingValue, checksForExtractedValue));
    }

    @Test
    public void getChecksForMissingItem() {
        when(extractor.extractFromMissingItem()).thenReturn(new ExtractingMatcher.KeyValue(key, missingValue));
        when(matcher.getChecksForMissingItem()).thenReturn(checksForExtractedValue);
        ReportingMatcher.Checks actual = new ExtractingMatcher<>(extractor).is(matcher).getChecksForMissingItem();
        verifyChecks(actual, kvc(key, missingValue, checksForExtractedValue));
    }


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
