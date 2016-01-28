package com.github.alkedr.matchers.reporting.sub.value.keys;

import org.junit.Test;

import static com.github.alkedr.matchers.reporting.sub.value.keys.ExtractorVerificationUtils.verifyBroken;
import static com.github.alkedr.matchers.reporting.sub.value.keys.ExtractorVerificationUtils.verifyMissing;
import static com.github.alkedr.matchers.reporting.sub.value.keys.ExtractorVerificationUtils.verifyPresent;
import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.hashMapKey;
import static java.util.Collections.singletonMap;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class HashMapKeyTest {
    @Test
    public void asStringTest() {
        assertEquals("123", hashMapKey(123).asString());
    }

    @Test
    public void hashCodeTest() {
        assertEquals(hashMapKey(0).hashCode(), hashMapKey(0).hashCode());
    }

    @Test
    public void equalsTest() {
        assertEquals(hashMapKey(0), hashMapKey(0));
        assertNotEquals(hashMapKey(0), hashMapKey(1));
    }


    @Test
    public void nullItem() {
        ExtractableKey key = hashMapKey("1");
        verifyMissing(
                listener -> key.run(null, listener),
                sameInstance(key)
        );
    }

    @Test
    public void itemIsNotMap() {
        ExtractableKey key = hashMapKey("1");
        verifyBroken(
                listener -> key.run(new Object(), listener),
                sameInstance(key),
                ClassCastException.class
        );
    }

    @Test
    public void keyIsMissing() {
        ExtractableKey key = hashMapKey("1");
        verifyMissing(
                listener -> key.run(singletonMap("2", "q"), listener),
                sameInstance(key)
        );
    }

    @Test
    public void keyIsPresent() {
        ExtractableKey key = hashMapKey("1");
        verifyPresent(
                listener -> key.run(singletonMap("1", "q"), listener),
                sameInstance(key),
                equalTo("q")
        );
    }

    @Test
    public void keyIsNull() {
        ExtractableKey key = hashMapKey(null);
        verifyPresent(
                listener -> key.run(singletonMap(null, "1"), listener),
                sameInstance(key),
                equalTo("1")
        );
    }

    @Test
    public void keyIsPresentButValueIsNull() {
        ExtractableKey key = hashMapKey("1");
        verifyPresent(
                listener -> key.run(singletonMap("1", null), listener),
                sameInstance(key),
                nullValue()
        );
    }

    @Test
    public void extractFrom_missingItem() {
        ExtractableKey key = hashMapKey("1");
        verifyMissing(
                key::runForMissingItem,
                sameInstance(key)
        );
    }
}
