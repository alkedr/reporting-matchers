package com.github.alkedr.matchers.reporting.keys;

import org.junit.Test;

import java.lang.reflect.Method;

import static com.github.alkedr.matchers.reporting.ReportingMatchers.noOp;
import static com.github.alkedr.matchers.reporting.check.results.CheckResults.missingSubValue;
import static com.github.alkedr.matchers.reporting.check.results.CheckResults.presentSubValue;
import static com.github.alkedr.matchers.reporting.keys.Keys.methodKey;
import static java.util.Collections.emptyIterator;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;

public class MethodKeyTest {
    private final Method inaccessibleMethod;
    private final Method inaccessibleStaticMethod;
    private final Method throwingMethod;
    private final MyClass item = new MyClass();

    public MethodKeyTest() throws NoSuchMethodException {
        inaccessibleMethod = MyClass.class.getDeclaredMethod("returnArg", int.class);
        inaccessibleStaticMethod = MyClass.class.getDeclaredMethod("returnArgStatic", int.class);
        throwingMethod = MyClass.class.getDeclaredMethod("throwingMethod");
    }


    @Test(expected = NullPointerException.class)
    public void nullMethod() {
        methodKey(null);
    }

    @Test(expected = NullPointerException.class)
    public void nullArguments() {
        methodKey(inaccessibleMethod, (Object[]) null);
    }

    @Test
    public void asStringTest() {
        assertEquals("returnArg()", methodKey(inaccessibleMethod).asString());
        assertEquals("returnArg(1, 2, qwe)", methodKey(inaccessibleMethod, 1, 2, "qwe").asString());
    }

    @Test
    public void hashCodeTest() {
        assertEquals(methodKey(inaccessibleMethod).hashCode(), methodKey(inaccessibleMethod).hashCode());
    }

    @Test
    public void equalsTest() {
        assertEquals(methodKey(inaccessibleMethod), methodKey(inaccessibleMethod));
        assertNotEquals(methodKey(inaccessibleMethod), methodKey(inaccessibleStaticMethod));
        assertNotEquals(methodKey(inaccessibleMethod, 1), methodKey(inaccessibleMethod, 2));
    }

    @Test
    public void nullItem() {
        assertEquals(
                missingSubValue(methodKey(inaccessibleMethod, 1), emptyIterator()),
                methodKey(inaccessibleMethod, 1).extractFrom(null).createCheckResult(noOp())
        );
    }

    @Test
    public void inaccessibleMethodWithArguments() {
        assertEquals(
                presentSubValue(methodKey(inaccessibleMethod, 2), 2, emptyIterator()),
                methodKey(inaccessibleMethod, 2).extractFrom(item).createCheckResult(noOp())
        );
    }

    @Test
    public void inaccessibleStaticMethodWithArguments() {
        assertEquals(
                presentSubValue(methodKey(inaccessibleStaticMethod, 3), 3, emptyIterator()),
                methodKey(inaccessibleStaticMethod, 3).extractFrom(item).createCheckResult(noOp())
        );
    }

    @Test
    public void inaccessibleStaticMethodWithArguments_nullItem() {
        assertEquals(
                presentSubValue(methodKey(inaccessibleStaticMethod, 3), 3, emptyIterator()),
                methodKey(inaccessibleStaticMethod, 3).extractFrom(null).createCheckResult(noOp())
        );
    }

    @Test
    public void itemHasWrongClass() {
        ExtractableKey.Result.Broken actual = (ExtractableKey.Result.Broken) methodKey(inaccessibleMethod, 1).extractFrom(new Object());
        assertEquals(methodKey(inaccessibleMethod, 1), actual.key);
        assertSame(IllegalArgumentException.class, actual.throwable.getClass());
    }

    @Test
    public void wrongParameterCount() {
        ExtractableKey.Result.Broken actual = (ExtractableKey.Result.Broken) methodKey(inaccessibleMethod).extractFrom(item);
        assertEquals(methodKey(inaccessibleMethod), actual.key);
        assertSame(IllegalArgumentException.class, actual.throwable.getClass());
    }

    @Test
    public void wrongParameterType() {
        ExtractableKey.Result.Broken actual = (ExtractableKey.Result.Broken) methodKey(inaccessibleMethod, "1").extractFrom(item);
        assertEquals(methodKey(inaccessibleMethod, "1"), actual.key);
        assertSame(IllegalArgumentException.class, actual.throwable.getClass());
    }

    @Test
    public void throwingMethod() {
        ExtractableKey.Result.Broken actual = (ExtractableKey.Result.Broken) methodKey(throwingMethod).extractFrom(item);
        assertEquals(methodKey(throwingMethod), actual.key);
        assertSame(RuntimeException.class, actual.throwable.getClass());
    }

    @Test
    public void extractFromMissingItem() {
        assertEquals(
                missingSubValue(methodKey(inaccessibleMethod, 1), emptyIterator()),
                methodKey(inaccessibleMethod, 1).extractFromMissingItem().createCheckResult(noOp())
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
