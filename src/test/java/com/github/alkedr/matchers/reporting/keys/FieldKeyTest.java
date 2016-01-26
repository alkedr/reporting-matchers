package com.github.alkedr.matchers.reporting.keys;

import org.junit.Test;

import static com.github.alkedr.matchers.reporting.keys.ExtractorVerificationUtils.verifyBroken;
import static com.github.alkedr.matchers.reporting.keys.ExtractorVerificationUtils.verifyMissing;
import static com.github.alkedr.matchers.reporting.keys.ExtractorVerificationUtils.verifyPresent;
import static com.github.alkedr.matchers.reporting.keys.Keys.fieldKey;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class FieldKeyTest {
    private final ExtractableKey inaccessibleFieldKey;
    private final ExtractableKey staticFieldKey;

    public FieldKeyTest() throws NoSuchFieldException {
        inaccessibleFieldKey = fieldKey(MyClass.class.getDeclaredField("myInaccessibleField"));
        staticFieldKey = fieldKey(MyClass.class.getDeclaredField("MY_STATIC_FIELD"));
    }

    @Test(expected = NullPointerException.class)
    public void nullField() {
        fieldKey(null);
    }

    @Test
    public void asStringTest() {
        assertEquals("myInaccessibleField", inaccessibleFieldKey.asString());
    }

    @Test
    public void hashCodeTest() {
        assertEquals(inaccessibleFieldKey.hashCode(), inaccessibleFieldKey.hashCode());
    }

    @Test
    public void equalsTest() {
        assertEquals(inaccessibleFieldKey, inaccessibleFieldKey);
        assertNotEquals(inaccessibleFieldKey, staticFieldKey);
    }


    @Test
    public void extractFrom_nullItem() {
        verifyMissing(
                () -> inaccessibleFieldKey.extractFrom(null),
                sameInstance(inaccessibleFieldKey)
        );
    }

    @Test
    public void extractFrom_inaccessibleField() {
        verifyPresent(
                () -> inaccessibleFieldKey.extractFrom(new MyClass()),
                sameInstance(inaccessibleFieldKey),
                equalTo(2)
        );
    }

    @Test
    public void extractFrom_inaccessibleStaticField_nullItem() {
        verifyPresent(
                () -> staticFieldKey.extractFrom(null),
                sameInstance(staticFieldKey),
                equalTo(3)
        );
    }

    @Test
    public void extractFrom_inaccessibleStaticField_missingItem() {
        verifyPresent(
                staticFieldKey::extractFromMissingItem,
                sameInstance(staticFieldKey),
                equalTo(3)
        );
    }

    @Test
    public void extractFrom_itemHasWrongClass() {
        verifyBroken(   // TODO: missing?
                () -> inaccessibleFieldKey.extractFrom(new Object()),
                sameInstance(inaccessibleFieldKey),
                IllegalArgumentException.class
        );
    }

    @Test
    public void extractFrom_missingItem() {
        verifyMissing(
                inaccessibleFieldKey::extractFromMissingItem,
                sameInstance(inaccessibleFieldKey)
        );
    }

    private static class MyClass {
        private final int myInaccessibleField = 2;
        private static final int MY_STATIC_FIELD = 3;
    }
}
