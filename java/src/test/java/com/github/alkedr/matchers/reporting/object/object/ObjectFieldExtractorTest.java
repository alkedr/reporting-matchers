package com.github.alkedr.matchers.reporting.object.object;

import com.github.alkedr.matchers.reporting.ExtractingMatcher.Extractor.ExtractedValue;
import com.github.alkedr.matchers.reporting.object.ObjectFieldExtractor;
import org.junit.Test;

import static com.github.alkedr.matchers.reporting.ReportingMatcher.Reporter.ValueStatus.BROKEN;
import static com.github.alkedr.matchers.reporting.ReportingMatcher.Reporter.ValueStatus.NORMAL;
import static com.github.alkedr.matchers.reporting.object.object.MyClass.FIELD;
import static com.github.alkedr.matchers.reporting.object.object.MyClass.INACCESSSIBLE_FIELD;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class ObjectFieldExtractorTest {
    // TODO: что правильнее делать?
    @Test(expected = NullPointerException.class)
    public void nullInput() {
        new ObjectFieldExtractor(FIELD).extractFrom(null);
    }

    @Test
    public void normal() {
        ExtractedValue actual = new ObjectFieldExtractor(FIELD).extractFrom(new MyClass(1));
        assertEquals(NORMAL, actual.getStatus());
        assertEquals("1", actual.getValueAsString());
        assertEquals(1, actual.getValue());
    }

    @Test
    public void noSuchField() {
        ExtractedValue actual = new ObjectFieldExtractor(FIELD).extractFrom(new Object());
        assertEquals(BROKEN, actual.getStatus());
        assertThat(actual.getValueAsString(), containsString("IllegalArgumentException"));
        assertNull(actual.getValue());
    }

    @Test
    public void inaccessibleField() {
        ExtractedValue actual = new ObjectFieldExtractor(INACCESSSIBLE_FIELD).extractFrom(new Object());
        assertEquals(BROKEN, actual.getStatus());
        assertThat(actual.getValueAsString(), containsString("IllegalAccessException"));
        assertNull(actual.getValue());
    }
}
