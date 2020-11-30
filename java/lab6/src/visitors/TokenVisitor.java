package visitors;

import tokens.Operation;
import tokens.Brace;
import tokens.NumberToken;

public interface TokenVisitor {
    void visit(NumberToken token);

    void visit(Brace token);

    void visit(Operation token);
}
