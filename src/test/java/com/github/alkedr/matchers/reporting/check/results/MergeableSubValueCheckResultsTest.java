package com.github.alkedr.matchers.reporting.check.results;

import com.github.alkedr.matchers.reporting.keys.Key;
import com.github.alkedr.matchers.reporting.keys.Keys;
import org.junit.Test;

import java.util.Iterator;

import static com.github.alkedr.matchers.reporting.check.results.CheckResults.*;
import static com.google.common.collect.Iterators.forArray;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class MergeableSubValueCheckResultsTest {
    private final Key key = mock(Key.class);
    private final Object value = new Object();
    private final Throwable throwable = new RuntimeException();

    @Test
    public void present_getCheckResults_createNewInstance() {
        Iterator<? extends CheckResult> oldCheckResults = forArray(correctlyPresent());
        Iterator<? extends CheckResult> newCheckResults = forArray(incorrectlyPresent());

        MergeableSubValueCheckResult oldCR = presentSubValue(key, value, oldCheckResults);
        MergeableSubValueCheckResult newCR = oldCR.createNewInstanceWithDifferentCheckResults(newCheckResults);

        assertNotSame(oldCR, newCR);
        assertEquals(oldCR, newCR);
        assertSame(oldCheckResults, oldCR.getCheckResults());
        assertSame(newCheckResults, newCR.getCheckResults());
    }

    @Test
    public void missing_getCheckResults_createNewInstance() {
        Iterator<? extends CheckResult> oldCheckResults = forArray(correctlyPresent());
        Iterator<? extends CheckResult> newCheckResults = forArray(incorrectlyPresent());

        MergeableSubValueCheckResult oldCR = missingSubValue(key, oldCheckResults);
        MergeableSubValueCheckResult newCR = oldCR.createNewInstanceWithDifferentCheckResults(newCheckResults);

        assertNotSame(oldCR, newCR);
        assertEquals(oldCR, newCR);
        assertSame(oldCheckResults, oldCR.getCheckResults());
        assertSame(newCheckResults, newCR.getCheckResults());
    }

    @Test
    public void broken_getCheckResults_createNewInstance() {
        Iterator<? extends CheckResult> oldCheckResults = forArray(correctlyPresent());
        Iterator<? extends CheckResult> newCheckResults = forArray(incorrectlyPresent());

        MergeableSubValueCheckResult oldCR = brokenSubValue(key, throwable, oldCheckResults);
        MergeableSubValueCheckResult newCR = oldCR.createNewInstanceWithDifferentCheckResults(newCheckResults);

        assertNotSame(oldCR, newCR);
        assertEquals(oldCR, newCR);
        assertSame(oldCheckResults, oldCR.getCheckResults());
        assertSame(newCheckResults, newCR.getCheckResults());
    }


    @Test
    public void present_hashCode() {
        assertEquals(
                presentSubValue(key, new Object(), forArray(correctlyPresent())).hashCode(),
                presentSubValue(key, new Object(), forArray(incorrectlyPresent())).hashCode()
        );
    }

    @Test
    public void missing_hashCode() {
        assertEquals(
                missingSubValue(key, forArray(correctlyPresent())).hashCode(),
                missingSubValue(key, forArray(incorrectlyPresent())).hashCode()
        );
    }

    @Test
    public void broken_hashCode() {
        assertEquals(
                brokenSubValue(key, throwable, forArray(correctlyPresent())).hashCode(),
                brokenSubValue(key, throwable, forArray(incorrectlyPresent())).hashCode()
        );
    }


    @Test
    public void present_equals() {
        CheckResult instance = presentSubValue(key, value, forArray());
        assertTrue(instance.equals(instance));
        assertFalse(instance.equals(null));
        assertFalse(instance.equals(new Object()));
        assertTrue(presentSubValue(Keys.elementKey(0), null, forArray())
                .equals(presentSubValue(Keys.elementKey(0), null, forArray())));
        assertTrue(presentSubValue(Keys.elementKey(0), value, forArray())
                .equals(presentSubValue(Keys.elementKey(0), value, forArray())));
        assertFalse(presentSubValue(Keys.elementKey(0), null, forArray())
                .equals(presentSubValue(Keys.elementKey(0), value, forArray())));
        assertFalse(presentSubValue(Keys.elementKey(0), value, forArray())
                .equals(presentSubValue(Keys.elementKey(1), value, forArray())));
        assertFalse(presentSubValue(Keys.elementKey(0), new Object(), forArray())
                .equals(presentSubValue(Keys.elementKey(0), new Object(), forArray())));
        assertTrue(presentSubValue(Keys.elementKey(0), new Integer(1), forArray())
                .equals(presentSubValue(Keys.elementKey(0), new Integer(1), forArray())));
        assertFalse(presentSubValue(Keys.elementKey(0), new Integer(1), forArray())
                .equals(presentSubValue(Keys.elementKey(0), new Integer(2), forArray())));
    }

    @Test
    public void missing_equals() {
        CheckResult instance = missingSubValue(key, forArray());
        assertTrue(instance.equals(instance));
        assertFalse(instance.equals(null));
        assertFalse(instance.equals(new Object()));
        assertTrue(missingSubValue(Keys.elementKey(0), forArray())
                .equals(missingSubValue(Keys.elementKey(0), forArray())));
        assertFalse(missingSubValue(Keys.elementKey(0), forArray())
                .equals(missingSubValue(Keys.elementKey(1), forArray())));
    }

    @Test
    public void broken_equals() {
        CheckResult instance = brokenSubValue(key, throwable, forArray());
        assertTrue(instance.equals(instance));
        assertFalse(instance.equals(null));
        assertFalse(instance.equals(new Object()));
        assertTrue(brokenSubValue(Keys.elementKey(0), throwable, forArray())
                .equals(brokenSubValue(Keys.elementKey(0), throwable, forArray())));
        assertFalse(brokenSubValue(Keys.elementKey(0), throwable, forArray())
                .equals(brokenSubValue(Keys.elementKey(1), throwable, forArray())));
        assertFalse(brokenSubValue(Keys.elementKey(0), new RuntimeException(), forArray())
                .equals(brokenSubValue(Keys.elementKey(0), new RuntimeException(), forArray())));
    }
}
