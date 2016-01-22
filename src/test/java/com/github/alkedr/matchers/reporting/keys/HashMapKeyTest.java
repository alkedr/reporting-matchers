package com.github.alkedr.matchers.reporting.keys;

import org.junit.Test;

import static com.github.alkedr.matchers.reporting.ReportingMatchers.noOp;
import static com.github.alkedr.matchers.reporting.check.results.CheckResults.missingSubValue;
import static com.github.alkedr.matchers.reporting.check.results.CheckResults.presentSubValue;
import static com.github.alkedr.matchers.reporting.keys.Keys.hashMapKey;
import static java.util.Collections.emptyIterator;
import static java.util.Collections.singletonMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;

public class HashMapKeyTest {
    @Test
    public void asStringTest() {
        assertEquals("123", hashMapKey("123").asString());
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
        assertEquals(
                missingSubValue(hashMapKey("1"), emptyIterator()),
                hashMapKey("1").extractFrom(null).createCheckResult(noOp())
        );
    }

    @Test
    public void itemIsNotMap() {
        ExtractableKey.Result.Broken actual = (ExtractableKey.Result.Broken) hashMapKey("1").extractFrom(new Object());
        assertEquals(hashMapKey("1"), actual.key);
        assertSame(ClassCastException.class, actual.throwable.getClass());
    }

    @Test
    public void keyIsMissing() {
        assertEquals(
                missingSubValue(hashMapKey("1"), emptyIterator()),
                hashMapKey("1").extractFrom(singletonMap("2", "q")).createCheckResult(noOp())
        );
    }

    @Test
    public void keyIsPresent() {
        assertEquals(
                presentSubValue(hashMapKey("1"), "q", emptyIterator()),
                hashMapKey("1").extractFrom(singletonMap("1", "q")).createCheckResult(noOp())
        );
    }

    @Test
    public void keyIsPresentButValueIsNull() {
        assertEquals(
                presentSubValue(hashMapKey("1"), null, emptyIterator()),
                hashMapKey("1").extractFrom(singletonMap("1", null)).createCheckResult(noOp())
        );
    }

    @Test
    public void extractFrom_missingItem() {
        assertEquals(
                missingSubValue(hashMapKey("1"), emptyIterator()),
                hashMapKey("1").extractFromMissingItem().createCheckResult(noOp())
        );
    }
}
