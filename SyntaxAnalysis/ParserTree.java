package SyntaxAnalysis;

import LexicalAnalysis.Token;

/**
 * Created by turingwy-PC on 2017/1/8.
 */
public class ParserTree {
    public static class TreeNode {
        TreeNode[] childs = new TreeNode[5];
        Parser.GType gType;
        Token token;
        boolean isTerminal;
    }

    TreeNode root;

    public void travesal() {
        _travesal(root);
    }

    private void _travesal(TreeNode root) {
        if(root.isTerminal)
            System.out.print(root.token.getAttributeValue() + " ");
        else
            System.out.print(root.gType + " ");
        int k = 0;
        while(root.childs[k]!=null) {
            _travesal(root.childs[k]);
            k++;
        }
    }
}
