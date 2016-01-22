package com.github.alkedr.matchers.reporting.keys;

import org.junit.Test;

import static com.github.alkedr.matchers.reporting.keys.Keys.fieldByNameKey;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class FieldByNameKeyTest {
    @Test(expected = NullPointerException.class)
    public void nullFieldName() {
        fieldByNameKey(null);
    }

    @Test
    public void asStringTest() {
        assertEquals("field1", fieldByNameKey("field1").asString());
    }

    @Test
    public void hashCodeTest() {
        assertEquals(fieldByNameKey("field1").hashCode(), fieldByNameKey("field1").hashCode());
    }

    @Test
    public void equalsTest() {
        assertEquals(fieldByNameKey("field1"), fieldByNameKey("field1"));
        assertNotEquals(fieldByNameKey("field1"), fieldByNameKey("field2"));
    }
}
