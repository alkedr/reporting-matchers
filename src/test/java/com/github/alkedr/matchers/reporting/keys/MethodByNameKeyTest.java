package com.github.alkedr.matchers.reporting.keys;

import org.junit.Test;

import java.lang.reflect.Method;

import static com.github.alkedr.matchers.reporting.keys.Keys.methodByNameKey;
import static com.github.alkedr.matchers.reporting.keys.Keys.methodKey;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class MethodByNameKeyTest {
    private final ExtractableKey.ResultListener result = mock(ExtractableKey.ResultListener.class);
    private final Method returnArgMethod;
    private final Method returnArgStaticMethod;

    public MethodByNameKeyTest() throws NoSuchMethodException {
        returnArgMethod = MyClass.class.getDeclaredMethod("returnArg", int.class);
        returnArgStaticMethod = MyClass.class.getDeclaredMethod("returnArgStatic", int.class);
    }

    @Test(expected = NullPointerException.class)
    public void nullMethod() {
        methodByNameKey(null);
    }

    @Test(expected = NullPointerException.class)
    public void nullArguments() {
        methodByNameKey("method1", (Object[]) null);
    }

    @Test
    public void asStringTest() {
        assertEquals("method1()", methodByNameKey("method1").asString());
        assertEquals("method1(1, 2, qwe)", methodByNameKey("method1", 1, 2, "qwe").asString());
    }

    @Test
    public void hashCodeTest() {
        assertEquals(methodByNameKey("method1").hashCode(), methodByNameKey("method1").hashCode());
    }

    @Test
    public void equalsTest() {
        assertEquals(methodByNameKey("method1"), methodByNameKey("method1"));
        assertNotEquals(methodByNameKey("method1"), methodByNameKey("method2"));
        assertNotEquals(methodByNameKey("method1", 1), methodByNameKey("method1", 2));
    }


    @Test
    public void nullItem() {
        ExtractableKey returnArgKey = methodByNameKey("returnArg", 1);
        returnArgKey.extractFrom(null, result);
        verify(result).missing(returnArgKey);
        verifyNoMoreInteractions(result);
    }

    @Test
    public void inaccessibleMethodWithArguments() {
        ExtractableKey returnArgKey = methodByNameKey("returnArg", 1);
        returnArgKey.extractFrom(new MyClass(), result);
        verify(result).present(methodKey(returnArgMethod, 1), 1);
        verifyNoMoreInteractions(result);
    }

    @Test
    public void inaccessibleStaticMethodWithArguments() {
        methodByNameKey("returnArgStatic", 2).extractFrom(new MyClass(), result);
        verify(result).present(methodKey(returnArgStaticMethod, 2), 2);
        verifyNoMoreInteractions(result);
    }

    @Test
    public void noSuchMethod() {
        ExtractableKey key = methodByNameKey("returnArg", 1);
        key.extractFrom(new Object(), result);
        verify(result).broken(eq(key), isA(NoSuchMethodException.class));
        verifyNoMoreInteractions(result);
    }

    @Test
    public void wrongParameterCount() {
        ExtractableKey key = methodByNameKey("returnArg");
        key.extractFrom(new MyClass(), result);
        verify(result).broken(eq(key), isA(NoSuchMethodException.class));
        verifyNoMoreInteractions(result);
    }

    @Test
    public void wrongParameterType() {
        ExtractableKey key = methodByNameKey("returnArg", "1");
        key.extractFrom(new MyClass(), result);
        verify(result).broken(eq(key), isA(NoSuchMethodException.class));
        verifyNoMoreInteractions(result);
    }

    @Test
    public void extractFromMissingItem() {
        ExtractableKey returnArgKey = methodByNameKey("returnArg", 1);
        returnArgKey.extractFromMissingItem(result);
        verify(result).missing(returnArgKey);
        verifyNoMoreInteractions(result);
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
