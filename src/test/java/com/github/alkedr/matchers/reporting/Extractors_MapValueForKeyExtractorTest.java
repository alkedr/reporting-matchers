package com.github.alkedr.matchers.reporting;

public class Extractors_MapValueForKeyExtractorTest {
    /*@Test
    public void nullItem() {
        assertReflectionEquals(missing(), new Extractors.ValueForKeyExtractor("1").extractFrom(null));
    }

    @Test
    public void itemIsNotMap() {
        ExtractingMatcher.Extractor.ExtractedValue actual = new Extractors.ValueForKeyExtractor("1").extractFrom(new Object());
        assertEquals(BROKEN, actual.getStatus());
        assertThat(actual.getValueAsString(), containsString("ClassCastException"));
        assertNull(actual.getValue());
    }

    @Test
    public void keyIsMissing() {
        assertReflectionEquals(missing(), new Extractors.ValueForKeyExtractor("2").extractFrom(singletonMap("1", "q")));
    }

    @Test
    public void keyIsPresent() {
        assertReflectionEquals(normal("q", "q"), new Extractors.ValueForKeyExtractor("1").extractFrom(singletonMap("1", "q")));
    }

    @Test
    public void keyIsPresentButValueIsNull() {
        assertReflectionEquals(normal("null", null), new Extractors.ValueForKeyExtractor("1").extractFrom(singletonMap("1", null)));
    }*/
}
