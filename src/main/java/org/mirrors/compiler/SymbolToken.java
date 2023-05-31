package org.mirrors.compiler;



public class SymbolToken extends Token {
    public String symbol;

    public SymbolToken(SourceLocation location, String symbol) {
        super(location);
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return this.symbol;
    }
}
