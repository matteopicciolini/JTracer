package org.mirrors.compiler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;

public class InStream {
    static public String SYMBOLS = "()<>[],*";
    static public String WHITESPACE = " \t\n\r";

    public InputStream stream;
    public SourceLocation location;
    public SourceLocation savedLocation;
    public char savedChar;
    public int tabulations;
    public Token savedToken = null;
    public Token isThereToken;

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

    private StringToken ParseStringToken(SourceLocation tokenLocation) throws IOException, GrammarError {

        String token = "";
        while (true) {
            char ch = readChar();
            if (ch == '"') {
                break;
            }
            if (ch == '\0') {
                throw new GrammarError(tokenLocation, "unterminated string");
            }


            token += (ch);
        }

        return new StringToken(tokenLocation, token.toString());
    }


    public LiteralNumberToken ParseFloatToken(String firstChar, SourceLocation tokenLocation) throws IOException, GrammarError {
        String token = firstChar;

        while (true) {
            char ch = readChar();

            if (!(Character.isDigit(ch) || ch == '.' || ch == 'e' || ch == 'E')) {
                this.unreadChar(ch);
                break;
            }


            token += (ch);
        }

        try {
            float value = (float) Double.parseDouble(token);
            return new LiteralNumberToken(tokenLocation, value);

        } catch (NumberFormatException e) {
            throw new GrammarError(tokenLocation, "That's an invalid floating-point number");
        }
    }


    public Token ParseKeywordOrIdentifierToken(String firstChar, SourceLocation tokenLocation) throws IOException {

        String token = "";

        while (true) {
            char ch = readChar();

            if (!(Character.isLetterOrDigit(ch) || ch == '_')) {
                unreadChar(ch);
                break;
            }

            token += ch;
        }

        switch (token){
            case "new":
                return new KeywordToken(tokenLocation, KeywordEnum.NEW);
            case "material":
                return new KeywordToken(tokenLocation, KeywordEnum.MATERIAL);
            case "shape":
                return new KeywordToken(tokenLocation, KeywordEnum.SHAPE);
            case "plane":
                return new KeywordToken(tokenLocation, KeywordEnum.PLANE);
            case "sphere":
                return new KeywordToken(tokenLocation, KeywordEnum.SPHERE);
            case "diffuse":
                return new KeywordToken(tokenLocation, KeywordEnum.DIFFUSE);
            case "specular":
                return new KeywordToken(tokenLocation, KeywordEnum.SPECULAR);
            case "uniform":
                return new KeywordToken(tokenLocation, KeywordEnum.UNIFORM);
            case "checkered":
                return new KeywordToken(tokenLocation, KeywordEnum.CHECKERED);
            case "image":
                return new KeywordToken(tokenLocation, KeywordEnum.IMAGE);
            case "identity":
                return new KeywordToken(tokenLocation, KeywordEnum.IDENTITY);
            case "translation":
                return new KeywordToken(tokenLocation, KeywordEnum.TRANSLATION);
            case "rotationX":
                return new KeywordToken(tokenLocation, KeywordEnum.ROTATION_X);
            case "rotationY":
                return new KeywordToken(tokenLocation, KeywordEnum.ROTATION_Y);
            case "rotationZ":
                return new KeywordToken(tokenLocation, KeywordEnum.ROTATION_Z);
            case "scaling":
                return new KeywordToken(tokenLocation, KeywordEnum.SCALING);
            case "camera":
                return new KeywordToken(tokenLocation, KeywordEnum.CAMERA);
            case "orthogonal":
                return new KeywordToken(tokenLocation, KeywordEnum.ORTHOGONAL);
            case "perspective":
                return new KeywordToken(tokenLocation, KeywordEnum.PERSPECTIVE);
            case "float":
                return new KeywordToken(tokenLocation, KeywordEnum.FLOAT);
            case "box":
                return new KeywordToken(tokenLocation, KeywordEnum.BOX);
            case "cylinder":
                return new KeywordToken(tokenLocation, KeywordEnum.CYLINDER);
            case "hyperboloid":
                return new KeywordToken(tokenLocation, KeywordEnum.HYPERBOLOID);
            case "union":
                return new KeywordToken(tokenLocation, KeywordEnum.CSGUNION);
            case "difference":
                return new KeywordToken(tokenLocation, KeywordEnum.CSGDIFFERENCE);
            case "intersection":
                return new KeywordToken(tokenLocation, KeywordEnum.CSGINTERSECTION);
            default:
                return new IdentifierToken(tokenLocation, token);
        }
    }

    public Token readToken() throws IOException, GrammarError {
        if (savedToken != null) {
            Token result = savedToken;
            savedToken = null;
            return result;
        }

        skipWhitespacesAndComments();

        // At this point we're sure that ch does *not* contain a whitespace character
        String ch = String.valueOf(readChar());
        if (ch.equals("")) {
            // No more characters in the file, so return a StopToken
            return new StopToken(location);
        }

        // At this point we must check what kind of token begins with the "ch" character (which has been
        // put back in the stream with unreadChar). First, we save the position in the stream
        SourceLocation tokenLocation = this.location.copy();

        if (SYMBOLS.contains(ch)) {
            // One-character symbol, like '(' or ','
            return new SymbolToken(tokenLocation, ch);
        } else if (ch.equals("\"")) {
            // A literal string (used for file names)
            return ParseStringToken(tokenLocation);
        } else if (Character.isDigit(ch.charAt(0)) || ch.equals("+") || ch.equals("-") || ch.equals(".")) {
            // A floating-point number
            return ParseFloatToken(ch, tokenLocation);
        } else if (Character.isLetter(ch.charAt(0)) || ch.equals("_")) {
            // Since it begins with an alphabetic character, it must either be a keyword or an identifier
            return ParseKeywordOrIdentifierToken(ch, tokenLocation);
        } else {
            // We got some weird character, like '@` or `&`
            throw new GrammarError(this.location, "Invalid character: " + ch);
        }
    }



    public void unreadToken(Token token){
        assert (this.savedToken == null);
        this.savedToken = token;
    }

}

