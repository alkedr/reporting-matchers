package com.github.alkedr.matchers.reporting.object.object;

import java.lang.reflect.Field;

public class MyClass {
    static final Field FIELD;
    static final Field INACCESSSIBLE_FIELD;

    static {
        try {
            FIELD = MyClass.class.getField("myField");
            INACCESSSIBLE_FIELD = MyClass.class.getDeclaredField("myInaccessibleField");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }


    public final int myField;
    private final int myInaccessibleField;

    MyClass(int myField) {
        this.myField = myField;
        this.myInaccessibleField = myField;
    }
}
