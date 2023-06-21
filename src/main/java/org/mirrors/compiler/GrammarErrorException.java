package org.mirrors.compiler;


public class GrammarErrorException extends Throwable {
    public SourceLocation location;
    public String message;

    public GrammarErrorException(SourceLocation location, String message){
        super(location.fileName + ":" + location.lineNum + ":" + location.colNum + ": " + message);
        this.message = message;
        this.location = location;
    }
}
