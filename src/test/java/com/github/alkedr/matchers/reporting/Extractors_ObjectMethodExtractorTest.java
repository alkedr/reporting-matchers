package com.github.alkedr.matchers.reporting;

import java.lang.reflect.Method;

public class Extractors_ObjectMethodExtractorTest {
    private final Method inaccessibleMethod;
    private final Method inaccessibleStaticMethod;
    private final Method throwingMethod;
    private final MyClass item = new MyClass();

    public Extractors_ObjectMethodExtractorTest() throws NoSuchMethodException {
        inaccessibleMethod = MyClass.class.getDeclaredMethod("returnArg", int.class);
        inaccessibleStaticMethod = MyClass.class.getDeclaredMethod("returnArgStatic", int.class);
        throwingMethod = MyClass.class.getDeclaredMethod("throwingMethod");
    }


    /*@Test(expected = NullPointerException.class)
    public void nullMethod() {
        new MethodExtractingMatcher(null);
    }

    @Test(expected = NullPointerException.class)
    public void nullArgumentsArray() {
        new MethodExtractingMatcher(inaccessibleMethod, (Object[]) null);
    }
*/
    /*@Test
    public void nullItem() {
        assertReflectionEquals(missing(), new Extractors.MethodExtractor(inaccessibleMethod, 1).extractFrom(null));
    }

    @Test
    public void inaccessibleMethodWithArguments() {
        assertReflectionEquals(normal("2", 2), new Extractors.MethodExtractor(inaccessibleMethod, 2).extractFrom(item));
    }

    @Test
    public void inaccessibleStaticMethodWithArguments() {
        assertReflectionEquals(normal("3", 3), new Extractors.MethodExtractor(inaccessibleStaticMethod, 3).extractFrom(item));
    }

    @Test
    public void inaccessibleStaticMethodWithArguments_nullItem() {
        assertReflectionEquals(normal("3", 3), new Extractors.MethodExtractor(inaccessibleStaticMethod, 3).extractFrom(null));
    }

    @Test
    public void itemHasWrongClass() {
        ExtractingMatcher.Extractor.ExtractedValue actual = new Extractors.MethodExtractor(inaccessibleMethod, 1).extractFrom(new Object());
        assertEquals(BROKEN, actual.getStatus());
        assertThat(actual.getValueAsString(), containsString("IllegalArgumentException"));
        assertNull(actual.getValue());
    }

    @Test
    public void wrongParameterCount() {
        ExtractingMatcher.Extractor.ExtractedValue actual = new Extractors.MethodExtractor(inaccessibleMethod).extractFrom(item);
        assertEquals(BROKEN, actual.getStatus());
        assertThat(actual.getValueAsString(), containsString("IllegalArgumentException"));
        assertNull(actual.getValue());
    }

    @Test
    public void wrongParameterType() {
        ExtractingMatcher.Extractor.ExtractedValue actual = new Extractors.MethodExtractor(inaccessibleMethod, "1").extractFrom(item);
        assertEquals(BROKEN, actual.getStatus());
        assertThat(actual.getValueAsString(), containsString("IllegalArgumentException"));
        assertNull(actual.getValue());
    }

    @Test
    public void throwingMethod() {
        ExtractingMatcher.Extractor.ExtractedValue actual = new Extractors.MethodExtractor(throwingMethod).extractFrom(item);
        assertEquals(BROKEN, actual.getStatus());
        assertThat(actual.getValueAsString(), containsString("RuntimeException"));
        assertNull(actual.getValue());
    }*/


    private static class MyClass {
        private int returnArg(int arg) {
            return arg;
        }

        private static int returnArgStatic(int arg) {
            return arg;
        }

        private void throwingMethod() {
            throw new RuntimeException();
        }
    }
}
