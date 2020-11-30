package visitors;

import tokens.*;

import java.util.ArrayList;
import java.util.List;

public final class ParserVisitor implements TokenVisitor {
    private final List<Token> res = new ArrayList<>();
    private final List<Token> deferred = new ArrayList<>();

    @Override
    public void visit(NumberToken token) {
        res.add(token);
    }

    @Override
    public void visit(Brace token) {
        if (token instanceof Left) deferred.add(token);
        else while (true) {
            if (deferred.isEmpty())
                throw new IllegalArgumentException("Incorrect expression error");
            Token prev = deferred.remove(deferred.size() - 1);
            if (prev instanceof Left) break;
            res.add(prev);
        }
    }

    @Override
    public void visit(Operation token) {
        while (!deferred.isEmpty()) {
            Token prev = deferred.get(deferred.size() - 1);
            if ((prev instanceof Operation) && (priority(prev) <= priority(token))) {
                res.add(prev);
                deferred.remove(deferred.size() - 1);
            } else break;
        }
        deferred.add(token);
    }

    public List<Token> getRPN() {
        while (!deferred.isEmpty()) {
            Token tok = deferred.remove(deferred.size() - 1);
            if (tok instanceof Left)
                throw new IllegalArgumentException("Unclosed brace error");
            res.add(tok);
        }
        return res;
    }

    private int priority(Token token) {
        if (token instanceof Mul) return 0;
        else if (token instanceof Div) return 0;
        else if (token instanceof Plus) return 1;
        else if (token instanceof Minus) return 1;
        else return 2;
    }
}
