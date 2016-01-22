package com.github.alkedr.matchers.reporting.keys;

import org.junit.Test;

import java.lang.reflect.Method;

import static com.github.alkedr.matchers.reporting.ReportingMatchers.noOp;
import static com.github.alkedr.matchers.reporting.check.results.CheckResults.missingSubValue;
import static com.github.alkedr.matchers.reporting.check.results.CheckResults.presentSubValue;
import static com.github.alkedr.matchers.reporting.keys.Keys.methodByNameKey;
import static com.github.alkedr.matchers.reporting.keys.Keys.methodKey;
import static java.util.Collections.emptyIterator;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;

public class MethodByNameKeyTest {
    private final Method returnArgMethod;
    private final Method returnArgStaticMethod;
    private final MyClass item = new MyClass();

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
        assertEquals(
                missingSubValue(methodByNameKey("returnArg", 1), emptyIterator()),
                methodByNameKey("returnArg", 1).extractFrom(null).createCheckResult(noOp())
        );
    }

    @Test
    public void inaccessibleMethodWithArguments() {
        assertEquals(
                presentSubValue(methodKey(returnArgMethod, 2), 2, emptyIterator()),
                methodByNameKey("returnArg", 2).extractFrom(item).createCheckResult(noOp())
        );
    }

    @Test
    public void inaccessibleStaticMethodWithArguments() {
        assertEquals(
                presentSubValue(methodKey(returnArgStaticMethod, 3), 3, emptyIterator()),
                methodByNameKey("returnArgStatic", 3).extractFrom(item).createCheckResult(noOp())
        );
    }

    @Test
    public void noSuchMethod() {
        ExtractableKey.Result.Broken actual = (ExtractableKey.Result.Broken) methodByNameKey("returnArg", 1).extractFrom(new Object());
        assertEquals(methodByNameKey("returnArg", 1), actual.key);
        assertSame(NoSuchMethodException.class, actual.throwable.getClass());
    }

    @Test
    public void wrongParameterCount() {
        ExtractableKey.Result.Broken actual = (ExtractableKey.Result.Broken) methodByNameKey("returnArg").extractFrom(item);
        assertEquals(methodByNameKey("returnArg"), actual.key);
        assertSame(NoSuchMethodException.class, actual.throwable.getClass());
    }

    @Test
    public void wrongParameterType() {
        ExtractableKey.Result.Broken actual = (ExtractableKey.Result.Broken) methodByNameKey("returnArg", "1").extractFrom(item);
        assertEquals(methodByNameKey("returnArg", "1"), actual.key);
        assertSame(NoSuchMethodException.class, actual.throwable.getClass());
    }

    @Test
    public void extractFromMissingItem() {
        assertEquals(
                missingSubValue(methodByNameKey("returnArg", 1), emptyIterator()),
                methodByNameKey("returnArg", 1).extractFromMissingItem().createCheckResult(noOp())
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
