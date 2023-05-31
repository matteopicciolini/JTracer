package org.mirrors.compiler;

public class StringToken extends Token {
    private String string;

    public StringToken(SourceLocation location, String s) {
        super(location);
        this.string = s;
    }

    @Override
    public String toString() {
        return string;
    }
}
