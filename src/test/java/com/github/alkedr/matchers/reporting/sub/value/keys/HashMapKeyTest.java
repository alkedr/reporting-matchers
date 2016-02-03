package com.github.alkedr.matchers.reporting.sub.value.keys;

public class HashMapKeyTest {
    /*@Test
    public void asStringTest() {
        assertEquals("123", hashMapKey(123).asString());
    }

    @Test
    public void hashCodeTest() {
        assertEquals(hashMapKey(0).hashCode(), hashMapKey(0).hashCode());
    }

    @Test
    public void equalsTest() {
        assertEquals(hashMapKey(0), hashMapKey(0));
        assertNotEquals(hashMapKey(0), hashMapKey(1));
    }


    @Test
    public void nullItem() {
        ExtractableKey key = hashMapKey("1");
        verifyAbsent(
                listener -> key.run(null, listener),
                sameInstance(key)
        );
    }

    @Test
    public void itemIsNotMap() {
        ExtractableKey key = hashMapKey("1");
        verifyBroken(
                listener -> key.run(new Object(), listener),
                sameInstance(key),
                ClassCastException.class
        );
    }

    @Test
    public void keyIsAbsent() {
        ExtractableKey key = hashMapKey("1");
        verifyAbsent(
                listener -> key.run(singletonMap("2", "q"), listener),
                sameInstance(key)
        );
    }

    @Test
    public void keyIsPresent() {
        ExtractableKey key = hashMapKey("1");
        verifyPresent(
                listener -> key.run(singletonMap("1", "q"), listener),
                sameInstance(key),
                equalTo("q")
        );
    }

    @Test
    public void keyIsNull() {
        ExtractableKey key = hashMapKey(null);
        verifyPresent(
                listener -> key.run(singletonMap(null, "1"), listener),
                sameInstance(key),
                equalTo("1")
        );
    }

    @Test
    public void keyIsPresentButValueIsNull() {
        ExtractableKey key = hashMapKey("1");
        verifyPresent(
                listener -> key.run(singletonMap("1", null), listener),
                sameInstance(key),
                nullValue()
        );
    }

    @Test
    public void extractFrom_absentItem() {
        ExtractableKey key = hashMapKey("1");
        verifyAbsent(
                key::runForAbsentItem,
                sameInstance(key)
        );
    }*/
}
