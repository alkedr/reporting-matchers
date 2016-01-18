package com.github.alkedr.matchers.reporting.extraction;

import com.github.alkedr.matchers.reporting.keys.MethodByNameKey;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.Method;

import static com.github.alkedr.matchers.reporting.ReportingMatcher.Value.missing;

public class MethodByNameExtractor extends MethodByNameKey implements ExtractingMatcher.Extractor {
    public MethodByNameExtractor(MethodKind methodKind, String methodName, Object... arguments) {
        super(methodKind, methodName, arguments);
    }

    @Override
    public ExtractingMatcher.KeyValue extractFrom(Object item) {
        if (item == null) {
            return new ExtractingMatcher.KeyValue(this, missing());
        }
        // TODO: исключения, null
        // TODO: брать метод, который выше всех в иерархии классов чтобы правильно объединять?
        Method method = MethodUtils.getMatchingAccessibleMethod(
                item.getClass(),
                getMethodName(),
                ClassUtils.toClass(getArguments())
        );   // TODO: исключения, null
        return new MethodExtractor(getMethodKind(), method, getArguments()).extractFrom(item);
    }

    @Override
    public ExtractingMatcher.KeyValue extractFromMissingItem() {
        return new ExtractingMatcher.KeyValue(this, missing());
    }
}
