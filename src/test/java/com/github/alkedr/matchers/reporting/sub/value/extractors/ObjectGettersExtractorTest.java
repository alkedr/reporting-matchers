package com.github.alkedr.matchers.reporting.sub.value.extractors;

import com.github.alkedr.matchers.reporting.sub.value.keys.Keys;
import org.junit.Test;

import java.lang.reflect.Method;

import static com.github.alkedr.matchers.reporting.sub.value.extractors.SubValuesExtractors.getters;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class ObjectGettersExtractorTest {
    private final SubValuesExtractor.SubValuesListener listener = mock(SubValuesExtractor.SubValuesListener.class);
    private final Method getGetter = MyClassWithGetter.class.getDeclaredMethod("getX");
    private final Method isGetter1 = MyClassWithGetter.class.getDeclaredMethod("isY");
    private final Method isGetter2 = MyClassWithGetter.class.getDeclaredMethod("isZ");

    public ObjectGettersExtractorTest() throws NoSuchMethodException {
    }

    @Test
    public void run_null() {
        getters().run(null, listener);
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void noGetters() {
        getters().run(new MyClassWithoutGetters(), listener);
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void twoGetter() {
        getters().run(new MyClassWithGetter(), listener);
        verify(listener).present(eq(Keys.renamedKey(Keys.methodKey(getGetter), "x")), eq(1));
        verify(listener).present(eq(Keys.renamedKey(Keys.methodKey(isGetter1), "y")), eq(true));
        verify(listener).present(eq(Keys.renamedKey(Keys.methodKey(isGetter2), "z")), eq(true));
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void runForAbsentItem() {
        getters().runForAbsentItem(listener);
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

        public static int getC() {
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
