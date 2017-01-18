package SyntaxAnalysis.AST;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by turingwy on 1/18/17.
 */
public class AbstractSyntaxTree {
    public enum NodeType {
        AST_ADD, AST_SUB, AST_MUL, AST_DIV,         // +-*/
        AST_NUM                                        // number
    }

    private List<String> stackLanguage;
    private ASTNode root;                             // root of the ast.
    private int result;

    public AbstractSyntaxTree(ASTNode root) {
        this.root = root;
    }

    public List<String> simpileCompiler() {
        if (stackLanguage != null)
            return stackLanguage;

        stackLanguage = new ArrayList<>();

        simpileCompiler(root);
        return stackLanguage;
    }

    private void simpileCompiler(ASTNode root) {
        if (root == null)
            return;

        String stmt = null;
        switch (root.type) {
            case AST_ADD:
                stmt = "ADD";
                break;
            case AST_SUB:
                stmt = "SUB";
                break;
            case AST_MUL:
                stmt = "MUL";
                break;
            case AST_DIV:
                stmt = "DIV";
                break;
            case AST_NUM:
                stmt = Integer.toString(((ASTNumber) root).getNumber());
                break;
        }

        if (root.type != NodeType.AST_NUM) {
            ASTOperator operator = (ASTOperator) root;
            simpileCompiler(operator.getLeft());
            simpileCompiler(operator.getRight());
        }

        stackLanguage.add(stmt);
    }

    public int compute() {
        if (result != 0)
            return result;

        result = compute(root);
        return result;
    }

    private int compute(ASTNode root) {
        if (root == null)
            return 0;
        if (root.type == NodeType.AST_NUM)
            return ((ASTNumber) root).getNumber();

        ASTOperator operator = (ASTOperator) root;
        switch (root.type) {
            case AST_ADD:
                return compute(operator.getLeft()) + compute(operator.getRight());
            case AST_SUB:
                return compute(operator.getLeft()) - compute(operator.getRight());
            case AST_MUL:
                return compute(operator.getLeft()) * compute(operator.getRight());
            case AST_DIV:
                return compute(operator.getLeft()) / compute(operator.getRight());
        }
        return 0;
    }
}
