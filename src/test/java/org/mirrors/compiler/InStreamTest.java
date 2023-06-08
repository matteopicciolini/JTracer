package org.mirrors.compiler;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class InStreamTest {
    public static void assertIsKeyword(Token token, KeywordEnum keyword) {
        assertTrue(token instanceof KeywordToken);
        KeywordToken keywordToken = (KeywordToken) token;
        assertEquals(keyword, keywordToken.keyword);
    }

    public static void assertIsIdentifier(Token token, String identifier) {
        assertTrue(token instanceof IdentifierToken);
        IdentifierToken identifierToken = (IdentifierToken) token;
        assertEquals(identifier, identifierToken.identifier);
    }

    public static void assertIsSymbol(Token token, String symbol) {
        assertTrue(token instanceof SymbolToken);
        SymbolToken symbolToken = (SymbolToken) token;
        assertEquals(symbol, symbolToken.toString());
    }

    public static void assertIsString(Token token, String s) {
        assertTrue(token instanceof StringToken);
        StringToken stringToken = (StringToken) token;
        assertEquals(s, stringToken.string);
    }

    @Test
    public void inputFileTest() throws IOException {
        String input = "abc   \nd\nef";
        InStream inStream = new InStream(new ByteArrayInputStream(input.getBytes()));

        assertTrue(inStream.location.lineNum == 1);
        assertTrue(inStream.location.colNum == 1);

        assertTrue(inStream.readChar() == 'a');
        assertTrue(inStream.location.lineNum == 1);
        assertTrue(inStream.location.colNum == 2);

        inStream.unreadChar('A');
        assertTrue(inStream.location.lineNum == 1);
        assertTrue(inStream.location.colNum == 1);

        assertTrue(inStream.readChar() == 'A');
        assertTrue(inStream.location.lineNum == 1);
        assertTrue(inStream.location.colNum == 2);

        assertTrue(inStream.readChar() == 'b');
        assertTrue(inStream.location.lineNum == 1);
        assertTrue(inStream.location.colNum == 3);

        assertTrue(inStream.readChar() == 'c');
        assertTrue(inStream.location.lineNum == 1);
        assertTrue(inStream.location.colNum == 4);

        inStream.skipWhitespacesAndComments();

        assertTrue(inStream.readChar() == 'd');
        assertTrue(inStream.location.lineNum == 2);
        assertTrue(inStream.location.colNum == 2);

        assertTrue(inStream.readChar() == '\n');
        assertTrue(inStream.location.lineNum == 3);
        assertTrue(inStream.location.colNum == 1);

        assertTrue(inStream.readChar() == 'e');
        assertTrue(inStream.location.lineNum == 3);
        assertTrue(inStream.location.colNum == 2);

        assertTrue(inStream.readChar() == 'f');
        assertTrue(inStream.location.lineNum == 3);
        assertTrue(inStream.location.colNum == 3);
    }

    @Test
    public void testLexer() throws IOException, GrammarError {
        String input = """
        # This is a comment
        # This is another comment
        new material sky_material(
            diffuse(image("my file.pfm")),
            <5.0, 500.0, 300.0>
        ) # Comment at the end of the line
""";
        InStream inStream = new InStream(new ByteArrayInputStream(input.getBytes()));

        assertIsKeyword(inStream.readToken(), KeywordEnum.NEW);
        assertIsKeyword(inStream.readToken(), KeywordEnum.MATERIAL);
        assertIsIdentifier(inStream.readToken(), "sky_material");
        assertIsSymbol(inStream.readToken(), "(");
        assertIsKeyword(inStream.readToken(), KeywordEnum.DIFFUSE);
        assertIsSymbol(inStream.readToken(), "(");
        assertIsKeyword(inStream.readToken(), KeywordEnum.IMAGE);
        assertIsSymbol(inStream.readToken(), "(");
        assertIsString(inStream.readToken(), "my file.pfm");
        assertIsSymbol(inStream.readToken(), ")");
    }
}