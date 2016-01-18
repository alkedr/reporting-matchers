package com.github.alkedr.matchers.reporting.extraction;

import com.github.alkedr.matchers.reporting.keys.FieldByNameKey;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;

import static com.github.alkedr.matchers.reporting.ReportingMatcher.Value.missing;

public class FieldByNameExtractor extends FieldByNameKey implements ExtractingMatcher.Extractor {
    public FieldByNameExtractor(String fieldName) {
        super(fieldName);
    }

    @Override
    public ExtractingMatcher.KeyValue extractFrom(Object item) {
        if (item == null) {
            return new ExtractingMatcher.KeyValue(this, missing());
        }
        Field field = FieldUtils.getField(item.getClass(), getFieldName(), true);  // TODO: проверить исключения, null
        return new FieldExtractor(field).extractFrom(item);
    }

    @Override
    public ExtractingMatcher.KeyValue extractFromMissingItem() {
        return new ExtractingMatcher.KeyValue(this, missing());
    }
}
