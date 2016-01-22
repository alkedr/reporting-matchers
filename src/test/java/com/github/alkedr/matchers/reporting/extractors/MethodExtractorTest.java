package com.github.alkedr.matchers.reporting.extractors;

import com.github.alkedr.matchers.reporting.keys.Keys;
import org.junit.Test;

import java.lang.reflect.Method;

import static com.github.alkedr.matchers.reporting.extractors.ExtractingMatcherExtractors.methodExtractor;
import static org.junit.Assert.assertEquals;
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
        methodExtractor(null);
    }

    @Test(expected = NullPointerException.class)
    public void nullArgumentsArray() {
        methodExtractor(inaccessibleMethod, (Object[]) null);
    }

    @Test
    public void nullItem() {
        assertReflectionEquals(
                new Extractor.Result.Missing(Keys.methodKey(inaccessibleMethod, 1)),
                methodExtractor(inaccessibleMethod, 1).extractFrom(null)
        );
    }

    @Test
    public void inaccessibleMethodWithArguments() {
        assertReflectionEquals(
                new Extractor.Result.Present(Keys.methodKey(inaccessibleMethod, 2), 2),
                methodExtractor(inaccessibleMethod, 2).extractFrom(item)
        );
    }

    @Test
    public void inaccessibleStaticMethodWithArguments() {
        assertReflectionEquals(
                new Extractor.Result.Present(Keys.methodKey(inaccessibleStaticMethod, 3), 3),
                methodExtractor(inaccessibleStaticMethod, 3).extractFrom(item)
        );
    }

    @Test
    public void inaccessibleStaticMethodWithArguments_nullItem() {
        assertReflectionEquals(
                new Extractor.Result.Present(Keys.methodKey(inaccessibleStaticMethod, 3), 3),
                methodExtractor(inaccessibleStaticMethod, 3).extractFrom(null)
        );
    }

    @Test
    public void itemHasWrongClass() {
        Extractor.Result.Broken actual = (Extractor.Result.Broken) methodExtractor(inaccessibleMethod, 1).extractFrom(new Object());
        assertEquals(Keys.methodKey(inaccessibleMethod, 1), actual.key);
        assertSame(IllegalArgumentException.class, actual.throwable.getClass());
    }

    @Test
    public void wrongParameterCount() {
        Extractor.Result.Broken actual = (Extractor.Result.Broken) methodExtractor(inaccessibleMethod).extractFrom(item);
        assertEquals(Keys.methodKey(inaccessibleMethod), actual.key);
        assertSame(IllegalArgumentException.class, actual.throwable.getClass());
    }

    @Test
    public void wrongParameterType() {
        Extractor.Result.Broken actual = (Extractor.Result.Broken) methodExtractor(inaccessibleMethod, "1").extractFrom(item);
        assertEquals(Keys.methodKey(inaccessibleMethod, "1"), actual.key);
        assertSame(IllegalArgumentException.class, actual.throwable.getClass());
    }

    @Test
    public void throwingMethod() {
        Extractor.Result.Broken actual = (Extractor.Result.Broken) methodExtractor(throwingMethod).extractFrom(item);
        assertEquals(Keys.methodKey(throwingMethod), actual.key);
        assertSame(RuntimeException.class, actual.throwable.getClass());
    }

    @Test
    public void extractFromMissingItem() {
        assertReflectionEquals(
                new Extractor.Result.Missing(Keys.methodKey(inaccessibleMethod, 1)),
                methodExtractor(inaccessibleMethod, 1).extractFromMissingItem()
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
