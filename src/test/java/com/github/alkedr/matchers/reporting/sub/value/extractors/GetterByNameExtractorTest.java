package com.github.alkedr.matchers.reporting.sub.value.extractors;

import org.junit.Test;

import java.lang.reflect.Method;

import static com.github.alkedr.matchers.reporting.sub.value.extractors.SubValuesExtractors.getterByName;
import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.methodByNameKey;
import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.methodKey;
import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.renamedKey;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class GetterByNameExtractorTest {
    private final SubValuesExtractor.SubValuesListener<Object> listener = mock(SubValuesExtractor.SubValuesListener.class);
    private final Method getXMethod = MyClass.class.getDeclaredMethod("getX");
    private final Method regularMethod = MyClass.class.getDeclaredMethod("regular");

    public GetterByNameExtractorTest() throws NoSuchMethodException {
    }


    @Test(expected = NullPointerException.class)
    public void nullMethodName() {
        getterByName(null);
    }

    @Test
    public void getterMethod() {
        getterByName("getX").run(new MyClass(), listener);
        verify(listener).present(renamedKey(methodKey(getXMethod), "x"), 1);
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void regularMethod() {
        getterByName("regular").run(new MyClass(), listener);
        verify(listener).present(renamedKey(methodKey(regularMethod), "regular"), 2);
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void runForAbsentItem() {
        getterByName("getX").runForAbsentItem(listener);
        verify(listener).absent(renamedKey(methodByNameKey("getX"), "x"));
        verifyNoMoreInteractions(listener);
    }


    public static class MyClass {
        public int getX() {
            return 1;
        }

        public int regular() {
            return 2;
        }
    }
}
