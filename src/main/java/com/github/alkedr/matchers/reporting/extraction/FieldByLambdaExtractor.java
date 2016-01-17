package com.github.alkedr.matchers.reporting.extraction;

import com.github.alkedr.matchers.reporting.ReportingMatcher;

import java.util.function.Function;

// TODO: пробовать передавать в лямбду Object, получать класс из ClassCastException
public class FieldByLambdaExtractor<T> implements ExtractingMatcher.Extractor, ReportingMatcher.Key {
    private final Function<T, Object> function;

    public FieldByLambdaExtractor(Function<T, Object> function) {
        this.function = function;
    }

    @Override
    public ExtractingMatcher.KeyValue extractFrom(Object item) {
        if (item == null) {
            return new ExtractingMatcher.KeyValue(this, ReportingMatcher.Value.missing());
        }

        try {
            Class<?> clazz = Class.forName("com.github.alkedr.matchers.reporting.extraction.Extractors_ObjectFieldExtractorTest$X");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        function.apply((T) item);

//        Class<T> persistentClass = (Class<T>)
//                ((ParameterizedType)getClass().getGenericSuperclass())
//                        .getActualTypeArguments()[0];

        return null;
    }

    @Override
    public ExtractingMatcher.KeyValue extractFromMissingItem() {
        return new ExtractingMatcher.KeyValue(this, ReportingMatcher.Value.missing());
    }

    @Override
    public String asString() {
        return null;   // TODO: что делать здесь?
    }
}
