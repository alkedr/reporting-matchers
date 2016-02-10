package com.github.alkedr.matchers.reporting.sub.value.extractors;

import org.junit.Test;

import java.lang.reflect.Method;

import static com.github.alkedr.matchers.reporting.sub.value.extractors.SubValuesExtractors.getter;
import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.methodKey;
import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.renamedKey;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class GetterExtractorTest {
    private final SubValuesExtractor.SubValuesListener<Object> listener = mock(SubValuesExtractor.SubValuesListener.class);
    private final Method getXMethod = MyClass.class.getDeclaredMethod("getX");
    private final Method regularMethod = MyClass.class.getDeclaredMethod("regular");

    public GetterExtractorTest() throws NoSuchMethodException {
    }


    @Test(expected = NullPointerException.class)
    public void nullMethod() {
        getter(null);
    }

    @Test
    public void getterMethod() {
        getter(getXMethod).run(new MyClass(), listener);
        verify(listener).present(renamedKey(methodKey(getXMethod), "x"), 1);
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void regularMethod() {
        getter(regularMethod).run(new MyClass(), listener);
        verify(listener).present(renamedKey(methodKey(regularMethod), "regular"), 2);
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void runForAbsentItem() {
        getter(getXMethod).runForAbsentItem(listener);
        verify(listener).absent(renamedKey(methodKey(getXMethod), "x"));
        verifyNoMoreInteractions(listener);
    }


    private static class MyClass {
        private int getX() {
            return 1;
        }

        private int regular() {
            return 2;
        }
    }
}
