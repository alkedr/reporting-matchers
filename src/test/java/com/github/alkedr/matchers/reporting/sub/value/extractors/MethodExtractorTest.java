package com.github.alkedr.matchers.reporting.sub.value.extractors;

import org.junit.Test;

import java.lang.reflect.Method;

import static com.github.alkedr.matchers.reporting.sub.value.extractors.SubValuesExtractors.method;
import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.methodKey;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class MethodExtractorTest {
    private final SubValuesExtractor.SubValuesListener<Object> listener = mock(SubValuesExtractor.SubValuesListener.class);
    private final Method returnArgMethod = MyClass.class.getDeclaredMethod("returnArg", int.class);
    private final Method returnArgStaticMethod = MyClass.class.getDeclaredMethod("returnArgStatic", int.class);
    private final Method throwingMethod = MyClass.class.getDeclaredMethod("throwingMethod");

    public MethodExtractorTest() throws NoSuchMethodException {
    }

    @Test(expected = NullPointerException.class)
    public void nullMethod() {
        method(null);
    }

    @Test(expected = NullPointerException.class)
    public void nullArguments() {
        method(returnArgMethod, (Object[]) null);
    }

    @Test
    public void nullItem() {
        method(returnArgMethod, 1).run(null, listener);
        verify(listener).absent(methodKey(returnArgMethod, 1));
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void inaccessibleMethodWithArguments() {
        method(returnArgMethod, 1).run(new MyClass(), listener);
        verify(listener).present(methodKey(returnArgMethod, 1), 1);
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void inaccessibleStaticMethodWithArguments() {
        method(returnArgStaticMethod, 2).run(new MyClass(), listener);
        verify(listener).present(methodKey(returnArgStaticMethod, 2), 2);
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void inaccessibleStaticMethodWithArguments_nullItem() {
        method(returnArgStaticMethod, 2).run(null, listener);
        verify(listener).present(methodKey(returnArgStaticMethod, 2), 2);
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void itemHasWrongClass() {
        method(returnArgMethod, 1).run(new Object(), listener);
        verify(listener).broken(eq(methodKey(returnArgMethod, 1)), isA(IllegalArgumentException.class));
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void wrongParameterCount() {
        method(returnArgMethod).run(new MyClass(), listener);
        verify(listener).broken(eq(methodKey(returnArgMethod)), isA(IllegalArgumentException.class));
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void wrongParameterType() {
        method(returnArgMethod, "1").run(new MyClass(), listener);
        verify(listener).broken(eq(methodKey(returnArgMethod, "1")), isA(IllegalArgumentException.class));
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void throwingMethod() {
        method(throwingMethod).run(new MyClass(), listener);
        verify(listener).broken(eq(methodKey(throwingMethod)), isA(RuntimeException.class));
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void extractFromAbsentItem() {
        method(returnArgMethod, 1).runForAbsentItem(listener);
        verify(listener).absent(methodKey(returnArgMethod, 1));
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void inaccessibleStaticMethodWithArguments_absentItem() {
        method(returnArgStaticMethod, 2).runForAbsentItem(listener);
        verify(listener).present(methodKey(returnArgStaticMethod, 2), 2);
        verifyNoMoreInteractions(listener);
    }


    private static class MyClass {
        private int returnArg(int arg) {
            return arg;
        }

        private static int returnArgStatic(int arg) {
            return arg;
        }

        private void throwingMethod() {
            throw new RuntimeException();
        }
    }
}
