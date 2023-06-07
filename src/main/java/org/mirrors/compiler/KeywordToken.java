package org.mirrors.compiler;

public class KeywordToken extends Token{
    public KeywordEnum keyword;

    public KeywordToken(SourceLocation location, KeywordEnum keywordEnum){
        super(location);
        this.keyword = keywordEnum;
    }
}
