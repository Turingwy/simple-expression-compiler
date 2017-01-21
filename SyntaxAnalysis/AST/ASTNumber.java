package SyntaxAnalysis.AST;

/**
 * Created by turingwy on 1/18/17.
 */
public class ASTNumber extends ASTNode {
    private String number;

    public String getNumber() {
        return number;
    }

    public ASTNumber(String number) {
        type = AbstractSyntaxTree.NodeType.AST_NUM;
        this.number = number;
    }
}
