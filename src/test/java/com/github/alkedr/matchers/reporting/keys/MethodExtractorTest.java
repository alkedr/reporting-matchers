package com.github.alkedr.matchers.reporting.keys;

import org.junit.Test;

import java.lang.reflect.Method;

import static com.github.alkedr.matchers.reporting.keys.Keys.methodKey;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Matchers.isA;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class MethodExtractorTest {
    private final ExtractableKey.ResultListener result = mock(ExtractableKey.ResultListener.class);
    private final ExtractableKey inaccessibleMethodKey;
    private final ExtractableKey inaccessibleStaticMethodKey;
    private final ExtractableKey throwingMethodKey;
    private final Method returnArgMethod;
    private final Method returnArgStaticMethod;

    public MethodExtractorTest() throws NoSuchMethodException {
        returnArgMethod = MyClass.class.getDeclaredMethod("returnArg", int.class);
        returnArgStaticMethod = MyClass.class.getDeclaredMethod("returnArgStatic", int.class);
        inaccessibleMethodKey = methodKey(returnArgMethod, 1);
        inaccessibleStaticMethodKey = methodKey(returnArgStaticMethod, 2);
        throwingMethodKey = methodKey(MyClass.class.getDeclaredMethod("throwingMethod"));
    }


    @Test(expected = NullPointerException.class)
    public void nullMethod() {
        methodKey(null);
    }

    @Test(expected = NullPointerException.class)
    public void nullArguments() {
        methodKey(returnArgMethod, (Object[]) null);
    }

    @Test
    public void asStringTest() {
        assertEquals("returnArg()", methodKey(returnArgMethod).asString());
        assertEquals("returnArg(1, 2, qwe)", methodKey(returnArgMethod, 1, 2, "qwe").asString());
    }

    @Test
    public void hashCodeTest() {
        assertEquals(methodKey(returnArgMethod).hashCode(), methodKey(returnArgMethod).hashCode());
    }

    @Test
    public void equalsTest() {
        assertEquals(methodKey(returnArgMethod), methodKey(returnArgMethod));
        assertNotEquals(methodKey(returnArgMethod), methodKey(returnArgStaticMethod));
        assertNotEquals(methodKey(returnArgMethod, 1), methodKey(returnArgMethod, 2));
    }


    @Test
    public void nullItem() {
        inaccessibleMethodKey.extractFrom(null, result);
        verify(result).missing(inaccessibleMethodKey);
        verifyNoMoreInteractions(result);
    }

    @Test
    public void inaccessibleMethodWithArguments() {
        inaccessibleMethodKey.extractFrom(new MyClass(), result);
        verify(result).present(inaccessibleMethodKey, 1);
        verifyNoMoreInteractions(result);
    }

    @Test
    public void inaccessibleStaticMethodWithArguments() {
        inaccessibleStaticMethodKey.extractFrom(new MyClass(), result);
        verify(result).present(inaccessibleStaticMethodKey, 2);
        verifyNoMoreInteractions(result);
    }

    @Test
    public void inaccessibleStaticMethodWithArguments_nullItem() {
        inaccessibleStaticMethodKey.extractFrom(null, result);
        verify(result).present(inaccessibleStaticMethodKey, 2);
        verifyNoMoreInteractions(result);
    }

    @Test
    public void itemHasWrongClass() {
        inaccessibleMethodKey.extractFrom(new Object(), result);
        verify(result).broken(same(inaccessibleMethodKey), isA(IllegalArgumentException.class));
        verifyNoMoreInteractions(result);
    }

    @Test
    public void wrongParameterCount() {
        ExtractableKey keyWithWrongNumberOfParameters = methodKey(returnArgMethod);
        keyWithWrongNumberOfParameters.extractFrom(new MyClass(), result);
        verify(result).broken(same(keyWithWrongNumberOfParameters), isA(IllegalArgumentException.class));
        verifyNoMoreInteractions(result);
    }

    @Test
    public void wrongParameterType() {
        ExtractableKey keyWithWrongParameterType = methodKey(returnArgMethod, "1");
        keyWithWrongParameterType.extractFrom(new MyClass(), result);
        verify(result).broken(same(keyWithWrongParameterType), isA(IllegalArgumentException.class));
        verifyNoMoreInteractions(result);
    }

    @Test
    public void throwingMethod() {
        throwingMethodKey.extractFrom(new MyClass(), result);
        verify(result).broken(same(throwingMethodKey), isA(RuntimeException.class));
        verifyNoMoreInteractions(result);
    }

    @Test
    public void extractFromMissingItem() {
        inaccessibleMethodKey.extractFromMissingItem(result);
        verify(result).missing(inaccessibleMethodKey);
        verifyNoMoreInteractions(result);
    }

    @Test
    public void inaccessibleStaticMethodWithArguments_missingItem() {
        inaccessibleStaticMethodKey.extractFromMissingItem(result);
        verify(result).present(inaccessibleStaticMethodKey, 2);
        verifyNoMoreInteractions(result);
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
