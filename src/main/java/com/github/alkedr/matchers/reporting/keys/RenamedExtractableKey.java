package com.github.alkedr.matchers.reporting.keys;

import org.apache.commons.lang3.Validate;

// не объединяется с непереименованным Key
// TODO: объединяться с непереименованным Key если названия совпадают?
class RenamedExtractableKey implements ExtractableKey {
    private final ExtractableKey originalKey;
    private final String name;

    RenamedExtractableKey(ExtractableKey originalKey, String name) {
        Validate.notNull(originalKey, "originalKey");
        Validate.notBlank(name, "name");
        this.originalKey = originalKey;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RenamedExtractableKey that = (RenamedExtractableKey) o;
        return originalKey.equals(that.originalKey) && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        int result = originalKey.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String asString() {
        return name;
    }

    @Override
    public ExtractionResult extractFrom(Object item) throws MissingException, BrokenException {
        return originalKey.extractFrom(item);
    }

    @Override
    public ExtractionResult extractFromMissingItem() throws MissingException, BrokenException {
        return originalKey.extractFromMissingItem();
    }
}
