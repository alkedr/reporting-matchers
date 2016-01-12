package com.github.alkedr.matchers.reporting.object;

import com.github.alkedr.matchers.reporting.ExtractingMatcher;
import org.junit.Test;

import java.lang.reflect.Method;

import static com.github.alkedr.matchers.reporting.ExtractingMatcher.Extractor.ExtractedValue.missing;
import static com.github.alkedr.matchers.reporting.ExtractingMatcher.Extractor.ExtractedValue.normal;
import static com.github.alkedr.matchers.reporting.ReportingMatcher.Reporter.ValueStatus.BROKEN;
import static com.github.alkedr.matchers.reporting.object.ReportingMatchersForObjects.createMethodExtractor;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
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


    @Test(expected = NullPointerException.class)
    public void nullMethod() {
        createMethodExtractor(null);
    }

    @Test(expected = NullPointerException.class)
    public void nullArgumentsArray() {
        createMethodExtractor(inaccessibleMethod, null);
    }

    @Test
    public void nullItem() {
        assertReflectionEquals(missing(), createMethodExtractor(inaccessibleMethod, 1).extractFrom(null));
    }

    @Test
    public void inaccessibleMethodWithArguments() {
        assertReflectionEquals(normal("2", 2), createMethodExtractor(inaccessibleMethod, 2).extractFrom(item));
    }

    @Test
    public void inaccessibleStaticMethodWithArguments() {
        assertReflectionEquals(normal("3", 3), createMethodExtractor(inaccessibleStaticMethod, 3).extractFrom(item));
    }

    @Test
    public void inaccessibleStaticMethodWithArguments_nullItem() {
        assertReflectionEquals(normal("3", 3), createMethodExtractor(inaccessibleStaticMethod, 3).extractFrom(null));
    }

    @Test
    public void itemHasWrongClass() {
        ExtractingMatcher.Extractor.ExtractedValue actual = createMethodExtractor(inaccessibleMethod, 1).extractFrom(new Object());
        assertEquals(BROKEN, actual.getStatus());
        assertThat(actual.getValueAsString(), containsString("IllegalArgumentException"));
        assertNull(actual.getValue());
    }

    @Test
    public void wrongParameterCount() {
        ExtractingMatcher.Extractor.ExtractedValue actual = createMethodExtractor(inaccessibleMethod).extractFrom(item);
        assertEquals(BROKEN, actual.getStatus());
        assertThat(actual.getValueAsString(), containsString("IllegalArgumentException"));
        assertNull(actual.getValue());
    }

    @Test
    public void wrongParameterType() {
        ExtractingMatcher.Extractor.ExtractedValue actual = createMethodExtractor(inaccessibleMethod, "1").extractFrom(item);
        assertEquals(BROKEN, actual.getStatus());
        assertThat(actual.getValueAsString(), containsString("IllegalArgumentException"));
        assertNull(actual.getValue());
    }

    @Test
    public void throwingMethod() {
        ExtractingMatcher.Extractor.ExtractedValue actual = createMethodExtractor(throwingMethod).extractFrom(item);
        assertEquals(BROKEN, actual.getStatus());
        assertThat(actual.getValueAsString(), containsString("RuntimeException"));
        assertNull(actual.getValue());
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
