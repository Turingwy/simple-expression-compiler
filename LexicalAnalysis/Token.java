package LexicalAnalysis;

/**
 * Created by turingwy-PC on 2017/1/8.
 */
public class Token {
    public enum Type {
        num, add, subtract, multiply, divide, lp, rp
    };

    private Type tokenName;
    private String attributeValue;

    public Token() {}

    public Token(Type tokenName, String attributeValue) {
        this.attributeValue = attributeValue;
        this.tokenName = tokenName;
    }

    public Type getTokenName() {
        return tokenName;
    }

    public void setTokenName(Type tokenName) {
        this.tokenName = tokenName;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }




}
