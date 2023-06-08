package org.mirrors.compiler;


public class GrammarError extends Throwable {
    public SourceLocation location;
    public String message;
    public GrammarError(SourceLocation source, String message){
        this.location = source;
        this.message = message;
    }
}
