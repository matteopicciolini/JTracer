package org.mirrors.compiler;
import org.mirrors.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.*;

import static java.util.Arrays.*;
import static org.mirrors.Global.*;

import org.javatuples.Pair;


/**

 This class represents an input stream for reading characters from an InputStream.

 It keeps track of the current position in the stream using a SourceLocation object.
 */
public class InStream {
    static public String SYMBOLS = "()<>[],*";
    static public String WHITESPACE = " \t\n\r";
    public InputStream stream;
    public SourceLocation location;
    public SourceLocation savedLocation;
    public char savedChar;
    public int tab = 4;
    public Token savedToken = null;

    private Map<String, Color> colorName = new HashMap<String, Color>() {{
        put("white", White);
        put("black", Black);
        put("navy", Navy);
        put("skyblue", SkyBlue);
        put("silver", Silver);
        put("crimson", Crimson);
        put("darkCyan", DarkCyan);
        put("olive", Olive);
        put("pink", Pink);
        put("darkRed", DarkRed);
        put("tomato", Tomato);
        put("gold", Gold);
        put("limeGreen", LimeGreen);
        put("green", Green);
        put("darkOrange", DarkOrange);
        put("purple", Purple);
        put("brown", Brown);
        put("red", Red);
        put("gray", Gray);
        put("darkGray", DarkGray);
        put("dimGray", DimGray);
        put("saddleBrown", SaddleBrown);
        put("darkBrown", DarkBrown);
        put("yellow", Yellow);
    }};


    /**

     Constructs an InStream object with a given InputStream and file name.
     The initial position is set to line 1, column 1 of the specified file.
     @param stream the InputStream to read characters from
     @param fileName the name of the file being read
     */
    public InStream(InputStream stream, String fileName) {
        this.stream = stream;
        this.location = new SourceLocation(fileName, 1, 1);
    }

    /**

     Constructs an InStream object with a given InputStream.
     The initial position is set to line 1, column 1.
     @param stream the InputStream to read characters from
     */
    public InStream(InputStream stream) {
        this.stream = stream;
        this.location = new SourceLocation("", 1, 1);
    }

    /**

     Updates the current position in the stream based on the given character.
     @param ch the character to update the position with
     */
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

    /**

     Reads the next character from the stream.

     @return the next character read

     @throws IOException if an I/O error occurs
     */
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

    /**

     Unreads the specified character, allowing it to be read again.
     @param ch the character to unread
     */
    public void unreadChar(char ch) {
        assert (this.savedChar == '\0');
        this.savedChar = ch;
        this.location = savedLocation.copy();
    }

    /**

     Skips whitespaces and comments in the input stream.
     Comments are identified by the '#' symbol.
     @throws IOException if an I/O error occurs
     */
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

    /**
     Parses a string token from the input stream starting at the given token location.
     @param tokenLocation the location of the string token
     @return the parsed StringToken
     @throws IOException if an I/O error occurs
     @throws GrammarErrorException if the string token is not properly terminated
     */
    private StringToken parseStringToken(SourceLocation tokenLocation) throws IOException, GrammarErrorException {
        String token = "";

        while (true) {
            char ch = readChar();
            if (ch == '"') {
                break;
            }
            if (ch == '\0') {
                throw new GrammarErrorException(tokenLocation, "unterminated string");
            }
            token += (ch);
        }
        return new StringToken(tokenLocation, token.toString());
    }

    /**

     Parses a floating-point number token from the input stream starting with the given first character and token location.
     @param firstChar the first character of the number token
     @param tokenLocation the location of the number token
     @return the parsed LiteralNumberToken
     @throws IOException if an I/O error occurs
     @throws GrammarErrorException if the floating-point number is invalid
     */
    public LiteralNumberToken parseFloatToken(char firstChar, SourceLocation tokenLocation) throws IOException, GrammarErrorException {
        String token = String.valueOf(firstChar);

        while (true) {
            char ch = readChar();
            if (!(Character.isDigit(ch) || ch == '.' || ch == 'e' || ch == 'E')) {
                this.unreadChar(ch);
                break;
            }
            token += ch;
        }

        try {
            float value = (float) Double.parseDouble(token);
            return new LiteralNumberToken(tokenLocation, value);

        } catch (NumberFormatException e) {
            throw new GrammarErrorException(tokenLocation, "That's an invalid floating-point number");
        }
    }

    /**
     Parses a keyword or identifier token from the input stream starting with the given first character and token location.
     @param firstChar the first character of the token
     @param tokenLocation the location of the token
     @return the parsed Token
     @throws IOException if an I/O error occurs
     */
    public Token parseKeywordOrIdentifierToken(char firstChar, SourceLocation tokenLocation) throws IOException {

        String token = String.valueOf(firstChar);

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
            case "triangle" -> new KeywordToken(tokenLocation, KeywordEnum.TRIANGLE);
            case "triangleMesh" -> new KeywordToken(tokenLocation, KeywordEnum.TRIANGLEMESH);
            case "tetrahedron" -> new KeywordToken(tokenLocation, KeywordEnum.TETRAHEDRON);
            case "octahedron" -> new KeywordToken(tokenLocation, KeywordEnum.OCTAHEDRON);
            case "dodecahedron" -> new KeywordToken(tokenLocation, KeywordEnum.DODECAHEDRON);
            case "icosahedron" -> new KeywordToken(tokenLocation, KeywordEnum.ICOSAHEDRON);
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
            case "fileshape" -> new KeywordToken(tokenLocation, KeywordEnum.FILESHAPE);
            case "cylinder" -> new KeywordToken(tokenLocation, KeywordEnum.CYLINDER);
            case "hyperboloid" -> new KeywordToken(tokenLocation, KeywordEnum.HYPERBOLOID);
            case "union" -> new KeywordToken(tokenLocation, KeywordEnum.CSGUNION);
            case "difference" -> new KeywordToken(tokenLocation, KeywordEnum.CSGDIFFERENCE);
            case "intersection" -> new KeywordToken(tokenLocation, KeywordEnum.CSGINTERSECTION);
            default -> new IdentifierToken(tokenLocation, token);
        };
    }

    /**
     Reads the next token from the input stream.
     @return the next Token read
     @throws IOException if an I/O error occurs
     @throws GrammarErrorException if a grammar error is encountered
     */
    public Token readToken() throws IOException, GrammarErrorException {
        if (savedToken != null) {
            Token result = this.savedToken;
            this.savedToken = null;
            return result;
        }

        this.skipWhitespacesAndComments();

        char ch = readChar();
        if (ch == '\0') {
            return new StopToken(this.location);
        }
        SourceLocation tokenLocation = this.location.copy();
        if (SYMBOLS.indexOf(ch) != -1) {
            return new SymbolToken(tokenLocation, ch);
        } else if (ch == '"') {
            return this.parseStringToken(tokenLocation);
        } else if (Character.isDigit(ch) || ch == '+' || ch == '-' || ch == '.') {
            return parseFloatToken(ch, tokenLocation);
        } else if (Character.isLetter(ch) || ch == '_') {
            return parseKeywordOrIdentifierToken(ch, tokenLocation);
        } else {
            throw new GrammarErrorException(this.location, "Invalid character: " + ch);
        }
    }

    /**
     Unreads the specified token, allowing it to be read again.
     @param token the token to unread
     */
    public void unreadToken(Token token){
        assert (this.savedToken == null);
        this.savedToken = token;
    }

    /**
     Expects the next token to be a specific symbol, otherwise throws a GrammarErrorException.
     @param symbol the expected symbol
     @throws GrammarErrorException if the next token is not the expected symbol
     @throws IOException if an I/O error occurs
     */
    public void expectSymbol(char symbol) throws GrammarErrorException, IOException {
        Token token = this.readToken();
        if (!(token instanceof SymbolToken) || ((SymbolToken) token).symbol != symbol) {
            throw new GrammarErrorException(token.location, "got '" + token + "' instead of '" + symbol + "'");
        }
    }

    /**
     Expects one of the specified keywords and returns the matched keyword.
     @param keywords the list of keywords to expect
     @return the matched keyword
     @throws GrammarErrorException if the next token is not a keyword or does not match any of the specified keywords
     @throws IOException if an I/O error occurs
     */
    public KeywordEnum expectKeywords(List<KeywordEnum> keywords) throws GrammarErrorException, IOException {
        Token token = this.readToken();
        if (!(token instanceof KeywordToken)) {
            throw new GrammarErrorException(token.location, "expected a keyword instead of '" + token + "'");
        }

        if (!keywords.contains(((KeywordToken) token).keyword)) {
            String expectedKeywords = String.join(",", keywords.stream().map(Enum::toString).toArray(String[]::new));
            throw new GrammarErrorException(token.location, "expected one of the keywords " + expectedKeywords + " instead of '" + token + "'");
        }
        return ((KeywordToken) token).keyword;
    }

    /**

     Expects a number token or an identifier token representing a float variable, and returns the corresponding float value.
     @param scene the scene object containing float variables
     @return the float value
     @throws GrammarErrorException if the next token is not a number or an identifier
     @throws IOException if an I/O error occurs
     */
    public float expectNumber(Scene scene) throws GrammarErrorException, IOException {
        Token token = this.readToken();
        if (token instanceof LiteralNumberToken) {
            return ((LiteralNumberToken) token).value;
        } else if (token instanceof IdentifierToken) {
            String variableName = ((IdentifierToken) token).identifier;
            if (!scene.floatVariables.containsKey(variableName)) {
                throw new GrammarErrorException(token.location, "unknown variable '" + token + "'");
            }
            return scene.floatVariables.get(variableName);
        }
        throw new GrammarErrorException(token.location, "got '" + token + "' instead of a number");
    }

    /**

     Expects a string token and returns the string value.
     @return the string value
     @throws GrammarErrorException if the next token is not a string
     @throws IOException if an I/O error occurs
     */
    public String expectString() throws GrammarErrorException, IOException {
        Token token = this.readToken();
        if (!(token instanceof StringToken)) {
            throw new GrammarErrorException(token.location, "got '" + token + "' instead of a string");
        }
        return token.toString();
    }

    /**

     Expects an identifier token and returns the identifier string.
     @return the identifier string
     @throws GrammarErrorException if the next token is not an identifier
     @throws IOException if an I/O error occurs
     */
    public String expectIdentifier() throws GrammarErrorException, IOException {
        Token token = this.readToken();
        if (!(token instanceof IdentifierToken)) {
            throw new GrammarErrorException(token.location, "got '" + token + "' instead of an identifier");
        }
        return ((IdentifierToken) token).identifier;
    }

    /**
     Parses a vector from the input stream.
     @param scene the scene object containing float variables
     @return the parsed Vec object
     @throws GrammarErrorException if a grammar error is encountered
     @throws IOException if an I/O error occurs
     */
    public Vec parseVector(Scene scene) throws GrammarErrorException, IOException {
        this.expectSymbol('[');
        float x = this.expectNumber(scene);
        this.expectSymbol(',');
        float y = this.expectNumber(scene);
        this.expectSymbol(',');
        float z = this.expectNumber(scene);
        this.expectSymbol(']');

        return new Vec(x, y, z);
    }

    /**
     Parses a point from the input stream.
     @param scene the scene object containing float variables
     @return the parsed Point object
     @throws GrammarErrorException if a grammar error is encountered
     @throws IOException if an I/O error occurs
     */
    public Point parsePoint(Scene scene) throws GrammarErrorException, IOException {
        this.expectSymbol('[');
        float x = this.expectNumber(scene);
        this.expectSymbol(',');
        float y = this.expectNumber(scene);
        this.expectSymbol(',');
        float z = this.expectNumber(scene);
        this.expectSymbol(']');

        return new Point(x, y, z);
    }


    /**
     Parses a color from the input stream.
     @param scene the scene object containing float variables
     @return the parsed Color object
     @throws GrammarErrorException if a grammar error is encountered
     @throws IOException if an I/O error occurs
     */
    public Color parseColor(Scene scene) throws GrammarErrorException, IOException {
        this.expectSymbol('<');
        Token token = readToken();
        Color result;
        if (token instanceof LiteralNumberToken){
            float red = ((LiteralNumberToken) token).value;
            this.expectSymbol(',');
            float green = this.expectNumber(scene);
            this.expectSymbol(',');
            float blue = this.expectNumber(scene);

            result = new Color(red, green, blue);
        }
        else if (token instanceof IdentifierToken){
            if (!colorName.containsKey(((IdentifierToken) token).identifier)){
                throw new GrammarErrorException(token.location, "'" + ((IdentifierToken) token).identifier +"' is not a valid color.");
            }
            else result = colorName.get(((IdentifierToken) token).identifier);
        }
        else{
            throw new GrammarErrorException(this.location, "expected a color");
        }

        this.expectSymbol('>');
        return result;
    }

    /**
     Parses a pigment from the input stream.
     @param scene the scene object containing float variables
     @return the parsed Pigment object
     @throws GrammarErrorException if a grammar error is encountered
     @throws IOException if an I/O error occurs
     */
    public Pigment parsePigment(Scene scene) throws GrammarErrorException, IOException {
        KeywordEnum keyword = this.expectKeywords(new ArrayList<>(Arrays.asList(KeywordEnum.UNIFORM, KeywordEnum.CHECKERED, KeywordEnum.IMAGE)));

        this.expectSymbol('(');
        Pigment result;

        if (keyword == KeywordEnum.UNIFORM) {
            Color color = this.parseColor(scene);
            result = new UniformPigment(color);
        } else if (keyword == KeywordEnum.CHECKERED) {
            Color color1 = this.parseColor(scene);
            this.expectSymbol(',');
            Color color2 = this.parseColor(scene);
            this.expectSymbol(',');
            int numOfSteps = (int) expectNumber(scene);
            result = new CheckeredPigment(color1, color2, numOfSteps);
        } else if (keyword == KeywordEnum.IMAGE) {
            String fileName = this.expectString();
            try (InputStream imageFile = new FileInputStream(fileName)) {
                HDRImage image = PfmCreator.readPfmImage(imageFile);
                result = new ImagePigment(image);
            } catch (IOException | InvalidPfmFileFormatException e) {
                throw new GrammarErrorException(this.location, "Error reading image file: " + fileName);
            }
        } else {
            throw new AssertionError("This line should be unreachable");
        }

        expectSymbol(')');
        return result;
    }

    /**
     Parses a BRDF (Bidirectional Reflectance Distribution Function) from the input stream.
     @param scene the scene object containing float variables
     @return the parsed BRDF object
     @throws GrammarErrorException if a grammar error is encountered
     @throws IOException if an I/O error occurs
     */
    public BRDF parseBRDF(Scene scene) throws GrammarErrorException, IOException {
        KeywordEnum BRDFKeyword = this.expectKeywords(new ArrayList<>(List.of(KeywordEnum.DIFFUSE, KeywordEnum.SPECULAR)));

        this.expectSymbol('(');
        Pigment pigment = this.parsePigment(scene);
        this.expectSymbol(')');

        if (BRDFKeyword == KeywordEnum.DIFFUSE) {
            return new DiffuseBRDF(pigment);
        } else if (BRDFKeyword == KeywordEnum.SPECULAR) {
            return new SpecularBRDF(pigment);
        } else {
            throw new AssertionError("This line should be unreachable");
        }
    }

    /**
     Parses a material from the input stream.
     @param scene the scene object containing float variables
     @return a Pair object containing the material name and the parsed Material object
     @throws GrammarErrorException if a grammar error is encountered
     @throws IOException if an I/O error occurs
     */
    public Pair<String, Material> parseMaterial(Scene scene) throws GrammarErrorException, IOException {
        String name = this.expectIdentifier();

        this.expectSymbol('(');
        BRDF brdf = this.parseBRDF(scene);
        this.expectSymbol(',');
        Pigment emittedRadiance = this.parsePigment(scene);
        this.expectSymbol(')');

        return new Pair<>(name, new Material(brdf, emittedRadiance));
    }

    /**
     Parses a transformation from the input stream.
     @param scene the scene object containing float variables
     @return the parsed Transformation object
     @throws GrammarErrorException if a grammar error is encountered
     @throws IOException if an I/O error occurs
     @throws InvalidMatrixException if the transformation matrix is invalid
     */
    public Transformation parseTransformation(Scene scene) throws GrammarErrorException, IOException, InvalidMatrixException {
        Transformation result = new Transformation();

        while (true) {
            KeywordEnum transformationKw = this.expectKeywords(Arrays.asList(
                    KeywordEnum.IDENTITY,
                    KeywordEnum.TRANSLATION,
                    KeywordEnum.ROTATION_X,
                    KeywordEnum.ROTATION_Y,
                    KeywordEnum.ROTATION_Z,
                    KeywordEnum.SCALING
            ));

            if (transformationKw == KeywordEnum.IDENTITY) {
                // Do nothing (this is a primitive form of optimization!)
            } else if (transformationKw == KeywordEnum.TRANSLATION) {
                this.expectSymbol('(');
                result = result.times(Transformation.translation(parseVector(scene)));
                this.expectSymbol(')');
            } else if (transformationKw == KeywordEnum.ROTATION_X) {
                this.expectSymbol('(');
                result = result.times(Transformation.rotationX(expectNumber(scene)));
                this.expectSymbol(')');
            } else if (transformationKw == KeywordEnum.ROTATION_Y) {
                this.expectSymbol('(');
                result = result.times(Transformation.rotationY(expectNumber(scene)));
                this.expectSymbol(')');
            } else if (transformationKw == KeywordEnum.ROTATION_Z) {
                this.expectSymbol('(');
                result = result.times(Transformation.rotationZ(expectNumber(scene)));
                this.expectSymbol(')');
            } else if (transformationKw == KeywordEnum.SCALING) {
                this.expectSymbol('(');
                result = result.times(Transformation.scaling(parseVector(scene)));
                this.expectSymbol(')');
            }

            // We must peek the next token to check if there is another transformation that is being
            // chained or if the sequence ends. Thus, this is a LL(1) parser.
            Token nextKw = this.readToken();
            if (!(nextKw instanceof SymbolToken) || ((SymbolToken) nextKw).symbol != '*') {
                // Pretend you never read this token and put it back!
                this.unreadToken(nextKw);
                break;
            }
        }

        return result;
    }

    /**
     Parses a sphere from the input stream.
     @param scene the scene object containing float variables
     @return the parsed Sphere object
     @throws GrammarErrorException if a grammar error is encountered
     @throws IOException if an I/O error occurs
     @throws InvalidMatrixException if the transformation matrix is invalid
     */
    public Sphere parseSphere(Scene scene) throws GrammarErrorException, IOException, InvalidMatrixException {
        this.expectSymbol('(');

        String materialName = this.expectIdentifier();
        if (!scene.materials.containsKey(materialName)) {
            throw new GrammarErrorException(this.location, "unknown material " + materialName);
        }

        this.expectSymbol(',');
        Transformation transformation = this.parseTransformation(scene);
        this.expectSymbol(')');

        return new Sphere(transformation, scene.materials.get(materialName));
    }
    public Triangle parseTriangle(Scene scene) throws GrammarErrorException, IOException, InvalidMatrixException {
        this.expectSymbol('(');

        Point v0 = this.parsePoint(scene);
        this.expectSymbol(',');
        Point v1 = this.parsePoint(scene);
        this.expectSymbol(',');
        Point v2 = this.parsePoint(scene);
        this.expectSymbol(',');
        String materialName = this.expectIdentifier();
        if (!scene.materials.containsKey(materialName)) {
            throw new GrammarErrorException(this.location, "unknown material " + materialName);
        }
        this.expectSymbol(')');

        return new Triangle(v0, v1, v2, scene.materials.get(materialName));
    }
    public TriangleMesh parseTriangleMesh(Scene scene) throws GrammarErrorException, IOException, InvalidMatrixException {
        this.expectSymbol('(');

        String materialName = this.expectIdentifier();
        if (!scene.materials.containsKey(materialName)) {
            throw new GrammarErrorException(this.location, "unknown material " + materialName);
        }
        this.expectSymbol(',');
        Transformation transformation = this.parseTransformation(scene);
        this.expectSymbol(')');

        return new TriangleMesh(scene.materials.get(materialName), transformation);
    }

    public TriangleMesh parseTetrahedron(Scene scene) throws GrammarErrorException, IOException, InvalidMatrixException {
        this.expectSymbol('(');

        String materialName = this.expectIdentifier();
        if (!scene.materials.containsKey(materialName)) {
            throw new GrammarErrorException(this.location, "unknown material " + materialName);
        }
        this.expectSymbol(',');
        Transformation transformation = this.parseTransformation(scene);
        this.expectSymbol(')');
        TriangleMesh tetra= new TriangleMesh(scene.materials.get(materialName));
        tetra.tetrahedron();

        return new TriangleMesh(tetra.vertices, scene.materials.get(materialName), transformation);
    }
    public TriangleMesh parseOctahedron(Scene scene) throws GrammarErrorException, IOException, InvalidMatrixException {
        this.expectSymbol('(');

        String materialName = this.expectIdentifier();
        if (!scene.materials.containsKey(materialName)) {
            throw new GrammarErrorException(this.location, "unknown material " + materialName);
        }
        this.expectSymbol(',');
        Transformation transformation = this.parseTransformation(scene);
        this.expectSymbol(')');
        TriangleMesh octa= new TriangleMesh(scene.materials.get(materialName));
        octa.octahedron();

        return new TriangleMesh(octa.vertices, scene.materials.get(materialName), transformation);
    }

    public TriangleMesh parseDodecahedron(Scene scene) throws GrammarErrorException, IOException, InvalidMatrixException {
        this.expectSymbol('(');

        String materialName = this.expectIdentifier();
        if (!scene.materials.containsKey(materialName)) {
            throw new GrammarErrorException(this.location, "unknown material " + materialName);
        }
        this.expectSymbol(',');
        Transformation transformation = this.parseTransformation(scene);
        this.expectSymbol(')');
        TriangleMesh dode= new TriangleMesh(scene.materials.get(materialName));
        dode.dodecahedron();

        return new TriangleMesh(dode.vertices, scene.materials.get(materialName), transformation);
    }

    public TriangleMesh parseFileShape(Scene scene) throws GrammarErrorException, IOException, InvalidMatrixException {
        this.expectSymbol('(');

        String materialName = this.expectIdentifier();
        if (!scene.materials.containsKey(materialName)) {
            throw new GrammarErrorException(this.location, "unknown material " + materialName);
        }
        this.expectSymbol(',');
        Transformation transformation = this.parseTransformation(scene);
        this.expectSymbol(',');
        String fileName= this.expectIdentifier();
        this.expectSymbol(')');

        TriangleMesh mesh= new TriangleMesh(scene.materials.get(materialName), transformation);
        mesh.createFileShape(fileName + ".txt");

        return new TriangleMesh(mesh.vertices, mesh.triangles, scene.materials.get(materialName), transformation);
    }
    public TriangleMesh parseIcosahedron(Scene scene) throws GrammarErrorException, IOException, InvalidMatrixException {

        this.expectSymbol('(');
        String materialName = this.expectIdentifier();
        if (!scene.materials.containsKey(materialName)) {
            throw new GrammarErrorException(this.location, "unknown material " + materialName);
        }
        this.expectSymbol(',');
        Transformation transformation = this.parseTransformation(scene);
        this.expectSymbol(')');
        TriangleMesh ico= new TriangleMesh(scene.materials.get(materialName));
        ico.icosahedron();

        return new TriangleMesh(ico.vertices, scene.materials.get(materialName), transformation);
    }

    /**
     Parses a box from the input stream.
     @param scene the scene object containing float variables
     @return the parsed Box object
     @throws GrammarErrorException if a grammar error is encountered
     @throws IOException if an I/O error occurs
     @throws InvalidMatrixException if the transformation matrix is invalid
     */

    public Box parseBox(Scene scene) throws GrammarErrorException, IOException, InvalidMatrixException {
        this.expectSymbol('(');
        Point min = this.parsePoint(scene);
        expectSymbol(',');
        Point max = this.parsePoint(scene);
        expectSymbol(',');
        String materialName = this.expectIdentifier();
        if (!scene.materials.containsKey(materialName)) {
            throw new GrammarErrorException(this.location, "unknown material " + materialName);
        }

        this.expectSymbol(',');
        Transformation transformation = this.parseTransformation(scene);
        this.expectSymbol(')');

        return new Box(min, max, transformation, scene.materials.get(materialName));
    }

    /**
     Parses a plane from the input stream.
     @param scene the scene object containing float variables
     @return the parsed Plane object
     @throws GrammarErrorException if a grammar error is encountered
     @throws IOException if an I/O error occurs
     @throws InvalidMatrixException if the transformation matrix is invalid
     */
    public Plain parsePlane(Scene scene) throws GrammarErrorException, IOException, InvalidMatrixException {
        this.expectSymbol('(');

        String materialName = this.expectIdentifier();
        if (!scene.materials.containsKey(materialName)) {
            throw new GrammarErrorException(this.location, "unknown material " + materialName);
        }

        this.expectSymbol(',');
        Transformation transformation = this.parseTransformation(scene);
        this.expectSymbol(')');

        return new Plain(transformation, scene.materials.get(materialName));
    }

    /**
     Parses a camera from the input stream.
     @param scene the scene object containing float variables
     @return the parsed Camera object
     @throws GrammarErrorException if a grammar error is encountered
     @throws IOException if an I/O error occurs
     @throws InvalidMatrixException if the transformation matrix is invalid
     */
    public Camera parseCamera(Scene scene) throws GrammarErrorException, IOException, InvalidMatrixException {
        this.expectSymbol('(');
        KeywordEnum typeKeyword = expectKeywords(Arrays.asList(KeywordEnum.PERSPECTIVE, KeywordEnum.ORTHOGONAL));
        this.expectSymbol(',');
        Transformation transformation = this.parseTransformation(scene);
        this.expectSymbol(',');
        float aspectRatio = expectNumber(scene);
        this.expectSymbol(',');
        float distance = expectNumber(scene);
        this.expectSymbol(')');

        Camera result;
        if (typeKeyword == KeywordEnum.PERSPECTIVE) {
            result = new PerspectiveCamera(distance, aspectRatio, transformation);
        } else if (typeKeyword == KeywordEnum.ORTHOGONAL) {
            result = new OrthogonalCamera(aspectRatio, transformation);
        } else {
            throw new GrammarErrorException(this.location, "Invalid camera type");
        }

        return result;
    }

    /**
     Parses the scene from the input stream.
     @return the parsed Scene object
     @throws GrammarErrorException if a grammar error is encountered
     @throws IOException if an I/O error occurs
     @throws InvalidMatrixException if the transformation matrix is invalid
     */
    public Scene parseScene() throws GrammarErrorException, IOException, InvalidMatrixException {
        Scene scene = new Scene();

        while (true) {
            Token what = this.readToken();
            if (what instanceof StopToken) {
                break;
            }

            if (!(what instanceof KeywordToken)) {
                throw new GrammarErrorException(what.location, "Expected a keyword instead of '" + what + "'");
            }

            KeywordEnum keyword = ((KeywordToken) what).keyword;

            if (keyword == KeywordEnum.FLOAT) {
                String variableName = this.expectIdentifier();
                this.expectSymbol('(');
                float variableValue = expectNumber(scene);
                this.expectSymbol(')');

                scene.floatVariables.put(variableName, variableValue);
            } else if (keyword == KeywordEnum.SPHERE) {
                scene.objects.add(this.parseSphere(scene));
            } else if (keyword == KeywordEnum.TRIANGLE) {
                scene.objects.add(this.parseTriangle(scene));
            } else if (keyword == KeywordEnum.TETRAHEDRON) {
                scene.objects.add(this.parseTetrahedron(scene));
            } else if (keyword == KeywordEnum.OCTAHEDRON) {
                scene.objects.add(this.parseOctahedron(scene));
            } else if (keyword == KeywordEnum.DODECAHEDRON) {
                scene.objects.add(this.parseDodecahedron(scene));
            } else if (keyword == KeywordEnum.ICOSAHEDRON) {
                scene.objects.add(this.parseIcosahedron(scene));
            } else if (keyword == KeywordEnum.FILESHAPE) {
                scene.objects.add(this.parseFileShape(scene));
            } else if (keyword == KeywordEnum.PLANE) {
                scene.objects.add(this.parsePlane(scene));
            } else if (keyword == KeywordEnum.BOX) {
                scene.objects.add(this.parseBox(scene));
            } else if (keyword == KeywordEnum.CAMERA) {
                if (scene.camera != null) {
                    throw new GrammarErrorException(what.location, "You cannot define more than one camera");
                }

                scene.camera = this.parseCamera(scene);
            } else if (keyword == KeywordEnum.MATERIAL) {
                Pair<String, Material> materialTuple = parseMaterial(scene);
                scene.materials.put(materialTuple.getValue0(), materialTuple.getValue1());
            }
        }
        return scene;
    }
}

