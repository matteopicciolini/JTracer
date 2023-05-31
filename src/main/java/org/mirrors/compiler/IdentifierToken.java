package org.mirrors.compiler;

public class IdentifierToken extends Token {
    private String identifier;

    public IdentifierToken(SourceLocation location, String s) {
        super(location);
        this.identifier = s;
    }

    @Override
    public String toString() {
        return identifier;
    }
}
