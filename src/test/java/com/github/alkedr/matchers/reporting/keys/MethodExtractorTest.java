package com.github.alkedr.matchers.reporting.keys;

import org.junit.Test;

import java.lang.reflect.Method;

import static com.github.alkedr.matchers.reporting.keys.ExtractorVerificationUtils.verifyBroken;
import static com.github.alkedr.matchers.reporting.keys.ExtractorVerificationUtils.verifyMissing;
import static com.github.alkedr.matchers.reporting.keys.ExtractorVerificationUtils.verifyPresent;
import static com.github.alkedr.matchers.reporting.keys.Keys.methodKey;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class MethodExtractorTest {
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
        verifyMissing(
                () -> inaccessibleMethodKey.extractFrom(null),
                sameInstance(inaccessibleMethodKey)
        );
    }

    @Test
    public void inaccessibleMethodWithArguments() {
        verifyPresent(
                () -> inaccessibleMethodKey.extractFrom(new MyClass()),
                sameInstance(inaccessibleMethodKey),
                equalTo(1)
        );
    }

    @Test
    public void inaccessibleStaticMethodWithArguments() {
        verifyPresent(
                () -> inaccessibleStaticMethodKey.extractFrom(new MyClass()),
                sameInstance(inaccessibleStaticMethodKey),
                equalTo(2)
        );
    }

    @Test
    public void inaccessibleStaticMethodWithArguments_nullItem() {
        verifyPresent(
                () -> inaccessibleStaticMethodKey.extractFrom(null),
                sameInstance(inaccessibleStaticMethodKey),
                equalTo(2)
        );
    }

    @Test
    public void itemHasWrongClass() {
        verifyBroken(
                () -> inaccessibleMethodKey.extractFrom(new Object()),
                sameInstance(inaccessibleMethodKey),
                IllegalArgumentException.class
        );
    }

    @Test
    public void wrongParameterCount() {
        ExtractableKey keyWithWrongNumberOfParameters = methodKey(returnArgMethod);
        verifyBroken(
                () -> keyWithWrongNumberOfParameters.extractFrom(new MyClass()),
                sameInstance(keyWithWrongNumberOfParameters),
                IllegalArgumentException.class
        );
    }

    @Test
    public void wrongParameterType() {
        ExtractableKey keyWithWrongParameterType = methodKey(returnArgMethod, "1");
        verifyBroken(
                () -> keyWithWrongParameterType.extractFrom(new MyClass()),
                sameInstance(keyWithWrongParameterType),
                IllegalArgumentException.class
        );
    }

    @Test
    public void throwingMethod() {
        verifyBroken(
                () -> throwingMethodKey.extractFrom(new MyClass()),
                sameInstance(throwingMethodKey),
                RuntimeException.class
        );
    }

    @Test
    public void extractFromMissingItem() {
        verifyMissing(
                inaccessibleMethodKey::extractFromMissingItem,
                sameInstance(inaccessibleMethodKey)
        );
    }

    @Test
    public void inaccessibleStaticMethodWithArguments_missingItem() {
        verifyPresent(
                inaccessibleStaticMethodKey::extractFromMissingItem,
                sameInstance(inaccessibleStaticMethodKey),
                equalTo(2)
        );
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
