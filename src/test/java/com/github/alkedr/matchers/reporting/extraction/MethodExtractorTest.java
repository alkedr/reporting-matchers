package com.github.alkedr.matchers.reporting.extraction;

import com.github.alkedr.matchers.reporting.ReportingMatcher;
import com.github.alkedr.matchers.reporting.keys.MethodKey;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class MethodExtractorTest {
    private final Method inaccessibleMethod;
    private final Method inaccessibleStaticMethod;
    private final Method throwingMethod;
    private final MyClass item = new MyClass();

    public MethodExtractorTest() throws NoSuchMethodException {
        inaccessibleMethod = MyClass.class.getDeclaredMethod("returnArg", int.class);
        inaccessibleStaticMethod = MyClass.class.getDeclaredMethod("returnArgStatic", int.class);
        throwingMethod = MyClass.class.getDeclaredMethod("throwingMethod");
    }

    // TODO преобразование метода наследника к методу родителя для объединения?


    @Test(expected = NullPointerException.class)
    public void nullMethod() {
        new MethodExtractor(null);
    }

    @Test(expected = NullPointerException.class)
    public void nullArgumentsArray() {
        new MethodExtractor(inaccessibleMethod, (Object[]) null);
    }

    @Test
    public void nullItem() {
        assertReflectionEquals(
                new ExtractingMatcher.KeyValue(new MethodKey(inaccessibleMethod, 1), ReportingMatcher.Value.missing()),
                new MethodExtractor(inaccessibleMethod, 1).extractFrom(null)
        );
    }

    @Test
    public void inaccessibleMethodWithArguments() {
        assertReflectionEquals(
                new ExtractingMatcher.KeyValue(new MethodKey(inaccessibleMethod, 2), ReportingMatcher.Value.present(2)),
                new MethodExtractor(inaccessibleMethod, 2).extractFrom(item)
        );
    }

    @Test
    public void inaccessibleStaticMethodWithArguments() {
        assertReflectionEquals(
                new ExtractingMatcher.KeyValue(new MethodKey(inaccessibleStaticMethod, 3), ReportingMatcher.Value.present(3)),
                new MethodExtractor(inaccessibleStaticMethod, 3).extractFrom(item)
        );
    }

    @Test
    public void inaccessibleStaticMethodWithArguments_nullItem() {
        assertReflectionEquals(
                new ExtractingMatcher.KeyValue(new MethodKey(inaccessibleStaticMethod, 3), ReportingMatcher.Value.present(3)),
                new MethodExtractor(inaccessibleStaticMethod, 3).extractFrom(null)
        );
    }

    @Test
    public void itemHasWrongClass() {
        ExtractingMatcher.KeyValue actual = new MethodExtractor(inaccessibleMethod, 1).extractFrom(new Object());
        assertEquals(new MethodKey(inaccessibleMethod, 1), actual.key);
        assertEquals(ReportingMatcher.PresenceStatus.MISSING, actual.value.presenceStatus());
        assertNull(actual.value.get());
        assertSame(IllegalArgumentException.class, actual.value.extractionThrowable().getClass());
    }

    @Test
    public void wrongParameterCount() {
        ExtractingMatcher.KeyValue actual = new MethodExtractor(inaccessibleMethod).extractFrom(item);
        assertEquals(new MethodKey(inaccessibleMethod), actual.key);
        assertEquals(ReportingMatcher.PresenceStatus.MISSING, actual.value.presenceStatus());
        assertNull(actual.value.get());
        assertSame(IllegalArgumentException.class, actual.value.extractionThrowable().getClass());
    }

    @Test
    public void wrongParameterType() {
        ExtractingMatcher.KeyValue actual = new MethodExtractor(inaccessibleMethod, "1").extractFrom(item);
        assertEquals(new MethodKey(inaccessibleMethod, "1"), actual.key);
        assertEquals(ReportingMatcher.PresenceStatus.MISSING, actual.value.presenceStatus());
        assertNull(actual.value.get());
        assertSame(IllegalArgumentException.class, actual.value.extractionThrowable().getClass());
    }

    @Test
    public void throwingMethod() {
        ExtractingMatcher.KeyValue actual = new MethodExtractor(throwingMethod).extractFrom(item);
        assertEquals(new MethodKey(throwingMethod), actual.key);
        assertEquals(ReportingMatcher.PresenceStatus.MISSING, actual.value.presenceStatus());
        assertNull(actual.value.get());
        assertSame(RuntimeException.class, actual.value.extractionThrowable().getClass());
    }

    @Test
    public void extractFromMissingItem() {
        assertReflectionEquals(
                new ExtractingMatcher.KeyValue(new MethodKey(inaccessibleMethod, 1), ReportingMatcher.Value.missing()),
                new MethodExtractor(inaccessibleMethod, 1).extractFromMissingItem()
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
