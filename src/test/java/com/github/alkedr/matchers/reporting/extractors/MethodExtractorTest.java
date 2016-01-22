package com.github.alkedr.matchers.reporting.extractors;

import org.junit.Test;

import java.lang.reflect.Method;

import static com.github.alkedr.matchers.reporting.ReportingMatchers.noOp;
import static com.github.alkedr.matchers.reporting.check.results.CheckResults.missingSubValue;
import static com.github.alkedr.matchers.reporting.check.results.CheckResults.presentSubValue;
import static com.github.alkedr.matchers.reporting.extractors.ExtractingMatcherExtractors.methodExtractor;
import static com.github.alkedr.matchers.reporting.keys.Keys.methodKey;
import static java.util.Collections.emptyIterator;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

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
        assertEquals(
                missingSubValue(methodKey(inaccessibleMethod, 1), emptyIterator()),
                methodExtractor(inaccessibleMethod, 1).extractFrom(null).createCheckResult(noOp())
        );
    }

    @Test
    public void inaccessibleMethodWithArguments() {
        assertEquals(
                presentSubValue(methodKey(inaccessibleMethod, 2), 2, emptyIterator()),
                methodExtractor(inaccessibleMethod, 2).extractFrom(item).createCheckResult(noOp())
        );
    }

    @Test
    public void inaccessibleStaticMethodWithArguments() {
        assertEquals(
                presentSubValue(methodKey(inaccessibleStaticMethod, 3), 3, emptyIterator()),
                methodExtractor(inaccessibleStaticMethod, 3).extractFrom(item).createCheckResult(noOp())
        );
    }

    @Test
    public void inaccessibleStaticMethodWithArguments_nullItem() {
        assertEquals(
                presentSubValue(methodKey(inaccessibleStaticMethod, 3), 3, emptyIterator()),
                methodExtractor(inaccessibleStaticMethod, 3).extractFrom(null).createCheckResult(noOp())
        );
    }

    @Test
    public void itemHasWrongClass() {
        Extractor.Result.Broken actual = (Extractor.Result.Broken) methodExtractor(inaccessibleMethod, 1).extractFrom(new Object());
        assertEquals(methodKey(inaccessibleMethod, 1), actual.key);
        assertSame(IllegalArgumentException.class, actual.throwable.getClass());
    }

    @Test
    public void wrongParameterCount() {
        Extractor.Result.Broken actual = (Extractor.Result.Broken) methodExtractor(inaccessibleMethod).extractFrom(item);
        assertEquals(methodKey(inaccessibleMethod), actual.key);
        assertSame(IllegalArgumentException.class, actual.throwable.getClass());
    }

    @Test
    public void wrongParameterType() {
        Extractor.Result.Broken actual = (Extractor.Result.Broken) methodExtractor(inaccessibleMethod, "1").extractFrom(item);
        assertEquals(methodKey(inaccessibleMethod, "1"), actual.key);
        assertSame(IllegalArgumentException.class, actual.throwable.getClass());
    }

    @Test
    public void throwingMethod() {
        Extractor.Result.Broken actual = (Extractor.Result.Broken) methodExtractor(throwingMethod).extractFrom(item);
        assertEquals(methodKey(throwingMethod), actual.key);
        assertSame(RuntimeException.class, actual.throwable.getClass());
    }

    @Test
    public void extractFromMissingItem() {
        assertEquals(
                missingSubValue(methodKey(inaccessibleMethod, 1), emptyIterator()),
                methodExtractor(inaccessibleMethod, 1).extractFromMissingItem().createCheckResult(noOp())
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
