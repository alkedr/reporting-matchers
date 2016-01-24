package com.github.alkedr.matchers.reporting.keys;

import org.junit.Test;

import static com.github.alkedr.matchers.reporting.keys.Keys.fieldKey;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class FieldKeyTest {
    private final ExtractableKey.ResultListener result = mock(ExtractableKey.ResultListener.class);
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
        inaccessibleFieldKey.extractFrom(null, result);
        verify(result).missing(same(inaccessibleFieldKey));
        verifyNoMoreInteractions(result);
    }

    @Test
    public void extractFrom_inaccessibleField() {
        inaccessibleFieldKey.extractFrom(new MyClass(), result);
        verify(result).present(same(inaccessibleFieldKey), eq(2));
        verifyNoMoreInteractions(result);
    }

    @Test
    public void extractFrom_inaccessibleStaticField_nullItem() {
        staticFieldKey.extractFrom(null, result);
        verify(result).present(same(staticFieldKey), eq(3));
        verifyNoMoreInteractions(result);
    }

    @Test
    public void extractFrom_inaccessibleStaticField_missingItem() {
        staticFieldKey.extractFromMissingItem(result);
        verify(result).present(same(staticFieldKey), eq(3));
        verifyNoMoreInteractions(result);
    }

    @Test
    public void extractFrom_itemHasWrongClass() {
        inaccessibleFieldKey.extractFrom(new Object(), result);
        verify(result).broken(eq(inaccessibleFieldKey), isA(IllegalArgumentException.class)); // TODO: missing?
        verifyNoMoreInteractions(result);
    }

    @Test
    public void extractFrom_missingItem() {
        inaccessibleFieldKey.extractFromMissingItem(result);
        verify(result).missing(same(inaccessibleFieldKey));
        verifyNoMoreInteractions(result);
    }

    private static class MyClass {
        private final int myInaccessibleField = 2;
        private static final int MY_STATIC_FIELD = 3;
    }
}
