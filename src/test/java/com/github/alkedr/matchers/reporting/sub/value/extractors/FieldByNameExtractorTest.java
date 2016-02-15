package com.github.alkedr.matchers.reporting.sub.value.extractors;

import org.junit.Test;

import java.lang.reflect.Field;

import static com.github.alkedr.matchers.reporting.sub.value.extractors.SubValuesExtractors.fieldByName;
import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.fieldByNameKey;
import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.fieldKey;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class FieldByNameExtractorTest {
    private final SubValuesExtractor.SubValuesListener<Object> listener = mock(SubValuesExtractor.SubValuesListener.class);
    private final Field inaccessibleField1 = MyClass.class.getDeclaredField("myInaccessibleField");
    private final Field inaccessibleField2 = MyClassWithTwoFields.class.getDeclaredField("myInaccessibleField");
    private final Field staticField1 = I1.class.getDeclaredField("MY_INT");

    public FieldByNameExtractorTest() throws NoSuchFieldException {
    }

    @Test(expected = NullPointerException.class)
    public void nullFieldName() {
        fieldByName(null);
    }

    @Test
    public void run_inaccessibleField_nullItem() {
        fieldByName("myInaccessibleField").run(null, listener);
        verify(listener).absent(fieldByNameKey("myInaccessibleField"));
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void run_inaccessibleField() {
        fieldByName("myInaccessibleField").run(new MyClass(), listener);
        verify(listener).present(fieldKey(inaccessibleField1), 2);
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void run_itemDoesNotHaveSuchField() {
        fieldByName("myInaccessibleField").run(new Object(), listener);
        verify(listener).absent(fieldByNameKey("myInaccessibleField"));
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void run_itemHasTwoMatchingFields() {
        fieldByName("myInaccessibleField").run(new MyClassWithTwoFields(), listener);
        verify(listener).present(fieldKey(inaccessibleField2), 3);
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void run_staticField() {
        fieldByName("MY_INT").run(new I1(){}, listener);
        verify(listener).present(fieldKey(staticField1), 4);
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void run_interfaceWithTwoFieldsWithTheSameNames() {
        fieldByName("MY_INT").run(new InterfaceWithTwoFieldsWithTheSameNames(){}, listener);
        verify(listener).broken(eq(fieldByNameKey("MY_INT")), isA(IllegalArgumentException.class));
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void run_absentItem() {
        fieldByName("myInaccessibleField").runForAbsentItem(listener);
        verify(listener).absent(fieldByNameKey("myInaccessibleField"));
        verifyNoMoreInteractions(listener);
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
