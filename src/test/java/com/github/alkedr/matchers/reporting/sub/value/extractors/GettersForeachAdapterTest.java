package com.github.alkedr.matchers.reporting.sub.value.extractors;

import org.junit.Test;

import java.lang.reflect.Method;

import static com.github.alkedr.matchers.reporting.sub.value.extractors.ForeachAdapters.gettersForeachAdepter;
import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.getterKey;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class GettersForeachAdapterTest {
    private final SubValuesExtractor.SubValuesListener listener = mock(SubValuesExtractor.SubValuesListener.class);
    private final Method getGetter = MyClassWithGetter.class.getDeclaredMethod("getX");
    private final Method isGetter1 = MyClassWithGetter.class.getDeclaredMethod("isY");
    private final Method isGetter2 = MyClassWithGetter.class.getDeclaredMethod("isZ");

    public GettersForeachAdapterTest() throws NoSuchMethodException {
    }

    @Test
    public void noGetters() {
        gettersForeachAdepter().run(new MyClassWithoutGetters(), listener);
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void twoGetter() {
        gettersForeachAdepter().run(new MyClassWithGetter(), listener);
        verify(listener).present(eq(getterKey(getGetter)), eq(1));
        verify(listener).present(eq(getterKey(isGetter1)), eq(true));
        verify(listener).present(eq(getterKey(isGetter2)), eq(true));
        verifyNoMoreInteractions(listener);
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
