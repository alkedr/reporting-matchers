package com.github.alkedr.matchers.reporting.extraction;

public class FieldByNameExtractorTest {
    /*private final MyClass item = new MyClass();

    @Test(expected = NullPointerException.class)
    public void nullField() {
        new Extractors.FieldByNameExtractor(null);
    }

    @Test
    public void nullItem() {
        assertReflectionEquals(missing(), new Extractors.FieldByNameExtractor("myField").extractFrom(null));
    }

    @Test
    public void inaccessibleField() {
        assertReflectionEquals(normal("2", 2), new Extractors.FieldByNameExtractor("myInaccessibleField").extractFrom(item));
    }

    @Test
    public void itemHasWrongClass() {
        ExtractingMatcher.Extractor.ExtractedValue actual = new Extractors.FieldByNameExtractor("myField").extractFrom(new Object());
        assertEquals(BROKEN, actual.getStatus());
        assertThat(actual.getValueAsString(), containsString("IllegalArgumentException"));
        assertNull(actual.getValue());
    }


    private static class MyClass {
        private final int myInaccessibleField = 2;
    }*/
}
