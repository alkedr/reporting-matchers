package com.github.alkedr.matchers.reporting.sub.value.extractors;

import org.junit.Test;

import java.lang.reflect.Field;

import static com.github.alkedr.matchers.reporting.sub.value.extractors.SubValueExtractors.objectFieldsExtractor;
import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.fieldKey;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class ObjectFieldsExtractorTest {
    private final SubValuesExtractor.SubValuesListener listener = mock(SubValuesExtractor.SubValuesListener.class);
    private final Field field = MyClassWithFields.class.getDeclaredField("field");
    private final Field staticField = MyClassWithFields.class.getDeclaredField("staticField");

    public ObjectFieldsExtractorTest() throws NoSuchFieldException {
    }

    @Test
    public void noFields() {
        objectFieldsExtractor().run(new MyClassWithoutFields(), listener);
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void twoFields() {
        objectFieldsExtractor().run(new MyClassWithFields(), listener);
        verify(listener).present(eq(fieldKey(field)), eq(1));
        verify(listener).present(eq(fieldKey(staticField)), eq(2));
        verifyNoMoreInteractions(listener);
    }


    private static class MyClassWithoutFields {
    }

    private static class MyClassWithFields {
        private final int field = 1;
        private static final int staticField = 2;
    }
}
