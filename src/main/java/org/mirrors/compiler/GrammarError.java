package org.mirrors.compiler;


public class GrammarError extends Throwable {
    public SourceLocation location;
    public String message;
    public GrammarError(SourceLocation source, String Message){
        this.location=source;
        this.message=Message;


    }
}
