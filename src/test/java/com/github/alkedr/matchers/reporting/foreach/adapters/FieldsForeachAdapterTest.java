package com.github.alkedr.matchers.reporting.foreach.adapters;

import com.github.alkedr.matchers.reporting.keys.Key;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.function.BiConsumer;

import static com.github.alkedr.matchers.reporting.foreach.adapters.ForeachAdapters.fieldsForeachAdepter;
import static com.github.alkedr.matchers.reporting.keys.Keys.fieldKey;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class FieldsForeachAdapterTest {
    private final BiConsumer<Key, Object> consumer = mock(BiConsumer.class);
    private final Field field = MyClassWithFields.class.getDeclaredField("field");
    private final Field staticField = MyClassWithFields.class.getDeclaredField("staticField");

    public FieldsForeachAdapterTest() throws NoSuchFieldException {
    }

    @Test
    public void noFields() {
        fieldsForeachAdepter().run(new MyClassWithoutFields(), consumer);
        verifyNoMoreInteractions(consumer);
    }

    @Test
    public void twoFields() {
        fieldsForeachAdepter().run(new MyClassWithFields(), consumer);
        verify(consumer).accept(eq(fieldKey(field)), eq(1));
        verify(consumer).accept(eq(fieldKey(staticField)), eq(2));
        verifyNoMoreInteractions(consumer);
    }


    private static class MyClassWithoutFields {
    }

    private static class MyClassWithFields {
        private final int field = 1;
        private static final int staticField = 2;
    }
}
