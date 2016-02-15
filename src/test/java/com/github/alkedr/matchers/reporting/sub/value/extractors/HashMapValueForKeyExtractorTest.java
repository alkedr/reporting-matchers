package com.github.alkedr.matchers.reporting.sub.value.extractors;

import org.junit.Test;

import static com.github.alkedr.matchers.reporting.sub.value.extractors.SubValuesExtractors.hashMapValueForKey;
import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.hashMapValueForKeyKey;
import static java.util.Collections.singletonMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class HashMapValueForKeyExtractorTest {
    private final SubValuesExtractor.SubValuesListener<Object> listener = mock(SubValuesExtractor.SubValuesListener.class);

    @Test
    public void nullItem() {
        hashMapValueForKey("1").run(null, listener);
        verify(listener).absent(hashMapValueForKeyKey("1"));
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void keyIsAbsent() {
        hashMapValueForKey("2").run(singletonMap("1", "q"), listener);
        verify(listener).absent(hashMapValueForKeyKey("2"));
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void keyIsPresent() {
        hashMapValueForKey("1").run(singletonMap("1", "q"), listener);
        verify(listener).present(hashMapValueForKeyKey("1"), "q");
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void keyIsNull() {
        hashMapValueForKey(null).run(singletonMap(null, "q"), listener);
        verify(listener).present(hashMapValueForKeyKey(null), "q");
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void keyIsPresentButValueIsNull() {
        hashMapValueForKey("1").run(singletonMap("1", null), listener);
        verify(listener).present(hashMapValueForKeyKey("1"), null);
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void extractFrom_absentItem() {
        hashMapValueForKey("1").runForAbsentItem(listener);
        verify(listener).absent(hashMapValueForKeyKey("1"));
        verifyNoMoreInteractions(listener);
    }
}
