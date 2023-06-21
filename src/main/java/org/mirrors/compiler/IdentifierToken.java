package org.mirrors.compiler;

public class IdentifierToken extends Token {
    public String identifier;

    public IdentifierToken(SourceLocation location, String identifier) {
        super(location);
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return identifier;
    }
}
