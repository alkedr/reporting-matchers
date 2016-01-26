package com.github.alkedr.matchers.reporting.keys;

import org.junit.Test;

import static com.github.alkedr.matchers.reporting.keys.ExtractorVerificationUtils.verifyMissing;
import static com.github.alkedr.matchers.reporting.keys.ExtractorVerificationUtils.verifyPresent;
import static com.github.alkedr.matchers.reporting.keys.Keys.fieldByNameKey;
import static com.github.alkedr.matchers.reporting.keys.Keys.fieldKey;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;

public class FieldByNameKeyTest {
    private final ExtractableKey myInaccessibleFieldByNameKey;
    private final ExtractableKey myInaccessibleField1Key;
    private final ExtractableKey myInaccessibleField2Key;

    public FieldByNameKeyTest() throws NoSuchFieldException {
        myInaccessibleFieldByNameKey = fieldByNameKey("myInaccessibleField");
        myInaccessibleField1Key = fieldKey(MyClass.class.getDeclaredField("myInaccessibleField"));
        myInaccessibleField2Key = fieldKey(MyClassWithTwoFields.class.getDeclaredField("myInaccessibleField"));
    }

    @Test(expected = NullPointerException.class)
    public void nullFieldName() {
        fieldByNameKey(null);
    }

    @Test
    public void asStringTest() {
        assertSame("field1", fieldByNameKey("field1").asString());
    }

    @Test
    public void hashCodeTest() {
        assertEquals(fieldByNameKey("field1").hashCode(), fieldByNameKey("field1").hashCode());
    }

    @Test
    public void equalsTest() {
        assertEquals(fieldByNameKey("field1"), fieldByNameKey("field1"));
        assertNotEquals(fieldByNameKey("field1"), fieldByNameKey("field2"));
    }


    @Test
    public void extractFrom_nullItem() {
        verifyMissing(
                () -> myInaccessibleFieldByNameKey.extractFrom(null),
                sameInstance(myInaccessibleFieldByNameKey)
        );
    }

    @Test
    public void extractFrom_inaccessibleField() {
        verifyPresent(
                () -> myInaccessibleFieldByNameKey.extractFrom(new MyClass()),
                equalTo(myInaccessibleField1Key),
                equalTo(2)
        );
    }

    @Test
    public void extractFrom_itemDoesNotHaveSuchField() {
        verifyMissing(
                () -> myInaccessibleFieldByNameKey.extractFrom(new Object()),
                sameInstance(myInaccessibleFieldByNameKey)
        );
    }

    @Test
    public void extractFrom_itemHasTwoMatchingFields() {
        verifyPresent(
                () -> myInaccessibleFieldByNameKey.extractFrom(new MyClassWithTwoFields()),
                equalTo(myInaccessibleField2Key),
                equalTo(3)
        );
    }

    @Test
    public void extractFrom_missingItem() {
        verifyMissing(
                myInaccessibleFieldByNameKey::extractFromMissingItem,
                sameInstance(myInaccessibleFieldByNameKey)
        );
    }

    // TODO: static поле?
    // TODO: static поле + не-static поле с одинаковыми именами
    // TODO: 2 поля с одинаковыми именами, такие, что FieldUtils.getField бросает исключение

    private static class MyClass {
        private final int myInaccessibleField = 2;
    }

    private static class MyClassWithTwoFields extends MyClass {
        private final int myInaccessibleField = 3;
    }
}
