package com.github.alkedr.matchers.reporting.extractors;

import org.junit.Test;

import java.lang.reflect.Method;

import static com.github.alkedr.matchers.reporting.ReportingMatchers.noOp;
import static com.github.alkedr.matchers.reporting.check.results.CheckResults.missingSubValue;
import static com.github.alkedr.matchers.reporting.check.results.CheckResults.presentSubValue;
import static com.github.alkedr.matchers.reporting.extractors.ExtractingMatcherExtractors.methodByNameExtractor;
import static com.github.alkedr.matchers.reporting.keys.Keys.methodByNameKey;
import static com.github.alkedr.matchers.reporting.keys.Keys.methodKey;
import static java.util.Collections.emptyIterator;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

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
        methodByNameExtractor(null);
    }

    @Test(expected = NullPointerException.class)
    public void nullArgumentsArray() {
        methodByNameExtractor("returnArg", (Object[]) null);
    }

    @Test
    public void nullItem() {
        assertEquals(
                missingSubValue(methodByNameKey("returnArg", 1), emptyIterator()),
                methodByNameExtractor("returnArg", 1).extractFrom(null).createCheckResult(noOp())
        );
    }

    @Test
    public void inaccessibleMethodWithArguments() {
        assertEquals(
                presentSubValue(methodKey(returnArgMethod, 2), 2, emptyIterator()),
                methodByNameExtractor("returnArg", 2).extractFrom(item).createCheckResult(noOp())
        );
    }

    @Test
    public void inaccessibleStaticMethodWithArguments() {
        assertEquals(
                presentSubValue(methodKey(returnArgStaticMethod, 3), 3, emptyIterator()),
                methodByNameExtractor("returnArgStatic", 3).extractFrom(item).createCheckResult(noOp())
        );
    }

    @Test
    public void noSuchMethod() {
        Extractor.Result.Broken actual = (Extractor.Result.Broken) methodByNameExtractor("returnArg", 1).extractFrom(new Object());
        assertEquals(methodByNameKey("returnArg", 1), actual.key);
        assertSame(NoSuchMethodException.class, actual.throwable.getClass());
    }

    @Test
    public void wrongParameterCount() {
        Extractor.Result.Broken actual = (Extractor.Result.Broken) methodByNameExtractor("returnArg").extractFrom(item);
        assertEquals(methodByNameKey("returnArg"), actual.key);
        assertSame(NoSuchMethodException.class, actual.throwable.getClass());
    }

    @Test
    public void wrongParameterType() {
        Extractor.Result.Broken actual = (Extractor.Result.Broken) methodByNameExtractor("returnArg", "1").extractFrom(item);
        assertEquals(methodByNameKey("returnArg", "1"), actual.key);
        assertSame(NoSuchMethodException.class, actual.throwable.getClass());
    }

    @Test
    public void extractFromMissingItem() {
        assertEquals(
                missingSubValue(methodByNameKey("returnArg", 1), emptyIterator()),
                methodByNameExtractor("returnArg", 1).extractFromMissingItem().createCheckResult(noOp())
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
