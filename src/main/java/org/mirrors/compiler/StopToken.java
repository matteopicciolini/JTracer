package org.mirrors.compiler;

public class StopToken extends Token {

    public StopToken(SourceLocation location) {
        super(location);
    }

    @Override
    public String toString() {
        return null;
    }
}
