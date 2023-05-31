package org.mirrors.compiler;

public class KeywordToken {
    public String keyword;
    public KeywordToken(SourceLocation location, KeywordEnum keyword){
        this.keyword= String.valueOf(keyword);
    }
}
