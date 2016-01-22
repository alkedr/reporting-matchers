package com.github.alkedr.matchers.reporting.extractors;

import com.github.alkedr.matchers.reporting.keys.Keys;
import org.junit.Test;

import java.lang.reflect.Method;

import static com.github.alkedr.matchers.reporting.extractors.ExtractingMatcherExtractors.methodByNameExtractor;
import static org.junit.Assert.assertEquals;
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
        methodByNameExtractor(null);
    }

    @Test(expected = NullPointerException.class)
    public void nullArgumentsArray() {
        methodByNameExtractor("returnArg", (Object[]) null);
    }

    @Test
    public void nullItem() {
        assertReflectionEquals(
                new Extractor.Result.Missing(Keys.methodByNameKey("returnArg", 1)),
                methodByNameExtractor("returnArg", 1).extractFrom(null)
        );
    }

    @Test
    public void inaccessibleMethodWithArguments() {
        assertReflectionEquals(
                new Extractor.Result.Present(Keys.methodKey(returnArgMethod, 2), 2),
                methodByNameExtractor("returnArg", 2).extractFrom(item)
        );
    }

    @Test
    public void inaccessibleStaticMethodWithArguments() {
        assertReflectionEquals(
                new Extractor.Result.Present(Keys.methodKey(returnArgStaticMethod, 3), 3),
                methodByNameExtractor("returnArgStatic", 3).extractFrom(item)
        );
    }

    @Test
    public void noSuchMethod() {
        Extractor.Result.Broken actual = (Extractor.Result.Broken) methodByNameExtractor("returnArg", 1).extractFrom(new Object());
        assertEquals(Keys.methodByNameKey("returnArg", 1), actual.key);
        assertSame(NoSuchMethodException.class, actual.throwable.getClass());
    }

    @Test
    public void wrongParameterCount() {
        Extractor.Result.Broken actual = (Extractor.Result.Broken) methodByNameExtractor("returnArg").extractFrom(item);
        assertEquals(Keys.methodByNameKey("returnArg"), actual.key);
        assertSame(NoSuchMethodException.class, actual.throwable.getClass());
    }

    @Test
    public void wrongParameterType() {
        Extractor.Result.Broken actual = (Extractor.Result.Broken) methodByNameExtractor("returnArg", "1").extractFrom(item);
        assertEquals(Keys.methodByNameKey("returnArg", "1"), actual.key);
        assertSame(NoSuchMethodException.class, actual.throwable.getClass());
    }

    @Test
    public void extractFromMissingItem() {
        assertReflectionEquals(
                new Extractor.Result.Missing(Keys.methodByNameKey("returnArg", 1)),
                methodByNameExtractor("returnArg", 1).extractFromMissingItem()
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
