package org.mirrors.compiler;

public class SymbolToken extends Token {
    public char symbol;

    public SymbolToken(SourceLocation location, char symbol) {
        super(location);
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return String.valueOf(this.symbol);
    }
}
