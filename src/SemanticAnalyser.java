package compiler;

public class SemanticAnalyzer {
    private final SymbolTable symbolTable = new SymbolTable();

    public void analyze(ASTNode root) {
        analyzeNode(root);
    }

    private void analyzeNode(ASTNode node) {
        switch (node.value) {
            case "Program":
                for (ASTNode child : node.children) {
                    analyzeNode(child);
                }
                break;
            case "Declaration":
                String type = node.children.get(0).value;
                String name = node.children.get(1).value;
                symbolTable.addSymbol(name, type);
                break;
            case "Assignment":
                String varName = node.children.get(0).value;
                Symbol symbol = symbolTable.getSymbol(varName);
                if (symbol == null) {
                    throw new RuntimeException("Undeclared variable: " + varName);
                }
                // More checks can be added here (type checking, etc.)
                break;
            case "If":
            case "While":
                // Add semantic checks for expressions
                analyzeNode(node.children.get(0)); // Condition
                analyzeNode(node.children.get(1)); // Block
                break;
            // Add other cases as needed
            default:
                // For expressions and other nodes
                break;
        }
    }
}
