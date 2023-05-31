package org.mirrors.compiler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;

import static org.mirrors.compiler.KeywordEnum.Keywords.KEYWORDS;

public class InStream {
    public InputStream stream;
    public SourceLocation location;
    public SourceLocation savedLocation;
    public char savedChar;
    public int tabulations;
    public Token savedToken;

    public InStream(InputStream stream, String fileName, int tabulation) {
        this.stream = stream;
        this.location = new SourceLocation(fileName, 1, 1);
    }

    private void updatePos(char ch) {
        if (Objects.isNull(ch)) {
            return;
        } else if (ch == '\n') {
            this.location.lineNum += 1;
            this.location.colNum = 1;
        } else if (ch == '\t') {
            this.location.colNum += this.tabulations;
        } else {
            this.location.colNum += 1;
        }
    }

    public char readChar() throws IOException {
        char ch;
        if (Objects.isNull(this.savedChar)) {
            ch = savedChar;
            this.savedChar = '\0';
        } else {
            int r = this.stream.read();
            ch = (r != -1) ? (char) r : null;
        }

        this.savedLocation = location.copy();
        this.updatePos(ch);
        return ch;
    }

    public void unreadChar(char ch) {
        assert (this.savedChar == '\0');
        this.savedChar = ch;
        this.location = savedLocation.copy();
    }

    public void skipWhitespacesAndComments() throws IOException {
        char ch = this.readChar();
        while (" \t\n\r".contains(String.valueOf(ch)) || ch == '#') {
            if (ch == '#') {
                while (!Arrays.asList('\r', '\n', '\0').contains(this.readChar())) {
                }
            }
            ch = this.readChar();
            if (ch == '\0') {
                return;
            }
        }
        this.unreadChar(ch);
    }
    private StringToken ParseStringToken(SourceLocation token_location) throws Throwable {
        StringBuilder token = new StringBuilder();
        while (true) {
            char ch = readChar();

            if (ch == '"') {
                break;
            }

            if (ch == '\0') {
                throw new GrammarError(token_location, "unterminated string");
            }

            token.append(ch);
        }

        return new StringToken(token_location, token.toString());
    }

    public LiteralNumberToken ParseFloatToken(String firstChar, SourceLocation tokenLocation) throws IOException, GrammarError {
        StringBuilder token = new StringBuilder(firstChar);
        while (true) {
            char ch = readChar();

            if (!(Character.isDigit(ch) || ch == '.' || ch == 'e' || ch == 'E')) {
                unreadChar(ch);
                break;
            }

            token.append(ch);
        }

        try {
            double value = Double.parseDouble(token.toString());
            return new LiteralNumberToken(tokenLocation, (float) value);
        } catch (NumberFormatException e) {
            throw new GrammarError(tokenLocation, "That's an invalid floating-point number");
        }
    }

    public Token ParseKeywordOrIdentifierToken(String firstChar, SourceLocation tokenLocation) throws IOException {
        StringBuilder token = new StringBuilder(firstChar);
        while (true) {
            char ch = readChar();

            if (!(Character.isLetterOrDigit(ch) || ch == '_')) {
                unreadChar(ch);
                break;
            }

            token.append(ch);
        }

        try {

            return new KeywordToken(tokenLocation, KEYWORDS.get(token.toString()));
        } catch (NullPointerException e) {

            return new IdentifierToken(tokenLocation, token.toString());
        }
    }
}
