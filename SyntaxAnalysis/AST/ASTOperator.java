package SyntaxAnalysis.AST;

/**
 * Created by turingwy on 1/18/17.
 */
public class ASTOperator extends ASTNode {
    private ASTNode left;
    private ASTNode right;

    public ASTNode getLeft() {
        return left;
    }

    public void setLeft(ASTNode left) {
        this.left = left;
    }

    public ASTNode getRight() {
        return right;
    }

    public void setRight(ASTNode right) {
        this.right = right;
    }

    public ASTOperator(AbstractSyntaxTree.NodeType type, ASTNode left, ASTNode right) {
        this.type = type;
        this.right = right;
        this.left = left;
    }

}
