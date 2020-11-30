package visitors;

import tokens.Operation;
import tokens.Brace;
import tokens.NumberToken;

public final class PrintVisitor implements TokenVisitor {
    @Override
    public void visit(NumberToken token) {
        System.out.print(token.toString());
    }

    @Override
    public void visit(Brace token) {
        System.out.print(token.toString());
    }

    @Override
    public void visit(Operation token) {
        System.out.print(token.toString());
    }
}
