package compiler;

import java.util.ArrayList;
import java.util.List;

public class IntermediateCodeGenerator {
    private int tempVarCount = 0;

    public List<String> generate(ASTNode root) {
        List<String> code = new ArrayList<>();
        generateNode(root, code);
        return code;
    }

    private void generateNode(ASTNode node, List<String> code) {
        switch (node.value) {
            case "Program":
                for (ASTNode child : node.children) {
                    generateNode(child, code);
                }
                break;
            case "Declaration":
                // Handle variable declarations
                break;
            case "Assignment":
                String tempVar = generateExpression(node.children.get(1), code);
                code.add(node.children.get(0).value + " = " + tempVar);
                break;
            case "If":
                String condition = generateExpression(node.children.get(0), code);
                String ifLabel = "L" + tempVarCount++;
                String endLabel = "L" + tempVarCount++;
                code.add("if " + condition + " goto " + ifLabel);
                generateNode(node.children.get(1), code);
                code.add("goto " + endLabel);
                code.add(ifLabel + ":");
                // Generate code for the block
                code.add(endLabel + ":");
                break;
            case "While":
                String loopStartLabel = "L" + tempVarCount++;
                String loopEndLabel = "L" + tempVarCount++;
                code.add(loopStartLabel + ":");
                String loopCondition = generateExpression(node.children.get(0), code);
                code.add("if " + loopCondition + " goto " + loopEndLabel);
                generateNode(node.children.get(1), code);
                code.add("goto " + loopStartLabel);
                code.add(loopEndLabel + ":");
                break;
            // Add other cases as needed
        }
    }

    private String generateExpression(ASTNode node, List<String> code) {
        if (node.children.isEmpty()) {
            return node.value;
        }
        // For simplicity, assume it's a binary operation
        String left = generateExpression(node.children.get(0), code);
        String right = generateExpression(node.children.get(1), code);
        String tempVar = "t" + tempVarCount++;
        code.add(tempVar + " = " + left + " " + node.value + " " + right);
        return tempVar;
    }
}
