package com.github.alkedr.matchers.reporting.extraction;

import com.github.alkedr.matchers.reporting.keys.MethodByNameKey;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.Method;

import static com.github.alkedr.matchers.reporting.ReportingMatcher.Value.broken;
import static com.github.alkedr.matchers.reporting.ReportingMatcher.Value.missing;

public class MethodByNameExtractor extends MethodByNameKey implements ExtractingMatcher.Extractor {
    public MethodByNameExtractor(String methodName, Object... arguments) {
        super(methodName, arguments);
    }

    @Override
    public ExtractingMatcher.KeyValue extractFrom(Object item) {
        if (item == null) {
            return new ExtractingMatcher.KeyValue(this, missing());
        }
        // TODO: брать метод, который выше всех в иерархии классов чтобы правильно объединять?
        Method method = MethodUtils.getMatchingAccessibleMethod(
                item.getClass(),
                getMethodName(),
                ClassUtils.toClass(getArguments())
        );   // TODO: исключения, null
        // TODO: broken если нет такого поля?
        return method == null
                ? new ExtractingMatcher.KeyValue(this, broken(new NoSuchMethodException(item.getClass().getName() + "." + toString())))
                : new MethodExtractor(method, getArguments()).extractFrom(item);
    }

    @Override
    public ExtractingMatcher.KeyValue extractFromMissingItem() {
        return new ExtractingMatcher.KeyValue(this, missing());
    }
}
