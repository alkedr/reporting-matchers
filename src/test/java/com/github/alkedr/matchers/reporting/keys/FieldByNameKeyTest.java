package com.github.alkedr.matchers.reporting.keys;

import org.junit.Test;

import static com.github.alkedr.matchers.reporting.keys.Keys.fieldByNameKey;
import static com.github.alkedr.matchers.reporting.keys.Keys.fieldKey;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class FieldByNameKeyTest {
    private final ExtractableKey.ResultListener result = mock(ExtractableKey.ResultListener.class);
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
        myInaccessibleFieldByNameKey.extractFrom(null, result);
        verify(result).missing(same(myInaccessibleFieldByNameKey));
        verifyNoMoreInteractions(result);
    }

    @Test
    public void extractFrom_inaccessibleField() {
        myInaccessibleFieldByNameKey.extractFrom(new MyClass(), result);
        verify(result).present(eq(myInaccessibleField1Key), eq(2));
        verifyNoMoreInteractions(result);
    }

    @Test
    public void extractFrom_itemDoesNotHaveSuchField() {
        myInaccessibleFieldByNameKey.extractFrom(new Object(), result);
        verify(result).missing(same(myInaccessibleFieldByNameKey));
        verifyNoMoreInteractions(result);
    }

    @Test
    public void extractFrom_itemHasTwoMatchingFields() {
        myInaccessibleFieldByNameKey.extractFrom(new MyClassWithTwoFields(), result);
        verify(result).present(eq(myInaccessibleField2Key), eq(3));
        verifyNoMoreInteractions(result);
    }

    @Test
    public void extractFrom_missingItem() {
        myInaccessibleFieldByNameKey.extractFromMissingItem(result);
        verify(result).missing(same(myInaccessibleFieldByNameKey));
        verifyNoMoreInteractions(result);
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
