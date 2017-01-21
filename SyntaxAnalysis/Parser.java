package SyntaxAnalysis;

import LexicalAnalysis.Token;
import LexicalAnalysis.Tokenizer;
import SyntaxAnalysis.AST.ASTNode;
import SyntaxAnalysis.AST.ASTNumber;
import SyntaxAnalysis.AST.ASTOperator;
import SyntaxAnalysis.AST.AbstractSyntaxTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by turingwy-PC on 2017/1/8.
 */
public class Parser {
    private Tokenizer tokenizer;
    List<Token> tokenList = new ArrayList<>();

    /* grammer
       E -> E + T | E - T | T
       T -> T * F | T / F | F
       F -> (E) | num

       generate a new grammer by eliminate left recursive of this grammer
       E  -> TE'
       E' -> +TE' | -TE' | e (empty string)
       T  -> FT'
       T' -> *FT' | /FT' | e
       F -> (E) | num
3*4+5+3
       and then we evaluate the FIRST(X) and FOLLOW(X) for all of the nonterminals above,
       FIRST(E) = FIRST(T) = FIRST(F) = {(, num}
       FIRST(E') = {+, -, e}
       FIRST(T') = {*, /, e}
       FOLLOW(E) = {), $}
       FOLLOW(E') = FOLLOW(E) = {), $}
       FOLLOW(T) = FIRST(E') except e(empty string) U FOLLOW(E') = {+, -, ), $}
       FOLLOW(T') = FOLLOW(T) = {+, -, ), $}
       FOLLOW(F) = FIRST(T') except e(empty stirng) U FOLLOW(T') = {*, /, +, -, ), $}

       for this grammer, if we want to generate a AST,first thing we must to do is draw a SDD.
       E  -> TE'                   |    E'.inh = T.node    E.node = E'.syn
       E' -> +TE'                  |    E'(r).inh = new Node("+", E'(l).inh, T.node)     E'(l).syn = E'(r).syn
          | -TE'                   |    E'(r).inh = new Node("-", E'(l).inh, T.node)     E'(l).syn = E'(r).syn
          | e                      |    E'.syn = E'.inh
       T  -> FT'                   |    T'.inh = F.node    T.node = T'.syn
       T' -> *FT'                  |    T'(r).inh = new Node("*",T'(l).inh, F.node)      T'(l).syn = T'(r).syn
          | /FT'                   |    T'(r).inh = new Node("/",T'(l).inh, F.node)      T'(l).syn = T'(r).syn
          | e                      |    T'.syn = T'.inh
       F -> (E)                    |    F.node = E.node
          | num                    |    F.node = new Node(num)

     */

    private ASTNode exp() throws Exception {
        ASTNode expNode;
        ASTNode tnode = term();
        return _exp(tnode);
    }

    private ASTNode _exp(ASTNode inh) throws Exception {
        Token token = tokenizer.nextToken();
        if (token == null)
            return inh;
        if (token.getTokenName() == Token.Type.add) {
            ASTNode tnode = term();
            return _exp(new ASTOperator(AbstractSyntaxTree.NodeType.AST_ADD, inh, tnode));
        } else if (token.getTokenName() == Token.Type.subtract) {
            ASTNode tnode = term();
            return _exp(new ASTOperator(AbstractSyntaxTree.NodeType.AST_SUB, inh, tnode));
        } else if (token.getTokenName() == Token.Type.rp) {
            tokenizer.store(token);
            return inh;
        }
        error(token.getAttributeValue());
        return null;

    }

    private ASTNode term() throws Exception {
        ASTNode fnode = factor();
        return _term(fnode);
    }

    private ASTNode _term(ASTNode inh) throws Exception {
        Token token = tokenizer.nextToken();
        if (token == null)
            return inh;
        if (token.getTokenName() == Token.Type.multiply) {
            ASTNode fnode = factor();
            return _term(new ASTOperator(AbstractSyntaxTree.NodeType.AST_MUL, inh, fnode));
        } else if (token.getTokenName() == Token.Type.divide) {
            ASTNode fnode = factor();
            return _term(new ASTOperator(AbstractSyntaxTree.NodeType.AST_DIV, inh, fnode));
        } else if (token.getTokenName() == Token.Type.rp
                || token.getTokenName() == Token.Type.add
                || token.getTokenName() == Token.Type.subtract) {
            tokenizer.store(token);
            return inh;
        }
        error(token.getAttributeValue());
        return null;
    }

    private ASTNode factor() throws Exception {
        Token token = tokenizer.nextToken();
        ASTNode fnode = null;
        if (token.getTokenName() == Token.Type.num) {
            fnode = new ASTNumber(token.getAttributeValue());
        } else if (token.getTokenName() == Token.Type.lp) {
            fnode = exp();
            token = tokenizer.nextToken();
            if (token == null)
                error("$");
            else if (token.getTokenName() != Token.Type.rp)
                error(token.getAttributeValue());
        }
        return fnode;
    }

    // TODO
    private void error(String tokenValue) throws Exception {
        throw new Exception("syntax error:" + tokenValue);
    }

    public Parser(String expr) {
        tokenizer = new Tokenizer(expr);
    }

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String exp = scanner.nextLine();
        while (!exp.isEmpty()) {
            Parser p = new Parser(exp);
            AbstractSyntaxTree ast = new AbstractSyntaxTree(p.exp());
            System.out.println("syntax correct\nThe answer is:");
            System.out.println(ast.compute());
            for (String s : ast.simpileCompiler()) {
                System.out.print(s + ";  ");
            }
            System.out.println();

            exp = scanner.nextLine();
        }
    }

}
