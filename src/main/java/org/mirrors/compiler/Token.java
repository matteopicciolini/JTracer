package org.mirrors.compiler;

public abstract class Token {
    public SourceLocation location;
    public Token(SourceLocation location){
        this.location = location;
    }

    public abstract String toString();

}
