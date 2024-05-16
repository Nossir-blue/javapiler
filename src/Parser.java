package compiler;

import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int pos = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    private Token peek() {
        return pos < tokens.size() ? tokens.get(pos) : new Token(TokenType.EOF, "");
    }

    private Token next() {
        return pos < tokens.size() ? tokens.get(pos++) : new Token(TokenType.EOF, "");
    }

    private void expect(TokenType type) {
        if (peek().type != type) {
            throw new RuntimeException("Expected " + type + " but got " + peek().type);
        }
        next();
    }

    public ASTNode parse() {
        return parseProgram();
    }

    private ASTNode parseProgram() {
        ASTNode program = new ASTNode("Program");
        while (peek().type != TokenType.EOF) {
            program.addChild(parseStatement());
        }
        return program;
    }

    private ASTNode parseStatement() {
        Token token = peek();
        switch (token.type) {
            case KEYWORD:
                return parseKeywordStatement();
            case IDENTIFIER:
                return parseAssignmentStatement();
            default:
                throw new RuntimeException("Unexpected token: " + token);
        }
    }

    private ASTNode parseKeywordStatement() {
        Token keyword = next();
        switch (keyword.value) {
            case "if":
                return parseIfStatement();
            case "while":
                return parseWhileStatement();
            case "int":
            case "float":
                return parseDeclaration();
            default:
                throw new RuntimeException("Unexpected keyword: " + keyword.value);
        }
    }

    private ASTNode parseIfStatement() {
        ASTNode ifNode = new ASTNode("If");
        expect(TokenType.KEYWORD);
        ifNode.addChild(parseExpression());
        ifNode.addChild(parseBlock());
        return ifNode;
    }

    private ASTNode parseWhileStatement() {
        ASTNode whileNode = new ASTNode("While");
        expect(TokenType.KEYWORD);
        whileNode.addChild(parseExpression());
        whileNode.addChild(parseBlock());
        return whileNode;
    }

    private ASTNode parseDeclaration() {
        Token type = next();
        Token identifier = next();
        expect(TokenType.SEPARATOR); // Expecting semicolon
        ASTNode declaration = new ASTNode("Declaration");
        declaration.addChild(new ASTNode(type.value));
        declaration.addChild(new ASTNode(identifier.value));
        return declaration;
    }

    private ASTNode parseAssignmentStatement() {
        Token identifier = next();
        expect(TokenType.OPERATOR);
        ASTNode assignment = new ASTNode("Assignment");
        assignment.addChild(new ASTNode(identifier.value));
        assignment.addChild(parseExpression());
        expect(TokenType.SEPARATOR); // Expecting semicolon
        return assignment;
    }

    private ASTNode parseExpression() {
        // For simplicity, let's assume expressions are just identifiers or numbers for now
        Token token = next();
        if (token.type == TokenType.IDENTIFIER || token.type == TokenType.NUMBER) {
            return new ASTNode(token.value);
        } else {
            throw new RuntimeException("Unexpected token in expression: " + token);
        }
    }

    private ASTNode parseBlock() {
        ASTNode block = new ASTNode("Block");
        expect(TokenType.SEPARATOR); // Expecting '{'
        while (peek().type != TokenType.SEPARATOR) { // Until ''
            block.addChild(parseStatement());
        }
        expect(TokenType.SEPARATOR); // Expecting '}'
        return block;
    }
}
