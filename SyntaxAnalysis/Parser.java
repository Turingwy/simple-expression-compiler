package SyntaxAnalysis;

import LexicalAnalysis.Token;
import LexicalAnalysis.Tokenizer;

import java.util.ArrayList;
import java.util.List;

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

       and then we can simply find that this grammer is also LL(1) grammer, so we can directly generate a parse table, look following codes
     */
    enum GType {
        // terminals
        E, EA, T, TA, F,
        // nonterminals
        NUM, ADD, SUB, MUL, DIV, LP, RP, e
    }

    private static ArrayList<GType>[][] table = new ArrayList[GType.F.ordinal() + 1][Token.Type.values().length + 1];

    static {
        ArrayList<GType> temp = table[GType.E.ordinal()][Token.Type.num.ordinal()] = new ArrayList<>();
        temp.add(GType.T);
        temp.add(GType.EA);
        temp = table[GType.E.ordinal()][Token.Type.lp.ordinal()] = new ArrayList<>();
        temp.add(GType.T);
        temp.add(GType.EA);

        temp = table[GType.EA.ordinal()][Token.Type.add.ordinal()] = new ArrayList<>();
        temp.add(GType.ADD);
        temp.add(GType.T);
        temp.add(GType.EA);
        temp = table[GType.EA.ordinal()][Token.Type.subtract.ordinal()] = new ArrayList<>();
        temp.add(GType.SUB);
        temp.add(GType.T);
        temp.add(GType.EA);
        temp = table[GType.EA.ordinal()][Token.Type.rp.ordinal()] = new ArrayList<>();
        temp.add(GType.e);
        temp = table[GType.EA.ordinal()][Token.Type.values().length] = new ArrayList<>();
        temp.add(GType.e);

        temp = table[GType.T.ordinal()][Token.Type.num.ordinal()] = new ArrayList<>();
        temp.add(GType.F);
        temp.add(GType.TA);
        temp = table[GType.T.ordinal()][Token.Type.lp.ordinal()] = new ArrayList<>();
        temp.add(GType.F);
        temp.add(GType.TA);

        temp = table[GType.TA.ordinal()][Token.Type.add.ordinal()] = new ArrayList<>();
        temp.add(GType.e);
        temp = table[GType.TA.ordinal()][Token.Type.subtract.ordinal()] = new ArrayList<>();
        temp.add(GType.e);
        temp = table[GType.TA.ordinal()][Token.Type.multiply.ordinal()] = new ArrayList<>();
        temp.add(GType.MUL);
        temp.add(GType.F);
        temp.add(GType.TA);
        temp = table[GType.TA.ordinal()][Token.Type.divide.ordinal()] = new ArrayList<>();
        temp.add(GType.DIV);
        temp.add(GType.F);
        temp.add(GType.TA);
        temp = table[GType.TA.ordinal()][Token.Type.rp.ordinal()] = new ArrayList<>();
        temp.add(GType.e);
        temp = table[GType.TA.ordinal()][Token.Type.values().length] = new ArrayList<>();
        temp.add(GType.e);

        temp = table[GType.F.ordinal()][Token.Type.num.ordinal()] = new ArrayList<>();
        temp.add(GType.NUM);
        temp = table[GType.F.ordinal()][Token.Type.lp.ordinal()] = new ArrayList<>();
        temp.add(GType.LP);
        temp.add(GType.E);
        temp.add(GType.RP);
    }


    public void derive(GType who) {
        Token token = tokenizer.nextToken();
        if (token == null) {        // EOF
            for (int i = 0; i < table[who.ordinal()].length; i++) {
                ArrayList<GType> production = table[who.ordinal()][i];
                if (production != null && production.get(0) == GType.e)
                    return;
            }

            error(who);
        }

        ArrayList<GType> production = table[who.ordinal()][token.getTokenName().ordinal()];
        tokenizer.store(token);
        if (production == null) {
            error(who);
        }

        System.out.print(who + "->");
        for (int i = 0; i < production.size(); i++) {
            System.out.print(production.get(i) + " ");
        }
        System.out.println();
        for (GType g : production) {
            // if g is a terminal
            if (g.compareTo(GType.F) <= 0) {
                derive(g);
            } else {
                if (g == GType.e) {
                    continue;
                }
                token = tokenizer.nextToken();
                tokenList.add(token);
                if (g.ordinal() - 5 != token.getTokenName().ordinal()) {
                    error(who);
                }
            }
        }
    }


    // TODO
    private void error(GType who) {
        System.err.println("syntax error:" + who);
        System.exit(-1);
    }

    public Parser(String expr) {
        tokenizer = new Tokenizer(expr);
    }

    public static void main(String[] args) {
        Parser parser = new Parser("5+6+9-(55*3)/2");
        parser.parse();
    }

    private void parse() {
        derive(GType.E);
    }
}
