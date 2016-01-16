package com.github.alkedr.matchers.reporting.extraction;

public class Extractors_ArrayElementExtractorTest {
    /*@Test
    public void nullItem() {
        assertReflectionEquals(missing(), new Extractors.ArrayElementExtractor(1).extractFrom(null));
    }

    @Test
    public void itemIsNotArray() {
        ExtractingMatcher.Extractor.ExtractedValue actual = new Extractors.ArrayElementExtractor(1).extractFrom(new Object());
        assertEquals(BROKEN, actual.getStatus());
        assertThat(actual.getValueAsString(), containsString("ClassCastException"));
        assertNull(actual.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void indexIsNegative() {
        new Extractors.ArrayElementExtractor(-1);
    }

    @Test
    public void indexIsGreaterThanSize() {
        assertReflectionEquals(missing(), new Extractors.ArrayElementExtractor(1).extractFrom(new Object[]{1}));
    }

    @Test
    public void elementIsPresent() {
        assertReflectionEquals(normal("2", 2), new Extractors.ArrayElementExtractor(0).extractFrom(new Object[]{2}));
    }*/
}
