package org.mirrors.compiler;

public class KeywordToken extends Token{
    public KeywordEnum keyword;
    public KeywordToken(SourceLocation location, KeywordEnum keyword){
        super(location);
        this.keyword = keyword;
    }
}
