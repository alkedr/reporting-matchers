package com.github.alkedr.matchers.reporting.keys;

public interface ExtractableKey extends Key {
    ExtractionResult extractFrom(Object item) throws MissingException, BrokenException;
    ExtractionResult extractFromMissingItem() throws MissingException, BrokenException;

    class ExtractionResult {
        private final Key key;
        private final Object value;

        public ExtractionResult(Key key, Object value) {
            this.key = key;
            this.value = value;
        }

        public Key getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }
    }

    class MissingException extends Exception {
        private final Key key;

        public MissingException(Key key) {
            this.key = key;
        }

        public Key getKey() {
            return key;
        }
    }

    class BrokenException extends Exception {
        private final Key key;

        public BrokenException(Key key, Throwable cause) {   // TODO: только description без cause?
            super(cause);
            this.key = key;
        }

        public Key getKey() {
            return key;
        }
    }
}
