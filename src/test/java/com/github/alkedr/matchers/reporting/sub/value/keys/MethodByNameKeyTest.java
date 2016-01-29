package com.github.alkedr.matchers.reporting.sub.value.keys;

import org.junit.Test;

import java.lang.reflect.Method;

import static com.github.alkedr.matchers.reporting.sub.value.keys.ExtractorVerificationUtils.verifyAbsent;
import static com.github.alkedr.matchers.reporting.sub.value.keys.ExtractorVerificationUtils.verifyBroken;
import static com.github.alkedr.matchers.reporting.sub.value.keys.ExtractorVerificationUtils.verifyPresent;
import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.methodByNameKey;
import static com.github.alkedr.matchers.reporting.sub.value.keys.Keys.methodKey;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

// TODO: аргумент null, аргумент null с перегрузками, из которых невозможно выбрать
public class MethodByNameKeyTest {
    private final Method returnArgMethod = MyClass.class.getDeclaredMethod("returnArg", int.class);
    private final Method returnArgStaticMethod = MyClass.class.getDeclaredMethod("returnArgStatic", int.class);

    public MethodByNameKeyTest() throws NoSuchMethodException {
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
        ExtractableKey key = methodByNameKey("returnArg", 1);
        verifyAbsent(
                listener -> key.run(null, listener),
                sameInstance(key)
        );
    }

    @Test
    public void inaccessibleMethodWithArguments() {
        ExtractableKey key = methodByNameKey("returnArg", 1);
        verifyPresent(
                listener -> key.run(new MyClass(), listener),
                equalTo(methodKey(returnArgMethod, 1)),
                equalTo(1)
        );
    }

    @Test
    public void inaccessibleStaticMethodWithArguments() {
        verifyPresent(
                listener -> methodByNameKey("returnArgStatic", 2).run(new MyClass(), listener),
                equalTo(methodKey(returnArgStaticMethod, 2)),
                equalTo(2)
        );
    }

    @Test
    public void noSuchMethod() {
        ExtractableKey key = methodByNameKey("returnArg", 1);
        verifyBroken(
                listener -> key.run(new Object(), listener),
                sameInstance(key),
                NoSuchMethodException.class
        );
    }

    @Test
    public void wrongParameterCount() {
        ExtractableKey key = methodByNameKey("returnArg");
        verifyBroken(
                listener -> key.run(new MyClass(), listener),
                sameInstance(key),
                NoSuchMethodException.class
        );
    }

    @Test
    public void wrongParameterType() {
        ExtractableKey key = methodByNameKey("returnArg", "1");
        verifyBroken(
                listener -> key.run(new MyClass(), listener),
                sameInstance(key),
                NoSuchMethodException.class
        );
    }

    @Test
    public void extractFromAbsentItem() {
        ExtractableKey key = methodByNameKey("returnArg", 1);
        verifyAbsent(
                key::runForAbsentItem,
                sameInstance(key)
        );
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
