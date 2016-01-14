package com.github.alkedr.matchers.reporting;

import com.github.alkedr.matchers.reporting.ExtractingMatcher.Extractor.ExtractedValue;
import org.junit.Test;

import java.lang.reflect.Field;

import static com.github.alkedr.matchers.reporting.ExtractingMatcher.Extractor.ExtractedValue.missing;
import static com.github.alkedr.matchers.reporting.ExtractingMatcher.Extractor.ExtractedValue.normal;
import static com.github.alkedr.matchers.reporting.ReportingMatcher.Reporter.ValueStatus.BROKEN;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class Extractors_ObjectFieldExtractorTest {
    private final Field inaccessibleField;
    private final MyClass item = new MyClass();

    public Extractors_ObjectFieldExtractorTest() throws NoSuchFieldException {
        inaccessibleField = MyClass.class.getDeclaredField("myInaccessibleField");
    }


    @Test(expected = NullPointerException.class)
    public void nullField() {
        new Extractors.FieldExtractor((Field) null);
    }

    @Test
    public void nullItem() {
        assertReflectionEquals(missing(), new Extractors.FieldExtractor(inaccessibleField).extractFrom(null));
    }

    @Test
    public void inaccessibleField() {
        assertReflectionEquals(normal("2", 2), new Extractors.FieldExtractor(inaccessibleField).extractFrom(item));
    }

    @Test
    public void itemHasWrongClass() {
        ExtractedValue actual = new Extractors.FieldExtractor(inaccessibleField).extractFrom(new Object());
        assertEquals(BROKEN, actual.getStatus());
        assertThat(actual.getValueAsString(), containsString("IllegalArgumentException"));
        assertNull(actual.getValue());
    }


    private static class MyClass {
        private final int myInaccessibleField = 2;
    }

    // TODO: static field?
}
