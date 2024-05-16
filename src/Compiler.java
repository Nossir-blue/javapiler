package compiler;

import java.util.List;

public class Compiler {
    public static void main(String[] args) {
        String inputCode = "int x; x = 10; if (x > 5) { x = x + 1; } while (x < 20) { x = x + 2; }";

        Lexer lexer = new Lexer(inputCode);
        List<Token> tokens = lexer.tokenize();
        System.out.println("Tokens: " + tokens);

        Parser parser = new Parser(tokens);
        ASTNode ast = parser.parse();
        System.out.println("AST: " + ast);

        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();
        semanticAnalyzer.analyze(ast);
        System.out.println("Semantic analysis passed");

        IntermediateCodeGenerator codeGenerator = new IntermediateCodeGenerator();
        List<String> intermediateCode = codeGenerator.generate(ast);
        System.out.println("Intermediate Code: ");
        for (String line : intermediateCode) {
            System.out.println(line);
        }
    }
}
