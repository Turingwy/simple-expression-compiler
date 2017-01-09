package LexicalAnalysis;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by turingwy-PC on 2017/1/8.
 */
public class Tokenizer {
    private String expr;
    private int exprCursor;
    private int exprStart;

    private char nextChar() {
        if(exprCursor >= expr.length()) {
            exprCursor++;
            return '\0';
        }
        return expr.charAt(exprCursor++);
    }


    private boolean isNumber(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == ')';
    }

    private Token retOpToken(char c) {
        if(c == '+')
            return new Token(Token.Type.add, "+");
        else if(c == '-')
            return new Token(Token.Type.subtract, "-");
        else if(c == '*')
            return new Token(Token.Type.multiply, "*");
        else if(c == '/')
            return new Token(Token.Type.divide, "/");
        else if(c == '(')
            return new Token(Token.Type.lp, "(");
        else
            return new Token(Token.Type.rp, ")");
    }

    private Token buffer;

    public void store(Token t) {
        buffer = t;
    }
    public Token nextToken() {
        if(buffer != null) {
            Token t = buffer;
            buffer = null;
            return t;
        }
        int state = 1;
        if(exprCursor >= expr.length())
            return null;
        // DFA start ^_^
        exprStart = exprCursor;
        while(true) {
            char c = nextChar();
            switch (state) {
                case 1:
                    if(isNumber(c)) {
                        state = 2;
                        break;
                    }

                    if(isOperator(c)) {
                        return retOpToken(c);
                    }
                    else
                        return null;
                case 2:
                    if(isNumber(c))
                        state = 2;
                    else if(c == '.')
                        state = 3;
                    else
                        return new Token(Token.Type.num, expr.substring(exprStart, --exprCursor));
                    break;
                case 3:
                    if(isNumber(c))
                        state = 4;
                    else
                        return null;
                    break;
                case 4:
                    if(isNumber(c))
                        state = 4;
                    else
                        return new Token(Token.Type.num, expr.substring(exprStart, --exprCursor));
                    break;
            }

        }

    }

    public Tokenizer(String expr) {
        this.expr = expr;
        exprCursor = exprStart = 0;
    }
}
