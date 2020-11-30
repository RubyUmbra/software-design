package tokens;

import visitors.TokenVisitor;

public final class NumberToken implements Token {
    private final int val;

    public NumberToken(int value) {
        val = value;
    }

    @Override
    public void accept(TokenVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "NUMBER(" + val + ")";
    }

    public int getValue() {
        return val;
    }
}
