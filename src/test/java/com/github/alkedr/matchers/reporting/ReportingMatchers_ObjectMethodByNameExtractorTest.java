package com.github.alkedr.matchers.reporting;

import org.junit.Test;

import static com.github.alkedr.matchers.reporting.ExtractingMatcher.Extractor.ExtractedValue.missing;
import static com.github.alkedr.matchers.reporting.ExtractingMatcher.Extractor.ExtractedValue.normal;
import static com.github.alkedr.matchers.reporting.ReportingMatcher.Reporter.ValueStatus.BROKEN;
import static com.github.alkedr.matchers.reporting.ReportingMatchers.createMethodByNameExtractor;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class ReportingMatchers_ObjectMethodByNameExtractorTest {
    private final MyClass item = new MyClass();

    @Test(expected = NullPointerException.class)
    public void nullMethod() {
        createMethodByNameExtractor(null);
    }

    @Test(expected = NullPointerException.class)
    public void nullArgumentsArray() {
        createMethodByNameExtractor("returnArg", null);
    }

    @Test
    public void nullItem() {
        assertReflectionEquals(missing(), createMethodByNameExtractor("returnArg", 1).extractFrom(null));
    }

    @Test
    public void inaccessibleMethodWithArguments() {
        assertReflectionEquals(normal("2", 2), createMethodByNameExtractor("returnArg", 2).extractFrom(item));
    }

    @Test
    public void inaccessibleStaticMethodWithArguments() {
        assertReflectionEquals(normal("3", 3), createMethodByNameExtractor("returnArgStatic", 3).extractFrom(item));
    }

    @Test
    public void itemHasWrongClass() {
        ExtractingMatcher.Extractor.ExtractedValue actual = createMethodByNameExtractor("returnArg", 1).extractFrom(new Object());
        assertEquals(BROKEN, actual.getStatus());
        assertThat(actual.getValueAsString(), containsString("NoSuchMethodException"));
        assertNull(actual.getValue());
    }

    @Test
    public void wrongParameterCount() {
        ExtractingMatcher.Extractor.ExtractedValue actual = createMethodByNameExtractor("returnArg").extractFrom(item);
        assertEquals(BROKEN, actual.getStatus());
        assertThat(actual.getValueAsString(), containsString("NoSuchMethodException"));
        assertNull(actual.getValue());
    }

    @Test
    public void wrongParameterType() {
        ExtractingMatcher.Extractor.ExtractedValue actual = createMethodByNameExtractor("returnArg", "1").extractFrom(item);
        assertEquals(BROKEN, actual.getStatus());
        assertThat(actual.getValueAsString(), containsString("NoSuchMethodException"));
        assertNull(actual.getValue());
    }

    @Test
    public void throwingMethod() {
        ExtractingMatcher.Extractor.ExtractedValue actual = createMethodByNameExtractor("throwingMethod").extractFrom(item);
        assertEquals(BROKEN, actual.getStatus());
        assertThat(actual.getValueAsString(), containsString("RuntimeException"));
        assertNull(actual.getValue());
    }


    public static class MyClass {
        public int returnArg(int arg) {
            return arg;
        }

        public static int returnArgStatic(int arg) {
            return arg;
        }

        public void throwingMethod() {
            throw new RuntimeException();
        }
    }
}
