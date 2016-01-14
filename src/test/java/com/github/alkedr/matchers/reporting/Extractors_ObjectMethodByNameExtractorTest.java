package com.github.alkedr.matchers.reporting;

import org.junit.Test;

import static com.github.alkedr.matchers.reporting.ExtractingMatcher.Extractor.ExtractedValue.missing;
import static com.github.alkedr.matchers.reporting.ExtractingMatcher.Extractor.ExtractedValue.normal;
import static com.github.alkedr.matchers.reporting.ReportingMatcher.Reporter.ValueStatus.BROKEN;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class Extractors_ObjectMethodByNameExtractorTest {
    private final MyClass item = new MyClass();

    @Test(expected = NullPointerException.class)
    public void nullMethod() {
        new Extractors.MethodByNameExtractor(null);
    }

    @Test(expected = NullPointerException.class)
    public void nullArgumentsArray() {
        new Extractors.MethodByNameExtractor("returnArg", (Object[]) null);
    }

    @Test
    public void nullItem() {
        assertReflectionEquals(missing(), new Extractors.MethodByNameExtractor("returnArg", 1).extractFrom(null));
    }

    @Test
    public void inaccessibleMethodWithArguments() {
        assertReflectionEquals(normal("2", 2), new Extractors.MethodByNameExtractor("returnArg", 2).extractFrom(item));
    }

    @Test
    public void inaccessibleStaticMethodWithArguments() {
        assertReflectionEquals(normal("3", 3), new Extractors.MethodByNameExtractor("returnArgStatic", 3).extractFrom(item));
    }

    @Test
    public void itemHasWrongClass() {
        ExtractingMatcher.Extractor.ExtractedValue actual = new Extractors.MethodByNameExtractor("returnArg", 1).extractFrom(new Object());
        assertEquals(BROKEN, actual.getStatus());
        assertThat(actual.getValueAsString(), containsString("NoSuchMethodException"));
        assertNull(actual.getValue());
    }

    @Test
    public void wrongParameterCount() {
        ExtractingMatcher.Extractor.ExtractedValue actual = new Extractors.MethodByNameExtractor("returnArg").extractFrom(item);
        assertEquals(BROKEN, actual.getStatus());
        assertThat(actual.getValueAsString(), containsString("NoSuchMethodException"));
        assertNull(actual.getValue());
    }

    @Test
    public void wrongParameterType() {
        ExtractingMatcher.Extractor.ExtractedValue actual = new Extractors.MethodByNameExtractor("returnArg", "1").extractFrom(item);
        assertEquals(BROKEN, actual.getStatus());
        assertThat(actual.getValueAsString(), containsString("NoSuchMethodException"));
        assertNull(actual.getValue());
    }

    @Test
    public void throwingMethod() {
        ExtractingMatcher.Extractor.ExtractedValue actual = new Extractors.MethodByNameExtractor("throwingMethod").extractFrom(item);
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
