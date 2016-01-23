package com.github.alkedr.matchers.reporting.extractors;

import com.github.alkedr.matchers.reporting.keys.HashMapKey;
import org.junit.After;
import org.junit.Test;

import static com.github.alkedr.matchers.reporting.extractors.Extractors.hashMapKeyExtractor;
import static java.util.Collections.singletonMap;
import static org.mockito.Matchers.isA;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class HashMapKeyExtractorTest {
    private final Extractor.ResultListener result = mock(Extractor.ResultListener.class);

    @After
    public void tearDown() {
        verifyNoMoreInteractions(result);
    }

    @Test
    public void nullItem() {
        HashMapKey key = new HashMapKey("1");
        hashMapKeyExtractor(key).extractFrom(null, result);
        verify(result).missing(key);
    }

    @Test
    public void itemIsNotMap() {
        HashMapKey key = new HashMapKey("1");
        hashMapKeyExtractor(key).extractFrom(new Object(), result);
        verify(result).broken(same(key), isA(ClassCastException.class));
    }

    @Test
    public void keyIsMissing() {
        HashMapKey key = new HashMapKey("1");
        hashMapKeyExtractor(key).extractFrom(singletonMap("2", "q"), result);
        verify(result).missing(key);
    }

    @Test
    public void keyIsPresent() {
        HashMapKey key = new HashMapKey("1");
        hashMapKeyExtractor(key).extractFrom(singletonMap("1", "q"), result);
        verify(result).present(key, "q");
    }

    @Test
    public void keyIsPresentButValueIsNull() {
        HashMapKey key = new HashMapKey("1");
        hashMapKeyExtractor(key).extractFrom(singletonMap("1", null), result);
        verify(result).present(key, null);
    }

    @Test
    public void extractFrom_missingItem() {
        HashMapKey key = new HashMapKey("1");
        hashMapKeyExtractor(key).extractFromMissingItem(result);
        verify(result).missing(key);
    }

    // TODO: key is null?
}
