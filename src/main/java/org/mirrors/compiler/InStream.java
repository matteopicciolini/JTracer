package org.mirrors.compiler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import static java.util.Arrays.*;

public class InStream {
    public InputStream stream;
    public SourceLocation location;
    public SourceLocation savedLocation;
    public char savedChar;
    public Token savedToken;
    public int tab = 4;

    public InStream(InputStream stream, String fileName) {
        this.stream = stream;
        this.location = new SourceLocation(fileName, 1, 1);
    }
    public InStream(InputStream stream) {
        this.stream = stream;
        this.location = new SourceLocation("", 1, 1);
    }

    private void updatePos(char ch) {
        if (ch == '\0') {
            return;
        } else if (ch == '\n') {
            this.location.lineNum += 1;
            this.location.colNum = 1;
        } else if (ch == '\t') {
            this.location.colNum += this.tab;
        } else {
            this.location.colNum += 1;
        }
    }

    public char readChar() throws IOException {
        char ch;
        if (this.savedChar != '\0') {
            ch = savedChar;
            this.savedChar = '\0';
        } else {
            int r = this.stream.read();
            ch = (r != -1) ? (char) r : '\0';
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
        while (" \t\n\r".contains(String.valueOf(ch)) || (ch == '#')) {
            if (ch == '#') {
                while (!asList('\r', '\n', '\0').contains(this.readChar())) {
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
            token += ch;
        }

        return new StringToken(tokenLocation, token);
    }

    public LiteralNumberToken ParseFloatToken(String firstChar, SourceLocation tokenLocation) throws IOException, GrammarError {
        String token = firstChar;
        while (true) {
            char ch = readChar();

            if (!(Character.isDigit(ch) || ch == '.' || ch == 'e' || ch == 'E')) {
                this.unreadChar(ch);
                break;
            }

            token += ch;
        }

        try {
            float value = Float.parseFloat(token);
            return new LiteralNumberToken(tokenLocation, value);
        } catch (NumberFormatException e) {
            throw new GrammarError(tokenLocation, "That's an invalid floating-point number");
        }
    }

    public Token ParseKeywordOrIdentifierToken(String firstChar, SourceLocation tokenLocation) throws IOException {
        String token = firstChar;
        while (true) {
            char ch = readChar();

            if (!(Character.isLetterOrDigit(ch) || ch == '_')) {
                unreadChar(ch);
                break;
            }

            token += ch;
        }

        return switch (token) {
            case "new" -> new KeywordToken(tokenLocation, KeywordEnum.NEW);
            case "material" -> new KeywordToken(tokenLocation, KeywordEnum.MATERIAL);
            case "shape" -> new KeywordToken(tokenLocation, KeywordEnum.SHAPE);
            case "plane" -> new KeywordToken(tokenLocation, KeywordEnum.PLANE);
            case "sphere" -> new KeywordToken(tokenLocation, KeywordEnum.SPHERE);
            case "diffuse" -> new KeywordToken(tokenLocation, KeywordEnum.DIFFUSE);
            case "specular" -> new KeywordToken(tokenLocation, KeywordEnum.SPECULAR);
            case "uniform" -> new KeywordToken(tokenLocation, KeywordEnum.UNIFORM);
            case "checkered" -> new KeywordToken(tokenLocation, KeywordEnum.CHECKERED);
            case "image" -> new KeywordToken(tokenLocation, KeywordEnum.IMAGE);
            case "identity" -> new KeywordToken(tokenLocation, KeywordEnum.IDENTITY);
            case "translation" -> new KeywordToken(tokenLocation, KeywordEnum.TRANSLATION);
            case "rotationX" -> new KeywordToken(tokenLocation, KeywordEnum.ROTATION_X);
            case "rotationY" -> new KeywordToken(tokenLocation, KeywordEnum.ROTATION_Y);
            case "rotationZ" -> new KeywordToken(tokenLocation, KeywordEnum.ROTATION_Z);
            case "scaling" -> new KeywordToken(tokenLocation, KeywordEnum.SCALING);
            case "camera" -> new KeywordToken(tokenLocation, KeywordEnum.CAMERA);
            case "orthogonal" -> new KeywordToken(tokenLocation, KeywordEnum.ORTHOGONAL);
            case "perspective" -> new KeywordToken(tokenLocation, KeywordEnum.PERSPECTIVE);
            case "float" -> new KeywordToken(tokenLocation, KeywordEnum.FLOAT);
            case "box" -> new KeywordToken(tokenLocation, KeywordEnum.BOX);
            case "cylinder" -> new KeywordToken(tokenLocation, KeywordEnum.CYLINDER);
            case "hyperboloid" -> new KeywordToken(tokenLocation, KeywordEnum.HYPERBOLOID);
            case "union" -> new KeywordToken(tokenLocation, KeywordEnum.CSGUNION);
            case "difference" -> new KeywordToken(tokenLocation, KeywordEnum.CSGDIFFERENCE);
            case "intersection" -> new KeywordToken(tokenLocation, KeywordEnum.CSGINTERSECTION);
            default -> new IdentifierToken(tokenLocation, token);
        };
    }

    public void unreadToken(Token token){
        assert (this.savedToken == null);
        this.savedToken = token;
    }
}
