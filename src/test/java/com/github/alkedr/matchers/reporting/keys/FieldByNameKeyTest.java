package com.github.alkedr.matchers.reporting.keys;

import org.junit.Test;

import java.lang.reflect.Field;

import static com.github.alkedr.matchers.reporting.keys.ExtractorVerificationUtils.verifyBroken;
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
    private final ExtractableKey myInaccessibleFieldByNameKey = fieldByNameKey("myInaccessibleField");
    private final Field myInaccessibleField1 = MyClass.class.getDeclaredField("myInaccessibleField");
    private final Field myInaccessibleField2 = MyClassWithTwoFields.class.getDeclaredField("myInaccessibleField");
    private final Field myStaticField1 = I1.class.getDeclaredField("MY_INT");

    public FieldByNameKeyTest() throws NoSuchFieldException {
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
                equalTo(fieldKey(myInaccessibleField1)),
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
                equalTo(fieldKey(myInaccessibleField2)),
                equalTo(3)
        );
    }

    @Test
    public void extractFrom_staticField() {
        verifyPresent(
                () -> fieldByNameKey("MY_INT").extractFrom(new I1(){}),
                equalTo(fieldKey(myStaticField1)),
                equalTo(4)
        );
    }

    @Test
    public void extractFrom_interfaceWithTwoFieldsWithTheSameNames() {
        ExtractableKey key = fieldByNameKey("MY_INT");
        verifyBroken(
                () -> key.extractFrom(new InterfaceWithTwoFieldsWithTheSameNames(){}),
                sameInstance(key),
                IllegalArgumentException.class
        );
    }

    @Test
    public void extractFrom_missingItem() {
        verifyMissing(
                myInaccessibleFieldByNameKey::extractFromMissingItem,
                sameInstance(myInaccessibleFieldByNameKey)
        );
    }


    private static class MyClass {
        public final int myInaccessibleField = 2;
    }

    private static class MyClassWithTwoFields extends MyClass {
        public final int myInaccessibleField = 3;
    }


    private interface I1 {
        int MY_INT = 4;
    }

    private interface I2 {
        int MY_INT = 5;
    }

    private interface InterfaceWithTwoFieldsWithTheSameNames extends I1, I2 {
    }
}
