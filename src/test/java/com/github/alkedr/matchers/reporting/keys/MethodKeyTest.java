package com.github.alkedr.matchers.reporting.keys;

import org.junit.Test;

import java.lang.reflect.Method;

import static com.github.alkedr.matchers.reporting.keys.ExtractorVerificationUtils.verifyBroken;
import static com.github.alkedr.matchers.reporting.keys.ExtractorVerificationUtils.verifyMissing;
import static com.github.alkedr.matchers.reporting.keys.ExtractorVerificationUtils.verifyPresent;
import static com.github.alkedr.matchers.reporting.keys.Keys.methodKey;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class MethodKeyTest {
    private final Method returnArgMethod = MyClass.class.getDeclaredMethod("returnArg", int.class);
    private final Method returnArgStaticMethod = MyClass.class.getDeclaredMethod("returnArgStatic", int.class);
    private final Method throwingMethod = MyClass.class.getDeclaredMethod("throwingMethod");

    public MethodKeyTest() throws NoSuchMethodException {
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
        ExtractableKey key = methodKey(returnArgMethod, 1);
        verifyMissing(
                () -> key.extractFrom(null),
                sameInstance(key)
        );
    }

    @Test
    public void inaccessibleMethodWithArguments() {
        ExtractableKey key = methodKey(returnArgMethod, 1);
        verifyPresent(
                () -> key.extractFrom(new MyClass()),
                sameInstance(key),
                equalTo(1)
        );
    }

    @Test
    public void inaccessibleStaticMethodWithArguments() {
        ExtractableKey key = methodKey(returnArgStaticMethod, 2);
        verifyPresent(
                () -> key.extractFrom(new MyClass()),
                sameInstance(key),
                equalTo(2)
        );
    }

    @Test
    public void inaccessibleStaticMethodWithArguments_nullItem() {
        ExtractableKey key = methodKey(returnArgStaticMethod, 2);
        verifyPresent(
                () -> key.extractFrom(null),
                sameInstance(key),
                equalTo(2)
        );
    }

    @Test
    public void itemHasWrongClass() {
        ExtractableKey key = methodKey(returnArgMethod, 1);
        verifyBroken(
                () -> key.extractFrom(new Object()),
                sameInstance(key),
                IllegalArgumentException.class
        );
    }

    @Test
    public void wrongParameterCount() {
        ExtractableKey key = methodKey(returnArgMethod);
        verifyBroken(
                () -> key.extractFrom(new MyClass()),
                sameInstance(key),
                IllegalArgumentException.class
        );
    }

    @Test
    public void wrongParameterType() {
        ExtractableKey key = methodKey(returnArgMethod, "1");
        verifyBroken(
                () -> key.extractFrom(new MyClass()),
                sameInstance(key),
                IllegalArgumentException.class
        );
    }

    @Test
    public void throwingMethod() {
        ExtractableKey key = methodKey(throwingMethod);
        verifyBroken(
                () -> key.extractFrom(new MyClass()),
                sameInstance(key),
                RuntimeException.class
        );
    }

    @Test
    public void extractFromMissingItem() {
        ExtractableKey key = methodKey(returnArgMethod, 1);
        verifyMissing(
                key::extractFromMissingItem,
                sameInstance(key)
        );
    }

    @Test
    public void inaccessibleStaticMethodWithArguments_missingItem() {
        ExtractableKey key = methodKey(returnArgStaticMethod, 2);
        verifyPresent(
                key::extractFromMissingItem,
                sameInstance(key),
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
