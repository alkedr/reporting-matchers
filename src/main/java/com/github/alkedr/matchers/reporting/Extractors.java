package com.github.alkedr.matchers.reporting;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.github.alkedr.matchers.reporting.ExtractedValueNameUtils.createMethodValueName;
import static com.github.alkedr.matchers.reporting.ReportingMatcher.Value.broken;
import static com.github.alkedr.matchers.reporting.ReportingMatcher.Value.missing;
import static com.github.alkedr.matchers.reporting.ReportingMatcher.Value.present;
import static java.lang.reflect.Modifier.isStatic;

public class Extractors {
    public static class FieldExtractor implements ExtractingMatcher.Extractor, ReportingMatcher.Key {
        private final Field field;

        public FieldExtractor(Field field) {
            Validate.notNull(field, "field");
            this.field = field;
        }

        @Override
        public KeyValue extractFrom(Object item) {
            if (item == null) {
                return new KeyValue(this, missing());
            }
            try {
                return new KeyValue(this, present(FieldUtils.readField(field, item, true)));
            } catch (IllegalArgumentException | IllegalAccessException e) {
                return new KeyValue(this, broken(e));  // TODO: rethrow?
            }
        }

        @Override
        public KeyValue extractFromMissingItem() {
            return new KeyValue(this, missing());
        }


        @Override
        public int hashCode() {
            return field.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            // FIXME: warning правильный
            return field.equals(obj);
        }

        @Override
        public String asString() {
            return field.getName();
        }
    }


    public static class FieldByNameExtractor implements ExtractingMatcher.Extractor, ReportingMatcher.Key {
        private final String fieldName;

        public FieldByNameExtractor(String fieldName) {
            Validate.notNull(fieldName, "fieldName");
            this.fieldName = fieldName;
        }

        @Override
        public KeyValue extractFrom(Object item) {
            if (item == null) {
                return new KeyValue(this, missing());
            }
            Field field = FieldUtils.getField(item.getClass(), fieldName, true);  // TODO: проверить исключения, null
            return new FieldExtractor(field).extractFrom(item);
        }

        @Override
        public KeyValue extractFromMissingItem() {
            return new KeyValue(this, missing());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FieldByNameExtractor that = (FieldByNameExtractor) o;
            return Objects.equals(fieldName, that.fieldName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(fieldName);
        }

        @Override
        public String asString() {
            return fieldName;
        }
    }


    public static class MethodExtractor implements ExtractingMatcher.Extractor, ReportingMatcher.Key {
        private final Method method;
        private final Object[] arguments;

        public MethodExtractor(Method method, Object... arguments) {
            Validate.notNull(method, "method");
            Validate.notNull(arguments, "arguments");
            this.method = method;
            this.arguments = arguments;
        }

        @Override
        public KeyValue extractFrom(Object item) {
            if (item == null && !isStatic(method.getModifiers())) {
                return new KeyValue(this, missing());
            }
            try {
                method.setAccessible(true);
                return new KeyValue(this, present(method.invoke(item, arguments)));
            } catch (IllegalArgumentException | IllegalAccessException e) {
                return new KeyValue(this, broken(e));  // TODO: rethrow?
            } catch (InvocationTargetException e) {
                return new KeyValue(this, broken(e.getCause()));
            }
        }

        @Override
        public KeyValue extractFromMissingItem() {
            return new KeyValue(this, missing());
        }

        @Override
        public String asString() {
            return createMethodValueName(method.getName(), arguments);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MethodExtractor that = (MethodExtractor) o;
            return Objects.equals(method, that.method) &&
                    Arrays.equals(arguments, that.arguments);
        }

        @Override
        public int hashCode() {
            return Objects.hash(method, arguments);
        }
    }


    public static class MethodByNameExtractor implements ExtractingMatcher.Extractor, ReportingMatcher.Key {
        private final String methodName;
        private final Object[] arguments;

        public MethodByNameExtractor(String methodName, Object... arguments) {
            Validate.notNull(methodName, "methodName");
            Validate.notNull(arguments, "arguments");
            this.methodName = methodName;
            this.arguments = arguments;
        }

        @Override
        public KeyValue extractFrom(Object item) {
            if (item == null) {
                return new KeyValue(this, missing());
            }
            Method method = MethodUtils.getMatchingAccessibleMethod(item.getClass(), methodName, ClassUtils.toClass(arguments));   // TODO: исключения, null
            return new MethodExtractor(method, arguments).extractFrom(item);
        }

        @Override
        public KeyValue extractFromMissingItem() {
            return new KeyValue(this, missing());
        }

        @Override
        public String asString() {
            return createMethodValueName(methodName, arguments);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MethodByNameExtractor that = (MethodByNameExtractor) o;
            return Objects.equals(methodName, that.methodName) &&
                    Arrays.equals(arguments, that.arguments);
        }

        @Override
        public int hashCode() {
            return Objects.hash(methodName, arguments);
        }
    }


    public static class ArrayElementExtractor implements ExtractingMatcher.Extractor, ReportingMatcher.Key {
        private final int index;

        public ArrayElementExtractor(int index) {
            Validate.isTrue(index >= 0, "index must be positive");
            this.index = index;
        }

        @Override
        public KeyValue extractFrom(Object item) {
            try {
                Object[] array = (Object[]) item;
                if (item == null || index < 0 || index >= array.length) {
                    return new KeyValue(this, missing());
                }
                return new KeyValue(this, present(array[index]));
            } catch (ClassCastException e) {
                return new KeyValue(this, broken(e));
            }
        }

        @Override
        public KeyValue extractFromMissingItem() {
            return new KeyValue(this, missing());
        }

        @Override
        public String asString() {
            return "[" + index + "]";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ArrayElementExtractor that = (ArrayElementExtractor) o;
            return index == that.index;
        }

        @Override
        public int hashCode() {
            return Objects.hash(index);
        }
    }


    // TODO: сделать универсальным (array, iterable, random access list)
    public static class ListElementExtractor implements ExtractingMatcher.Extractor, ReportingMatcher.Key {
        private final int index;

        public ListElementExtractor(int index) {
            Validate.isTrue(index >= 0, "index must be positive");
            this.index = index;
        }

        @Override
        public KeyValue extractFrom(Object item) {
            try {
                List<?> list = (List<?>) item;
                if (item == null || index < 0 || index >= list.size()) {
                    return new KeyValue(this, missing());
                }
                return new KeyValue(this, present(list.get(index)));
            } catch (ClassCastException e) {
                return new KeyValue(this, broken(e));
            }
        }

        @Override
        public KeyValue extractFromMissingItem() {
            return new KeyValue(this, missing());
        }

        @Override
        public String asString() {
            return "[" + index + "]";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ListElementExtractor that = (ListElementExtractor) o;
            return index == that.index;
        }

        @Override
        public int hashCode() {
            return Objects.hash(index);
        }
    }


    public static class ValueForKeyExtractor implements ExtractingMatcher.Extractor, ReportingMatcher.Key {
        private final Object key;

        public ValueForKeyExtractor(Object key) {
            this.key = key;
        }

        @Override
        public KeyValue extractFrom(Object item) {
            try {
                if (item == null || !((Map<?, ?>) item).containsKey(key)) {
                    return new KeyValue(this, missing());
                }
                return new KeyValue(this, present(((Map<?, ?>) item).get(key)));
            } catch (ClassCastException e) {
                return new KeyValue(this, broken(e));
            }
        }

        @Override
        public KeyValue extractFromMissingItem() {
            return new KeyValue(this, missing());
        }

        @Override
        public String asString() {
            return String.valueOf(key);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ValueForKeyExtractor that = (ValueForKeyExtractor) o;
            return Objects.equals(key, that.key);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key);
        }
    }
}
