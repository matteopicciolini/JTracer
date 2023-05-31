package org.mirrors.compiler;

    public class LiteralNumberToken extends Token {
        private float value;

        public LiteralNumberToken(SourceLocation location, float value) {
            super(location);
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }


