package com.github.alkedr.matchers.reporting.sub.value.extractors;

import org.junit.Test;

import java.lang.reflect.Method;

import static com.github.alkedr.matchers.reporting.sub.value.extractors.SubValuesExtractors.methodByName;
import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.methodByNameKey;
import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.methodKey;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

// TODO: аргумент null, аргумент null с перегрузками, из которых невозможно выбрать
public class MethodByNameExtractorText {
    private final SubValuesExtractor.SubValuesListener<Object> listener = mock(SubValuesExtractor.SubValuesListener.class);
    private final Method returnArgMethod = MyClass.class.getDeclaredMethod("returnArg", int.class);
    private final Method returnArgStaticMethod = MyClass.class.getDeclaredMethod("returnArgStatic", int.class);

    public MethodByNameExtractorText() throws NoSuchMethodException {
    }

    @Test(expected = NullPointerException.class)
    public void nullMethodName() {
        methodByName(null);
    }

    @Test(expected = NullPointerException.class)
    public void nullArguments() {
        methodByName("returnArg", (Object[]) null);
    }

    @Test
    public void nullItem() {
        methodByName("returnArg", 1).run(null, listener);
        verify(listener).absent(methodByNameKey("returnArg", 1));
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void inaccessibleMethodWithArguments() {
        methodByName("returnArg", 1).run(new MyClass(), listener);
        verify(listener).present(methodKey(returnArgMethod, 1), 1);
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void inaccessibleStaticMethodWithArguments() {
        methodByName("returnArgStatic", 2).run(new MyClass(), listener);
        verify(listener).present(methodKey(returnArgStaticMethod, 2), 2);
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void noSuchMethod() {
        methodByName("returnArg", 1).run(new Object(), listener);
        verify(listener).broken(eq(methodByNameKey("returnArg", 1)), isA(NoSuchMethodException.class));
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void wrongParameterCount() {
        methodByName("returnArg").run(new MyClass(), listener);
        verify(listener).broken(eq(methodByNameKey("returnArg")), isA(NoSuchMethodException.class));
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void wrongParameterType() {
        methodByName("returnArg", "1").run(new MyClass(), listener);
        verify(listener).broken(eq(methodByNameKey("returnArg", "1")), isA(NoSuchMethodException.class));
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void extractFromAbsentItem() {
        methodByName("returnArg", 1).runForAbsentItem(listener);
        verify(listener).absent(methodByNameKey("returnArg", 1));
        verifyNoMoreInteractions(listener);
    }

    public static class MyClass {
        public int returnArg(int arg) {
            return arg;
        }

        public static int returnArgStatic(int arg) {
            return arg;
        }
    }
}
