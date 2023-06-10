package org.mirrors.compiler;
import org.mirrors.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.List;
import java.util.ArrayList;
import static java.util.Arrays.*;
import java.util.Arrays;
import org.javatuples.Pair;

public class InStream {
    static public String SYMBOLS = "()<>[],*";
    static public String WHITESPACE = " \t\n\r";
    public InputStream stream;
    public SourceLocation location;
    public SourceLocation savedLocation;
    public char savedChar;
    public int tab = 4;
    public Token savedToken = null;

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

    private StringToken parseStringToken(SourceLocation tokenLocation) throws IOException, GrammarError {
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


    public LiteralNumberToken parseFloatToken(char firstChar, SourceLocation tokenLocation) throws IOException, GrammarError {
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
            throw new GrammarError(tokenLocation, "That's an invalid floating-point number");
        }
    }


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

    public Token readToken() throws IOException, GrammarError {
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
            throw new GrammarError(this.location, "Invalid character: " + ch);
        }
    }

    public void unreadToken(Token token){
        assert (this.savedToken == null);
        this.savedToken = token;
    }

    public void expectSymbol(char symbol) throws GrammarError, IOException {
        Token token = this.readToken();
        if (!(token instanceof SymbolToken) || ((SymbolToken) token).symbol != symbol) {
            throw new GrammarError(token.location, "got '" + token + "' instead of '" + symbol + "'");
        }
    }

    public KeywordEnum expectKeywords(List<KeywordEnum> keywords) throws GrammarError, IOException {
        Token token = this.readToken();
        if (!(token instanceof KeywordToken)) {
            throw new GrammarError(token.location, "expected a keyword instead of '" + token + "'");
        }

        if (!keywords.contains(((KeywordToken) token).keyword)) {
            String expectedKeywords = String.join(",", keywords.stream().map(Enum::toString).toArray(String[]::new));
            throw new GrammarError(token.location, "expected one of the keywords " + expectedKeywords + " instead of '" + token + "'");
        }
        return ((KeywordToken) token).keyword;
    }

    public float expectNumber(Scene scene) throws GrammarError, IOException {
        Token token = this.readToken();
        if (token instanceof LiteralNumberToken) {
            return ((LiteralNumberToken) token).value;
        } else if (token instanceof IdentifierToken) {
            String variableName = ((IdentifierToken) token).identifier;
            if (!scene.floatVariables.containsKey(variableName)) {
                throw new GrammarError(token.location, "unknown variable '" + token + "'");
            }
            return scene.floatVariables.get(variableName);
        }
        throw new GrammarError(token.location, "got '" + token + "' instead of a number");
    }

    public String expectString() throws GrammarError, IOException {
        Token token = this.readToken();
        if (!(token instanceof StringToken)) {
            throw new GrammarError(token.location, "got '" + token + "' instead of a string");
        }
        return token.toString();
    }

    public String expectIdentifier() throws GrammarError, IOException {
        Token token = this.readToken();
        if (!(token instanceof IdentifierToken)) {
            throw new GrammarError(token.location, "got '" + token + "' instead of an identifier");
        }
        return ((IdentifierToken) token).identifier;
    }

    public Vec parseVector(Scene scene) throws GrammarError, IOException {
        this.expectSymbol('[');
        float x = this.expectNumber(scene);
        this.expectSymbol(',');
        float y = this.expectNumber(scene);
        this.expectSymbol(',');
        float z = this.expectNumber(scene);
        this.expectSymbol(']');

        return new Vec(x, y, z);
    }

    public Color parseColor(Scene scene) throws GrammarError, IOException {
        this.expectSymbol('<');
        float red = this.expectNumber(scene);
        this.expectSymbol(',');
        float green = this.expectNumber(scene);
        this.expectSymbol(',');
        float blue = this.expectNumber(scene);
        this.expectSymbol('>');

        return new Color(red, green, blue);
    }

    public Pigment parsePigment(Scene scene) throws GrammarError, IOException {
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
                throw new GrammarError(this.location, "Error reading image file: " + fileName);
            }
        } else {
            throw new AssertionError("This line should be unreachable");
        }

        expectSymbol(')');
        return result;
    }

    public BRDF parseBRDF(Scene scene) throws GrammarError, IOException {
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

    public Pair<String, Material> parseMaterial(Scene scene) throws GrammarError, IOException {
        String name = this.expectIdentifier();

        this.expectSymbol('(');
        BRDF brdf = this.parseBRDF(scene);
        this.expectSymbol(',');
        Pigment emittedRadiance = this.parsePigment(scene);
        this.expectSymbol(')');

        return new Pair<>(name, new Material(brdf, emittedRadiance));
    }

    public Transformation parseTransformation(Scene scene) throws GrammarError, IOException, InvalidMatrixException {
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
                result.times(Transformation.translation(parseVector(scene)));
                this.expectSymbol(')');
            } else if (transformationKw == KeywordEnum.ROTATION_X) {
                this.expectSymbol('(');
                result.times(Transformation.rotationX(expectNumber(scene)));
                this.expectSymbol(')');
            } else if (transformationKw == KeywordEnum.ROTATION_Y) {
                this.expectSymbol('(');
                result.times(Transformation.rotationY(expectNumber(scene)));
                this.expectSymbol(')');
            } else if (transformationKw == KeywordEnum.ROTATION_Z) {
                this.expectSymbol('(');
                result.times(Transformation.rotationZ(expectNumber(scene)));
                this.expectSymbol(')');
            } else if (transformationKw == KeywordEnum.SCALING) {
                this.expectSymbol('(');
                result.times(Transformation.scaling(parseVector(scene)));
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

    public Sphere parseSphere(Scene scene) throws GrammarError, IOException, InvalidMatrixException {
        this.expectSymbol('(');

        String materialName = this.expectIdentifier();
        if (!scene.materials.containsKey(materialName)) {
            throw new GrammarError(this.location, "unknown material " + materialName);
        }

        this.expectSymbol(',');
        Transformation transformation = this.parseTransformation(scene);
        this.expectSymbol(')');

        return new Sphere(transformation, scene.materials.get(materialName));
    }


    public Plain parsePlane(Scene scene) throws GrammarError, IOException, InvalidMatrixException {
        this.expectSymbol('(');

        String materialName = this.expectIdentifier();
        if (!scene.materials.containsKey(materialName)) {
            throw new GrammarError(this.location, "unknown material " + materialName);
        }

        this.expectSymbol(',');
        Transformation transformation = this.parseTransformation(scene);
        this.expectSymbol(')');

        return new Plain(transformation, scene.materials.get(materialName));
    }

    public Camera parseCamera(Scene scene) throws GrammarError, IOException, InvalidMatrixException {
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
            throw new GrammarError(this.location, "Invalid camera type");
        }

        return result;
    }

    public Scene parseScene() throws GrammarError, IOException, InvalidMatrixException {
        Scene scene = new Scene();

        while (true) {
            Token what = this.readToken();
            if (what instanceof StopToken) {
                break;
            }

            if (!(what instanceof KeywordToken)) {
                throw new GrammarError(what.location, "Expected a keyword instead of '" + what + "'");
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
            } else if (keyword == KeywordEnum.PLANE) {
                scene.objects.add(this.parsePlane(scene));
            } else if (keyword == KeywordEnum.CAMERA) {
                if (scene.camera != null) {
                    throw new GrammarError(what.location, "You cannot define more than one camera");
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

