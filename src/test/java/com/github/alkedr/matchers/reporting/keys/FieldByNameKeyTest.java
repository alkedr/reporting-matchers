package com.github.alkedr.matchers.reporting.keys;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;

public class FieldByNameKeyTest {
    @Test(expected = NullPointerException.class)
    public void nullFieldName() {
        new FieldByNameKey(null);
    }

    @Test
    public void getters() {
        assertSame("123", new FieldByNameKey("123").getFieldName());
    }

    @Test
    public void asStringTest() {
        assertSame("field1", new FieldByNameKey("field1").asString());
    }

    @Test
    public void hashCodeTest() {
        assertEquals(new FieldByNameKey("field1").hashCode(), new FieldByNameKey("field1").hashCode());
    }

    @Test
    public void equalsTest() {
        assertEquals(new FieldByNameKey("field1"), new FieldByNameKey("field1"));
        assertNotEquals(new FieldByNameKey("field1"), new FieldByNameKey("field2"));
    }
}
