package com.github.alkedr.matchers.reporting.extraction;

public class FieldExtractorTest {
    /*private final Field inaccessibleField;
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
*/
    // TODO: static field?
}
