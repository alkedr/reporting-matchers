package com.github.alkedr.matchers.reporting.object;

import com.github.alkedr.matchers.reporting.ExtractingMatcher.Extractor.ExtractedValue;
import org.junit.Test;

import java.lang.reflect.Field;

import static com.github.alkedr.matchers.reporting.ExtractingMatcher.Extractor.ExtractedValue.missing;
import static com.github.alkedr.matchers.reporting.ExtractingMatcher.Extractor.ExtractedValue.normal;
import static com.github.alkedr.matchers.reporting.ReportingMatcher.Reporter.ValueStatus.BROKEN;
import static com.github.alkedr.matchers.reporting.object.ReportingMatchersForObjects.createFieldExtractor;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class FieldExtractorTest {
    private final Field inaccessibleField;
    private final MyClass item = new MyClass();

    public FieldExtractorTest() throws NoSuchFieldException {
        inaccessibleField = MyClass.class.getDeclaredField("myInaccessibleField");
    }


    @Test(expected = NullPointerException.class)
    public void nullField() {
        createFieldExtractor(null);
    }

    @Test
    public void nullItem() {
        assertReflectionEquals(missing(), createFieldExtractor(inaccessibleField).extractFrom(null));
    }

    @Test
    public void inaccessibleField() {
        assertReflectionEquals(normal("2", 2), createFieldExtractor(inaccessibleField).extractFrom(item));
    }

    @Test
    public void itemHasWrongClass() {
        ExtractedValue actual = createFieldExtractor(inaccessibleField).extractFrom(new Object());
        assertEquals(BROKEN, actual.getStatus());
        assertThat(actual.getValueAsString(), containsString("IllegalArgumentException"));
        assertNull(actual.getValue());
    }


    private static class MyClass {
        private final int myInaccessibleField = 2;
    }

    // TODO: static field?
}
