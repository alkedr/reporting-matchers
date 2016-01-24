package com.github.alkedr.matchers.reporting.keys;

import org.junit.Test;

import static java.util.Collections.singletonMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Matchers.isA;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class HashMapKeyTest {
    private final ExtractableKey.ResultListener result = mock(ExtractableKey.ResultListener.class);

    @Test
    public void asStringTest() {
        assertEquals("123", Keys.hashMapKey(123).asString());
    }

    @Test
    public void hashCodeTest() {
        assertEquals(Keys.hashMapKey(0).hashCode(), Keys.hashMapKey(0).hashCode());
    }

    @Test
    public void equalsTest() {
        assertEquals(Keys.hashMapKey(0), Keys.hashMapKey(0));
        assertNotEquals(Keys.hashMapKey(0), Keys.hashMapKey(1));
    }


    @Test
    public void nullItem() {
        ExtractableKey key = Keys.hashMapKey("1");
        key.extractFrom(null, result);
        verify(result).missing(key);
        verifyNoMoreInteractions(result);
    }

    @Test
    public void itemIsNotMap() {
        ExtractableKey key = Keys.hashMapKey("1");
        key.extractFrom(new Object(), result);
        verify(result).broken(same(key), isA(ClassCastException.class));
        verifyNoMoreInteractions(result);
    }

    @Test
    public void keyIsMissing() {
        ExtractableKey key = Keys.hashMapKey("1");
        key.extractFrom(singletonMap("2", "q"), result);
        verify(result).missing(key);
        verifyNoMoreInteractions(result);
    }

    @Test
    public void keyIsPresent() {
        ExtractableKey key = Keys.hashMapKey("1");
        key.extractFrom(singletonMap("1", "q"), result);
        verify(result).present(key, "q");
        verifyNoMoreInteractions(result);
    }

    @Test
    public void keyIsPresentButValueIsNull() {
        ExtractableKey key = Keys.hashMapKey("1");
        key.extractFrom(singletonMap("1", null), result);
        verify(result).present(key, null);
        verifyNoMoreInteractions(result);
    }

    @Test
    public void extractFrom_missingItem() {
        ExtractableKey key = Keys.hashMapKey("1");
        key.extractFromMissingItem(result);
        verify(result).missing(key);
        verifyNoMoreInteractions(result);
    }

    // TODO: key is null?
}
