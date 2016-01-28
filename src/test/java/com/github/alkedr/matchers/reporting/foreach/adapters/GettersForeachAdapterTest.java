package com.github.alkedr.matchers.reporting.foreach.adapters;

import com.github.alkedr.matchers.reporting.keys.Key;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.function.BiConsumer;

import static com.github.alkedr.matchers.reporting.foreach.adapters.ForeachAdapters.gettersForeachAdepter;
import static com.github.alkedr.matchers.reporting.keys.Keys.getterKey;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class GettersForeachAdapterTest {
    private final BiConsumer<Key, Object> consumer = mock(BiConsumer.class);
    private final Method getGetter = MyClassWithGetter.class.getDeclaredMethod("getX");
    private final Method isGetter1 = MyClassWithGetter.class.getDeclaredMethod("isY");
    private final Method isGetter2 = MyClassWithGetter.class.getDeclaredMethod("isZ");

    public GettersForeachAdapterTest() throws NoSuchMethodException {
    }

    @Test
    public void noGetters() {
        gettersForeachAdepter().run(new MyClassWithoutGetters(), consumer);
        verifyNoMoreInteractions(consumer);
    }

    @Test
    public void twoGetter() {
        gettersForeachAdepter().run(new MyClassWithGetter(), consumer);
        verify(consumer).accept(eq(getterKey(getGetter)), eq(1));
        verify(consumer).accept(eq(getterKey(isGetter1)), eq(true));
        verify(consumer).accept(eq(getterKey(isGetter2)), eq(true));
        verifyNoMoreInteractions(consumer);
    }


    public static class MyClassWithoutGetters {
        public int getter() {
            return 0;
        }

        public void getA() {
        }

        public int getB(int x) {
            return 0;
        }

        public int isY() {
            return 0;
        }
    }

    public static class MyClassWithGetter {
        public int getX() {
            return 1;
        }

        public boolean isY() {
            return true;
        }

        public Boolean isZ() {
            return true;
        }
    }
}
