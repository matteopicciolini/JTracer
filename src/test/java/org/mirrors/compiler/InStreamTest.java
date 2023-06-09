package org.mirrors.compiler;

import org.junit.jupiter.api.Test;
import org.mirrors.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mirrors.Global.Black;

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
        new material skyMaterial(
            diffuse(image("my file.pfm")),
            <5.0, 500.0, 300.0>
        ) # Comment at the end of the line
""";
        InStream inStream = new InStream(new ByteArrayInputStream(input.getBytes()));

        assertIsKeyword(inStream.readToken(), KeywordEnum.NEW);
        assertIsKeyword(inStream.readToken(), KeywordEnum.MATERIAL);
        assertIsIdentifier(inStream.readToken(), "skyMaterial");
        assertIsSymbol(inStream.readToken(), "(");
        assertIsKeyword(inStream.readToken(), KeywordEnum.DIFFUSE);
        assertIsSymbol(inStream.readToken(), "(");
        assertIsKeyword(inStream.readToken(), KeywordEnum.IMAGE);
        assertIsSymbol(inStream.readToken(), "(");
        assertIsString(inStream.readToken(), "my file.pfm");
        assertIsSymbol(inStream.readToken(), ")");
    }

    @Test
    public void TestParser() throws InvalidMatrixException, GrammarError, IOException {
        String input = """
        float clock(150)
    
        material skyMaterial(
            diffuse(uniform(<0, 0, 0>)),
            uniform(<0.7, 0.5, 1>)
        )
    
        # Here is a comment
    
        material groundMaterial(
            diffuse(checkered(<0.3, 0.5, 0.1>,
                              <0.1, 0.2, 0.5>, 4)),
            uniform(<0, 0, 0>)
        )
    
        material sphereMaterial(
            specular(uniform(<0.5, 0.5, 0.5>)),
            uniform(<0, 0, 0>)
        )
    
        plane (skyMaterial, translation([0, 0, 100]) * rotationY(clock))
        plane (groundMaterial, identity)
    
        sphere(sphereMaterial, translation([0, 0, 1]))
    
        camera(perspective, rotationZ(30) * translation([-4, 0, 1]), 1.0, 1.0)
        """;
        InStream inStream = new InStream(new ByteArrayInputStream(input.getBytes()));
        Scene scene = inStream.parseScene();

        assertEquals(1, scene.floatVariables.size());
        assertTrue(scene.floatVariables.containsKey("clock"));
        assertEquals(150.0, scene.floatVariables.get("clock"), 1e-5f);


        assertEquals(3, scene.materials.size());
        assertTrue(scene.materials.containsKey("sphereMaterial"));
        assertTrue(scene.materials.containsKey("skyMaterial"));
        assertTrue(scene.materials.containsKey("groundMaterial"));

        Material sphereMaterial = scene.materials.get("sphereMaterial");
        Material skyMaterial = scene.materials.get("skyMaterial");
        Material groundMaterial = scene.materials.get("groundMaterial");

        assertTrue(skyMaterial.brdf instanceof DiffuseBRDF);
        assertTrue(skyMaterial.brdf.pigment instanceof UniformPigment);
        assertTrue(skyMaterial.brdf.pigment.getColor(new Vec2d()).isClose(Black));


        assertTrue(groundMaterial.brdf instanceof DiffuseBRDF);
        assertTrue(groundMaterial.brdf.pigment instanceof CheckeredPigment);
        CheckeredPigment checkeredPigment = (CheckeredPigment) groundMaterial.brdf.pigment;
        assertTrue(checkeredPigment.firstColor.isClose(new Color(0.3f, 0.5f, 0.1f)));
        assertTrue(checkeredPigment.secondColor.isClose(new Color(0.1f, 0.2f, 0.5f)));
        assertEquals(4, checkeredPigment.stepsNumber);

        assertTrue(sphereMaterial.brdf instanceof SpecularBRDF);
        assertTrue(sphereMaterial.brdf.pigment instanceof UniformPigment);
        assertTrue(sphereMaterial.brdf.pigment.getColor(new Vec2d()).isClose(new Color(0.5f, 0.5f, 0.5f)));

        assertTrue(skyMaterial.emittedRadiance instanceof UniformPigment);
        assertTrue(skyMaterial.emittedRadiance.getColor(new Vec2d()).isClose(new Color(0.7f, 0.5f, 1.f)));
        assertTrue(groundMaterial.emittedRadiance instanceof  UniformPigment);
        assertTrue(groundMaterial.emittedRadiance.getColor(new Vec2d()).isClose(Black));
        assertTrue(sphereMaterial.emittedRadiance instanceof UniformPigment);
        assertTrue(sphereMaterial.emittedRadiance.getColor(new Vec2d()).isClose(Black));

        assertEquals(scene.objects.size(), 3, 1e-3);
        assertTrue(scene.objects.get(0) instanceof Plain);
        assertTrue(scene.objects.get(1) instanceof Plain);
        assertTrue(scene.objects.get(2) instanceof Sphere);

        assertTrue(scene.camera instanceof PerspectiveCamera);
    }

    @Test
    public void testParserUndefinedMaterial() throws InvalidMatrixException, IOException {
        try {
            String input = """
    plane(this_material_does_not_exist, identity)
    """;
            InStream inStream = new InStream(new ByteArrayInputStream(input.getBytes()));
            Scene scene = inStream.parseScene();
            fail("The code did not throw an exception");
        } catch (GrammarError e) {
            // Exception was thrown as expected
        }
    }

    @Test
    public void testParserDoubleCamera() throws InvalidMatrixException, IOException {
        try {
            String input = """
    camera(perspective, rotation_z(30) * translation([-4, 0, 1]), 1.0, 1.0)
    camera(orthogonal, identity, 1.0, 1.0)
    """;
            InStream inStream = new InStream(new ByteArrayInputStream(input.getBytes()));
            Scene scene = inStream.parseScene();
            fail("The code did not throw an exception");
        } catch (GrammarError e) {
            // Exception was thrown as expected
        }
    }
}