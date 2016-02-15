package com.github.alkedr.matchers.reporting.sub.value.extractors;

import org.junit.Test;

import java.lang.reflect.Field;

import static com.github.alkedr.matchers.reporting.sub.value.extractors.SubValuesExtractors.field;
import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.fieldKey;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class FieldExtractorTest {
    private final SubValuesExtractor.SubValuesListener<Object> listener = mock(SubValuesExtractor.SubValuesListener.class);
    private final Field inaccessibleField = MyClass.class.getDeclaredField("myInaccessibleField");
    private final Field staticField = MyClass.class.getDeclaredField("MY_STATIC_FIELD");

    public FieldExtractorTest() throws NoSuchFieldException {
    }

    @Test(expected = NullPointerException.class)
    public void nullField() {
        field(null);
    }

    @Test
    public void run_inaccessibleField_nullItem() {
        field(inaccessibleField).run(null, listener);
        verify(listener).absent(fieldKey(inaccessibleField));
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void run_inaccessibleField_correctItem() {
        field(inaccessibleField).run(new MyClass(), listener);
        verify(listener).present(fieldKey(inaccessibleField), 2);
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void run_staticField_nullItem() {
        field(staticField).run(null, listener);
        verify(listener).present(fieldKey(staticField), 3);
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void run_staticField_absentItem() {
        field(staticField).runForAbsentItem(listener);
        verify(listener).present(fieldKey(staticField), 3);
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void run_itemHasWrongClass() {
        field(inaccessibleField).run(new Object(), listener);
        verify(listener).broken(eq(fieldKey(inaccessibleField)), isA(IllegalArgumentException.class));
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void run_inaccessibleField_absentItem() {
        field(inaccessibleField).runForAbsentItem(listener);
        verify(listener).absent(fieldKey(inaccessibleField));
        verifyNoMoreInteractions(listener);
    }

    private static class MyClass {
        private final int myInaccessibleField = 2;
        private static final int MY_STATIC_FIELD = 3;
    }
}
