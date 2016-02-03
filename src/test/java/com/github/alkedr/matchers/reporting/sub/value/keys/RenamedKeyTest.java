package com.github.alkedr.matchers.reporting.sub.value.keys;

import static org.mockito.Mockito.mock;

public class RenamedKeyTest {
    private final Object item = new Object();
    private final Object extractedItem = new Object();
    private final Throwable throwable = new RuntimeException();
    private final Key key1 = mock(Key.class);
    private final Key key2 = mock(Key.class);
    private final Key key3 = mock(Key.class);

    /*@Test(expected = NullPointerException.class)
    public void nullKey() {
        renamedExtractableKey(null, "");
    }

    @Test(expected = NullPointerException.class)
    public void nullName() {
        renamedExtractableKey(elementKey(0), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyName() {
        renamedExtractableKey(elementKey(0), "");
    }

    @Test
    public void asStringTest() {
        assertEquals("123", renamedExtractableKey(elementKey(0), "123").asString());
    }

    @Test
    public void hashCodeTest() {
        assertEquals(renamedExtractableKey(elementKey(0), "123").hashCode(), renamedExtractableKey(elementKey(0), "123").hashCode());
    }

    @Test
    public void equalsTest() {
        assertEquals(renamedExtractableKey(elementKey(0), "123"), renamedExtractableKey(elementKey(0), "123"));
        assertNotEquals(renamedExtractableKey(elementKey(0), "123"), renamedExtractableKey(elementKey(1), "123"));
        assertNotEquals(renamedExtractableKey(elementKey(0), "123"), renamedExtractableKey(elementKey(0), "234"));
    }

    @Test
    public void run() {
        SubValuesExtractor.SubValuesListener subValuesListener = mock(SubValuesExtractor.SubValuesListener.class);
        renamedExtractableKey(extractableKey, "1").run(item, subValuesListener);
        verify(subValuesListener).present(eq(renamedKey(key1, "1")), same(extractedItem));
        verify(subValuesListener).absent(eq(renamedKey(key2, "1")));
        verify(subValuesListener).broken(eq(renamedKey(key3, "1")), same(throwable));
        verifyNoMoreInteractions(subValuesListener);
    }

    @Test
    public void runForAbsentItemTest() {
        SubValuesExtractor.SubValuesListener subValuesListener = mock(SubValuesExtractor.SubValuesListener.class);
        renamedExtractableKey(extractableKey, "1").runForAbsentItem(subValuesListener);
        verify(subValuesListener).broken(eq(renamedKey(key1, "1")), same(throwable));
        verify(subValuesListener).absent(eq(renamedKey(key2, "1")));
        verify(subValuesListener).present(eq(renamedKey(key3, "1")), same(extractedItem));
        verifyNoMoreInteractions(subValuesListener);
    }*/
}
