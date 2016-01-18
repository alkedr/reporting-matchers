package com.github.alkedr.matchers.reporting.extraction;

import com.github.alkedr.matchers.reporting.ReportingMatcher;
import com.github.alkedr.matchers.reporting.keys.MethodByNameKey;
import com.github.alkedr.matchers.reporting.keys.MethodKey;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class MethodByNameExtractorTest {
    private final Method returnArgMethod;
    private final Method returnArgStaticMethod;
    private final MyClass item = new MyClass();

    public MethodByNameExtractorTest() throws NoSuchMethodException {
        returnArgMethod = MyClass.class.getDeclaredMethod("returnArg", int.class);
        returnArgStaticMethod = MyClass.class.getDeclaredMethod("returnArgStatic", int.class);
    }

    @Test(expected = NullPointerException.class)
    public void nullMethod() {
        new MethodByNameExtractor(null);
    }

    @Test(expected = NullPointerException.class)
    public void nullArgumentsArray() {
        new MethodByNameExtractor("returnArg", (Object[]) null);
    }

    @Test
    public void nullItem() {
        assertReflectionEquals(
                new ExtractingMatcher.KeyValue(new MethodByNameKey("returnArg", 1), ReportingMatcher.Value.missing()),
                new MethodByNameExtractor("returnArg", 1).extractFrom(null)
        );
    }

    @Test
    public void inaccessibleMethodWithArguments() {
        assertReflectionEquals(
                new ExtractingMatcher.KeyValue(new MethodKey(returnArgMethod, 2), ReportingMatcher.Value.present(2)),
                new MethodByNameExtractor("returnArg", 2).extractFrom(item)
        );
    }

    @Test
    public void inaccessibleStaticMethodWithArguments() {
        assertReflectionEquals(
                new ExtractingMatcher.KeyValue(new MethodKey(returnArgStaticMethod, 3), ReportingMatcher.Value.present(3)),
                new MethodByNameExtractor("returnArgStatic", 3).extractFrom(item)
        );
    }

    @Test
    public void noSuchMethod() {
        ExtractingMatcher.KeyValue actual = new MethodByNameExtractor("returnArg", 1).extractFrom(new Object());
        assertEquals(new MethodByNameKey("returnArg", 1), actual.key);
        assertEquals(ReportingMatcher.PresenceStatus.MISSING, actual.value.presenceStatus());
        assertNull(actual.value.get());
        assertSame(NoSuchMethodException.class, actual.value.extractionThrowable().getClass());
    }

    @Test
    public void wrongParameterCount() {
        ExtractingMatcher.KeyValue actual = new MethodByNameExtractor("returnArg").extractFrom(item);
        assertEquals(new MethodByNameKey("returnArg"), actual.key);
        assertEquals(ReportingMatcher.PresenceStatus.MISSING, actual.value.presenceStatus());
        assertNull(actual.value.get());
        assertSame(NoSuchMethodException.class, actual.value.extractionThrowable().getClass());
    }

    @Test
    public void wrongParameterType() {
        ExtractingMatcher.KeyValue actual = new MethodByNameExtractor("returnArg", "1").extractFrom(item);
        assertEquals(new MethodByNameKey("returnArg", "1"), actual.key);
        assertEquals(ReportingMatcher.PresenceStatus.MISSING, actual.value.presenceStatus());
        assertNull(actual.value.get());
        assertSame(NoSuchMethodException.class, actual.value.extractionThrowable().getClass());
    }

    @Test
    public void extractFromMissingItem() {
        assertReflectionEquals(
                new ExtractingMatcher.KeyValue(new MethodByNameKey("returnArg", 1), ReportingMatcher.Value.missing()),
                new MethodByNameExtractor("returnArg", 1).extractFromMissingItem()
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
