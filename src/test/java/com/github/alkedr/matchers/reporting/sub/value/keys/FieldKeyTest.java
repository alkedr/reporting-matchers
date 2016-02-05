package com.github.alkedr.matchers.reporting.sub.value.keys;

public class FieldKeyTest {
    /*private final ExtractableKey inaccessibleFieldKey = fieldKey(MyClass.class.getDeclaredField("myInaccessibleField"));
    private final ExtractableKey staticFieldKey = fieldKey(MyClass.class.getDeclaredField("MY_STATIC_FIELD"));

    public FieldKeyTest() throws NoSuchFieldException {
    }

    @Test(expected = NullPointerException.class)
    public void nullField() {
        fieldKey(null);
    }

    @Test
    public void asStringTest() {
        assertEquals("myInaccessibleField", inaccessibleFieldKey.asString());
    }

    @Test
    public void hashCodeTest() {
        assertEquals(inaccessibleFieldKey.hashCode(), inaccessibleFieldKey.hashCode());
    }

    @Test
    public void equalsTest() {
        assertEquals(inaccessibleFieldKey, inaccessibleFieldKey);
        assertNotEquals(inaccessibleFieldKey, staticFieldKey);
    }


    @Test
    public void extractFrom_nullItem() {
        verifyAbsent(
                listener -> inaccessibleFieldKey.run(null, listener),
                sameInstance(inaccessibleFieldKey)
        );
    }

    @Test
    public void extractFrom_inaccessibleField() {
        verifyPresent(
                listener -> inaccessibleFieldKey.run(new MyClass(), listener),
                sameInstance(inaccessibleFieldKey),
                equalTo(2)
        );
    }

    @Test
    public void extractFrom_inaccessibleStaticField_nullItem() {
        verifyPresent(
                listener -> staticFieldKey.run(null, listener),
                sameInstance(staticFieldKey),
                equalTo(3)
        );
    }

    @Test
    public void extractFrom_inaccessibleStaticField_absentItem() {
        verifyPresent(
                staticFieldKey::runForAbsentItem,
                sameInstance(staticFieldKey),
                equalTo(3)
        );
    }

    @Test
    public void extractFrom_itemHasWrongClass() {
        verifyBroken(   // TODO: missing?
                listener ->inaccessibleFieldKey.run(new Object(), listener),
                sameInstance(inaccessibleFieldKey),
                IllegalArgumentException.class
        );
    }

    @Test
    public void extractFrom_absentItem() {
        verifyAbsent(
                inaccessibleFieldKey::runForAbsentItem,
                sameInstance(inaccessibleFieldKey)
        );
    }

    private static class MyClass {
        private final int myInaccessibleField = 2;
        private static final int MY_STATIC_FIELD = 3;
    }*/
}