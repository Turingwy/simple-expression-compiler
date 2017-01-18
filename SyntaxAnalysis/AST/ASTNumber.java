package SyntaxAnalysis.AST;

/**
 * Created by turingwy on 1/18/17.
 */
public class ASTNumber extends ASTNode {
    private int number;

    public int getNumber() {
        return number;
    }

    public ASTNumber(int number) {
        type = AbstractSyntaxTree.NodeType.AST_NUM;
        this.number = number;
    }
}
