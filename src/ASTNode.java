package compiler;

import java.util.ArrayList;
import java.util.List;

public class ASTNode {
    String value;
    List<ASTNode> children = new ArrayList<>();

    public ASTNode(String value) {
        this.value = value;
    }

    public void addChild(ASTNode child) {
        children.add(child);
    }

    @Override
    public String toString() {
        return value + (children.isEmpty() ? "" : children.toString());
    }
}
