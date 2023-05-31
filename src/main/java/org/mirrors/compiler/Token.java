package org.mirrors.compiler;

import org.mirrors.compiler.SourceLocation;

public abstract class Token {
    public SourceLocation location;
    public Token(SourceLocation location){
        this.location=location;
    }
}
